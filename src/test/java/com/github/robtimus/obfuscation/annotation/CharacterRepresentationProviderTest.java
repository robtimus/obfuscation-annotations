/*
 * CharacterRepresentationProviderTest.java
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

import static com.github.robtimus.obfuscation.annotation.CharacterRepresentationProvider.createInstance;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import com.github.robtimus.obfuscation.annotation.CharacterRepresentationProvider.BooleanArrayToString;
import com.github.robtimus.obfuscation.annotation.CharacterRepresentationProvider.ByteArrayToString;
import com.github.robtimus.obfuscation.annotation.CharacterRepresentationProvider.CharArrayToString;
import com.github.robtimus.obfuscation.annotation.CharacterRepresentationProvider.DoubleArrayToString;
import com.github.robtimus.obfuscation.annotation.CharacterRepresentationProvider.FloatArrayToString;
import com.github.robtimus.obfuscation.annotation.CharacterRepresentationProvider.IntArrayToString;
import com.github.robtimus.obfuscation.annotation.CharacterRepresentationProvider.LongArrayToString;
import com.github.robtimus.obfuscation.annotation.CharacterRepresentationProvider.ObjectArrayDeepToString;
import com.github.robtimus.obfuscation.annotation.CharacterRepresentationProvider.ObjectArrayToString;
import com.github.robtimus.obfuscation.annotation.CharacterRepresentationProvider.ShortArrayToString;
import com.github.robtimus.obfuscation.annotation.CharacterRepresentationProvider.ToString;

@SuppressWarnings("nls")
class CharacterRepresentationProviderTest {

    @Nested
    @DisplayName("createInstance(Class<? extends StringRepresentationProvider<?>>)")
    class CreateInstance {

        @Test
        @DisplayName("ToString.class")
        void testWithToString() {
            assertSame(ToString.INSTANCE, createInstance(ToString.class));
        }

        @Test
        @DisplayName("BooleanArrayToString.class")
        void testWithBooleanArrayToString() {
            assertSame(BooleanArrayToString.INSTANCE, createInstance(BooleanArrayToString.class));
        }

        @Test
        @DisplayName("CharArrayToString.class")
        void testWithCharArrayToString() {
            assertSame(CharArrayToString.INSTANCE, createInstance(CharArrayToString.class));
        }

        @Test
        @DisplayName("ByteArrayToString.class")
        void testWithByteArrayToString() {
            assertSame(ByteArrayToString.INSTANCE, createInstance(ByteArrayToString.class));
        }

        @Test
        @DisplayName("ShortArrayToString.class")
        void testShortArrayToString() {
            assertSame(ShortArrayToString.INSTANCE, createInstance(ShortArrayToString.class));
        }

        @Test
        @DisplayName("IntArrayToString.class")
        void testWithIntArrayToString() {
            assertSame(IntArrayToString.INSTANCE, createInstance(IntArrayToString.class));
        }

        @Test
        @DisplayName("LongArrayToString.class")
        void testWithLongArrayToString() {
            assertSame(LongArrayToString.INSTANCE, createInstance(LongArrayToString.class));
        }

        @Test
        @DisplayName("FloatArrayToString.class")
        void testWithFloatArrayToString() {
            assertSame(FloatArrayToString.INSTANCE, createInstance(FloatArrayToString.class));
        }

        @Test
        @DisplayName("DoubleArrayToString.class")
        void testWithDoubleArrayToString() {
            assertSame(DoubleArrayToString.INSTANCE, createInstance(DoubleArrayToString.class));
        }

        @Test
        @DisplayName("ObjectArrayToString.class")
        void testWithObjectArrayToString() {
            assertSame(ObjectArrayToString.INSTANCE, createInstance(ObjectArrayToString.class));
        }

        @Test
        @DisplayName("ObjectArrayDeepToString.class")
        void testWithObjectArrayDeepToString() {
            assertSame(ObjectArrayDeepToString.INSTANCE, createInstance(ObjectArrayDeepToString.class));
        }

        @Test
        void testWithValidImplemenation() {
            CharacterRepresentationProvider provider = createInstance(ValidImplementation.class);
            Object value = new Object();
            CharSequence representation = provider.toCharSequence(value);
            assertEquals(value.toString(), representation);
        }

        @Test
        void testWithInvalidImplemenation() {
            IllegalStateException exception = assertThrows(IllegalStateException.class, () -> createInstance(InvalidImplementation.class));
            assertThat(exception.getCause(), instanceOf(ReflectiveOperationException.class));
        }
    }

    @Nested
    @DisplayName("ToString")
    class ToStringTest {

        @Test
        @DisplayName("toCharSequence(Object)")
        void testStringRepresentation() {
            Object value = new Object();
            CharSequence representation = ToString.INSTANCE.toCharSequence(value);
            assertEquals(value.toString(), representation);
        }

        @Test
        @DisplayName("toString()")
        void testToString() {
            assertEquals(ToString.class.getName().replace('$', '.'), ToString.INSTANCE.toString());
        }
    }

    @Nested
    @DisplayName("BooleanArrayToString")
    class BooleanArrayToStringTest {

        @Nested
        @DisplayName("toCharSequence(Object)")
        class ToCharSequence {

            @Test
            @DisplayName("with boolean[] input")
            void testWithBooleanArrayInput() {
                boolean[] value = { true, false };
                CharSequence representation = BooleanArrayToString.INSTANCE.toCharSequence(value);
                assertEquals("[true, false]", representation);
            }

            @Test
            @DisplayName("with non-boolean[] input")
            void testWithNonBooleanArrayInput() {
                Object value = new Object();
                CharSequence representation = BooleanArrayToString.INSTANCE.toCharSequence(value);
                assertEquals(value.toString(), representation);
            }
        }

        @Test
        @DisplayName("toString()")
        void testToString() {
            assertEquals(BooleanArrayToString.class.getName().replace('$', '.'), BooleanArrayToString.INSTANCE.toString());
        }
    }

    @Nested
    @DisplayName("CharArrayToString")
    class CharArrayToStringTest {

        @Nested
        @DisplayName("toCharSequence(Object)")
        class ToCharSequence {

            @Test
            @DisplayName("with char[] input")
            void testWithCharArrayInput() {
                char[] value = "hello".toCharArray();
                CharSequence representation = CharArrayToString.INSTANCE.toCharSequence(value);
                assertEquals("[h, e, l, l, o]", representation);
            }

            @Test
            @DisplayName("with non-char[] input")
            void testWithNonCharArrayInput() {
                Object value = new Object();
                CharSequence representation = CharArrayToString.INSTANCE.toCharSequence(value);
                assertEquals(value.toString(), representation);
            }
        }

        @Test
        @DisplayName("toString()")
        void testToString() {
            assertEquals(CharArrayToString.class.getName().replace('$', '.'), CharArrayToString.INSTANCE.toString());
        }
    }

    @Nested
    @DisplayName("ByteArrayToString")
    class ByteArrayToStringTest {

        @Nested
        @DisplayName("toCharSequence(Object)")
        class ToCharSequence {

            @Test
            @DisplayName("with byte[] input")
            void testWithByteArrayInput() {
                byte[] value = { 1, 2, 3 };
                CharSequence representation = ByteArrayToString.INSTANCE.toCharSequence(value);
                assertEquals("[1, 2, 3]", representation);
            }

            @Test
            @DisplayName("with non-byte[] input")
            void testWithNonByteArrayInput() {
                Object value = new Object();
                CharSequence representation = ByteArrayToString.INSTANCE.toCharSequence(value);
                assertEquals(value.toString(), representation);
            }
        }

        @Test
        @DisplayName("toString()")
        void testToString() {
            assertEquals(ByteArrayToString.class.getName().replace('$', '.'), ByteArrayToString.INSTANCE.toString());
        }
    }

    @Nested
    @DisplayName("ShortArrayToString")
    class ShortArrayToStringTest {

        @Nested
        @DisplayName("toCharSequence(Object)")
        class ToCharSequence {

            @Test
            @DisplayName("with short[] input")
            void testWithShortArrayInput() {
                short[] value = { 1, 2, 3 };
                CharSequence representation = ShortArrayToString.INSTANCE.toCharSequence(value);
                assertEquals("[1, 2, 3]", representation);
            }

            @Test
            @DisplayName("with non-short[] input")
            void testWithNonShortArrayInput() {
                Object value = new Object();
                CharSequence representation = ShortArrayToString.INSTANCE.toCharSequence(value);
                assertEquals(value.toString(), representation);
            }
        }

        @Test
        @DisplayName("toString()")
        void testToString() {
            assertEquals(ShortArrayToString.class.getName().replace('$', '.'), ShortArrayToString.INSTANCE.toString());
        }
    }

    @Nested
    @DisplayName("IntArrayToString")
    class IntArrayToStringTest {

        @Nested
        @DisplayName("toCharSequence(Object)")
        class ToCharSequence {

            @Test
            @DisplayName("with int[] input")
            void testWithIntArrayInput() {
                int[] value = { 1, 2, 3 };
                CharSequence representation = IntArrayToString.INSTANCE.toCharSequence(value);
                assertEquals("[1, 2, 3]", representation);
            }

            @Test
            @DisplayName("with non-int[] input")
            void testWithNonIntArrayInput() {
                Object value = new Object();
                CharSequence representation = IntArrayToString.INSTANCE.toCharSequence(value);
                assertEquals(value.toString(), representation);
            }
        }

        @Test
        @DisplayName("toString()")
        void testToString() {
            assertEquals(IntArrayToString.class.getName().replace('$', '.'), IntArrayToString.INSTANCE.toString());
        }
    }

    @Nested
    @DisplayName("LongArrayToString")
    class LongArrayToStringTest {

        @Nested
        @DisplayName("toCharSequence(Object)")
        class ToCharSequence {

            @Test
            @DisplayName("with long[] input")
            void testWithLongArrayInput() {
                long[] value = { 1, 2, 3 };
                CharSequence representation = LongArrayToString.INSTANCE.toCharSequence(value);
                assertEquals("[1, 2, 3]", representation);
            }

            @Test
            @DisplayName("with non-long[] input")
            void testWithNonLongArrayInput() {
                Object value = new Object();
                CharSequence representation = LongArrayToString.INSTANCE.toCharSequence(value);
                assertEquals(value.toString(), representation);
            }
        }

        @Test
        @DisplayName("toString()")
        void testToString() {
            assertEquals(LongArrayToString.class.getName().replace('$', '.'), LongArrayToString.INSTANCE.toString());
        }
    }

    @Nested
    @DisplayName("FloatArrayToString")
    class FloatArrayToStringTest {

        @Nested
        @DisplayName("toCharSequence(Object)")
        class ToCharSequence {

            @Test
            @DisplayName("with float[] input")
            void testWithFloatArrayInput() {
                float[] value = { 1, 2, 3, Float.NaN };
                CharSequence representation = FloatArrayToString.INSTANCE.toCharSequence(value);
                assertEquals("[1.0, 2.0, 3.0, NaN]", representation);
            }

            @Test
            @DisplayName("with non-float[] input")
            void testWithNonFloatArrayInput() {
                Object value = new Object();
                CharSequence representation = FloatArrayToString.INSTANCE.toCharSequence(value);
                assertEquals(value.toString(), representation);
            }
        }

        @Test
        @DisplayName("toString()")
        void testToString() {
            assertEquals(FloatArrayToString.class.getName().replace('$', '.'), FloatArrayToString.INSTANCE.toString());
        }
    }

    @Nested
    @DisplayName("DoubleArrayToString")
    class DoubleArrayToStringTest {

        @Nested
        @DisplayName("toCharSequence(Object)")
        class ToCharSequence {

            @Test
            @DisplayName("with double[] input")
            void testWithDoubleArrayInput() {
                double[] value = { 1, 2, 3, Double.NaN };
                CharSequence representation = DoubleArrayToString.INSTANCE.toCharSequence(value);
                assertEquals("[1.0, 2.0, 3.0, NaN]", representation);
            }

            @Test
            @DisplayName("with non-double[] input")
            void testWithNonDoubleArrayInput() {
                Object value = new Object();
                CharSequence representation = DoubleArrayToString.INSTANCE.toCharSequence(value);
                assertEquals(value.toString(), representation);
            }
        }

        @Test
        @DisplayName("toString()")
        void testToString() {
            assertEquals(DoubleArrayToString.class.getName().replace('$', '.'), DoubleArrayToString.INSTANCE.toString());
        }
    }

    @Nested
    @DisplayName("ObjectArrayToString")
    class ObjectArrayToStringTest {

        @Nested
        @DisplayName("toCharSequence(Object)")
        class ToCharSequence {

            @Test
            @DisplayName("with Object[] input")
            void testWithObjectArrayInput() {
                Object[] value = { true, 1, "foo" };
                CharSequence representation = ObjectArrayToString.INSTANCE.toCharSequence(value);
                assertEquals("[true, 1, foo]", representation);
            }

            @Test
            @DisplayName("with non-Object[] input")
            void testWithNonObjectArrayInput() {
                Object value = new Object();
                CharSequence representation = ObjectArrayToString.INSTANCE.toCharSequence(value);
                assertEquals(value.toString(), representation);
            }
        }

        @Test
        @DisplayName("toString()")
        void testToString() {
            assertEquals(ObjectArrayToString.class.getName().replace('$', '.'), ObjectArrayToString.INSTANCE.toString());
        }
    }

    @Nested
    @DisplayName("ObjectArrayDeepToString")
    class ObjectArrayDeepToStringTest {

        @Nested
        @DisplayName("toCharSequence(Object)")
        class ToCharSequence {

            @Test
            @DisplayName("with Object[] input")
            void testWithObjectArrayInput() {
                Object[] value = { true, 1, "foo", new byte[] { 1, 2, 3 } };
                CharSequence representation = ObjectArrayDeepToString.INSTANCE.toCharSequence(value);
                assertEquals("[true, 1, foo, [1, 2, 3]]", representation);
            }

            @Test
            @DisplayName("with non-Object[] input")
            void testWithNonObjectArrayInput() {
                Object value = new Object();
                CharSequence representation = ObjectArrayDeepToString.INSTANCE.toCharSequence(value);
                assertEquals(value.toString(), representation);
            }
        }

        @Test
        @DisplayName("toString()")
        void testToString() {
            assertEquals(ObjectArrayDeepToString.class.getName().replace('$', '.'), ObjectArrayDeepToString.INSTANCE.toString());
        }
    }

    public static final class ValidImplementation implements CharacterRepresentationProvider {

        @Override
        public CharSequence toCharSequence(Object value) {
            return value.toString();
        }
    }

    private static final class InvalidImplementation implements CharacterRepresentationProvider {

        @Override
        public CharSequence toCharSequence(Object value) {
            throw new UnsupportedOperationException();
        }
    }
}
