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
 * Represents node accepting single character from the given char class
 * in Unicode case insensitive manner.
 * This character can be supplementary (2 chars to represent) or from
 * basic multilingual pane (1 char to represent).
 */
class TUCISupplRangeSet extends TSupplRangeSet {

    public TUCISupplRangeSet(TAbstractCharClass cs, TAbstractSet next) {
        super(cs, next);
    }

    public TUCISupplRangeSet(TAbstractCharClass cc) {
        super(cc);
    }

    @Override
    public boolean contains(int ch) {
        return chars.contains(Character.toLowerCase(Character.toUpperCase(ch)));
    }

    @Override
    protected String getName() {
        return "UCI range:" + (alt ? "^ " : " ") + chars.toString();
    }
}
