/*
 *  Copyright 2016 "Alexey Andreev"
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.teavm.classlib.java.util.regex;

/**
 * This class is used to split the range that contains surrogate characters into
 * two ranges: the first consisting of these surrogate characters and the second
 * consisting of all others characters from the parent range. This class
 * represents the parent range split in such a manner.
 */
class TCompositeRangeSet extends TJointSet {

    // range without surrogates
    TAbstractSet withoutSurrogates;

    // range containing surrogates only
    TAbstractSet withSurrogates;

    public TCompositeRangeSet(TAbstractSet withoutSurrogates, TAbstractSet withSurrogates, TAbstractSet next) {
        this.withoutSurrogates = withoutSurrogates;
        this.withSurrogates = withSurrogates;
        setNext(next);
    }

    public TCompositeRangeSet(TAbstractSet withoutSurrogates, TAbstractSet withSurrogates) {
        this.withoutSurrogates = withoutSurrogates;
        this.withSurrogates = withSurrogates;
    }

    /**
     * Returns the next.
     */
    @Override
    public TAbstractSet getNext() {
        return next;
    }

    @Override
    public int matches(int stringIndex, CharSequence testString, TMatchResultImpl matchResult) {
        int shift = withoutSurrogates.matches(stringIndex, testString, matchResult);

        if (shift < 0) {
            shift = withSurrogates.matches(stringIndex, testString, matchResult);
        }

        if (shift >= 0) {
            return shift;
        }
        return -1;
    }

    /**
     * Sets next abstract set.
     *
     * @param next
     *            The next to set.
     */
    @Override
    public void setNext(TAbstractSet next) {
        this.next = next;
        withSurrogates.setNext(next);
        withoutSurrogates.setNext(next);
    }

    public TAbstractSet getSurrogates() {
        return withSurrogates;
    }

    public TAbstractSet getWithoutSurrogates() {
        return withoutSurrogates;
    }

    @Override
    protected String getName() {
        return "CompositeRangeSet: " + " <nonsurrogate> " + withoutSurrogates + " <surrogate> " + withSurrogates;
    }

    @Override
    public boolean hasConsumed(TMatchResultImpl matchResult) {
        return true;
    }

    @Override
    public boolean first(TAbstractSet set) {
        return true;
    }
}
