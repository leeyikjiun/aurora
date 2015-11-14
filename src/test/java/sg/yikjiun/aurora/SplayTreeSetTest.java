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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Lee Yik Jiun
 */
public class SplayTreeSetTest {
    SplayTreeSet<Integer> set;
    List<Integer> integers;

    @Before
    public void setUp() throws Exception {
        set = new SplayTreeSet<>();

        integers = new ArrayList<>();
        for (int i = Integer.MIN_VALUE; i < Integer.MIN_VALUE + 100; ++i) {
            integers.add(i);
        }
        for (int i = -100; i <= 100; ++i) {
            integers.add(i);
        }
        for (int i = Integer.MAX_VALUE; i > Integer.MAX_VALUE - 100; --i) {
            integers.add(i);
        }
        Collections.shuffle(integers);
    }

    @Test
    public void test() {
        assertTrue(set.isEmpty());
        assertEquals(0, set.size());
        int size = 0;
        for (Integer integer : integers) {
            assertFalse(set.contains(integer));
            set.add(integer);
            assertTrue(set.root.e.equals(integer));
            assertTrue(set.contains(integer));
            assertFalse(set.isEmpty());
            assertEquals(++size, set.size());
        }
        for (Integer integer : integers) {
            assertFalse(set.isEmpty());
            assertTrue(set.contains(integer));
            assertTrue(set.remove(integer));
            assertFalse(set.contains(integer));
            assertEquals(--size, set.size());
        }
        assertTrue(set.isEmpty());
        assertEquals(0, set.size());
    }

    @Test
    public void testRemoveRoot() {
        set.add(1);
        assertTrue(set.remove(1));
    }

    @Test
    public void testRemoveInvalid() {
        set.add(1);
        assertFalse(set.remove(0));
    }

    @Test
    public void testRemoveWithNoChild() {
        set.add(4);
        set.add(3);
        assertTrue(set.contains(4));
        assertTrue(set.remove(4));
        assertFalse(set.contains(4));
    }

    @Test
    public void testRemoveWithRightChild() {
        set.add(4);
        set.add(3);
        set.add(2);
        assertTrue(set.contains(3));
        assertTrue(set.remove(3));
        assertFalse(set.contains(3));
    }

    @Test
    public void testRemoveWithLeftChild() {
        set.add(2);
        set.add(3);
        set.add(4);
        assertTrue(set.contains(3));
        assertTrue(set.remove(3));
        assertFalse(set.contains(3));
    }

    @Test
    public void testRemoveWithBothChild() {
        set.add(5);
        set.add(3);
        set.add(2);
        set.add(4);
        assertTrue(set.contains(4));
        assertTrue(set.remove(4));
        assertFalse(set.contains(4));
    }
}