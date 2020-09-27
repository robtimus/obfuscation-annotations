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

import static com.github.robtimus.obfuscation.annotation.CharacterRepresentationProvider.getDefaultInstance;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
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

    @ParameterizedTest(name = "{0}")
    @MethodSource
    @DisplayName("getDefaultInstance(Class<?>)")
    void testGetDefaultInstance(Class<?> type, CharacterRepresentationProvider expected) {
        assertSame(expected, getDefaultInstance(type));
    }

    static Arguments[] testGetDefaultInstance() {
        return new Arguments[] {
                arguments(boolean[].class, BooleanArrayToString.INSTANCE),
                arguments(char[].class, CharArrayToString.INSTANCE),
                arguments(byte[].class, ByteArrayToString.INSTANCE),
                arguments(short[].class, ShortArrayToString.INSTANCE),
                arguments(int[].class, IntArrayToString.INSTANCE),
                arguments(long[].class, LongArrayToString.INSTANCE),
                arguments(float[].class, FloatArrayToString.INSTANCE),
                arguments(double[].class, DoubleArrayToString.INSTANCE),
                arguments(Object[].class, ObjectArrayToString.INSTANCE),
                arguments(String[].class, ObjectArrayToString.INSTANCE),
                arguments(boolean.class, ToString.INSTANCE),
                arguments(Boolean.class, ToString.INSTANCE),
                arguments(char.class, ToString.INSTANCE),
                arguments(Character.class, ToString.INSTANCE),
                arguments(byte.class, ToString.INSTANCE),
                arguments(Byte.class, ToString.INSTANCE),
                arguments(short.class, ToString.INSTANCE),
                arguments(Short.class, ToString.INSTANCE),
                arguments(int.class, ToString.INSTANCE),
                arguments(Integer.class, ToString.INSTANCE),
                arguments(long.class, ToString.INSTANCE),
                arguments(Long.class, ToString.INSTANCE),
                arguments(float.class, ToString.INSTANCE),
                arguments(Float.class, ToString.INSTANCE),
                arguments(double.class, ToString.INSTANCE),
                arguments(Double.class, ToString.INSTANCE),
                arguments(void.class, ToString.INSTANCE),
                arguments(Void.class, ToString.INSTANCE),
                arguments(String.class, ToString.INSTANCE),
        };
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
}
