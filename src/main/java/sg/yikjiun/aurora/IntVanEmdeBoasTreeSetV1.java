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
public class IntVanEmdeBoasTreeSetV1 implements Set<Integer> {
    private long M = 1L << 32;
    private int sqrtM = 1 << 16;
    private int size = 0;
    private int max = -1;
    private long min = M;
    private IntVanEmdeBoasTreeSetV1 auxiliary;
    private IntVanEmdeBoasTreeSetV1[] children;

    public IntVanEmdeBoasTreeSetV1() {
        this(1L << 32);
    }

    public IntVanEmdeBoasTreeSetV1(long M) {
        this.M = M;
        sqrtM = (int) Math.sqrt(M);
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

        int i = (int) Math.floor(integer / sqrtM);
        IntVanEmdeBoasTreeSetV1 child = children[i];
        return child != null && child.contains(integer % sqrtM);
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
        int i = (int) Math.floor(integer / sqrtM);
        if (children == null) {
            children = new IntVanEmdeBoasTreeSetV1[sqrtM];
        }
        if (children[i] == null) {
            children[i] = new IntVanEmdeBoasTreeSetV1(sqrtM);
        }
        IntVanEmdeBoasTreeSetV1 child = children[i];
        child.add(integer % sqrtM);
        if (child.min == child.max) {
            if (auxiliary == null) {
                auxiliary = new IntVanEmdeBoasTreeSetV1(sqrtM);
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
            min = M;
            max = -1;
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
                // need to include high order bits
                i = (int) auxiliary.min;
                integer = (int) (children[i].min);
                min = i * sqrtM + integer;
            }
        } else if (integer == max) {
            if (auxiliary == null || auxiliary.isEmpty()) {
                max = (int) min;
                --size;
                return true;
            } else {
                i = auxiliary.max;
                integer = children[i].max;
                max = i * sqrtM + integer;
            }
        } else if (auxiliary == null || auxiliary.isEmpty()) {
            return false;
        } else {
            i = integer / sqrtM;
            integer %= sqrtM;
        }
        IntVanEmdeBoasTreeSetV1 child = children[i];
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
