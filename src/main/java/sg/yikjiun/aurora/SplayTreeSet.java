/*
 *    Copyright 2015 Lee Yik Jiun
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package sg.yikjiun.aurora;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/**
 * @author Lee Yik Jiun
 */
public class SplayTreeSet<E extends Comparable<E>> implements Set<E> {
    Node root = null;
    private int size = 0;

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        E e = (E) o;
        return find(e) != null;
    }

    @Override
    public Iterator<E> iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    @Override
    public boolean add(E e) {
        Node node = insert(new Node(e));
        if (node == null) {
            return false;
        }

        splay(node);
        ++size;
        return true;
    }

    @CheckReturnValue
    @Nullable
    private Node insert(@Nonnull Node newNode) {
        if (root == null) {
            root = newNode;
        } else {
            Node parent = null;
            Node node = root;
            boolean isLeft = true;
            while (node != null) {
                parent = node;
                int cmp = node.e.compareTo(newNode.e);
                if (cmp < 0) {
                    node = node.right;
                    isLeft = false;
                } else if (cmp == 0) {
                    return null;
                } else if (cmp > 0) {
                    node = node.left;
                    isLeft = true;
                }
            }
            if (isLeft) {
                parent.linkLeft(newNode);
            } else {
                parent.linkRight(newNode);
            }
        }
        return newNode;
    }

    @Override
    public boolean remove(Object o) {
        E e = (E) o;
        Node node = find(e);
        if (node == null) {
            return false;
        }
        --size;
        return remove(node);
    }

    private boolean remove(Node node) {
        System.out.println("node: " + node.e);
        if (node.left != null && node.right != null) {
            Node predecessor = node.getPredecessor();
            node.e = predecessor.e;
            return remove(predecessor);
        }
        Node parent = node.parent;
        if (node.left != null) {
            parent.linkLeft(node.left);
        } else if (node.right != null) {
            parent.linkRight(node.right);
        } else if (node.isLeftChild()) {
            parent.left = null;
        } else {
            parent.right = null;
        }
        splay(parent);
        return true;
    }

    private void splay(Node node) {
        Node parent = node.parent;
        while (parent != null) {
            Node grandparent = parent.parent;
            if (grandparent == null) {
                if (node.isLeftChild()) {
                    node.zig();
                } else {
                    node.zag();
                }
            } else if (parent.isLeftChild()) {
                if (node.isLeftChild()) {
                    node.zigZig();
                } else {
                    node.zigZag();
                }
            } else if (parent.isRightChild()) {
                if (node.isRightChild()) {
                    node.zagZag();
                } else {
                    node.zagZig();
                }
            }
            parent = node.parent;
        }
        root = node;
    }

    @Nullable
    private Node find(E e) {
        Node node = root;
        while (node != null) {
            int cmp = node.e.compareTo(e);
            if (cmp < 0) {
                node = node.right;
            } else if (cmp == 0) {
                break;
            } else if (cmp > 0) {
                node = node.left;
            }
        }
        return node;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    static class Node<E extends Comparable<E>> {
        E e;
        Node parent;
        Node left;
        Node right;

        public Node(E e) {
            this.e = e;
        }

        void zig() {
            assert this.isLeftChild();
            Node grandparent = parent.parent;
            parent.linkLeft(right);
            linkRight(parent);

            if (grandparent == null) {
                parent = null;
            } else {
                grandparent.linkRight(this);
            }
        }

        void zag() {
            assert this.isRightChild();
            Node grandparent = parent.parent;
            parent.linkRight(left);
            linkLeft(parent);

            if (grandparent == null) {
                parent = null;
            } else {
                grandparent.linkLeft(this);
            }
        }

        void zigZig() {
            assert this.isLeftChild();
            assert parent.isLeftChild();

            parent.zig();
            this.zig();
        }

        void zagZag() {
            assert this.isRightChild();
            assert parent.isRightChild();

            parent.zag();
            this.zag();
        }

        void zigZag() {
            Node grandparent = parent.parent;
            assert grandparent != null;
            assert parent.isLeftChild();
            assert this.isRightChild();

            zag();
            zig();
        }

        void zagZig() {
            Node grandparent = parent.parent;
            assert grandparent != null;
            assert parent.isRightChild();
            assert this.isLeftChild();

            zig();
            zag();
        }

        void linkLeft(Node child) {
            left = child;
            if (child != null) {
                child.parent = this;
            }
        }

        void linkRight(Node child) {
            right = child;
            if (child != null) {
                child.parent = this;
            }
        }

        @Nullable
        Node getPredecessor() {
            Node prevNode = null;
            Node node = left;
            while (node != null) {
                prevNode = node;
                node = node.right;
            }
            return prevNode;
        }

        @Nullable
        Node getSuccessor() {
            Node prevNode = null;
            Node node = right;
            while (node != null) {
                prevNode = node;
                node = node.left;
            }
            return prevNode;
        }

        public boolean isLeftChild() {
            return this.equals(parent.left);
        }

        public boolean isLeftChild(Node parent) {
            return this.parent.equals(parent) && this.equals(parent.left);
        }

        public boolean isRightChild() {
            return !isLeftChild();
        }

        public boolean isRightChild(Node parent) {
            return this.parent.equals(parent) && this.equals(parent.right);
        }
    }
}
