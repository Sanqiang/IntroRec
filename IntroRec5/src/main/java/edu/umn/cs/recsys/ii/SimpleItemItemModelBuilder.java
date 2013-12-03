package edu.umn.cs.recsys.ii;

import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.longs.LongSet;
import it.unimi.dsi.fastutil.longs.LongSortedSet;
import java.util.ArrayList;
import org.grouplens.lenskit.collections.LongUtils;
import org.grouplens.lenskit.core.Transient;
import org.grouplens.lenskit.cursors.Cursor;
import org.grouplens.lenskit.data.dao.ItemDAO;
import org.grouplens.lenskit.data.dao.UserEventDAO;
import org.grouplens.lenskit.data.event.Event;
import org.grouplens.lenskit.data.history.RatingVectorUserHistorySummarizer;
import org.grouplens.lenskit.data.history.UserHistory;
import org.grouplens.lenskit.scored.ScoredId;
import org.grouplens.lenskit.scored.ScoredIdListBuilder;
import org.grouplens.lenskit.scored.ScoredIds;
import org.grouplens.lenskit.vectors.ImmutableSparseVector;
import org.grouplens.lenskit.vectors.MutableSparseVector;
import org.grouplens.lenskit.vectors.SparseVector;
import org.grouplens.lenskit.vectors.VectorEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

/**
 * @author <a href="http://www.grouplens.org">GroupLens Research</a>
 */
public class SimpleItemItemModelBuilder implements Provider<SimpleItemItemModel> {

    private final ItemDAO itemDao;
    private final UserEventDAO userEventDao;
    private static final Logger logger = LoggerFactory.getLogger(SimpleItemItemModelBuilder.class);

    ;

    @Inject
    public SimpleItemItemModelBuilder(@Transient ItemDAO idao,
            @Transient UserEventDAO uedao) {
        itemDao = idao;
        userEventDao = uedao;
    }

    @Override
    public SimpleItemItemModel get() {
        // Get the transposed rating matrix
        // This gives us a map of item IDs to those items' rating vectors
        Map<Long, ImmutableSparseVector> itemVectors = getItemVectors();

        // Get all items - you might find this useful
        LongSortedSet items = LongUtils.packedSet(itemVectors.keySet());
        // Map items to vectors of item similarities
        Map<Long, MutableSparseVector> itemSimilarities = new HashMap<Long, MutableSparseVector>();
        for (Long long1 : items) {
            MutableSparseVector vector = MutableSparseVector.create(items.toLongArray());
            for (Long long2 : items) {
                /*if (itemSimilarities.containsKey(long2) && itemSimilarities.get(long2).containsKey(long1)) {
                 double score = itemSimilarities.get(long2).get(long1);
                 vector.set(long2, score);
                 continue;
                 }*/
                if (long1 == long2) {
                    continue;
                }
                MutableSparseVector v1 = itemVectors.get(long1).mutableCopy(), v2 = itemVectors.get(long2).mutableCopy();
                //v1.add(v1.mean() * -1);
                //v2.add(v2.mean() * -1);

                double norm1 = 0d, norm2 = 0d, score = 0d;
                for (VectorEntry vectorEntry : v1) {
                    if (v2.containsKey(vectorEntry.getKey())) {
                        score += vectorEntry.getValue() * v2.get(vectorEntry.getKey());
                        //norm1 += Math.pow(vectorEntry.getValue(), 2);
                        //norm2 += Math.pow(v2.get(vectorEntry.getKey()), 2);
                    }
                }
                //score /= (Math.sqrt(norm1) * Math.sqrt(norm2));
                score /= (v1.norm() * v2.norm());
                /*double norm = v1.norm() * v2.norm();
                 v1.multiply(v2);
                
                 double score = v1.sum() / norm;
                 */
                if (score > 0) {
                    vector.set(long2, score);
                }
            }
            itemSimilarities.put(long1, vector);
        }
        // TODO Compute the similarities between each pair of items
        HashMap<Long, List<ScoredId>> map = new HashMap<Long, List<ScoredId>>();
        for (Long termId : itemSimilarities.keySet()) {
            map.put(termId, new ArrayList<ScoredId>());
            for (VectorEntry simList : itemSimilarities.get(termId)) {
                map.get(termId).add(ScoredIds.create(simList.getKey(), simList.getValue()));
            }
            Collections.sort(map.get(termId), new Comparator<ScoredId>() {
                @Override
                public int compare(ScoredId o1, ScoredId o2) {
                    if (o1.getScore() > o2.getScore()) {
                        return -1;
                    } else {
                        return 1;
                    }
                }
            });
        }
        // It will need to be in a map of longs to lists of Scored IDs to store in the model
        return new SimpleItemItemModel(map);
    }

    /**
     * Load the data into memory, indexed by item.
     *
     * @return A map from item IDs to item rating vectors. Each vector contains
     * users' ratings for the item, keyed by user ID.
     */
    public Map<Long, ImmutableSparseVector> getItemVectors() {
        // set up storage for building each item's rating vector
        LongSet items = itemDao.getItemIds();
        // map items to maps from users to ratings
        Map<Long, Map<Long, Double>> itemData = new HashMap<Long, Map<Long, Double>>();
        for (long item : items) {
            itemData.put(item, new HashMap<Long, Double>());
        }
        // itemData should now contain a map to accumulate the ratings of each item

        // stream over all user events
        Cursor<UserHistory<Event>> stream = userEventDao.streamEventsByUser();
        try {
            for (UserHistory<Event> evt : stream) {
                MutableSparseVector vector = RatingVectorUserHistorySummarizer.makeRatingVector(evt).mutableCopy();
                // vector is now the user's rating vector
                // TODO Normalize this vector and store the ratings in the item data
                double mean = vector.mean();
                double norm = vector.norm();
                for (VectorEntry vectorEntry : vector) {
                    itemData.get(vectorEntry.getKey()).put(evt.getUserId(), (vectorEntry.getValue() - mean) );
                }
            }
        } finally {
            stream.close();
        }

        // This loop converts our temporary item storage to a map of item vectors
        Map<Long, ImmutableSparseVector> itemVectors = new HashMap<Long, ImmutableSparseVector>();
        for (Map.Entry<Long, Map<Long, Double>> entry : itemData.entrySet()) {
            MutableSparseVector vec = MutableSparseVector.create(entry.getValue());
            itemVectors.put(entry.getKey(), vec.immutable());
        }
        return itemVectors;
    }
}
