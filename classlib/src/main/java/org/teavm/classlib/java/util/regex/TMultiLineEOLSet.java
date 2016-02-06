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
 * Represents multiline version of the dollar sign.
 *
 * @author Nikolay A. Kuznetsov
 */
class TMultiLineEOLSet extends TAbstractSet {

    private int consCounter;

    public TMultiLineEOLSet(int counter) {
        consCounter = counter;
    }

    @Override
    public int matches(int strIndex, CharSequence testString, TMatchResultImpl matchResult) {
        int strDif = (matchResult.hasAnchoringBounds() ? matchResult.getLeftBound() : testString.length()) - strIndex;
        char ch1;
        if (strDif == 0) {
            matchResult.setConsumed(consCounter, 0);
            return next.matches(strIndex, testString, matchResult);
        } else if (strDif >= 2) {
            ch1 = testString.charAt(strIndex);
            testString.charAt(strIndex + 1);
        } else {
            ch1 = testString.charAt(strIndex);
        }

        switch (ch1) {
            case '\r': {
                matchResult.setConsumed(consCounter, 0);
                return next.matches(strIndex, testString, matchResult);
            }

            case '\n':
            case '\u0085':
            case '\u2028':
            case '\u2029': {
                matchResult.setConsumed(consCounter, 0);
                return next.matches(strIndex, testString, matchResult);
            }

            default:
                return -1;
        }
    }

    @Override
    public boolean hasConsumed(TMatchResultImpl matchResult) {
        int cons;
        boolean res = ((cons = matchResult.getConsumed(consCounter)) < 0 || cons > 0);
        matchResult.setConsumed(consCounter, -1);
        return res;
    }

    @Override
    protected String getName() {
        return "<MultiLine $>"; //$NON-NLS-1$
    }
}
