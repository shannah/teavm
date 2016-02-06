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
 * This class represents low surrogate character.
 */
class TLowSurrogateCharSet extends TJointSet {

    /*
     * Note that we can use high and low surrogate characters that don't combine
     * into supplementary code point. See
     * http://www.unicode.org/reports/tr18/#Supplementary_Characters
     */
    private char low;

    public TLowSurrogateCharSet(char low) {
        this.low = low;
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

    @Override
    public int matches(int stringIndex, CharSequence testString, TMatchResultImpl matchResult) {

        if (stringIndex + 1 > matchResult.getRightBound()) {
            matchResult.hitEnd = true;
            return -1;
        }

        char low = testString.charAt(stringIndex);

        if (stringIndex > matchResult.getLeftBound()) {
            char high = testString.charAt(stringIndex - 1);

            /*
             * we consider high surrogate followed by low surrogate as a
             * codepoint
             */
            if (Character.isHighSurrogate(high)) {
                return -1;
            }
        }

        if (this.low == low) {
            return next.matches(stringIndex + 1, testString, matchResult);
        }

        return -1;
    }

    @Override
    public int find(int strIndex, CharSequence testString, TMatchResultImpl matchResult) {
        if (testString instanceof String) {
            String testStr = (String)testString;
            int startStr = matchResult.getLeftBound();
            int strLength = matchResult.getRightBound();

            while (strIndex < strLength) {

                strIndex = testStr.indexOf(low, strIndex);
                if (strIndex < 0) {
                    return -1;
                }

                if (strIndex > startStr) {

                    /*
                     * we consider high surrogate followed by low surrogate as a
                     * codepoint
                     */
                    if (Character.isHighSurrogate(testStr.charAt(strIndex - 1))) {
                        strIndex++;
                        continue;
                    }
                }

                if (next.matches(strIndex + 1, testString, matchResult) >= 0) {
                    return strIndex;
                }
                strIndex++;
            }

            return -1;
        }

        return super.find(strIndex, testString, matchResult);
    }

    @Override
    public int findBack(int strIndex, int lastIndex, CharSequence testString, TMatchResultImpl matchResult) {
        if (testString instanceof String) {
            int startStr = matchResult.getLeftBound();
            String testStr = (String)testString;

            while (lastIndex >= strIndex) {
                lastIndex = testStr.lastIndexOf(low, lastIndex);
                if (lastIndex < 0 || lastIndex < strIndex) {
                    return -1;
                }

                if (lastIndex > startStr) {

                    /*
                     * we consider high surrogate followed by low surrogate as a
                     * codepoint
                     */
                    if (Character.isHighSurrogate(testStr.charAt(lastIndex - 1))) {
                        lastIndex -= 2;
                        continue;
                    }
                }

                if (next.matches(lastIndex + 1, testString, matchResult) >= 0) {
                    return lastIndex;
                }

                lastIndex--;
            }

            return -1;
        }

        return super.findBack(strIndex, lastIndex, testString, matchResult);
    }

    @Override
    protected String getName() {
        return "" + low;
    }

    protected int getChar() {
        return low;
    }

    @Override
    public boolean first(TAbstractSet set) {
        return !(set instanceof TCharSet || set instanceof TRangeSet || set instanceof TSupplRangeSet
                || set instanceof TSupplCharSet || set instanceof THighSurrogateCharSet
                || set instanceof TLowSurrogateCharSet);

    }

    @Override
    public boolean hasConsumed(TMatchResultImpl matchResult) {
        return true;
    }
}
