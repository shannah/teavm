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

import java.util.ArrayList;

/**
 * Positive lookahead node.
 *
 * @author Nikolay A. Kuznetsov
 */
class TPositiveLookAhead extends TAtomicJointSet {
    public TPositiveLookAhead(ArrayList<TAbstractSet> children, TFSet fSet) {
        super(children, fSet);
    }

    /**
     * Returns stringIndex+shift, the next position to match
     */
    @Override
    public int matches(int stringIndex, CharSequence testString, TMatchResultImpl matchResult) {
        int size = children.size();
        for (TAbstractSet e : children) {
            int shift = e.matches(stringIndex, testString, matchResult);
            if (shift >= 0) {
                // PosLookaheadFset always returns true, position remains the
                // same
                // next.match() from;
                return next.matches(stringIndex, testString, matchResult);
            }
        }

        return -1;
    }

    @Override
    public boolean hasConsumed(TMatchResultImpl matchResult) {
        return false;
    }

    @Override
    protected String getName() {
        return "PosLookaheadJointSet";
    }
}
