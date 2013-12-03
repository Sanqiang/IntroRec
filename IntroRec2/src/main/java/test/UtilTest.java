/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.util.ArrayList;
import org.grouplens.lenskit.vectors.MutableSparseVector;
import org.grouplens.lenskit.vectors.SparseVector;

/**
 *
 * @author sanqiangzhao
 */
public class UtilTest {

    public static void main(String[] args) {
        ArrayList<Long> keys = new ArrayList<Long>();
        keys.add(1l);keys.add(2l);
        MutableSparseVector sv1 = MutableSparseVector.create(keys);
        sv1.fill(2d);
        MutableSparseVector sv2 = MutableSparseVector.create(keys);
        sv2.set(1, 3);
        sv2.set(2, 4);
        System.out.println(sv1.norm() + " = " + sv2.norm());
        System.out.println(sv1.dot(sv2));
    }
}
