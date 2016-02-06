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

/**
 * @author Nikolay A. Kuznetsov
 */
package org.teavm.classlib.java.util.regex;

/**
 * Node accepting any character including line terminators.
 *
 * @author Nikolay A. Kuznetsov
 */
class TDotAllSet extends TJointSet {

    @Override
    public int matches(int stringIndex, CharSequence testString, TMatchResultImpl matchResult) {
        int strLength = matchResult.getRightBound();

        if (stringIndex + 1 > strLength) {
            matchResult.hitEnd = true;
            return -1;
        }

        char high = testString.charAt(stringIndex);

        if (Character.isHighSurrogate(high) && (stringIndex + 2 <= strLength)) {
            char low = testString.charAt(stringIndex + 1);

            if (Character.isSurrogatePair(high, low)) {
                return next.matches(stringIndex + 2, testString, matchResult);
            }
        }
        return next.matches(stringIndex + 1, testString, matchResult);
    }

    @Override
    protected String getName() {
        return "DotAll";
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
    public int getType() {
        return TAbstractSet.TYPE_DOTSET;
    }

    @Override
    public boolean hasConsumed(TMatchResultImpl matchResult) {
        return true;
    }
}
