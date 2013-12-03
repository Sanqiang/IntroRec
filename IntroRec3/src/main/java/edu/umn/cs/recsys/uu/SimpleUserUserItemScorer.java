package edu.umn.cs.recsys.uu;

import it.unimi.dsi.fastutil.longs.LongSet;
import java.util.Queue;
import java.util.concurrent.PriorityBlockingQueue;
import org.grouplens.lenskit.basic.AbstractItemScorer;
import org.grouplens.lenskit.data.dao.ItemEventDAO;
import org.grouplens.lenskit.data.dao.UserEventDAO;
import org.grouplens.lenskit.data.event.Rating;
import org.grouplens.lenskit.data.history.History;
import org.grouplens.lenskit.data.history.RatingVectorUserHistorySummarizer;
import org.grouplens.lenskit.data.history.UserHistory;
import org.grouplens.lenskit.vectors.MutableSparseVector;
import org.grouplens.lenskit.vectors.SparseVector;
import org.grouplens.lenskit.vectors.VectorEntry;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import org.grouplens.lenskit.vectors.similarity.CosineVectorSimilarity;

/**
 * User-user item scorer.
 *
 * @author <a href="http://www.grouplens.org">GroupLens Research</a>
 */
public class SimpleUserUserItemScorer extends AbstractItemScorer {

    private final UserEventDAO userDao;
    private final ItemEventDAO itemDao;

    @Inject
    public SimpleUserUserItemScorer(UserEventDAO udao, ItemEventDAO idao) {
        userDao = udao;
        itemDao = idao;
    }

    @Override
    public void score(long user, @Nonnull MutableSparseVector scores) {
        SparseVector userVector = getUserRatingVector(user);
        CosineVectorSimilarity cosObj = new CosineVectorSimilarity();
        // TODO Score items for this user using user-user collaborative filtering
        double userMean = userVector.mean();
        //get copy of u1 vector and implement the mean-vector
        MutableSparseVector userVectorX = userVector.mutableCopy();
        userVectorX.add(userMean * -1);
        // This is the loop structure to iterate over items to score
        for (VectorEntry e : scores.fast(VectorEntry.State.EITHER)) {
            long itemId = e.getKey();
            LongSet userList = itemDao.getUsersForItem(itemId);
            double rightSideUp = 0d, rightSideDown = 0d;

            Queue<ScoreObj> SimQ = new PriorityBlockingQueue<ScoreObj>();
            for (long user2Id : userList) {
                //did not count myselft
                if (user2Id == user) {
                    continue;
                }
                //get copy of u2 vector and implement the mean-vector
                SparseVector user2Vector = getUserRatingVector(user2Id);
                MutableSparseVector user2VectorX = user2Vector.mutableCopy();
                double user2Mean = user2Vector.mean();
                user2VectorX.add(user2Mean * -1);
                //use mean vector for cosin similarity
                double cosinSim = cosObj.similarity(userVectorX, user2VectorX);
                //i built my priority queue for picking largest sim userid.
                ScoreObj obj = new ScoreObj();
                obj.sim = cosinSim;
                obj.userId = user2Id;
                SimQ.add(obj);
            }

            for (int i = 0; i < 30; i++) {
                ScoreObj obj = SimQ.poll();
                
                SparseVector user2Vector = getUserRatingVector(obj.userId);
                MutableSparseVector user2VectorX = user2Vector.mutableCopy();
                double user2Mean = user2Vector.mean();
                user2VectorX.add(user2Mean * -1);
                double cosinSim = cosObj.similarity(userVectorX, user2VectorX);
                //accumulate the norminator and dinomenator
                rightSideDown += Math.abs(cosinSim);
                rightSideUp += (user2Vector.get(itemId) - user2Mean) * cosinSim;
            }
           
            double score = userMean + rightSideUp / rightSideDown;
            scores.set(e.getKey(), score);
        }
    }

    /**
     * Get a user's rating vector.
     *
     * @param user The user ID.
     * @return The rating vector.
     */
    private SparseVector getUserRatingVector(long user) {
        UserHistory<Rating> history = userDao.getEventsForUser(user, Rating.class);
        if (history == null) {
            history = History.forUser(user);
        }
        return RatingVectorUserHistorySummarizer.makeRatingVector(history);
    }
}
