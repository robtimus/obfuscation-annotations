/*
 * CharacterRepresentationProvider.java
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

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import com.github.robtimus.obfuscation.Obfuscator;

/**
 * A provider for character representations for objects, that can be used to obfuscate objects using
 * {@link Obfuscator#obfuscateObject(Object, Supplier)}, {@link Obfuscator#obfuscateCollection(Collection, Function)},
 * {@link Obfuscator#obfuscateList(List, Function)}, {@link Obfuscator#obfuscateSet(Set, Function)} or {@link Obfuscator#obfuscateMap(Map, Function)}.
 *
 * @author Rob Spoor
 * @since 2.0
 */
@FunctionalInterface
public interface CharacterRepresentationProvider {

    /**
     * Returns a character representation for a value.
     *
     * @param value The value to return the character representation for.
     * @return A {@code CharSequence} containing the value's character representation.
     * @throws NullPointerException If the given value is {@code null}.
     */
    CharSequence toCharSequence(Object value);

    /**
     * Returns a default {@code CharacterRepresentationProvider} instance for a specific type.
     * This method will return:
     * <ul>
     * <li>{@link BooleanArrayToString#INSTANCE} for {@code boolean[]}</li>
     * <li>{@link CharArrayToString#INSTANCE} for {@code char[]}</li>
     * <li>{@link ByteArrayToString#INSTANCE} for {@code byte[]}</li>
     * <li>{@link ShortArrayToString#INSTANCE} for {@code short[]}</li>
     * <li>{@link IntArrayToString#INSTANCE} for {@code int[]}</li>
     * <li>{@link LongArrayToString#INSTANCE} for {@code long[]}</li>
     * <li>{@link FloatArrayToString#INSTANCE} for {@code float[]}</li>
     * <li>{@link DoubleArrayToString#INSTANCE} for {@code double[]}</li>
     * <li>{@link ObjectArrayToString#INSTANCE} for {@code Object[]} and sub types</li>
     * <li>{@code Identity#INSTANCE} for {@link CharSequence} and sub types</li>
     * <li>{@link ToString#INSTANCE} for all other cases</li>
     * </ul>
     *
     * @param type The type for which to return a default {@code CharacterRepresentationProvider} instance.
     * @return A default {@code CharacterRepresentationProvider} instance for the given type.
     * @throws NullPointerException If the given type is {@code null}.
     */
    static CharacterRepresentationProvider getDefaultInstance(Class<?> type) {
        Objects.requireNonNull(type);
        if (type == boolean[].class) {
            return BooleanArrayToString.INSTANCE;
        }
        if (type == char[].class) {
            return CharArrayToString.INSTANCE;
        }
        if (type == byte[].class) {
            return ByteArrayToString.INSTANCE;
        }
        if (type == short[].class) {
            return ShortArrayToString.INSTANCE;
        }
        if (type == int[].class) {
            return IntArrayToString.INSTANCE;
        }
        if (type == long[].class) {
            return LongArrayToString.INSTANCE;
        }
        if (type == float[].class) {
            return FloatArrayToString.INSTANCE;
        }
        if (type == double[].class) {
            return DoubleArrayToString.INSTANCE;
        }
        if (Object[].class.isAssignableFrom(type)) {
            return ObjectArrayToString.INSTANCE;
        }
        if (CharSequence.class.isAssignableFrom(type)) {
            return Identity.INSTANCE;
        }
        return ToString.INSTANCE;
    }

    /**
     * A character representation provider that uses {@link Object#toString()}.
     *
     * @author Rob Spoor
     * @since 2.0
     */
    final class ToString implements CharacterRepresentationProvider {

        /** The single instance. */
        public static final ToString INSTANCE = new ToString();

        private ToString() {
            super();
        }

        @Override
        public CharSequence toCharSequence(Object value) {
            return value.toString();
        }

        @Override
        @SuppressWarnings("nls")
        public String toString() {
            return CharacterRepresentationProvider.class.getName() + "." + getClass().getSimpleName();
        }
    }

