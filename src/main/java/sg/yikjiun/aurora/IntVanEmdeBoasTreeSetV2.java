/*
 * Copyright 2015 Lee Yik Jiun
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package sg.yikjiun.aurora;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/**
 * @author Lee Yik Jiun
 */
public class IntVanEmdeBoasTreeSetV2 implements Set<Integer> {
    private final int numBits;
    private final int halfNumBits;
    private int size = 0;
    private int max = Integer.MIN_VALUE;
    private int min = Integer.MAX_VALUE;
    private IntVanEmdeBoasTreeSetV2 auxiliary;
    private IntVanEmdeBoasTreeSetV2[] children;

    public IntVanEmdeBoasTreeSetV2() {
        this(32);
    }

    public IntVanEmdeBoasTreeSetV2(int numBits) {
        this.numBits = numBits;
        halfNumBits = numBits >> 1;
    }

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
        int integer = (int) o;
        if (min > max) {
            return false;
        }

        if (integer == min || integer == max) {
            return true;
        }

        if (auxiliary == null || auxiliary.isEmpty()) {
            return false;
        }

        if (children == null) {
            return false;
        }

        int i = getHigherOrderBits(integer);
        IntVanEmdeBoasTreeSetV2 child = children[i];
        return child != null && child.contains(getLowerOrderBits(integer));
    }

    private int getHigherOrderBits(int i) {
        assert numBits == 32 || i < (1 << numBits);
        return i >> halfNumBits;
    }

    private int getLowerOrderBits(int i) {
        assert numBits == 32 || i < (1 << numBits);
        return i & (1 << halfNumBits)-1;
    }

    private int getBits(int higherOrderBits, int lowerOrderBits) {
        return (higherOrderBits << halfNumBits) | lowerOrderBits;
    }

    @Override
    public Iterator<Integer> iterator() {
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
    public boolean add(Integer integer) {
        // empty
        if (min > max) {
            min = max = integer;
            ++size;
            return true;
        }

        if (min == max) {
            if (min == integer && integer == max) {
                return false;
            }

            if (integer < min) {
                min = integer;
            } else if (integer > max) {
                max = integer;
            }
            ++size;
            return true;
        }

        if (integer < min) {
            int tmp = integer;
            integer = (int) min;
            min = tmp;
        } else if (integer > max) {
            int tmp = integer;
            integer = max;
            max = tmp;
        }
        int i = getHigherOrderBits(integer);
        if (children == null) {
            children = new IntVanEmdeBoasTreeSetV2[1 << halfNumBits];
        }
        if (children[i] == null) {
            children[i] = new IntVanEmdeBoasTreeSetV2(halfNumBits);
        }
        IntVanEmdeBoasTreeSetV2 child = children[i];
        child.add(getLowerOrderBits(integer));
        if (child.min == child.max) {
            if (auxiliary == null) {
                auxiliary = new IntVanEmdeBoasTreeSetV2(halfNumBits);
            }
            auxiliary.add(i);
        }
        ++size;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int integer = (int) o;
        if (min > max || integer < min || integer > max) {
            return false;
        }

        if (min == integer && integer == max) {
            min = Integer.MAX_VALUE;
            max = Integer.MIN_VALUE;
            --size;
            assert isEmpty();
            return true;
        }

        int i;
        if (integer == min) {
            if (auxiliary == null || auxiliary.isEmpty()) {
                min = max;
                --size;
                return true;
            } else {
                i = auxiliary.min;
                integer = children[i].min;
                min = getBits(i, integer);
            }
        } else if (integer == max) {
            if (auxiliary == null || auxiliary.isEmpty()) {
                max = min;
                --size;
                return true;
            } else {
                i = auxiliary.max;
                integer = children[i].max;
                max = getBits(i, integer);
            }
        } else if (auxiliary == null || auxiliary.isEmpty()) {
            return false;
        } else {
            i = getHigherOrderBits(integer);
            integer = getLowerOrderBits(integer);
        }
        IntVanEmdeBoasTreeSetV2 child = children[i];
        if (child == null) {
            return false;
        }

        child.remove(integer);
        if (child.isEmpty()) {
            auxiliary.remove(i);
        }
        --size;
        return true;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends Integer> c) {
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

    }
}
