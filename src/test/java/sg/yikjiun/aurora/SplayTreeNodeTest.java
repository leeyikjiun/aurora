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

import org.junit.Before;
import org.junit.Test;
import sg.yikjiun.aurora.SplayTreeSet.Node;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Lee Yik Jiun
 */
public class SplayTreeNodeTest {
    Node<Integer> grandparent, grandparentLeft, grandparentRight;
    Node<Integer> parent, parentLeft, parentRight;
    Node<Integer> node, left, right;

    @Before
    public void setUp() throws Exception {
        grandparent = grandparentLeft = grandparentRight = null;
        parent = parentLeft = parentRight = null;
        node = left = right = null;
    }

    @Test
    public void testZig() throws Exception {
        Node<Integer> parent = new Node<>(4);
        parent.linkRight(parentRight = new Node<>(5));
        parent.linkLeft(node = new Node<>(2));
        node.linkLeft(left = new Node<>(1));
        node.linkRight(right = new Node<>(3));

        node.zig();

        assertTrue(left.isLeftChild(node));
        assertTrue(parent.isRightChild(node));
        assertTrue(right.isLeftChild(parent));
        assertTrue(parentRight.isRightChild(parent));
    }

    @Test
    public void testZag() throws Exception {
        parent = new Node<>(2);
        parent.linkLeft(parentLeft = new Node<>(1));
        parent.linkRight(node = new Node<>(4));
        node.linkLeft(left = new Node<>(3));
        node.linkRight(right = new Node<>(5));

        node.zag();

        assertTrue(right.isRightChild(node));
        assertTrue(parent.isLeftChild(node));
        assertTrue(left.isRightChild(parent));
        assertTrue(parentLeft.isLeftChild(parent));
    }

    @Test
    public void testZigZig() throws Exception {
        grandparent = new Node<>(6);
        grandparent.linkRight(grandparentRight = new Node<>(7));
        grandparent.linkLeft(parent = new Node<>(4));
        parent.linkRight(parentRight = new Node<>(5));
        parent.linkLeft(node = new Node<>(2));
        node.linkLeft(left = new Node<>(1));
        node.linkRight(right = new Node<>(3));

        node.zigZig();

        assertTrue(left.isLeftChild(node));
        assertTrue(right.isLeftChild(parent));
        assertTrue(parentRight.isLeftChild(grandparent));
        assertTrue(grandparentRight.isRightChild(grandparent));
    }

    @Test
    public void testZagZag() throws Exception {
        grandparent = new Node<>(2);
        grandparent.linkLeft(grandparentLeft = new Node<>(1));
        grandparent.linkRight(parent = new Node<>(4));
        parent.linkLeft(parentLeft = new Node<>(3));
        parent.linkRight(node = new Node<>(6));
        node.linkLeft(left = new Node<>(5));
        node.linkRight(right = new Node<>(7));

        node.zagZag();

        assertTrue(right.isRightChild(node));
        assertTrue(parent.isLeftChild(node));
        assertTrue(left.isRightChild(parent));
        assertTrue(parentLeft.isRightChild(grandparent));
        assertTrue(grandparentLeft.isLeftChild(grandparent));
    }

    @Test
    public void testZigZag() throws Exception {
        grandparent = new Node<>(6);
        grandparent.linkRight(grandparentRight = new Node<>(7));
        grandparent.linkLeft(parent = new Node<>(2));
        parent.linkLeft(parentLeft = new Node<>(1));
        parent.linkRight(node = new Node<>(4));
        node.linkLeft(left = new Node<>(3));
        node.linkRight(right = new Node<>(5));

        node.zigZag();

        assertTrue(parentLeft.isLeftChild(parent));
        assertTrue(left.isRightChild(parent));
        assertTrue(right.isLeftChild(grandparent));
        assertTrue(grandparentRight.isRightChild(grandparent));
        assertTrue(parent.isLeftChild(node));
        assertTrue(grandparent.isRightChild(node));
    }

    @Test
    public void testZagZig() throws Exception {
        grandparent = new Node<>(2);
        grandparent.linkLeft(grandparentLeft = new Node<>(1));
        grandparent.linkRight(parent = new Node<>(6));
        parent.linkRight(parentRight = new Node<>(7));
        parent.linkLeft(node = new Node<>(4));
        node.linkLeft(left = new Node<>(3));
        node.linkRight(right = new Node<>(5));

        node.zagZig();

        assertTrue(parentRight.isRightChild(parent));
        assertTrue(right.isLeftChild(parent));
        assertTrue(left.isRightChild(grandparent));
        assertTrue(grandparentLeft.isLeftChild(grandparent));
        assertTrue(parent.isRightChild(node));
        assertTrue(grandparent.isLeftChild(node));
    }

    @Test
    public void testLinkLeft() throws Exception {
        node = new Node<>(2);
        node.linkLeft(left = new Node<>(1));

        assertEquals(left, node.left);
        assertEquals(node, left.parent);
    }

    @Test
    public void testLinkRight() throws Exception {
        node = new Node<>(1);
        node.linkRight(right = new Node<>(2));

        assertEquals(right, node.right);
        assertEquals(node, right.parent);
    }

    @Test
    public void testGetPredecessor() throws Exception {
        grandparent = new Node<>(6);
        grandparent.linkRight(grandparentRight = new Node<>(7));
        grandparent.linkLeft(parent = new Node<>(4));
        parent.linkRight(parentRight = new Node<>(5));
        parent.linkLeft(node = new Node<>(2));
        node.linkLeft(left = new Node<>(1));
        node.linkRight(right = new Node<>(3));

        Node predecessor = grandparent.getPredecessor();
        assertEquals(parentRight, predecessor);
    }

    @Test
    public void testGetSuccessor() throws Exception {
        grandparent = new Node<>(2);
        grandparent.linkLeft(grandparentLeft = new Node<>(1));
        grandparent.linkRight(parent = new Node<>(4));
        parent.linkLeft(parentLeft = new Node<>(3));
        parent.linkRight(node = new Node<>(6));
        node.linkLeft(left = new Node<>(5));
        node.linkRight(right = new Node<>(7));

        Node successor = grandparent.getSuccessor();
        assertEquals(parentLeft, successor);
    }

    @Test
    public void testIsLeftChild() throws Exception {
        node = new Node<>(2);
        node.linkLeft(left = new Node<>(1));

        assertTrue(left.isLeftChild());
        assertTrue(left.isLeftChild(node));
    }

    @Test
    public void testIsRightChild() throws Exception {
        node = new Node<>(1);
        node.linkRight(right = new Node<>(2));

        assertTrue(right.isRightChild());
        assertTrue(right.isRightChild(node));
    }
}