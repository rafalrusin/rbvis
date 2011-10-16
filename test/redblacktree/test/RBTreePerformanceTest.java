package redblacktree.test;

import java.util.Comparator;
import junit.framework.TestCase;
import rbvis.client.RBTree;

public class RBTreePerformanceTest extends TestCase {
    private int testSize = 1000000;

    public void testInsert() {
        Comparator comparator = new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((Integer) o1).compareTo((Integer) o2);
            }
        };

        RBTree tree = new RBTree(comparator);
        for (int i = 0; i < testSize; i++) {
            tree.insert(i);
        }

        assertEquals(tree.size(), testSize);
        System.out.println(tree.verifyRedBlack());
    }

    public void testDelete() {
        Comparator comparator = new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((Integer) o1).compareTo((Integer) o2);
            }
        };

        RBTree tree = new RBTree(comparator);
        for (int i = 0; i < testSize; i++) {
            tree.insert(i);
        }

        assertEquals(tree.size(), testSize);
        
        for (int i = 0; i < testSize; i++) {
            tree.remove(i);
        }

        assertEquals(tree.size(), 0);
    }
}