    /**
     * A character representation provider that returns {@link CharSequence CharSequences} unmodified, or {@link Object#toString()} for other types.
     *
     * @author Rob Spoor
     * @since 2.0
     */
    final class Identity extends TypeSpecific<CharSequence> {

        /** The single instance. */
        public static final Identity INSTANCE = new Identity();

        private Identity() {
            super(CharSequence.class);
        }

        @Override
        protected CharSequence convert(CharSequence value) {
            return value;
        }

        @Override
        @SuppressWarnings("nls")
        public String toString() {
            return CharacterRepresentationProvider.class.getName() + "." + getClass().getSimpleName();
        }
    }

    /**
     * A character representation provider that uses {@link Arrays#toString(boolean[])}, or {@link Object#toString()} for other types.
     *
     * @author Rob Spoor
     * @since 2.0
     */
    final class BooleanArrayToString extends TypeSpecific<boolean[]> {

        /** The single instance. */
        public static final BooleanArrayToString INSTANCE = new BooleanArrayToString();

        private BooleanArrayToString() {
            super(boolean[].class);
        }

        @Override
        protected CharSequence convert(boolean[] value) {
            return Arrays.toString(value);
        }

        @Override
        @SuppressWarnings("nls")
        public String toString() {
            return CharacterRepresentationProvider.class.getName() + "." + getClass().getSimpleName();
        }
    }

    /**
     * A character representation provider that uses {@link Arrays#toString(char[])}, or {@link Object#toString()} for other types.
     *
     * @author Rob Spoor
     * @since 2.0
     */
    final class CharArrayToString extends TypeSpecific<char[]> {

        /** The single instance. */
        public static final CharArrayToString INSTANCE = new CharArrayToString();

        private CharArrayToString() {
            super(char[].class);
        }

        @Override
        protected CharSequence convert(char[] value) {
            return Arrays.toString(value);
        }

        @Override
        @SuppressWarnings("nls")
        public String toString() {
            return CharacterRepresentationProvider.class.getName() + "." + getClass().getSimpleName();
        }
    }

    /**
     * A character representation provider that uses {@link Arrays#toString(byte[])}, or {@link Object#toString()} for other types.
     *
     * @author Rob Spoor
     * @since 2.0
     */
    final class ByteArrayToString extends TypeSpecific<byte[]> {

        /** The single instance. */
        public static final ByteArrayToString INSTANCE = new ByteArrayToString();

        private ByteArrayToString() {
            super(byte[].class);
        }

        @Override
        protected CharSequence convert(byte[] value) {
            return Arrays.toString(value);
        }

        @Override
        @SuppressWarnings("nls")
        public String toString() {
            return CharacterRepresentationProvider.class.getName() + "." + getClass().getSimpleName();
        }
    }

    /**
     * A character representation provider that uses {@link Arrays#toString(short[])}, or {@link Object#toString()} for other types.
     *
     * @author Rob Spoor
     * @since 2.0
     */
    final class ShortArrayToString extends TypeSpecific<short[]> {

        /** The single instance. */
        public static final ShortArrayToString INSTANCE = new ShortArrayToString();

        private ShortArrayToString() {
            super(short[].class);
        }

        @Override
        protected CharSequence convert(short[] value) {
            return Arrays.toString(value);
        }

        @Override
        @SuppressWarnings("nls")
        public String toString() {
            return CharacterRepresentationProvider.class.getName() + "." + getClass().getSimpleName();
        }
    }

    /**
     * A character representation provider that uses {@link Arrays#toString(int[])}, or {@link Object#toString()} for other types.
     *
     * @author Rob Spoor
     * @since 2.0
     */
    final class IntArrayToString extends TypeSpecific<int[]> {

        /** The single instance. */
        public static final IntArrayToString INSTANCE = new IntArrayToString();

        private IntArrayToString() {
            super(int[].class);
        }

        @Override
        protected CharSequence convert(int[] value) {
            return Arrays.toString(value);
        }

        @Override
        @SuppressWarnings("nls")
        public String toString() {
            return CharacterRepresentationProvider.class.getName() + "." + getClass().getSimpleName();
        }
    }

