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
package org.teavm.classlib.java.lang;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author Alexey Andreev
 */
public class SystemTest {
    @Test
    public void copiesArray() {
        Object a = new Object();
        Object b = new Object();
        Object[] src = { a, b, a };
        Object[] dest = new Object[3];
        System.arraycopy(src, 0, dest, 0, 3);
        assertSame(a, dest[0]);
        assertSame(b, dest[1]);
        assertSame(a, dest[2]);
    }

    @Test
    public void copiesPrimitiveArray() {
        int[] src = { 23, 24, 25 };
        int[] dest = new int[3];
        System.arraycopy(src, 0, dest, 0, 3);
        assertEquals(23, dest[0]);
        assertEquals(24, dest[1]);
        assertEquals(25, dest[2]);
    }

    @Test
    public void copiesToSubclassArray() {
        String[] src = { "foo", "bar", "baz" };
        Object[] dest = new Object[3];
        System.arraycopy(src, 0, dest, 0, 3);
        assertEquals("foo", dest[0]);
        assertEquals("bar", dest[1]);
        assertEquals("baz", dest[2]);
    }

    @SuppressWarnings("SuspiciousSystemArraycopy")
    @Test
    public void copiesToSuperclassArrayWhenItemsMatch() {
        Object[] src = { "foo", "bar", "baz" };
        String[] dest = new String[3];
        System.arraycopy(src, 0, dest, 0, 3);
        assertEquals("foo", dest[0]);
        assertEquals("bar", dest[1]);
        assertEquals("baz", dest[2]);
    }

    @SuppressWarnings("SuspiciousSystemArraycopy")
    @Test
    public void failsToConverItemsCopyingArray() {
        Object[] src = { "foo", 23, "baz" };
        String[] dest = new String[3];
        try {
            System.arraycopy(src, 0, dest, 0, 3);
            fail("Exception expected");
        } catch (ArrayStoreException e) {
            assertEquals("foo", dest[0]);
            assertNull(dest[1]);
            assertNull(dest[2]);
        }
    }

    @SuppressWarnings("SuspiciousSystemArraycopy")
    @Test(expected = ArrayStoreException.class)
    public void failsToCopyToUnrelatedReferenceArray() {
        String[] src = { "foo", "bar", "baz" };
        Integer[] dest = new Integer[3];
        System.arraycopy(src, 0, dest, 0, 3);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void failsToCopyArraysWithInvalidIndexes() {
        System.arraycopy(new Object[0], 0, new Object[0], 0, 1);
    }

    @SuppressWarnings("SuspiciousSystemArraycopy")
    @Test(expected = ArrayStoreException.class)
    public void failsToCopyArraysWithIncompatibleElements() {
        System.arraycopy(new Object[1], 0, new int[1], 0, 1);
    }

    @SuppressWarnings("SuspiciousSystemArraycopy")
    @Test(expected = NullPointerException.class)
    public void failsToCopyFromNullSource() {
        System.arraycopy(null, 0, new int[1], 0, 1);
    }

    @SuppressWarnings("SuspiciousSystemArraycopy")
    @Test(expected = NullPointerException.class)
    public void failsToCopyToNullTarget() {
        System.arraycopy(new Object[1], 0, null, 0, 1);
    }
}
