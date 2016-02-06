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
 * Represents node accepting single character from the given char class. This
 * character can be supplementary (2 chars needed to represent) or from basic
 * multilingual pane (1 needed char to represent it).
 */
class TSupplRangeSet extends TJointSet {

    protected TAbstractCharClass chars;

    protected boolean alt;

    public TSupplRangeSet(TAbstractCharClass cs, TAbstractSet next) {
        chars = cs.getInstance();
        alt = cs.alt;
        this.next = next;
    }

    public TSupplRangeSet(TAbstractCharClass cc) {
        chars = cc.getInstance();
        alt = cc.alt;
    }

    @Override
    public int matches(int stringIndex, CharSequence testString, TMatchResultImpl matchResult) {
        int strLength = matchResult.getRightBound();
        int offset = -1;

        if (stringIndex < strLength) {
            char high = testString.charAt(stringIndex++);

            if (contains(high) && (offset = next.matches(stringIndex, testString, matchResult)) > 0) {
                return offset;
            }

            if (stringIndex < strLength) {
                char low = testString.charAt(stringIndex++);

                if (Character.isSurrogatePair(high, low) && contains(Character.toCodePoint(high, low))) {
                    return next.matches(stringIndex, testString, matchResult);
                }
            }
        }

        return -1;
    }

    @Override
    protected String getName() {
        return "range:" + (alt ? "^ " : " ") + chars.toString();
    }

    public boolean contains(int ch) {
        return chars.contains(ch);
    }

    @Override
    public boolean first(TAbstractSet set) {
        if (set instanceof TSupplCharSet) {
            return TAbstractCharClass.intersects(chars, ((TSupplCharSet)set).getCodePoint());
        } else if (set instanceof TCharSet) {
            return TAbstractCharClass.intersects(chars, ((TCharSet)set).getChar());
        } else if (set instanceof TSupplRangeSet) {
            return TAbstractCharClass.intersects(chars, ((TSupplRangeSet)set).chars);
        } else if (set instanceof TRangeSet) {
            return TAbstractCharClass.intersects(chars, ((TRangeSet)set).getChars());
        }

        return true;
    }

    protected TAbstractCharClass getChars() {
        return chars;
    }

    @Override
    public TAbstractSet getNext() {
        return next;
    }

    @Override
    public void setNext(TAbstractSet next) {
        this.next = next;
    }

    @Override
    public boolean hasConsumed(TMatchResultImpl mr) {
        return true;
    }
}
