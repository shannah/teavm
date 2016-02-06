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

/*
 * This class is a range that contains only surrogate characters.
 */
class TLowHighSurrogateRangeSet extends TJointSet {

    protected TAbstractCharClass surrChars;

    protected boolean alt;

    public TLowHighSurrogateRangeSet(TAbstractCharClass surrChars, TAbstractSet next) {
        this.surrChars = surrChars.getInstance();
        alt = surrChars.alt;
        setNext(next);
    }

    public TLowHighSurrogateRangeSet(TAbstractCharClass surrChars) {
        this.surrChars = surrChars.getInstance();
        alt = surrChars.alt;
    }

    /**
     * Returns the next.
     */
    @Override
    public TAbstractSet getNext() {
        return next;
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
    }

    /**
     * Returns stringIndex+shift, the next position to match
     */
    @Override
    public int matches(int stringIndex, CharSequence testString, TMatchResultImpl matchResult) {
        int startStr = matchResult.getLeftBound();
        int strLength = matchResult.getRightBound();

        if (stringIndex + 1 > strLength) {
            matchResult.hitEnd = true;
            return -1;
        }

        char ch = testString.charAt(stringIndex);

        if (!surrChars.contains(ch)) {
            return -1;
        }

        if (Character.isHighSurrogate(ch)) {

            if (stringIndex + 1 < strLength) {
                char low = testString.charAt(stringIndex + 1);

                if (Character.isLowSurrogate(low)) {
                    return -1;
                }
            }
        } else if (Character.isLowSurrogate(ch)) {

            if (stringIndex > startStr) {
                char high = testString.charAt(stringIndex - 1);

                if (Character.isHighSurrogate(high)) {
                    return -1;
                }
            }
        }

        return next.matches(stringIndex + 1, testString, matchResult);
    }

    @Override
    protected String getName() {
        return "range:" + (alt ? "^ " : " ") + surrChars.toString();
    }

    @Override
    public boolean first(TAbstractSet set) {
        return !(set instanceof TCharSet || set instanceof TRangeSet || set instanceof TSupplRangeSet
                || set instanceof TSupplCharSet);

    }

    protected TAbstractCharClass getChars() {
        return surrChars;
    }

    @Override
    public boolean hasConsumed(TMatchResultImpl matchResult) {
        return true;
    }
}
