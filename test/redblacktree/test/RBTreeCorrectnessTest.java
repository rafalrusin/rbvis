package redblacktree.test;

import java.util.Comparator;
import junit.framework.TestCase;
import rbvis.client.RBTree;

public class RBTreeCorrectnessTest extends TestCase {
    private int testSize = 1000;

    public void testInsert() {
        Comparator comparator = new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((Integer) o1).compareTo((Integer) o2);
            }
        };

        RBTree tree = new RBTree(comparator);
        for (int i = 1; i < testSize; i++) {
            tree.insert(i);
            tree.verifyRedBlack();
        }

//        System.out.println(tree);

        System.out.println(tree.verifyRedBlack());
    }

    public void testDelete() {
        Comparator comparator = new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((Integer) o1).compareTo((Integer) o2);
            }
        };

        RBTree tree = new RBTree(comparator);
        for (int i = 1; i < testSize; i++) {
            tree.insert(i);
            assertEquals(i, tree.size());
            tree.verifyRedBlack();
        }
        
        //System.out.println(tree);

        for (int i = 1; i < testSize; i++) {
            tree.remove(i);
            tree.verifyRedBlack();
            assertEquals(testSize-1-i, tree.size());
        }

        System.out.println(tree.verifyRedBlack());
    }
}