    /**
     * A character representation provider that uses {@link Arrays#toString(long[])}, or {@link Object#toString()} for other types.
     *
     * @author Rob Spoor
     * @since 2.0
     */
    final class LongArrayToString extends TypeSpecific<long[]> {

        /** The single instance. */
        public static final LongArrayToString INSTANCE = new LongArrayToString();

        private LongArrayToString() {
            super(long[].class);
        }

        @Override
        protected CharSequence convert(long[] value) {
            return Arrays.toString(value);
        }

        @Override
        @SuppressWarnings("nls")
        public String toString() {
            return CharacterRepresentationProvider.class.getName() + "." + getClass().getSimpleName();
        }
    }

    /**
     * A character representation provider that uses {@link Arrays#toString(float[])}, or {@link Object#toString()} for other types.
     *
     * @author Rob Spoor
     * @since 2.0
     */
    final class FloatArrayToString extends TypeSpecific<float[]> {

        /** The single instance. */
        public static final FloatArrayToString INSTANCE = new FloatArrayToString();

        private FloatArrayToString() {
            super(float[].class);
        }

        @Override
        protected CharSequence convert(float[] value) {
            return Arrays.toString(value);
        }

        @Override
        @SuppressWarnings("nls")
        public String toString() {
            return CharacterRepresentationProvider.class.getName() + "." + getClass().getSimpleName();
        }
    }

    /**
     * A character representation provider that uses {@link Arrays#toString(double[])}, or {@link Object#toString()} for other types.
     *
     * @author Rob Spoor
     * @since 2.0
     */
    final class DoubleArrayToString extends TypeSpecific<double[]> {

        /** The single instance. */
        public static final DoubleArrayToString INSTANCE = new DoubleArrayToString();

        private DoubleArrayToString() {
            super(double[].class);
        }

        @Override
        protected CharSequence convert(double[] value) {
            return Arrays.toString(value);
        }

        @Override
        @SuppressWarnings("nls")
        public String toString() {
            return CharacterRepresentationProvider.class.getName() + "." + getClass().getSimpleName();
        }
    }

    /**
     * A character representation provider that uses {@link Arrays#toString(Object[])}, or {@link Object#toString()} for other types.
     *
     * @author Rob Spoor
     * @since 2.0
     */
    final class ObjectArrayToString extends TypeSpecific<Object[]> {

        /** The single instance. */
        public static final ObjectArrayToString INSTANCE = new ObjectArrayToString();

        private ObjectArrayToString() {
            super(Object[].class);
        }

        @Override
        protected CharSequence convert(Object[] value) {
            return Arrays.toString(value);
        }

        @Override
        @SuppressWarnings("nls")
        public String toString() {
            return CharacterRepresentationProvider.class.getName() + "." + getClass().getSimpleName();
        }
    }

    /**
     * A character representation provider that uses {@link Arrays#deepToString(Object[])}, or {@link Object#toString()} for other types.
     *
     * @author Rob Spoor
     * @since 2.0
     */
    final class ObjectArrayDeepToString extends TypeSpecific<Object[]> {

        /** The single instance. */
        public static final ObjectArrayDeepToString INSTANCE = new ObjectArrayDeepToString();

        private ObjectArrayDeepToString() {
            super(Object[].class);
        }

        @Override
        protected CharSequence convert(Object[] value) {
            return Arrays.deepToString(value);
        }

        @Override
        @SuppressWarnings("nls")
        public String toString() {
            return CharacterRepresentationProvider.class.getName() + "." + getClass().getSimpleName();
        }
    }

    /**
     * A character representation provider that uses specific character representations for a specific type,
     * or {@link Object#toString()} for other types.
     *
     * @author Rob Spoor
     * @param <T> The type for which to return a specific character representation.
     * @since 2.0
     */
    abstract class TypeSpecific<T> implements CharacterRepresentationProvider {

        private final Class<T> type;

        protected TypeSpecific(Class<T> type) {
            this.type = type;
        }

        protected abstract CharSequence convert(T value);

        @Override
        public CharSequence toCharSequence(Object value) {
            Objects.requireNonNull(value);
            return type.isInstance(value)
                    ? convert(type.cast(value))
                    : value.toString();
        }
    }
}
