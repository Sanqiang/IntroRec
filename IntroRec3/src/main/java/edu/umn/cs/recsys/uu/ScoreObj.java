/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.umn.cs.recsys.uu;

/**
 *
 * @author sanqiangzhao
 */
public class ScoreObj implements Comparable<ScoreObj> {

    public long userId;
    public double sim;

    @Override
    public int compareTo(ScoreObj t) {
        return sim < t.sim ? 1 : -1;
    }
}
