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

        // Returns whether or not value is contained in current node
        // Parameters: int n: value to be looked for in current node
        // If value not in node returns false
        private boolean contains (int n) {
            if (isTwoNode()) return (value1 == n);
            else if (isThreeNode()) return (value1 == n || value2 == n);
            else if (isFourNode()) return (value1 == n || value2 == n || value3 == n); // If atleast one of the values in the node equals n return true
            return false;
        }

        // Returns child that contains the interval of value n
        // Parameters: int n: value that exists within node's interval
        // If value is already contained in node then returns null
        private TwoFourTreeItem next (int n) {
            if (contains(n)) return null; // If current node contains n then there is no other node to traverse to
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

        // Recursively searches tree in order to find a particular node
        // If node doesn't exist returns false otherwise returns true
        public boolean find (int n) {
            if (contains(n)) return true; // If n in current node stop
            TwoFourTreeItem next = next(n);
            if (next == null) return false; // If there isn't another node to traverse to stop
            return next.find(n); // Go to next node in tree and continue
        }

        // Splits four node into 3 two nodes, the middle value being the top node and the left and right values being its children
        // If node is root, center value becomes a two node and becomes the new root, otherwise center value gets inserted into parent
        // Returns top of newly split node
        // If an attempt is made to split on a node that is not a four node then will return null
        private TwoFourTreeItem splitUp () {
            TwoFourTreeItem l = new TwoFourTreeItem(value1); // New left and right nodes containing left and right values of four node
            TwoFourTreeItem r = new TwoFourTreeItem(value3);

            l.leftChild = leftChild; // New left and right nodes get assigned four node's previous children
            l.rightChild = centerLeftChild;
            r.leftChild = centerRightChild;
            r.rightChild = rightChild;
            if (l.leftChild != null) l.leftChild.parent = l; // Assign children's parents and classify whether they're leaves
            if (l.rightChild != null) l.rightChild.parent = l;
            if (l.leftChild != null || l.rightChild != null) l.isLeaf = false;

            if (r.leftChild != null) r.leftChild.parent = r;
            if (r.rightChild != null) r.rightChild.parent = r;
            if (r.leftChild != null || r.rightChild != null) r.isLeaf = false;

            if (isRoot()) { // If root split middle value up and make its left and right children l and r
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

            l.parent = parent; // l and r's parents should be previous four node's parents
            r.parent = parent;
            
            if (parent.isTwoNode()) { 
                if (parent.leftChild == this) {
                    parent.insert(value2); // Insert middle value into parent
                    parent.leftChild = l; // Assign parent its new children
                    parent.centerChild = r;
                    return parent;
                } else if (parent.rightChild == this) {
                    parent.insert(value2); // Insert middle value into parent
                    parent.centerChild = l; // Assign parent its new children
                    parent.rightChild = r;
                    return parent;
                }
            } else if (parent.isThreeNode()) {
                if (parent.leftChild == this) {
                    parent.insert(value2); // Insert middle value into parent
                    parent.centerRightChild = parent.centerChild; // Assign parent its new children
                    parent.centerChild = null;
                    parent.leftChild = l;
                    parent.centerLeftChild = r;
                    return parent;
                } else if (parent.centerChild == this) {
                    parent.insert(value2); // Insert middle value into parent
                    parent.centerLeftChild = l;
                    parent.centerRightChild = r;
                    parent.centerChild = null;
                    return parent;
                } else if (parent.rightChild == this) {
                    parent.insert(value2); // Insert middle value into parent
                    parent.centerLeftChild = parent.centerChild; // Assign parent its new children
                    parent.centerChild = null;
                    parent.centerRightChild = l;
                    parent.rightChild = r;
                    return parent;
                }
            }
            return null;
        }

        // Inserts value n into node, shift over other values to make space
        // Parameters: int n: value to be inserted into node
        // Cannot insert four node, if an attempt is made returns false, otherwise returns true
        private boolean insert (int n) {
            if (isFourNode() || contains(n)) return false; // Can't insert into four nodes, won't insert into nodes that already contain value you're inserting
            if (isTwoNode()) {
                if (n < value1) { // Shift right and add n
                    value2 = value1;
                    value1 = n;
                    values = 2;
                    return true;
                } else if (n > value1) { // Add n to value 2
                    value2 = n;
                    values = 2;
                    return true;
                }
            } else if (isThreeNode()) {
                if (n < value1) { // Shift right and add n
                    value3 = value2;
                    value2 = value1;
                    value1 = n;
                    values = 3;
                    return true;
                } else if (n > value1 && n < value2) { // Shift right and add n
                    value3 = value2;
                    value2 = n;
                    values = 3;
                    return true;
                } else if (n > value2) { // Add n to value 3
                    value3 = n;
                    values = 3;
                    return true;
                }
            }
            return false;
        }

        // Traverses tree properly adding value into tree
        // Parameters: int n: value to be inserted into tree
        public boolean addValue (int n) {
            if (isFourNode()) return splitUp().next(n).addValue(n); // Split four node, middle value going up, go to next node and continue
            if (isLeaf) return insert(n); // If is leaf insert into the leaf
            return next(n).addValue(n); // Go to next node and continue
        }

        // Returns sibling to the left of current node
        // Returns a node item
        // If node doesn't have a right sibling, i.e is leftmost child in parent node then returns null
        private TwoFourTreeItem leftSibling () {
            if (parent == null || parent.leftChild == this) return null; // Not possible for root to have siblings or for left children to have left siblings

            if (parent.isTwoNode()) { // Returns child immediately to the left of the current child
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

        // Returns sibling to the right of current node
        // Returns a node item
        // If node doesn't have a right sibling, i.e is rightmost child in parent node then returns null
        private TwoFourTreeItem rightSibling () {
            if (parent == null || parent.rightChild == this) return null; // Not possible for root to have siblings or for right children to have right siblings

            if (parent.isTwoNode()) { // Returns child immediately to the right of the current child
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

        // Removes value n from node
        // Parameters: int n: value in node to be deleted
        // n must exist in node otherwise will return false
        // Returns true if removal was successful, false otherwise. Doesn't rearrange children
        private boolean remove (int n) {
            if (isTwoNode() || !contains(n)) return false; // Cannot remove from two node or remove a value that doesn't exist
            if (isThreeNode()) { // Delete value x and shift proceeding values left
                if (value1 == n) {
                    value1 = value2;
                    value2 = 0;
                    values = 1;
                    return true;
                } else if (value2 == n) {
                    value2 = 0;
                    values = 1;
                    return true;
                }
            } else if (isFourNode()) {
                if (value1 == n) {
                    value1 = value2;
                    value2 = value3;
                    value3 = 0;
                    values = 2;
                    return true;
                } else if (value2 == n) {
                    value2 = value3;
                    value3 = 0;
                    values = 2;
                    return true;
                } else if (value3 == n) {
                    value3 = 0;
                    values = 2;
                    return true;
                }
            }

            return false;
        }

        // Fuses current node's left sibling and the parent value between the two nodes into current node
        // If current node doesn't have a parent or doesn't have a left sibling or its left sibling isn't a
        // two node or current node isn't a two node or parent node is a two node then returns false
        // Returns true if fuse was successful, false if above conditions aren't met
        private boolean leftFuse () {
            TwoFourTreeItem ls = leftSibling();
            if (parent == null || parent.isTwoNode() || !isTwoNode() || ls == null || !ls.isTwoNode()) return false;

            // System.out.println(value1 + ", " + value2 + ", " + value3);

            centerRightChild = leftChild; // Rearrange children
            centerLeftChild = ls.rightChild;
            leftChild = ls.leftChild;
            if (centerLeftChild != null) centerLeftChild.parent = this; // Reassign children's new parent; this
            if (leftChild != null) leftChild.parent = this;

            if (parent.isThreeNode()) { // Rearrange parent's values and children
                if (parent.centerChild == this) {
                    value3 = value1;
                    value2 = parent.value1;
                    value1 = ls.value1;
                    values = 3;
                    parent.remove(parent.value1);
                    parent.centerChild = null;
                    parent.leftChild = this;
                    return true;
                } else if (parent.rightChild == this) {
                    value3 = value1;
                    value2 = parent.value2;
                    value1 = ls.value1;
                    values = 3;
                    parent.remove(parent.value2);
                    parent.centerChild = null;
                    parent.rightChild = this;
                    return true;
                }
            } else if (parent.isFourNode()) {
                if (parent.centerLeftChild == this) {
                    value3 = value1;
                    value2 = parent.value1;
                    value1 = ls.value1;
                    values = 3;
                    parent.remove(parent.value1);
                    parent.leftChild = this;
                    parent.centerLeftChild = null;
                    parent.centerChild = parent.centerRightChild;
                    parent.centerRightChild = null;
                    return true;
                } else if (parent.centerRightChild == this) {
                    value3 = value1;
                    value2 = parent.value2;
                    value1 = ls.value1;
                    values = 3;
                    parent.remove(parent.value2);
                    parent.centerChild = this;
                    parent.centerLeftChild = null;
                    parent.centerRightChild = null;
                    return true;
                } else if (parent.rightChild == this) {
                    value3 = value1;
                    value2 = parent.value3;
                    value1 = ls.value1;
                    values = 3;
                    parent.remove(parent.value3);
                    parent.centerChild = parent.centerLeftChild;
                    parent.centerLeftChild = null;
                    parent.centerRightChild = null;
                    return true;                    
                }
            }

            return false;
        }

        // Fuses current node's right sibling and the parent value between the two nodes into current node
        // If current node doesn't have a parent or doesn't have a right sibling or its right sibling isn't a
        // two node or current node isn't a two node or parent node is a two node then returns false
        // Returns true if fuse was successful, false if above conditions aren't met
        private boolean rightFuse () {
            TwoFourTreeItem rs = rightSibling();
            if (parent == null || parent.isTwoNode() || !isTwoNode() || rs == null || !rs.isTwoNode()) return false;

            centerLeftChild = rightChild; // Former right child becomes center left child of new four node
            centerRightChild = rs.leftChild; // Right sibling's left child becomes center right child of new four node
            rightChild = rs.rightChild; // Right sibling's right child becomes right child of new four node
            if (centerRightChild != null) centerRightChild.parent = this; // Right siblings former children's new parent is this
            if (rightChild != null) rightChild.parent = this;

            if (parent.isThreeNode()) {
                if (parent.leftChild == this) {
                    value2 = parent.value1; // N P R, the values in the new four node, P being the first value of parent
                    value3 = rs.value1;
                    values = 3;
                    parent.remove(parent.value1); // Parent loses the value between this and right sibling
                    parent.leftChild = this; // Parent's left child is new four node
                    parent.centerChild = null; // Parent becomes two node so no center child
                    return true;
                } else if (parent.centerChild == this) {
                    value2 = parent.value2; // N P R, the values in the new four node, P being the second value of parent
                    value3 = rs.value1;
                    values = 3;
                    parent.remove(parent.value2); // Parent loses value between this and right sibling
                    parent.rightChild = this; // Parent's right child is new four node
                    parent.centerChild = null; // Parent becomes two node so no center child
                    return true;
                }
            } else if (parent.isFourNode()) {
                if (parent.leftChild == this) {
                    value2 = parent.value1; // N P R, the values in the new four node, P being the first value of parent
                    value3 = rs.value1;
                    values = 3;
                    parent.remove(parent.value1); // Parent loses value between this and right sibling
                    parent.leftChild = this; // Parent's left child becomes this
                    parent.centerChild = parent.centerRightChild;
                    parent.centerLeftChild = null; // Parent loses center left and right pointers because it's now a three node
                    parent.centerRightChild = null;
                    return true;
                } else if (parent.centerLeftChild == this) {
                    value2 = parent.value2; // N P R, the values in the new four node, P being the second value of parent
                    value3 = rs.value1;
                    values = 3;
                    parent.remove(parent.value2); // Parent loses value between this and right sibling
                    parent.centerChild = this; // Parents center child becomes this
                    parent.centerLeftChild = null; // Parent loses center left and right pointers because it's now a three node
                    parent.centerRightChild = null;
                    return true;
                } else if (parent.centerRightChild == this) {
                    value2 = parent.value3; // N P R, the values in the new four node, P being the third value of parent
                    value3 = rs.value1;
                    values = 3;
                    parent.remove(parent.value3); // Parent loses value between this and right sibling
                    parent.rightChild = this; // Parents right child becomes this
                    parent.centerChild = parent.centerLeftChild;
                    parent.centerLeftChild = null; // Parent loses center left and right pointers because it's now a three node
                    parent.centerRightChild = null;
                    return true;
                }
            }

            return false;
        }

        // Replaces a certain value in current node with a new value
        // Parameters: int x: value in node to be replaced, int y: value that will replace x
        // If x does not exist in node then returns false, otherwise returns true
        private boolean replace (int x, int y) {
            if (!contains(x)) return false; // Cannot a replace a value that doesn't exist

            if (isTwoNode()) { // Finds value in node, x and replaces it with y
                if (value1 == x) value1 = y;
            } else if (isThreeNode()) {
                if (value1 == x) value1 = y;
                else if (value2 == x) value2 = y;
            } else if (isFourNode()) {
                if (value1 == x) value1 = y;
                else if (value2 == x) value2 = y;
                else if (value3 == x) value3 = y;
            }

            return true;
        }

        // Returns value that is immediately to the left of n in a node
        // Parameters: int n: value in node, whos left neighboring value gets returned
        // If n is a leftmost value there cannot be value to the left of it and therefore returns false
        private int immediateleft (int n) {
            if (contains(n)) return -1; // There is no immediate left value if value exists in node

            if (isTwoNode()) { // Finds value that is immediately to the left of n in the node
                if (n > value1) return value1;
            } else if(isThreeNode()) {
                if (n > value1 && n < value2) return value1;
                else if (n > value2) return value2; 
            } else if (isFourNode()) {
                if (n > value1 && n < value2) return value1;
                else if (n > value2 && n < value3) return value2;
                else if (n > value3) return value3;
            }

            return -1;
        }

        // Returns value that is immediately to the right of n in a node
        // Parameters: int n: value in node, whos right neighboring value gets returned
        // If n is a rightmost value there cannot be value to the right of it and therefore returns false
        private int immediateRight (int n) {
            if (contains(n)) return -1; // There is no immediate right value if value exists in node

            if (isTwoNode()) { // Finds value that is immediately to the right of n in the node
                if (n < value1) return value1;
            } else if(isThreeNode()) {
                if (n > value1 && n < value2) return value2;
                else if (n < value1) return value1; 
            } else if (isFourNode()) {
                if (n > value1 && n < value2) return value2;
                else if (n > value2 && n < value3) return value3;
                else if (n < value1) return value1;
            }

            return -1;
        }

        // Returns the right most value of a node
        // If an attempt is made to retrieve the rightmost value of anything that isn't a 2,3 or 4 node returns -1
        private int rightMostValue () {
            if (isTwoNode()) return value1;
            else if (isThreeNode()) return value2;
            else if (isFourNode()) return value3;

            return -1;
        }

        // Makes current node a three node from a two node by rotating parents value and left sibling's value clockwise
        // Returns true if rotation was successful false otherwise
        // Will not rotate if current node is not a two node, doesn't have a parent, or who's left sibling is a two node
        private boolean rotCW () {
            TwoFourTreeItem ls = leftSibling();

            if (parent == null || ls == null || ls.isTwoNode() || !isTwoNode()) return false;

            int pilv = parent.immediateleft(value1);
            int lsrm = ls.rightMostValue();

            parent.replace(pilv, lsrm); // Replace parent's immediate left value with left siblings right most value
            ls.remove(lsrm); // Remove left siblings right most value
            insert(pilv); // Insert parent's immediate left value into current node

            centerChild = leftChild; // Shift children right
            leftChild = ls.rightChild; // Steal left sibling's child
            if (leftChild != null) leftChild.parent = this;
            
            if (ls.isTwoNode()) { // Shift left sibling's children right
                ls.rightChild = ls.centerChild;
                ls.centerChild = null;
            } else if (ls.isThreeNode()) {
                ls.rightChild = ls.centerRightChild;
                ls.centerChild = ls.centerLeftChild;
                ls.centerLeftChild = null;
                ls.centerRightChild = null;
            }

            return true;
        }

        // Makes current node a three node from a two node by rotating parents value and right sibling's value counter clockwise
        // Returns true if rotation was successful false otherwise
        // Will not rotate if current node is not a two node, doesn't have a parent, or who's right sibling is a two node
        private boolean rotCCW () {
            TwoFourTreeItem rs = rightSibling();

            if (parent == null || rs == null || rs.isTwoNode() || !isTwoNode()) return false;

            int pirv = parent.immediateRight(value1); // Parent's value that is immediately to the right of current node's only value
            int rslm = rs.value1; // Right sibling's left most value

            parent.replace(pirv, rslm); // Replace parent's right immediate with right right sibling's left most
            rs.remove(rslm); // Remove right sibling's left most value
            insert(pirv); // Insert parent's right immediate into current node

            centerChild = rightChild; // Shift left children
            rightChild = rs.leftChild; // Steal right siblings left most child
            if(rightChild != null) rightChild.parent = this;

            if (rs.isTwoNode()) { // Shift the right sibling's children left
                rs.leftChild = rs.centerChild;
                rs.centerChild = null;
            } else if (rs.isThreeNode()) {
                rs.leftChild = rs.centerLeftChild;
                rs.centerChild = rs.centerRightChild;
                rs.centerLeftChild = null;
                rs.centerRightChild = null;
            }

            return true;
        }

        // Fuses root into a four node and only the root iff it has two children that are two nodes and itself is a two node
        // Returns true if fuse was successful, false if conditions above weren't met.
        private boolean fuseRoot () {
            if (!isRoot() || !isTwoNode() || leftChild == null || rightChild == null || !leftChild.isTwoNode() || !rightChild.isTwoNode()) return false; // This, left and right children must be two nodes and this must be the root
            value2 = value1; // Merge children's values into this
            value1 = leftChild.value1;
            value3 = rightChild.value1;
            values = 3;
            if (leftChild.isLeaf && rightChild.isLeaf) isLeaf = true; // If both children were leaves then this is now a leaf
            centerLeftChild = leftChild.rightChild; // Adopt children's children
            leftChild = leftChild.leftChild;
            centerRightChild = rightChild.leftChild;
            rightChild = rightChild.rightChild;
            if (leftChild != null) leftChild.parent = this; // Children's new parent is this
            if (centerLeftChild != null) centerLeftChild.parent = this;
            if (centerRightChild != null) centerRightChild.parent = this;
            if (rightChild != null) rightChild.parent = this;
            return true;
        }

        // Returns child that is immediately to the right of the current node's value, n
        // Parameters: int n: value in node used in order to find immediate right child
        // If n doesn't exist or the child to the right is null it will return null
        private TwoFourTreeItem immediateRightChild (int n) {
            if (isTwoNode()) { // Compare n with each value to see which child is to the right of it
                if (n == value1) return rightChild;
            } else if (isThreeNode()) {
                if (n == value1) return centerChild;
                if (n == value2) return rightChild;
            } else if (isFourNode()) {
                if (n == value1) return centerLeftChild;
                if (n == value2) return centerRightChild;
                if (n == value3) return rightChild;
            }

            return null; // n doesn't exist in node
        }

        // Retrieves left most descdant of a value, fuses/rotates along the way
        // Will return null if node called on doesn't have a left child and isn't a leaf itself
        private TwoFourTreeItem leftmostDescendant () {
            if (isTwoNode()) fuseRotate();
            if (isLeaf) return this; // Stop, left most descendant which should be a leaf found
            return leftChild.leftmostDescendant(); // Otherwise, continue
        }

        // Decision tree for deciding whether to fuse or rotate and in what direction.
        // Cannot fuse or rotate on any node that isn't a two node, if attempted will return false, returns true if successful
        private boolean fuseRotate () {
            if (!isTwoNode()) return false;
            TwoFourTreeItem ls = leftSibling(), rs = rightSibling();

            if (rs != null && !rs.isTwoNode()) return rotCCW(); // Right sibling is not a two node -> Counter Clockwise Rotation
            else if (ls != null && !ls.isTwoNode()) return rotCW(); // Left sibling is not a two node -> Clockwise rotation
            else if (ls != null) return leftFuse(); // Fuse left
            else if (rs != null) return rightFuse(); // Fuse right

            return false;
        }

        // Deletes value from tree
        // Parameters: int n: Value to be deleted
        // If value doesn't exist in tree returns false, otherwise true
        public boolean deleteValue (int n) {
            if (isRoot()) fuseRoot(); // Fuse root if itself and its children are all two nodes
            TwoFourTreeItem next = next(n);
            if (isTwoNode()) fuseRotate(); // Fuse/rotate two nodes
            if (contains(n)) {
                if (isLeaf) return remove(n); // If target contained in leaf just remove it
                TwoFourTreeItem irc = immediateRightChild(n); // Goto target node's immediate right's left most descendant, fuse/rotate along the way
                if (irc.isTwoNode()) irc.fuseRotate();
                if (irc.contains(n)) irc.deleteValue(n); // irc fused into node containing value

                TwoFourTreeItem irlmd = irc.leftmostDescendant(); // Immediate right child's left most decendant
                replace(n, irlmd.value1); // Swap left most decendant's left most value with target node's target value
                irlmd.replace(irlmd.value1, n);
                return irlmd.remove(n);
            }

            return (next == null) ? false : next.deleteValue(n); // Next is null, value not in tree, otherwise continue
        }
    }

    TwoFourTreeItem root = null;

    // Adds value to tree, uses helper addValue
    public boolean addValue(int value) {
        if (root == null) { // If no root then create new root with new value
            root = new TwoFourTreeItem(value);
            return true;
        }
        return root.addValue(value); // Otherwise add value
    }

    // Searches for value in tree, uses helper find
    public boolean hasValue(int value) {
        if (root == null) return false; // If no root then value isn't in tree
        return root.find(value); // Otherwise find root
    }

    // Deletes value from tree, uses helper deleteValue
    public boolean deleteValue(int value) {
        if (root == null) return false; // If no root then no tree to delete value with, returns false
        return root.deleteValue(value); // Otherwise delete value
    }

    // Prints in order traversal of tree
    public void printInOrder() {
        if(root != null) root.printInOrder(0);
    }

    public TwoFourTree() {

    }
}
