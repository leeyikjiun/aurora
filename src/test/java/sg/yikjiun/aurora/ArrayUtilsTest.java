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

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;

/**
 * @author Lee Yik Jiun
 */
public class ArrayUtilsTest {
    @Test
    public void testFisherYatesShuffle() {
        int n = 10;
        int[] control = new int[n];
        for (int i = 0; i < n; ++i) {
            control[i] = i;
        }
        int[] test = Arrays.copyOf(control, n);

        ArrayUtils.fisherYatesShuffle(test);
        assertFalse(Arrays.equals(control, test));
    }

    @Test
    public void testLsdRadixSort() {
        int n = 10;
        int[] control = new int[n];
        for (int i = 0; i < n; ++i) {
            control[i] = i;
        }
        int[] test = Arrays.copyOf(control, n);
        ArrayUtils.fisherYatesShuffle(test);

        assertArrayEquals(control, ArrayUtils.LsdRadixSort(test));
    }

    @Test
    public void testReverse() {
        int n = 10;
        int[] control = new int[n];
        int[] test = new int[n];
        for (int i = 0; i < n; ++i) {
            control[i] = i;
            test[i] = n - i - 1;
        }
        ArrayUtils.reverse(test);

        assertArrayEquals(control, test);
    }
}