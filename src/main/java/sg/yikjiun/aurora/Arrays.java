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

/**
 * @author Lee Yik Jiun
 */
public class Arrays {
    /**
     * Returns a sorted array using lsd radix sort.
     *
     * Only works on non-negative integers.
     * Time: O(31n)
     * Space: O(n)
     * where n is the number of elements in the array to be sorted
     */
    public static int[] LsdRadixSort(int[] nums) {
        int n = nums.length;
        for (int i = 0, j = 1; i < 31; ++i, j <<= 1) {
            int[] tmpNums = new int[n];
            int numOnes = 0;
            for (int num : nums) {
                if ((num & j) == j) {
                    ++numOnes;
                }
            }
            int s = 0;
            int t = n - numOnes;
            for (int num : nums) {
                if ((num & j) == j) {
                    tmpNums[t++] = num;
                } else {
                    tmpNums[s++] = num;
                }
            }
            nums = tmpNums;
        }
        return nums;
    }
}
