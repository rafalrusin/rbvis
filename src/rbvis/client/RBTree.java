package rbvis.client;

import java.util.Comparator;

public class RBTree {

    private RBNode root = null;
    private Comparator comparator;

    public RBTree(Comparator comparator) {
        this.comparator = comparator;
    }
    
    public RBNode getRoot() {
		return root;
	}

	private RBNode grandparent(RBNode n) {
        if ((n != null) && (n.parent != null)) {
            return n.parent.parent;
        } else {
            return null;
        }
    }

    private RBNode uncle(RBNode n) {
        RBNode g = grandparent(n);
        if (g == null) {
            return null; // No grandparent means no uncle
        }
        if (n.parent == g.left) {
            return g.right;
        } else {
            return g.left;
        }
    }

    private void rotate_left(RBNode x) {
        RBNode y = x.right;

        RBNode a = x.left;
        RBNode b = y.left;
        RBNode c = y.right;

        y.parent = x.parent;
        if (x.parent != null) {
            if (x.parent.left == x) {
                x.parent.left = y;
            } else {
                x.parent.right = y;
            }
        }

        x.parent = y;
        y.left = x;

        x.left = a;
        if (a != null) {
            a.parent = x;
        }

        x.right = b;
        if (b != null) {
            b.parent = x;
        }

        y.right = c;
        if (c != null) {
            c.parent = y;
        }

        updateRoot();
    }

    private void rotate_right(RBNode x) {
        RBNode y = x.left;

        RBNode a = x.right;
        RBNode b = y.right;
        RBNode c = y.left;

        y.parent = x.parent;
        if (x.parent != null) {
            if (x.parent.left == x) {
                x.parent.left = y;
            } else {
                x.parent.right = y;
            }
        }

        x.parent = y;
        y.right = x;

        x.right = a;
        if (a != null) {
            a.parent = x;
        }

        x.left = b;
        if (b != null) {
            b.parent = x;
        }

        y.left = c;
        if (c != null) {
            c.parent = y;
        }

        updateRoot();
    }

    private void insert_case1(RBNode n) {
        if (n.parent == null) {
            n.isBlack = true;
        } else {
            insert_case2(n);
        }
    }

    private void insert_case2(RBNode n) {
        if (n.parent.isBlack) {
            return; /* Tree is still valid */
        } else {
            insert_case3(n);
        }
    }

    private void insert_case3(RBNode n) {
        RBNode u = uncle(n);
        RBNode g;

        if ((u != null) && (u.isBlack == false)) {
            //Parent is red
            n.parent.isBlack = true;
            u.isBlack = true;
            g = grandparent(n);
            g.isBlack = false;
            insert_case1(g);
        } else {
            insert_case4(n);
        }
    }

    private void insert_case4(RBNode n) {
        RBNode g = grandparent(n);

        if ((n == n.parent.right) && (n.parent == g.left)) {
            rotate_left(n.parent);
            n = n.left;
        } else if ((n == n.parent.left) && (n.parent == g.right)) {
            rotate_right(n.parent);
            n = n.right;
        }
        insert_case5(n);
    }

    private void insert_case5(RBNode n) {
        RBNode g = grandparent(n);

        n.parent.isBlack = true;
        g.isBlack = false;
        if ((n == n.parent.left) && (n.parent == g.left)) {
            rotate_right(g);
        } else if ((n == n.parent.right) && (n.parent == g.right)) {
            rotate_left(g);
        } else {
            throw new IllegalStateException("Unknown case");
        }
    }

    public void insert(Object element) {
        RBNode node = new RBNode();
        node.isBlack = false;
        node.value = element;
        insertIntoBST(node);
        insert_case1(node);
    }

    private RBNode sibling(RBNode n, RBNode nparent) {
        if (n == nparent.left) {
            return nparent.right;
        } else {
            return nparent.left;
        }
    }

    private void swap_value(RBNode a, RBNode b) {
        Object tmp;
        tmp = a.value;
        a.value = b.value;
        b.value = tmp;
    }

    private int verifyRedBlack(RBNode n) {
        if (n == null) {
            return 1;
        } else {
            RBNode p = n.parent;
            if (p == null) {
                verify(n.isBlack);
            } else {
                verify(p.isBlack || n.isBlack);
            }
            int l = verifyRedBlack(n.left);
            int r = verifyRedBlack(n.right);
            verify(l == r);
            return l + (n.isBlack ? 1 : 0);
        }
    }

    private int size(RBNode n) {
        if (n == null) {
            return 0;
        } else {
            RBNode p = n.parent;
            return 1 + size(n.left) + size(n.right);
        }
    }

    public int size() {
        return size(root);
    }

    public int verifyRedBlack() {
        return verifyRedBlack(root);
    }

