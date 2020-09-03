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

import java.lang.reflect.Constructor;
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
 */
@FunctionalInterface
public interface CharacterRepresentationProvider {

    /**
     * Returns a character representation for a value.
     *
     * @param value The value to return the character representation for.
     * @return A {@code CharSequence} containing the value's character representation.
     * @throws NullPointerException If the given value is {@code null}.
     * @since 2.0
     */
    CharSequence toCharSequence(Object value);

    /**
     * Creates an instance of a character representation provider type.
     *
     * @param providerClass The character representation provider type.
     * @return The created instance.
     * @throws IllegalStateException If the given provider type does not have a public, no-argument constructor,
     *                                   and is not one of the nested classes of this interface.
     */
    static CharacterRepresentationProvider createInstance(Class<? extends CharacterRepresentationProvider> providerClass) {
        if (providerClass == ToString.class) {
            return ToString.INSTANCE;
        }
        if (providerClass == BooleanArrayToString.class) {
            return BooleanArrayToString.INSTANCE;
        }
        if (providerClass == CharArrayToString.class) {
            return CharArrayToString.INSTANCE;
        }
        if (providerClass == ByteArrayToString.class) {
            return ByteArrayToString.INSTANCE;
        }
        if (providerClass == ShortArrayToString.class) {
            return ShortArrayToString.INSTANCE;
        }
        if (providerClass == IntArrayToString.class) {
            return IntArrayToString.INSTANCE;
        }
        if (providerClass == LongArrayToString.class) {
            return LongArrayToString.INSTANCE;
        }
        if (providerClass == FloatArrayToString.class) {
            return FloatArrayToString.INSTANCE;
        }
        if (providerClass == DoubleArrayToString.class) {
            return DoubleArrayToString.INSTANCE;
        }
        if (providerClass == ObjectArrayToString.class) {
            return ObjectArrayToString.INSTANCE;
        }
        if (providerClass == ObjectArrayDeepToString.class) {
            return ObjectArrayDeepToString.INSTANCE;
        }

        try {
            Constructor<? extends CharacterRepresentationProvider> constructor = providerClass.getConstructor();
            return constructor.newInstance();
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * A character representation provider that uses {@link Object#toString()}.
     *
     * @author Rob Spoor
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
     * A character representation provider that uses {@link Arrays#toString(boolean[])}, or {@link Object#toString()} for other types.
     *
     * @author Rob Spoor
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
