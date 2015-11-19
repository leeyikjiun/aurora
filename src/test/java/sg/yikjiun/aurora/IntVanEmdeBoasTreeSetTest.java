package sg.yikjiun.aurora;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * @author Lee Yik Jiun
 */
public class IntVanEmdeBoasTreeSetTest {
    Set<Integer> set;
    List<Integer> integers;

    @Before
    public void setUp() throws Exception {
        set = new IntVanEmdeBoasTreeSetV3();

        integers = new ArrayList<>();
        for (int i = 0; i <= 100; ++i) {
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
            assertTrue(set.add(integer));
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
}