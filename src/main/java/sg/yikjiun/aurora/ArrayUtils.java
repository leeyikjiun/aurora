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

import java.util.Collections;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.CheckReturnValue;

/**
 * @author Lee Yik Jiun
 */
public class ArrayUtils {
    /**
     * Shuffles an array using Fisher-Yates shuffle.
     *
     * Time: O(n)
     * Space: O(1)
     * where n is the number of elements in the array.
     */
    public static void fisherYatesShuffle(int[] nums) {
        Random r = ThreadLocalRandom.current();
        int n = nums.length;
        for (int i = n-1; i > 1; --i) {
            int j = r.nextInt(i + 1);
            swap(nums, i, j);
        }
    }

    private static void swap(int[] nums, int i, int j) {
        int t = nums[i];
        nums[i] = nums[j];
        nums[j] = t;
    }

    /**
     * Returns a sorted array using counting sort.
     * 
     * Only works if k is an integer (i.e. Range of values not too huge)
     * Time: O(k + n)
     * Space: O(k + n)
     * where n is the number of elements in the array to be sorted
     *       k is the difference between smallest and largest value
     */
    @CheckReturnValue
    public static int[] countingSort(int[] nums) {
        int n = nums.length;
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;

        // Check range of values by extracting min/max: O(n)
        for (int num : nums) {
            min = Math.min(min, num);
            max = Math.max(max, num);
        }

        // Count instances of each value: O(n)
        int k = max - min + 1;
        int[] countArr = new int[k];
        for (int num : nums) {
            ++countArr[num - min];
        }

        int[] sortedNums = new int[n];
        for (int i = 0, j = 0; i < k; ++i) {
            while (countArr[i]-- > 0) {
                sortedNums[j++] = i + min;
            }
        }
        return sortedNums;
    }

    /**
     * Returns a sorted array using lsd radix sort.
     *
     * Only works on non-negative integers.
     * Time: O(31n)
     * Space: O(n)
     * where n is the number of elements in the array to be sorted
     */
    @CheckReturnValue
    public static int[] lsdRadixSort(int[] nums) {
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

    public static void nextPermutation(int[] nums) {
        int n = nums.length;
        int i;
        for (i = n - 2; i >= 0; --i) {
            if (nums[i] < nums[i + 1]) {
                break;
            }
        }
        if (i < 0) {
            return;
        }
        int j;
        for (j = n - 1; j > i; --j) {
            if (nums[i] < nums[j]) {
                break;
            }
        }
        swap(nums, i, j);
        reverse(nums, i + 1);
    }

    /**
     * Reverses an array
     *
     * Time: O(n)
     * Space: O(1)
     * where n is the number of elements in the array.
     */
    public static void reverse(int[] nums) {
        reverse(nums, 0);
    }

    /**
     * Reverses an array.
     *
     * Time: O(n)
     * Space: O(1)
     * where n is the number of elements in the array.
     * @param beginIndex the begin index, inclusive.
     */
    public static void reverse(int[] nums, int beginIndex) {
        reverse(nums, beginIndex, nums.length);
    }

    /**
     * Reverses an array.
     *
     * Time: O(n)
     * Space: O(1)
     * where n is the number of elements in the array.
     * @param beginIndex the begin index, inclusive.
     * @param endIndex the end index, exclusive.
     */
    public static void reverse(int[] nums, int beginIndex, int endIndex) {
        int i = beginIndex;
        int j = endIndex - 1;

        while (i < j) {
            swap(nums, i++, j--);
        }
    }
}
