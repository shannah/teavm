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
 * Represents node accepting single supplementary codepoint.
 */
class TSupplCharSet extends TLeafSet {

    /*
     * UTF-16 encoding of this supplementary codepoint
     */
    private char high;

    private char low;

    // int value of this supplementary codepoint
    private int ch;

    public TSupplCharSet(int ch) {
        charCount = 2;
        this.ch = ch;
        char[] chUTF16 = Character.toChars(ch);
        high = chUTF16[0];

        /*
         * we suppose that SupplCharSet is build over supplementary codepoints
         * only
         */
        low = chUTF16[1];
    }

    @Override
    public int accepts(int strIndex, CharSequence testString) {
        char high = testString.charAt(strIndex++);
        char low = testString.charAt(strIndex);
        return ((this.high == high) && (this.low == low)) ? 2 : -1;
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

                strIndex++;
                if (strIndex < strLength) {
                    char ch = testStr.charAt(strIndex);

                    if ((low == ch) && (next.matches(strIndex + 1, testString, matchResult) >= 0)) {
                        return --strIndex;
                    }
                    strIndex++;
                }
            }
            return -1;
        }

        return super.find(strIndex, testString, matchResult);
    }

    @Override
    public int findBack(int strIndex, int lastIndex, CharSequence testString, TMatchResultImpl matchResult) {

        if (testString instanceof String) {
            String testStr = (String)testString;

            while (lastIndex >= strIndex) {
                lastIndex = testStr.lastIndexOf(low, lastIndex);
                lastIndex--;
                if (lastIndex < 0 || lastIndex < strIndex) {
                    return -1;
                }

                if ((high == testStr.charAt(lastIndex)) && next.matches(lastIndex + 2, testString, matchResult) >= 0) {
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
        return "" + high + low;
    }

    protected int getCodePoint() {
        return ch;
    }

    @Override
    public boolean first(TAbstractSet set) {
        if (set instanceof TSupplCharSet) {
            return ((TSupplCharSet)set).getCodePoint() == ch;
        } else if (set instanceof TSupplRangeSet) {
            return ((TSupplRangeSet)set).contains(ch);
        } else if (set instanceof TCharSet || set instanceof TRangeSet) {
            return false;
        }

        return true;
    }
}
