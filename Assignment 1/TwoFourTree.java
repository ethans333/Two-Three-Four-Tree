public class TwoFourTree {
    private class TwoFourTreeItem {
        int values = 1;
        int value1 = 0;                             // always exists.
        int value2 = 0;                             // exists iff the node is a 3-node or 4-node.
        int value3 = 0;                             // exists iff the node is a 4-node.
        boolean isLeaf = true;
        
        TwoFourTreeItem parent = null;              // parent exists iff the node is not root.
        TwoFourTreeItem leftChild = null;           // left and right child exist iff the note is a non-leaf.
        TwoFourTreeItem rightChild = null;          
        TwoFourTreeItem centerChild = null;         // center child exists iff the node is a non-leaf 3-node.
        TwoFourTreeItem centerLeftChild = null;     // center-left and center-right children exist iff the node is a non-leaf 4-node.
        TwoFourTreeItem centerRightChild = null;

        public boolean isTwoNode() {
            return (values == 1);
        }

        public boolean isThreeNode() {
            return (values == 2);
        }

        public boolean isFourNode() {
            return (values == 3);
        }

        public boolean isRoot() {
            return (parent == null);
        }

        public TwoFourTreeItem(int value1) {
            this.value1 = value1;
            this.values = 1;
        }

        public TwoFourTreeItem(int value1, int value2) {
            this.value1 = value1;
            this.value2 = value2;
            this.values = 2;
        }

        public TwoFourTreeItem(int value1, int value2, int value3) {
            this.value1 = value1;
            this.value2 = value2;
            this.value3 = value3;
            this.values = 3;
        }

        private void printIndents(int indent) {
            for(int i = 0; i < indent; i++) System.out.printf("  ");
        }

        public void printInOrder(int indent) {
            if(!isLeaf) leftChild.printInOrder(indent + 1);
            printIndents(indent);
            System.out.printf("%d\n", value1);
            if(isThreeNode()) {
                if(!isLeaf) centerChild.printInOrder(indent + 1);
                printIndents(indent);
                System.out.printf("%d\n", value2);
            } else if(isFourNode()) {
                if(!isLeaf) centerLeftChild.printInOrder(indent + 1);
                printIndents(indent);
                System.out.printf("%d\n", value2);
                if(!isLeaf) centerRightChild.printInOrder(indent + 1);
                printIndents(indent);
                System.out.printf("%d\n", value3);
            }
            if(!isLeaf) rightChild.printInOrder(indent + 1);
        }

        private boolean contains (int n) {
            if (isTwoNode()) return (value1 == n);
            if (isThreeNode()) return (value1 == n || value2 == n);
            if (isFourNode()) return (value1 == n || value2 == n || value3 == n);
            return false;
        }

        private TwoFourTreeItem next (int n) {
            if (contains(n)) return null;
            if (isTwoNode()) {
                if (n < value1) return leftChild;
                if (n > value1) return rightChild;
            } else if (isThreeNode()) {
                if (n < value1) return leftChild;
                if (n > value1 && n < value2) return centerChild;
                if (n > value2) return rightChild;
            } else if (isFourNode()) {
                if (n < value1) return leftChild;
                if (n > value1 && n < value2) return centerLeftChild;
                if (n > value2 && n < value3) return centerRightChild;
                if (n > value3) return rightChild;
            }
            return null;
        }

        public boolean find (int n) {
            if (contains(n)) return true;
            TwoFourTreeItem next = next(n);
            if (next == null) return false;
            return next.find(n);
        }

        private TwoFourTreeItem splitUp () {
            TwoFourTreeItem l = new TwoFourTreeItem(value1);
            TwoFourTreeItem r = new TwoFourTreeItem(value3);
            l.leftChild = leftChild;
            l.rightChild = centerLeftChild;
            r.leftChild = centerRightChild;
            r.rightChild = rightChild;
            if (l.leftChild != null) l.leftChild.parent = l;
            if (l.rightChild != null) l.rightChild.parent = l;
            if (l.leftChild != null || l.rightChild != null) l.isLeaf = false;

            if (r.leftChild != null) r.leftChild.parent = r;
            if (r.rightChild != null) r.rightChild.parent = r;
            if (r.leftChild != null || r.rightChild != null) r.isLeaf = false;

            if (isRoot()) {
                value1 = value2;
                value2 = 0;
                value3 = 0;
                values = 1;
                leftChild = l;
                rightChild = r;
                l.parent = this;
                r.parent = this;
                centerLeftChild = null;
                centerRightChild = null;
                isLeaf = false;
                return this;
            }

            l.parent = parent;
            r.parent = parent;
            
            if (parent.isTwoNode()) {
                if (parent.leftChild == this) {
                    parent.value2 = parent.value1;
                    parent.value1 = value2;
                    parent.values = 2;
                    parent.leftChild = l;
                    parent.centerChild = r;
                    return parent;
                } else if (parent.rightChild == this) {
                    parent.value2 = value2;
                    parent.values = 2;
                    parent.centerChild = l;
                    parent.rightChild = r;
                    return parent;
                }
            } else if (parent.isThreeNode()) {
                if (parent.leftChild == this) {
                    parent.value3 = parent.value2;
                    parent.value2 = parent.value1;
                    parent.value1 = value2;
                    parent.values = 3;
                    parent.centerRightChild = parent.centerChild;
                    parent.centerChild = null;
                    parent.leftChild = l;
                    parent.centerLeftChild = r;
                    return parent;
                } else if (parent.centerChild == this) {
                    parent.value3 = parent.value2;
                    parent.value2 = value2;
                    parent.values = 3;
                    parent.centerLeftChild = l;
                    parent.centerRightChild = r;
                    parent.centerChild = null;
                    return parent;
                } else if (parent.rightChild == this) {
                    parent.value3 = value2;
                    parent.values = 3;
                    parent.centerLeftChild = parent.centerChild;
                    parent.centerChild = null;
                    parent.centerRightChild = l;
                    parent.rightChild = r;
                    return parent;
                }
            }
            return null;
        }

        private boolean insert (int n) {
            if (isFourNode() || contains(n) || !isLeaf) return false;
            if (isTwoNode()) {
                if (n < value1) {
                    value2 = value1;
                    value1 = n;
                    values = 2;
                    return true;
                } else if (n > value1) {
                    value2 = n;
                    values = 2;
                    return true;
                }
            } else if (isThreeNode()) {
                if (n < value1) {
                    value3 = value2;
                    value2 = value1;
                    value1 = n;
                    values = 3;
                    return true;
                } else if (n > value1 && n < value2) {
                    value3 = value2;
                    value2 = n;
                    values = 3;
                    return true;
                } else if (n > value2) {
                    value3 = n;
                    values = 3;
                    return true;
                }
            }
            return false;
        }

        public boolean addValue (int n) {
            if (isFourNode()) return splitUp().next(n).addValue(n);
            if (isLeaf) return insert(n);
            return next(n).addValue(n);
        }

        private TwoFourTreeItem leftSibling () {
            if (parent == null || parent.leftChild == this) return null;

            if (parent.isTwoNode()) {
                if (parent.rightChild == this) return parent.leftChild;
            } else if (parent.isThreeNode()) {
                if (parent.centerChild == this) return parent.leftChild;
                if (parent.rightChild == this) return parent.centerChild;
            } else if (parent.isFourNode()) {
                if (parent.centerLeftChild == this) return parent.leftChild;
                if (parent.centerRightChild == this) return parent.centerLeftChild;
                if (parent.rightChild == this) return parent.centerRightChild;
            }

            return null;
        }

        private TwoFourTreeItem rightSibling () {
            if (parent == null || parent.rightChild == this) return null;

            if (parent.isTwoNode()) {
                if (parent.leftChild == this) return parent.rightChild;
            } else if (parent.isThreeNode()) {
                if (parent.centerChild == this) return parent.rightChild;
                if (parent.leftChild == this) return parent.centerChild;
            } else if (parent.isFourNode()) {
                if (parent.centerLeftChild == this) return parent.centerRightChild;
                if (parent.centerRightChild == this) return parent.rightChild;
                if (parent.leftChild == this) return parent.centerLeftChild;
            }

            return null;
        }

        private boolean leftFuse () {
            if (parent == null || parent.isTwoNode() || !isTwoNode())
        }

        private boolean rightFuse () {

        }

        private boolean rotCW () {

        }

        private boolean rotCCW () {

        }

        private boolean deleteValue () {

        }
    }

    TwoFourTreeItem root = null;

    public boolean addValue(int value) {
        if (root == null) {
            root = new TwoFourTreeItem(value);
            return true;
        }
        return root.addValue(value);
    }

    public boolean hasValue(int value) {
        if (root == null) return false;
        return root.find(value);
    }

    public boolean deleteValue(int value) {
        return false;
    }

    public void printInOrder() {
        if(root != null) root.printInOrder(0);
    }

    public TwoFourTree() {

    }
}
