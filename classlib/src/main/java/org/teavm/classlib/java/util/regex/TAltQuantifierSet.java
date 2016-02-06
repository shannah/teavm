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
 * Represents "?" quantifier over leaf sets.
 *
 * @author Nikolay A. Kuznetsov
 */
class TAltQuantifierSet extends TLeafQuantifierSet {

    public TAltQuantifierSet(TLeafSet innerSet, TAbstractSet next, int type) {
        super(innerSet, next, type);
    }

    @Override
    public int matches(int stringIndex, CharSequence testString, TMatchResultImpl matchResult) {
        int shift = innerSet.matches(stringIndex, testString, matchResult);
        return shift >= 0 ? shift : next.matches(stringIndex, testString, matchResult);
    }

    @Override
    public void setNext(TAbstractSet next) {
        super.setNext(next);
        innerSet.setNext(next);
    }
}
