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

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author Lee Yik Jiun
 */
public class SplayTreeSet<E extends Comparable<E>> implements Set<E> {
    Node<E> root = null;
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
        Node<E> node = insert(new Node<E>(e));
        if (node == null) {
            return false;
        }

        splay(node);
        ++size;
        return true;
    }

    @CheckReturnValue
    @Nullable
    private Node<E> insert(@Nonnull Node<E> newNode) {
        if (root == null) {
            root = newNode;
        } else {
            Node<E> parent = null;
            Node<E> node = root;
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
        Node<E> node = find(e);
        if (node == null) {
            return false;
        }
        --size;
        return remove(node);
    }

    private boolean remove(Node<E> node) {
        if (node.left != null && node.right != null) {
            Node<E> predecessor = node.getPredecessor();
            assert predecessor != null;
            node.e = predecessor.e;
            return remove(predecessor);
        }

        // 0 or 1 child
        Node<E> parent = node.parent;
        if (parent == null) {
            if (node.left != null) {
                root = node.left;
            } else if (node.right != null) {
                root = node.right;
            } else {
                root = null;
            }
        } else {
            Node child = node.left;
            if (child == null) {
                child = node.right;
            }
            if (node.isLeftChild()) {
                parent.linkLeft(child);
            } else {
                parent.linkRight(child);
            }
            splay(parent);
        }
        return true;
    }

    private void splay(Node<E> node) {
        Node<E> parent = node.parent;
        while (parent != null) {
            assert node.isValid();
            Node<E> grandparent = parent.parent;
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
    private Node<E> find(E e) {
        Node<E> node = root;
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
        for (Object o : c) {
            if (!contains(o)) {
                return false;
            }
        }
        return true;
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
        @Nullable Node<E> parent;
        @Nullable Node<E> left;
        @Nullable Node<E> right;

        public Node(E e) {
            this.e = e;
        }

        void zig() {
            assert parent != null;
            assert this.isLeftChild();
            Node<E> grandparent = parent.parent;
            boolean wasLeft = grandparent == null || parent.isLeftChild();
            parent.linkLeft(right);
            linkRight(parent);

            if (grandparent == null) {
                parent = null;
            } else {
                if (wasLeft) {
                    grandparent.linkLeft(this);
                } else {
                    grandparent.linkRight(this);
                }
            }
        }

        void zag() {
            assert parent != null;
            assert this.isRightChild();
            Node<E> grandparent = parent.parent;
            boolean wasLeft = grandparent == null || parent.isLeftChild();
            parent.linkRight(left);
            linkLeft(parent);

            if (grandparent == null) {
                parent = null;
            } else {
                if (wasLeft) {
                    grandparent.linkLeft(this);
                } else {
                    grandparent.linkRight(this);
                }
            }
        }

        void zigZig() {
            assert parent != null;
            assert this.isLeftChild();
            assert parent.isLeftChild();

            parent.zig();
            this.zig();
        }

        void zagZag() {
            assert parent != null;
            assert this.isRightChild();
            assert parent.isRightChild();

            parent.zag();
            this.zag();
        }

        void zigZag() {
            assert parent != null;
            Node<E> grandparent = parent.parent;
            assert grandparent != null;
            assert parent.isLeftChild();
            assert this.isRightChild();

            zag();
            zig();
        }

        void zagZig() {
            assert parent != null;
            Node<E> grandparent = parent.parent;
            assert grandparent != null;
            assert parent.isRightChild();
            assert this.isLeftChild();

            zig();
            zag();
        }

        void linkLeft(Node<E> child) {
            left = child;
            if (child != null) {
                assert left.e.compareTo(e) < 0;
                child.parent = this;
            }
        }

        void linkRight(Node<E> child) {
            right = child;
            if (child != null) {
                assert e.compareTo(right.e) < 0;
                child.parent = this;
            }
        }

        @Nullable
        Node<E> getPredecessor() {
            Node<E> prevNode = null;
            Node<E> node = left;
            while (node != null) {
                prevNode = node;
                node = node.right;
            }
            return prevNode;
        }

        @Nullable
        Node<E> getSuccessor() {
            Node<E> prevNode = null;
            Node<E> node = right;
            while (node != null) {
                prevNode = node;
                node = node.left;
            }
            return prevNode;
        }

        public boolean isLeftChild() {
            return this.equals(parent.left);
        }

        public boolean isLeftChild(Node<E> parent) {
            return this.parent.equals(parent) && this.equals(parent.left);
        }

        public boolean isRightChild() {
            return !isLeftChild();
        }

        public boolean isRightChild(Node<E> parent) {
            return this.parent.equals(parent) && this.equals(parent.right);
        }

        boolean isValid() {
            return
                (parent == null || (isLeftChild() && e.compareTo(parent.e) < 0) || (isRightChild() && parent.e.compareTo(e) < 0))
                    && (left == null || left.e.compareTo(e) < 0)
                    && (right == null || e.compareTo(right.e) < 0);
        }
    }
}
