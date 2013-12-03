package com.mycompany.introrec1;

import java.text.DecimalFormat;
import java.util.*;

/**
 * Hello world!
 *
 */
public class App {

    public static long[] MoviceIds = {1900, 275, 585, 11, 121, 8587};

    public static void main(String[] args) {
        Matrix m = new Matrix();
        for (long movieIdForMe : MoviceIds) {
            System.out.println("Recommender for : " + m.Movies.get(movieIdForMe));
            int px = 0, pxNeg = 0;
            HashMap<Long, Integer> pxy = new HashMap<Long, Integer>(), pxyNeg = new HashMap<Long, Integer>();
            for (long userIdLoop : m.Users.keySet()) {
                HashSet<Long> movieSetByCurrentLoopUser = m.MovieByUser.get(userIdLoop);
                
                if (movieSetByCurrentLoopUser.contains(movieIdForMe)) {
                    ++px;
                    for (Long MovieIdLoop : m.Movies.keySet()) {
                        if (MovieIdLoop == movieIdForMe) {
                            continue;
                        }

                        if (movieSetByCurrentLoopUser.contains(MovieIdLoop)) {
                            if (!pxy.containsKey(MovieIdLoop)) {
                                pxy.put(MovieIdLoop, 0);
                            }
                            pxy.put(MovieIdLoop, 1 + pxy.get(MovieIdLoop));
                        } /*else {
                         if (!pxyNeg.containsKey(MovieIdLoop)) {
                         pxyNeg.put(MovieIdLoop, 0);
                         }
                         pxyNeg.put(MovieIdLoop, 1 + pxyNeg.get(MovieIdLoop));
                         }*/
                    }
                } else {
                    ++pxNeg;
                    for (Long MovieIdLoop : m.Movies.keySet()) {
                        if (MovieIdLoop == movieIdForMe) {
                            continue;
                        }
                        if (movieSetByCurrentLoopUser.contains(MovieIdLoop)) {
                            if (!pxyNeg.containsKey(MovieIdLoop)) {
                                pxyNeg.put(MovieIdLoop, 0);
                            }
                            pxyNeg.put(MovieIdLoop, 1 + pxyNeg.get(MovieIdLoop));
                        }
                    }
                }
            }

            DecimalFormat df = new DecimalFormat("0.00");
            final TreeMap<Long, Float> SimpleScores = new TreeMap<Long, Float>(), ComplexScores = new TreeMap<Long, Float>(); //movie-score
            for (Long MovieIdLoop : m.Movies.keySet()) {
                if (pxy.containsKey(MovieIdLoop)) {
                    //simple
                    float score = (float) pxy.get(MovieIdLoop) / (float) px;
                    SimpleScores.put(MovieIdLoop, score);
                    //complex
                    if (pxyNeg.containsKey(MovieIdLoop)) {
                        score = score / ((float) pxyNeg.get(MovieIdLoop) / (float)pxNeg);
                        ComplexScores.put(MovieIdLoop, score);

                    } else {
                        ComplexScores.put(MovieIdLoop, score + 1);
                    }
                }
            }

            TreeMap<Long, Float> sortedSimpleScore = new TreeMap<Long, Float>(new Comparator<Long>() {
                public int compare(Long t, Long t1) {
                    return -1 * SimpleScores.get(t).compareTo(SimpleScores.get(t1));
                }
            });
            sortedSimpleScore.putAll(SimpleScores);
            TreeMap<Long, Float> sortedComplexScore = new TreeMap<Long, Float>(new Comparator<Long>() {
                public int compare(Long t, Long t1) {
                    return -1 * ComplexScores.get(t).compareTo(ComplexScores.get(t1));
                }
            });
            sortedComplexScore.putAll(ComplexScores);
            int count = 0;
            Iterator it = sortedSimpleScore.keySet().iterator();
            System.out.print(movieIdForMe + ",");
            while (it.hasNext()) {
                long OutputmovieId = Long.parseLong(String.valueOf(it.next()));
                System.out.print(OutputmovieId + "," + df.format(sortedSimpleScore.get(OutputmovieId)) + ",");
                if (++count == 5) {
                    break;
                }
            }
            System.out.println();
            count = 0;
            System.out.print(movieIdForMe + ",");
            it = sortedComplexScore.keySet().iterator();
            while (it.hasNext()) {
                long OutputmovieId = Long.parseLong(String.valueOf(it.next()));
                System.out.print(OutputmovieId + "," + df.format(sortedComplexScore.get(OutputmovieId)) + ",");
                if (++count == 5) {
                    break;
                }
            }
            System.out.println();
            System.out.println("===================================");
        }



        System.out.println("Hello World!");
    }
}
