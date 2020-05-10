/*
 * StringRepresentationProviderTest.java
 * Copyright 2020 Rob Spoor
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.robtimus.obfuscation.annotation;

import static com.github.robtimus.obfuscation.annotation.StringRepresentationProvider.createInstance;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.util.function.Supplier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import com.github.robtimus.obfuscation.annotation.StringRepresentationProvider.BooleanArrayToString;
import com.github.robtimus.obfuscation.annotation.StringRepresentationProvider.ByteArrayToString;
import com.github.robtimus.obfuscation.annotation.StringRepresentationProvider.CharArrayToString;
import com.github.robtimus.obfuscation.annotation.StringRepresentationProvider.DoubleArrayToString;
import com.github.robtimus.obfuscation.annotation.StringRepresentationProvider.FloatArrayToString;
import com.github.robtimus.obfuscation.annotation.StringRepresentationProvider.IntArrayToString;
import com.github.robtimus.obfuscation.annotation.StringRepresentationProvider.LongArrayToString;
import com.github.robtimus.obfuscation.annotation.StringRepresentationProvider.ObjectArrayDeepToString;
import com.github.robtimus.obfuscation.annotation.StringRepresentationProvider.ObjectArrayToString;
import com.github.robtimus.obfuscation.annotation.StringRepresentationProvider.ShortArrayToString;
import com.github.robtimus.obfuscation.annotation.StringRepresentationProvider.ToString;

@SuppressWarnings({ "javadoc", "nls" })
public class StringRepresentationProviderTest {

    @Nested
    @DisplayName("createInstance(Class<? extends StringRepresentationProvider<?>>)")
    public class CreateInstance {

        @Test
        @DisplayName("ToString.class")
        public void testWithToString() {
            assertSame(ToString.INSTANCE, createInstance(ToString.class));
        }

        @Test
        @DisplayName("BooleanArrayToString.class")
        public void testWithBooleanArrayToString() {
            assertSame(BooleanArrayToString.INSTANCE, createInstance(BooleanArrayToString.class));
        }

        @Test
        @DisplayName("CharArrayToString.class")
        public void testWithCharArrayToString() {
            assertSame(CharArrayToString.INSTANCE, createInstance(CharArrayToString.class));
        }

        @Test
        @DisplayName("ByteArrayToString.class")
        public void testWithByteArrayToString() {
            assertSame(ByteArrayToString.INSTANCE, createInstance(ByteArrayToString.class));
        }

        @Test
        @DisplayName("ShortArrayToString.class")
        public void testShortArrayToString() {
            assertSame(ShortArrayToString.INSTANCE, createInstance(ShortArrayToString.class));
        }

        @Test
        @DisplayName("IntArrayToString.class")
        public void testWithIntArrayToString() {
            assertSame(IntArrayToString.INSTANCE, createInstance(IntArrayToString.class));
        }

        @Test
        @DisplayName("LongArrayToString.class")
        public void testWithLongArrayToString() {
            assertSame(LongArrayToString.INSTANCE, createInstance(LongArrayToString.class));
        }

        @Test
        @DisplayName("FloatArrayToString.class")
        public void testWithFloatArrayToString() {
            assertSame(FloatArrayToString.INSTANCE, createInstance(FloatArrayToString.class));
        }

        @Test
        @DisplayName("DoubleArrayToString.class")
        public void testWithDoubleArrayToString() {
            assertSame(DoubleArrayToString.INSTANCE, createInstance(DoubleArrayToString.class));
        }

        @Test
        @DisplayName("ObjectArrayToString.class")
        public void testWithObjectArrayToString() {
            assertSame(ObjectArrayToString.INSTANCE, createInstance(ObjectArrayToString.class));
        }

        @Test
        @DisplayName("ObjectArrayDeepToString.class")
        public void testWithObjectArrayDeepToString() {
            assertSame(ObjectArrayDeepToString.INSTANCE, createInstance(ObjectArrayDeepToString.class));
        }

        @Test
        public void testWithValidImplemenation() {
            StringRepresentationProvider provider = createInstance(ValidImplementation.class);
            Object value = new Object();
            Supplier<? extends CharSequence> representation = provider.stringRepresentation(value);
            assertEquals(value.toString(), representation.get());
        }

        @Test
        public void testWithInvalidImplemenation() {
            IllegalStateException exception = assertThrows(IllegalStateException.class, () -> createInstance(InvalidImplementation.class));
            assertThat(exception.getCause(), instanceOf(ReflectiveOperationException.class));
        }
    }

    @Nested
    @DisplayName("ToString")
    public class ToStringTest {

        @Test
        @DisplayName("stringRepresentation(Object)")
        public void testStringRepresentation() {
            Object value = new Object();
            Supplier<? extends CharSequence> representation = ToString.INSTANCE.stringRepresentation(value);
            assertEquals(value.toString(), representation.get());
        }

        @Test
        @DisplayName("toString()")
        public void testToString() {
            assertEquals(ToString.class.getName().replace('$', '.'), ToString.INSTANCE.toString());
        }
    }

    @Nested
    @DisplayName("BooleanArrayToString")
    public class BooleanArrayToStringTest {

        @Test
        @DisplayName("stringRepresentation(boolean[])")
        public void testStringRepresentationForBooleanArray() {
            boolean[] value = { true, false };
            Supplier<? extends CharSequence> representation = BooleanArrayToString.INSTANCE.stringRepresentation(value);
            assertEquals("[true, false]", representation.get());
        }

        @Test
        @DisplayName("stringRepresentation(Object)")
        public void testStringRepresentationForOther() {
            Object value = new Object();
            Supplier<? extends CharSequence> representation = BooleanArrayToString.INSTANCE.stringRepresentation(value);
            assertEquals(value.toString(), representation.get());
        }

        @Test
        @DisplayName("toString()")
        public void testToString() {
            assertEquals(BooleanArrayToString.class.getName().replace('$', '.'), BooleanArrayToString.INSTANCE.toString());
        }
    }

    @Nested
    @DisplayName("CharArrayToString")
    public class CharArrayToStringTest {

        @Test
        @DisplayName("stringRepresentation(char[])")
        public void testStringRepresentationForCharArray() {
            char[] value = "hello".toCharArray();
            Supplier<? extends CharSequence> representation = CharArrayToString.INSTANCE.stringRepresentation(value);
            assertEquals("[h, e, l, l, o]", representation.get());
        }

        @Test
        @DisplayName("stringRepresentation(Object)")
        public void testStringRepresentationForOther() {
            Object value = new Object();
            Supplier<? extends CharSequence> representation = CharArrayToString.INSTANCE.stringRepresentation(value);
            assertEquals(value.toString(), representation.get());
        }

        @Test
        @DisplayName("toString()")
        public void testToString() {
            assertEquals(CharArrayToString.class.getName().replace('$', '.'), CharArrayToString.INSTANCE.toString());
        }
    }

    @Nested
    @DisplayName("ByteArrayToString")
    public class ByteArrayToStringTest {

        @Test
        @DisplayName("stringRepresentation(byte[])")
        public void testStringRepresentationForByteArray() {
            byte[] value = { 1, 2, 3 };
            Supplier<? extends CharSequence> representation = ByteArrayToString.INSTANCE.stringRepresentation(value);
            assertEquals("[1, 2, 3]", representation.get());
        }

        @Test
        @DisplayName("stringRepresentation(Object)")
        public void testStringRepresentationForOther() {
            Object value = new Object();
            Supplier<? extends CharSequence> representation = ByteArrayToString.INSTANCE.stringRepresentation(value);
            assertEquals(value.toString(), representation.get());
        }

        @Test
        @DisplayName("toString()")
        public void testToString() {
            assertEquals(ByteArrayToString.class.getName().replace('$', '.'), ByteArrayToString.INSTANCE.toString());
        }
    }

    @Nested
    @DisplayName("ShortArrayToString")
    public class ShortArrayToStringTest {

        @Test
        @DisplayName("stringRepresentation(short[])")
        public void testStringRepresentationForShortArray() {
            short[] value = { 1, 2, 3 };
            Supplier<? extends CharSequence> representation = ShortArrayToString.INSTANCE.stringRepresentation(value);
            assertEquals("[1, 2, 3]", representation.get());
        }

        @Test
        @DisplayName("stringRepresentation(Object)")
        public void testStringRepresentationForOther() {
            Object value = new Object();
            Supplier<? extends CharSequence> representation = ShortArrayToString.INSTANCE.stringRepresentation(value);
            assertEquals(value.toString(), representation.get());
        }

        @Test
        @DisplayName("toString()")
        public void testToString() {
            assertEquals(ShortArrayToString.class.getName().replace('$', '.'), ShortArrayToString.INSTANCE.toString());
        }
    }

    @Nested
    @DisplayName("IntArrayToString")
    public class IntArrayToStringTest {

        @Test
        @DisplayName("stringRepresentation(int[])")
        public void testStringRepresentationForIntArray() {
            int[] value = { 1, 2, 3 };
            Supplier<? extends CharSequence> representation = IntArrayToString.INSTANCE.stringRepresentation(value);
            assertEquals("[1, 2, 3]", representation.get());
        }

        @Test
        @DisplayName("stringRepresentation(Object)")
        public void testStringRepresentationForOther() {
            Object value = new Object();
            Supplier<? extends CharSequence> representation = IntArrayToString.INSTANCE.stringRepresentation(value);
            assertEquals(value.toString(), representation.get());
        }

        @Test
        @DisplayName("toString()")
        public void testToString() {
            assertEquals(IntArrayToString.class.getName().replace('$', '.'), IntArrayToString.INSTANCE.toString());
        }
    }

    @Nested
    @DisplayName("LongArrayToString")
    public class LongArrayToStringTest {

        @Test
        @DisplayName("stringRepresentation(long[])")
        public void testStringRepresentationForLongArray() {
            long[] value = { 1, 2, 3 };
            Supplier<? extends CharSequence> representation = LongArrayToString.INSTANCE.stringRepresentation(value);
            assertEquals("[1, 2, 3]", representation.get());
        }

        @Test
        @DisplayName("stringRepresentation(Object)")
        public void testStringRepresentationForOther() {
            Object value = new Object();
            Supplier<? extends CharSequence> representation = LongArrayToString.INSTANCE.stringRepresentation(value);
            assertEquals(value.toString(), representation.get());
        }

        @Test
        @DisplayName("toString()")
        public void testToString() {
            assertEquals(LongArrayToString.class.getName().replace('$', '.'), LongArrayToString.INSTANCE.toString());
        }
    }

    @Nested
    @DisplayName("FloatArrayToString")
    public class FloatArrayToStringTest {

        @Test
        @DisplayName("stringRepresentation(float[])")
        public void testStringRepresentationForFloatArray() {
            float[] value = { 1, 2, 3, Float.NaN };
            Supplier<? extends CharSequence> representation = FloatArrayToString.INSTANCE.stringRepresentation(value);
            assertEquals("[1.0, 2.0, 3.0, NaN]", representation.get());
        }

        @Test
        @DisplayName("stringRepresentation(Object)")
        public void testStringRepresentationForOther() {
            Object value = new Object();
            Supplier<? extends CharSequence> representation = FloatArrayToString.INSTANCE.stringRepresentation(value);
            assertEquals(value.toString(), representation.get());
        }

        @Test
        @DisplayName("toString()")
        public void testToString() {
            assertEquals(FloatArrayToString.class.getName().replace('$', '.'), FloatArrayToString.INSTANCE.toString());
        }
    }

    @Nested
    @DisplayName("DoubleArrayToString")
    public class DoubleArrayToStringTest {

        @Test
        @DisplayName("stringRepresentation(double[])")
        public void testStringRepresentationForDoubleArray() {
            double[] value = { 1, 2, 3, Double.NaN };
            Supplier<? extends CharSequence> representation = DoubleArrayToString.INSTANCE.stringRepresentation(value);
            assertEquals("[1.0, 2.0, 3.0, NaN]", representation.get());
        }

        @Test
        @DisplayName("stringRepresentation(Object)")
        public void testStringRepresentationForOther() {
            Object value = new Object();
            Supplier<? extends CharSequence> representation = DoubleArrayToString.INSTANCE.stringRepresentation(value);
            assertEquals(value.toString(), representation.get());
        }

        @Test
        @DisplayName("toString()")
        public void testToString() {
            assertEquals(DoubleArrayToString.class.getName().replace('$', '.'), DoubleArrayToString.INSTANCE.toString());
        }
    }

    @Nested
    @DisplayName("ObjectArrayToString")
    public class ObjectArrayToStringTest {

        @Test
        @DisplayName("stringRepresentation(Object[])")
        public void testStringRepresentationForObjectArray() {
            Object[] value = { true, 1, "foo" };
            Supplier<? extends CharSequence> representation = ObjectArrayToString.INSTANCE.stringRepresentation(value);
            assertEquals("[true, 1, foo]", representation.get());
        }

        @Test
        @DisplayName("stringRepresentation(Object)")
        public void testStringRepresentationForOther() {
            Object value = new Object();
            Supplier<? extends CharSequence> representation = ObjectArrayToString.INSTANCE.stringRepresentation(value);
            assertEquals(value.toString(), representation.get());
        }

        @Test
        @DisplayName("toString()")
        public void testToString() {
            assertEquals(ObjectArrayToString.class.getName().replace('$', '.'), ObjectArrayToString.INSTANCE.toString());
        }
    }

    @Nested
    @DisplayName("ObjectArrayDeepToString")
    public class ObjectArrayDeepToStringTest {

        @Test
        @DisplayName("stringRepresentation(Object[])")
        public void testStringRepresentationForObjectArray() {
            Object[] value = { true, 1, "foo", new byte[] { 1, 2, 3 } };
            Supplier<? extends CharSequence> representation = ObjectArrayDeepToString.INSTANCE.stringRepresentation(value);
            assertEquals("[true, 1, foo, [1, 2, 3]]", representation.get());
        }

        @Test
        @DisplayName("stringRepresentation(Object)")
        public void testStringRepresentationForOther() {
            Object value = new Object();
            Supplier<? extends CharSequence> representation = ObjectArrayDeepToString.INSTANCE.stringRepresentation(value);
            assertEquals(value.toString(), representation.get());
        }

        @Test
        @DisplayName("toString()")
        public void testToString() {
            assertEquals(ObjectArrayDeepToString.class.getName().replace('$', '.'), ObjectArrayDeepToString.INSTANCE.toString());
        }
    }

    public static final class ValidImplementation implements StringRepresentationProvider {

        @Override
        public Supplier<? extends CharSequence> stringRepresentation(Object value) {
            return value::toString;
        }
    }

    private static final class InvalidImplementation implements StringRepresentationProvider {

        @Override
        public Supplier<? extends CharSequence> stringRepresentation(Object value) {
            throw new UnsupportedOperationException();
        }
    }
}
