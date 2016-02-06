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

import java.util.Arrays;

/**
 * Match result implementation Note: probably it might make sense to combine
 * this class with Matcher.
 *
 * @author Nikolay A. Kuznetsov
 */
class TMatchResultImpl implements TMatchResult {

    private int[] groupBounds;

    private int[] consumers;

    private int[] compQuantCounters;

    private CharSequence string;

    private int groupCount;

    private boolean valid;

    private int leftBound;

    private int rightBound;

    int startIndex;

    private boolean transparentBounds;

    private boolean anchoringBounds;

    boolean hitEnd;

    boolean requireEnd;

    int previousMatch = -1;

    private int mode;

    TMatchResultImpl(CharSequence string, int leftBound, int rightBound, int groupCount, int compQuantCount,
            int consumersCount) {
        this.groupCount = ++groupCount;
        groupBounds = new int[groupCount * 2];

        consumers = new int[consumersCount];
        Arrays.fill(consumers, -1);

        if (compQuantCount > 0) {
            compQuantCounters = new int[compQuantCount];
        }
        Arrays.fill(groupBounds, -1);
        reset(string, leftBound, rightBound);
    }

    TMatchResult cloneImpl() {
        TMatchResultImpl res = new TMatchResultImpl(string, leftBound, rightBound, groupCount - 1, 0, 0);

        res.valid = valid;
        if (valid) {
            System.arraycopy(groupBounds, 0, res.groupBounds, 0, groupBounds.length);
        }
        return res;
    }

    public void setConsumed(int counter, int value) {
        consumers[counter] = value;
    }

    public int getConsumed(int counter) {
        return consumers[counter];
    }

    @Override
    public int end() {
        return end(0);
    }

    @Override
    public int end(int group) {
        checkGroup(group);
        return groupBounds[group * 2 + 1];
    }

    void setStart(int group, int offset) {
        groupBounds[group * 2] = offset;
    }

    void setEnd(int group, int offset) {
        groupBounds[group * 2 + 1] = offset;
    }

    int getStart(int group) {
        return groupBounds[group * 2];
    }

    int getEnd(int group) {
        return groupBounds[group * 2 + 1];
    }

    @Override
    public String group() {
        return group(0);
    }

    @Override
    public String group(int group) {
        if (start(group) < 0) {
            return null;
        }
        return string.subSequence(start(group), end(group)).toString();
    }

    String getGroupNoCheck(int group) {
        int st = getStart(group);
        int end = getEnd(group);
        if ((end | st | (end - st)) < 0 || end > string.length()) {
            return null;
        }

        return string.subSequence(st, end).toString();
    }

    @Override
    public int groupCount() {
        return groupCount - 1;
    }

    @Override
    public int start() {
        return start(0);
    }

    @Override
    public int start(int group) {
        checkGroup(group);
        return groupBounds[group * 2];
    }

    /*
     * This method being called after any successful match; For now it's being
     * used to check zero group for empty match;
     */
    public void finalizeMatch() {
        if (groupBounds[0] == -1) {
            groupBounds[0] = startIndex;
            groupBounds[1] = startIndex;
        }

        previousMatch = end();
    }

    public int getEnterCounter(int setCounter) {
        return compQuantCounters[setCounter];
    }

    public void setEnterCounter(int setCounter, int value) {
        compQuantCounters[setCounter] = value;
    }

    private void checkGroup(int group) {
        if (!valid) {
            throw new IllegalStateException();
        }

        if (group < 0 || group >= groupCount) {
            throw new IndexOutOfBoundsException(String.valueOf(group));
        }
    }

    void updateGroup(int index, int srtOffset, int endOffset) {
        checkGroup(index);
        groupBounds[index * 2] = srtOffset;
        groupBounds[index * 2 + 1] = endOffset;
    }

    protected void setValid() {
        valid = true;
    }

    protected boolean isValid() {
        return valid;
    }

    protected void reset(CharSequence newSequence, int leftBound, int rightBound) {
        valid = false;
        mode = TMatcher.MODE_MATCH;
        Arrays.fill(groupBounds, -1);
        Arrays.fill(consumers, -1);

        if (newSequence != null) {
            string = newSequence;
        }
        if (leftBound >= 0) {
            setBounds(leftBound, rightBound);
        }
        startIndex = this.leftBound;
    }

    protected void reset() {
        reset(null, -1, -1);
    }

    private void setBounds(int leftBound, int rightBound) {
        this.leftBound = leftBound;
        this.rightBound = rightBound;
    }

    protected void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
        previousMatch = previousMatch >= 0 ? previousMatch : startIndex;
    }

    public int getLeftBound() {
        return leftBound;
    }

    public int getRightBound() {
        return rightBound;
    }

    protected void setMode(int mode) {
        this.mode = mode;
    }

    protected int mode() {
        return mode;
    }

    protected void useAnchoringBounds(boolean value) {
        anchoringBounds = value;
    }

    protected boolean hasAnchoringBounds() {
        return anchoringBounds;
    }

    protected void useTransparentBounds(boolean value) {
        transparentBounds = value;
    }

    protected boolean hasTransparentBounds() {
        return transparentBounds;
    }

    int getPreviousMatchEnd() {
        return previousMatch;
    }
}
