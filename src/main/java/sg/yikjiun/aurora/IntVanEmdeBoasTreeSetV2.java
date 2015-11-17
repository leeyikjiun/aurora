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

/**
 * @author Lee Yik Jiun
 */
public class IntVanEmdeBoasTreeSetV2 extends BaseIntVanEmdeBoasTreeSet {
    private final int numBits;
    private final int halfNumBits;

    public IntVanEmdeBoasTreeSetV2() {
        this(32);
    }

    public IntVanEmdeBoasTreeSetV2(int numBits) {
        this.numBits = numBits;
        halfNumBits = numBits >> 1;
    }

    @Override
    protected int getHigherOrderBits(int i) {
        assert numBits == 32 || i < (1 << numBits);
        return i >> halfNumBits;
    }

    @Override
    protected int getLowerOrderBits(int i) {
        assert numBits == 32 || i < (1 << numBits);
        return i & (1 << halfNumBits) - 1;
    }

    @Override
    protected int getBits(int higherOrderBits, int lowerOrderBits) {
        return (higherOrderBits << halfNumBits) | lowerOrderBits;
    }

    @Override
    protected BaseIntVanEmdeBoasTreeSet newIntVanEmdeBoasTreeSet() {
        return new IntVanEmdeBoasTreeSetV2(halfNumBits);
    }

    @Override
    protected BaseIntVanEmdeBoasTreeSet[] newChildren() {
        return new IntVanEmdeBoasTreeSetV2[1 << halfNumBits];
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }
}