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
 * This class represents high surrogate character.
 */
class THighSurrogateCharSet extends TJointSet {

    /*
     * Note that we can use high and low surrogate characters that don't combine
     * into supplementary code point. See
     * http://www.unicode.org/reports/tr18/#Supplementary_Characters
     */

    private char high;

    public THighSurrogateCharSet(char high) {
        this.high = high;
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
        int strLength = matchResult.getRightBound();

        if (stringIndex + 1 > strLength) {
            matchResult.hitEnd = true;
            return -1;
        }

        char high = testString.charAt(stringIndex);

        if (stringIndex + 1 < strLength) {
            char low = testString.charAt(stringIndex + 1);

            /*
             * we consider high surrogate followed by low surrogate as a
             * codepoint
             */
            if (Character.isLowSurrogate(low)) {
                return -1;
            }
        }

        if (this.high == high) {
            return next.matches(stringIndex + 1, testString, matchResult);
        }

        return -1;
    }

    @Override
    public int find(int strIndex, CharSequence testString, TMatchResultImpl matchResult) {
        if (testString instanceof String) {
            String testStr = (String)testString;
            int strLength = matchResult.getRightBound();

            while (strIndex < strLength) {

                strIndex = testStr.indexOf(high, strIndex);
                if (strIndex < 0) {
                    return -1;
                }

                if (strIndex + 1 < strLength) {

                    /*
                     * we consider high surrogate followed by low surrogate as a
                     * codepoint
                     */
                    if (Character.isLowSurrogate(testStr.charAt(strIndex + 1))) {
                        strIndex += 2;
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
            String testStr = (String)testString;
            int strLength = matchResult.getRightBound();

            while (lastIndex >= strIndex) {
                lastIndex = testStr.lastIndexOf(high, lastIndex);
                if (lastIndex < 0 || lastIndex < strIndex) {
                    return -1;
                }

                if (lastIndex + 1 < strLength) {

                    /*
                     * we consider high surrogate followed by low surrogate as a
                     * codepoint
                     */
                    if (Character.isLowSurrogate(testStr.charAt(lastIndex + 1))) {
                        lastIndex--;
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
        return "" + high;
    }

    protected int getChar() {
        return high;
    }

    @Override
    public boolean first(TAbstractSet set) {
        if (set instanceof TCharSet || set instanceof TRangeSet || set instanceof TSupplRangeSet
                || set instanceof TSupplCharSet || set instanceof TLowSurrogateCharSet) {
            return false;
        } else if (set instanceof THighSurrogateCharSet) {
            return ((THighSurrogateCharSet)set).high == high;
        }

        return true;
    }

    @Override
    public boolean hasConsumed(TMatchResultImpl matchResult) {
        return true;
    }
}