    private void insertIntoBST(RBNode node) {
        RBNode x = root;
        RBNode last = null;
        boolean wasRight = false;
        while (x != null) {
            last = x;
            if (comparator.compare(x.value, node.value) < 0) {
                x = x.right;
                wasRight = true;
            } else {
                x = x.left;
                wasRight = false;
            }
        }
        if (last == null) {
            root = node;
        } else {
            if (wasRight) {
                last.right = node;
                node.parent = last;
            } else {
                last.left = node;
                node.parent = last;
            }
        }
    }

    private RBNode findInBST(Object value) {
        RBNode x = root;
        while (x != null) {
            if (comparator.compare(x.value, value) == 0) {
                return x;
            }
            if (comparator.compare(x.value, value) < 0) {
                x = x.right;
            } else {
                x = x.left;
            }
        }
        return null;
    }

    private RBNode findNext(RBNode n) {
        if (n.right != null) {
            RBNode x = n.right;
            while (x.left != null) {
                x = x.left;
            }
            return x;
        }
        return null;
    }

    private void verify(boolean b) {
        if (!b) {
            throw new IllegalStateException();
        }
    }

    @Override
    public String toString() {
        return root.toString();
    }

    private void updateRoot() {
        while (root.parent != null) {
            root = root.parent;
        }
    }

    public void remove(Object value) {
        RBNode n = findInBST(value);
        if (n != null) {
		    if (n.left != null && n.right != null) {
		        RBNode m = findNext(n);
		        swap_value(n, m);
		        n = m;
		    }
		    delete_one_child(n);
        }
    }

    private void delete_one_child(RBNode n) {
        /*
         * Precondition: n has at most one non-null child.
         */
        RBNode child = n.right == null ? n.left : n.right;

        RBNode nparent = n.parent;
        
        replace_node(n, child);

        if (n.isBlack) {
            if (child != null && child.isBlack == false) {
                child.isBlack = true;
            } else {
                delete_case1(child, nparent);
            }
        }
    }

    private void delete_case1(RBNode n, RBNode nparent) {
        if (nparent != null) {
            delete_case2(n, nparent);
        }
    }

    private void delete_case2(RBNode n, RBNode nparent) {
        RBNode s = sibling(n, nparent);

        if (s.isBlack == false) {
            nparent.isBlack = false;
            s.isBlack = true;
            if (n == nparent.left) {
                rotate_left(nparent);
            } else {
                rotate_right(nparent);
            }
        }
        delete_case3(n, nparent);
    }

    private void delete_case3(RBNode n, RBNode nparent) {
        RBNode s = sibling(n, nparent);

        if ((nparent.isBlack)
                && (s.isBlack)
                && (s.left == null || s.left.isBlack)
                && (s.right == null || s.right.isBlack)) {
            s.isBlack = false;
            delete_case1(nparent, nparent.parent);
        } else {
            delete_case4(n, nparent);
        }
    }

    private void delete_case4(RBNode n, RBNode nparent) {
        RBNode s = sibling(n, nparent);

        if ((nparent.isBlack == false)
                && (s.isBlack)
                && (s.left == null || s.left.isBlack)
                && (s.right == null || s.right.isBlack)) {
            s.isBlack = false;
            nparent.isBlack = true;
        } else {
            delete_case5(n, nparent);
        }
    }

    void delete_case5(RBNode n, RBNode nparent) {
        RBNode s = sibling(n, nparent);

        if (s != null && s.isBlack) { /* this if statement is trivial,
            due to Case 2 (even though Case two changed the sibling to a sibling's child,
            the sibling's child can't be RBColor.RED, since no RBColor.RED parent can have a RBColor.RED child). */
            /* the following statements just force the RBColor.RED to be on the left of the left of the parent,
            or right of the right, so case six will rotate correctly. */
            if ((n == nparent.left)
                    && (s.right.isBlack)
                    && (s.left.isBlack == false)) { /* this last test is trivial too due to cases 2-4. */
                s.isBlack = false;
                s.left.isBlack = true;
                rotate_right(s);
            } else if ((n == nparent.right)
                    && (s.left.isBlack)
                    && (s.right.isBlack == false)) {/* this last test is trivial too due to cases 2-4. */
                s.isBlack = false;
                s.right.isBlack = true;
                rotate_left(s);
            }
        }
        delete_case6(n, nparent);
    }

    void delete_case6(RBNode n, RBNode nparent) {
        RBNode s = sibling(n, nparent);

        s.isBlack = nparent.isBlack;
        nparent.isBlack = true;

        if (n == nparent.left) {
            s.right.isBlack = true;
            rotate_left(nparent);
        } else {
            s.left.isBlack = true;
            rotate_right(nparent);
        }
    }

    /** 
     * Removes n and puts child in it's place
     * @param n
     * @param child
     */
    private void replace_node(RBNode n, RBNode child) {
        if (child != null) {
            child.parent = n.parent;
        }
        if (n.parent != null) {
            if (n.parent.left == n) {
                n.parent.left = child;
            } else {
                n.parent.right = child;
            }
        }
        n.left = null;
        n.right = null;
        n.parent = null;
        if (root == n) {
            root = child;
        }
    }
}
