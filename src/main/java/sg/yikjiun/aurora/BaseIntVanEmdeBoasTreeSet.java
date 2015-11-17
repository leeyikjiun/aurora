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
public abstract class BaseIntVanEmdeBoasTreeSet implements Set<Integer> {
    private int size = 0;
    private int max = Integer.MIN_VALUE;
    private int min = Integer.MAX_VALUE;
    private BaseIntVanEmdeBoasTreeSet auxiliary;
    private BaseIntVanEmdeBoasTreeSet[] children;

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
        BaseIntVanEmdeBoasTreeSet child = children[i];
        return child != null && child.contains(getLowerOrderBits(integer));
    }

    protected abstract int getLowerOrderBits(int integer);

    protected abstract int getHigherOrderBits(int integer);

    @Override
    public Iterator<Integer> iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
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
            children = newChildren();
        }
        if (children[i] == null) {
            children[i] = newIntVanEmdeBoasTreeSet();
        }
        BaseIntVanEmdeBoasTreeSet child = children[i];
        child.add(getLowerOrderBits(integer));
        if (child.min == child.max) {
            if (auxiliary == null) {
                auxiliary = newIntVanEmdeBoasTreeSet();
            }
            auxiliary.add(i);
        }
        ++size;
        return true;
    }

    protected abstract BaseIntVanEmdeBoasTreeSet newIntVanEmdeBoasTreeSet();

    protected abstract BaseIntVanEmdeBoasTreeSet[] newChildren();

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
        BaseIntVanEmdeBoasTreeSet child = children[i];
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

    protected abstract int getBits(int i, int integer);

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
