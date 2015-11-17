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
public class IntVanEmdeBoasTreeSetV1 extends BaseIntVanEmdeBoasTreeSet {
    private final int sqrtM;

    public IntVanEmdeBoasTreeSetV1(int M) {
        sqrtM = (int) Math.sqrt(M);
    }

    public IntVanEmdeBoasTreeSetV1() {
        sqrtM = 1 << 16;
    }

    @Override
    protected int getLowerOrderBits(int integer) {
        return integer % sqrtM;
    }

    @Override
    protected int getHigherOrderBits(int integer) {
        return (int) Math.floor(integer / sqrtM);
    }

    @Override
    protected BaseIntVanEmdeBoasTreeSet newIntVanEmdeBoasTreeSet() {
        return new IntVanEmdeBoasTreeSetV1(sqrtM);
    }

    @Override
    protected BaseIntVanEmdeBoasTreeSet[] newChildren() {
        return new IntVanEmdeBoasTreeSetV1[sqrtM];
    }

    @Override
    protected int getBits(int i, int integer) {
        return i * sqrtM + integer;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }
}