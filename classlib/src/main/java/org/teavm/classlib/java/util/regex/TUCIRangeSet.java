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
 * Represents node accepting single character from the given char class. Note,
 * this class contains normalized characters fo unicode case, asci case is
 * supported through adding both symbols to the range.
 *
 * @author Nikolay A. Kuznetsov
 */
class TUCIRangeSet extends TLeafSet {

    private TAbstractCharClass chars;

    private boolean alt;

    public TUCIRangeSet(TAbstractCharClass cs, TAbstractSet next) {
        super(next);
        chars = cs.getInstance();
        alt = cs.alt;
    }

    public TUCIRangeSet(TAbstractCharClass cc) {
        chars = cc.getInstance();
        alt = cc.alt;
    }

    @Override
    public int accepts(int strIndex, CharSequence testString) {
        return (chars.contains(Character.toLowerCase(Character
                .toUpperCase(testString.charAt(strIndex))))) ? 1 : -1;
    }

    @Override
    protected String getName() {
        return "UCI range:" + (alt ? "^ " : " ") + chars.toString();
    }
}