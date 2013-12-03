package edu.umn.cs.recsys.ii;

import java.util.Collections;
import org.grouplens.lenskit.basic.AbstractItemScorer;
import org.grouplens.lenskit.data.dao.UserEventDAO;
import org.grouplens.lenskit.data.event.Rating;
import org.grouplens.lenskit.data.history.History;
import org.grouplens.lenskit.data.history.RatingVectorUserHistorySummarizer;
import org.grouplens.lenskit.data.history.UserHistory;
import org.grouplens.lenskit.knn.NeighborhoodSize;
import org.grouplens.lenskit.scored.ScoredId;
import org.grouplens.lenskit.vectors.MutableSparseVector;
import org.grouplens.lenskit.vectors.SparseVector;
import org.grouplens.lenskit.vectors.VectorEntry;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;
import org.grouplens.lenskit.cursors.Cursor;
import org.grouplens.lenskit.data.event.Event;

/**
 * @author <a href="http://www.grouplens.org">GroupLens Research</a>
 */
public class SimpleItemItemScorer extends AbstractItemScorer {

    private final SimpleItemItemModel model;
    private final UserEventDAO userEvents;
    private final int neighborhoodSize;

    @Inject
    public SimpleItemItemScorer(SimpleItemItemModel m, UserEventDAO dao,
            @NeighborhoodSize int nnbrs) {
        model = m;
        userEvents = dao;
        neighborhoodSize = nnbrs;
    }

    /**
     * Score items for a user.
     *
     * @param user The user ID.
     * @param scores The score vector. Its key domain is the items to score, and
     * the scores (rating predictions) should be written back to this vector.
     */
    @Override
    public void score(long user, @Nonnull MutableSparseVector scores) {
        SparseVector ratings = getUserRatingVector(user);

        for (VectorEntry e : scores.fast(VectorEntry.State.UNSET)) {
            long item = e.getKey();
            List<ScoredId> neighbors = model.getNeighbors(item);
            // TODO Score this item and save the score into scores
            int count = 0;
            double score = 0d, norm = 0d;
            for (ScoredId scoredId : neighbors) {
                long itemHelpId = scoredId.getId();
                if (ratings.containsKey(itemHelpId)) {
                    score += (scoredId.getScore() * ratings.get(itemHelpId));
                    norm += Math.abs(scoredId.getScore());
                    if (++count == neighborhoodSize) {
                        break;
                    }
                }
            }
            score /= norm;
            scores.set(item, score);
        }
    }

    /**
     * Get a user's ratings.
     *
     * @param user The user ID.
     * @return The ratings to retrieve.
     */
    private SparseVector getUserRatingVector(long user) {
        UserHistory<Rating> history = userEvents.getEventsForUser(user, Rating.class);
        if (history == null) {
            history = History.forUser(user);
        }

        return RatingVectorUserHistorySummarizer.makeRatingVector(history);
    }
}
