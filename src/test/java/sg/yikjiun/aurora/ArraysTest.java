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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;

/**
 * @author Lee Yik Jiun
 */
public class ArraysTest {
    @Test
    public void testLsdRadixSort() {
        int n = 10;

        List<Integer> list = new ArrayList<Integer>(n);
        for (int i = 0; i < n; ++i) {
            list.add(i);
        }
        Collections.shuffle(list);

        int[] control = new int[n];
        int[] test = new int[n];
        for (int i = 0; i < n; ++i) {
            control[i] = i;
            test[i] = list.get(i);
        }

        assertArrayEquals(control, Arrays.LsdRadixSort(test));
    }
}