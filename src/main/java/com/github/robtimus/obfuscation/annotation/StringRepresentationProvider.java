/*
 * StringRepresentationProvider.java
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
import java.util.Objects;
import java.util.function.Supplier;
import com.github.robtimus.obfuscation.Obfuscator;

/**
 * A provider for string representations, that can be used with {@link Obfuscator#obfuscateObject(Object, Supplier)}.
 *
 * @author Rob Spoor
 */
public interface StringRepresentationProvider {

    /**
     * Returns the string representation for a value.
     *
     * @param value The value to return the string representation for.
     * @return A supplier for the string representation of the value.
     * @throws NullPointerException If the given value is {@code null}.
     */
    Supplier<? extends CharSequence> stringRepresentation(Object value);

    /**
     * Creates an instance of a string representation provider type.
     *
     * @param providerClass The string representation provider type.
     * @return The created instance.
     * @throws IllegalStateException If the given provider type does not have a public, no-argument constructor,
     *                                   and is not one of the nested classes of this interface.
     */
    static StringRepresentationProvider createInstance(Class<? extends StringRepresentationProvider> providerClass) {
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
            Constructor<? extends StringRepresentationProvider> constructor = providerClass.getConstructor();
            return constructor.newInstance();
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * A string representation provider that uses {@link Object#toString()}.
     *
     * @author Rob Spoor
     */
    final class ToString implements StringRepresentationProvider {

        /** The single instance. */
        public static final ToString INSTANCE = new ToString();

        private ToString() {
            super();
        }

        @Override
        public Supplier<? extends CharSequence> stringRepresentation(Object value) {
            Objects.requireNonNull(value);
            return value::toString;
        }

        @Override
        @SuppressWarnings("nls")
        public String toString() {
            return StringRepresentationProvider.class.getName() + "." + getClass().getSimpleName();
        }
    }

    /**
     * A string representation provider that uses {@link Arrays#toString(boolean[])}, or {@link Object#toString()} for other types.
     *
     * @author Rob Spoor
     */
    final class BooleanArrayToString extends ForValueType<boolean[]> {

        /** The single instance. */
        public static final BooleanArrayToString INSTANCE = new BooleanArrayToString();

        private BooleanArrayToString() {
            super(boolean[].class);
        }

        @Override
        protected Supplier<? extends CharSequence> typeSpecificStringRepresentation(boolean[] value) {
            return () -> Arrays.toString(value);
        }

        @Override
        @SuppressWarnings("nls")
        public String toString() {
            return StringRepresentationProvider.class.getName() + "." + getClass().getSimpleName();
        }
    }

    /**
     * A string representation provider that uses {@link Arrays#toString(char[])}, or {@link Object#toString()} for other types.
     *
     * @author Rob Spoor
     */
    final class CharArrayToString extends ForValueType<char[]> {

        /** The single instance. */
        public static final CharArrayToString INSTANCE = new CharArrayToString();

        private CharArrayToString() {
            super(char[].class);
        }

        @Override
        protected Supplier<? extends CharSequence> typeSpecificStringRepresentation(char[] value) {
            return () -> Arrays.toString(value);
        }

        @Override
        @SuppressWarnings("nls")
        public String toString() {
            return StringRepresentationProvider.class.getName() + "." + getClass().getSimpleName();
        }
    }

    /**
     * A string representation provider that uses {@link Arrays#toString(byte[])}, or {@link Object#toString()} for other types.
     *
     * @author Rob Spoor
     */
    final class ByteArrayToString extends ForValueType<byte[]> {

        /** The single instance. */
        public static final ByteArrayToString INSTANCE = new ByteArrayToString();

        private ByteArrayToString() {
            super(byte[].class);
        }

        @Override
        protected Supplier<? extends CharSequence> typeSpecificStringRepresentation(byte[] value) {
            return () -> Arrays.toString(value);
        }

        @Override
        @SuppressWarnings("nls")
        public String toString() {
            return StringRepresentationProvider.class.getName() + "." + getClass().getSimpleName();
        }
    }

    /**
     * A string representation provider that uses {@link Arrays#toString(short[])}, or {@link Object#toString()} for other types.
     *
     * @author Rob Spoor
     */
    final class ShortArrayToString extends ForValueType<short[]> {

        /** The single instance. */
        public static final ShortArrayToString INSTANCE = new ShortArrayToString();

        private ShortArrayToString() {
            super(short[].class);
        }

        @Override
        protected Supplier<? extends CharSequence> typeSpecificStringRepresentation(short[] value) {
            return () -> Arrays.toString(value);
        }

        @Override
        @SuppressWarnings("nls")
        public String toString() {
            return StringRepresentationProvider.class.getName() + "." + getClass().getSimpleName();
        }
    }

    /**
     * A string representation provider that uses {@link Arrays#toString(int[])}, or {@link Object#toString()} for other types.
     *
     * @author Rob Spoor
     */
    final class IntArrayToString extends ForValueType<int[]> {

        /** The single instance. */
        public static final IntArrayToString INSTANCE = new IntArrayToString();

        private IntArrayToString() {
            super(int[].class);
        }

        @Override
        protected Supplier<? extends CharSequence> typeSpecificStringRepresentation(int[] value) {
            return () -> Arrays.toString(value);
        }

        @Override
        @SuppressWarnings("nls")
        public String toString() {
            return StringRepresentationProvider.class.getName() + "." + getClass().getSimpleName();
        }
    }

    /**
     * A string representation provider that uses {@link Arrays#toString(long[])}, or {@link Object#toString()} for other types.
     *
     * @author Rob Spoor
     */
    final class LongArrayToString extends ForValueType<long[]> {

        /** The single instance. */
        public static final LongArrayToString INSTANCE = new LongArrayToString();

        private LongArrayToString() {
            super(long[].class);
        }

        @Override
        protected Supplier<? extends CharSequence> typeSpecificStringRepresentation(long[] value) {
            return () -> Arrays.toString(value);
        }

        @Override
        @SuppressWarnings("nls")
        public String toString() {
            return StringRepresentationProvider.class.getName() + "." + getClass().getSimpleName();
        }
    }

    /**
     * A string representation provider that uses {@link Arrays#toString(float[])}, or {@link Object#toString()} for other types.
     *
     * @author Rob Spoor
     */
    final class FloatArrayToString extends ForValueType<float[]> {

        /** The single instance. */
        public static final FloatArrayToString INSTANCE = new FloatArrayToString();

        private FloatArrayToString() {
            super(float[].class);
        }

        @Override
        protected Supplier<? extends CharSequence> typeSpecificStringRepresentation(float[] value) {
            return () -> Arrays.toString(value);
        }

        @Override
        @SuppressWarnings("nls")
        public String toString() {
            return StringRepresentationProvider.class.getName() + "." + getClass().getSimpleName();
        }
    }

    /**
     * A string representation provider that uses {@link Arrays#toString(double[])}, or {@link Object#toString()} for other types.
     *
     * @author Rob Spoor
     */
    final class DoubleArrayToString extends ForValueType<double[]> {

        /** The single instance. */
        public static final DoubleArrayToString INSTANCE = new DoubleArrayToString();

        private DoubleArrayToString() {
            super(double[].class);
        }

        @Override
        protected Supplier<? extends CharSequence> typeSpecificStringRepresentation(double[] value) {
            return () -> Arrays.toString(value);
        }

        @Override
        @SuppressWarnings("nls")
        public String toString() {
            return StringRepresentationProvider.class.getName() + "." + getClass().getSimpleName();
        }
    }

    /**
     * A string representation provider that uses {@link Arrays#toString(Object[])}, or {@link Object#toString()} for other types.
     *
     * @author Rob Spoor
     */
    final class ObjectArrayToString extends ForValueType<Object[]> {

        /** The single instance. */
        public static final ObjectArrayToString INSTANCE = new ObjectArrayToString();

        private ObjectArrayToString() {
            super(Object[].class);
        }

        @Override
        protected Supplier<? extends CharSequence> typeSpecificStringRepresentation(Object[] value) {
            return () -> Arrays.toString(value);
        }

        @Override
        @SuppressWarnings("nls")
        public String toString() {
            return StringRepresentationProvider.class.getName() + "." + getClass().getSimpleName();
        }
    }

    /**
     * A string representation provider that uses {@link Arrays#deepToString(Object[])}, or {@link Object#toString()} for other types.
     *
     * @author Rob Spoor
     */
    final class ObjectArrayDeepToString extends ForValueType<Object[]> {

        /** The single instance. */
        public static final ObjectArrayDeepToString INSTANCE = new ObjectArrayDeepToString();

        private ObjectArrayDeepToString() {
            super(Object[].class);
        }

        @Override
        protected Supplier<? extends CharSequence> typeSpecificStringRepresentation(Object[] value) {
            return () -> Arrays.deepToString(value);
        }

        @Override
        @SuppressWarnings("nls")
        public String toString() {
            return StringRepresentationProvider.class.getName() + "." + getClass().getSimpleName();
        }
    }

    /**
     * A string representation provider that uses specific string representations for a specific type, or {@link Object#toString()} for other types.
     *
     * @author Rob Spoor
     * @param <T> The type for which to return a specific string representation.
     */
    abstract class ForValueType<T> implements StringRepresentationProvider {

        private final Class<T> valueType;

        protected ForValueType(Class<T> valueType) {
            this.valueType = valueType;
        }

        protected abstract Supplier<? extends CharSequence> typeSpecificStringRepresentation(T value);

        @Override
        public Supplier<? extends CharSequence> stringRepresentation(Object value) {
            Objects.requireNonNull(value);
            return valueType.isInstance(value)
                    ? typeSpecificStringRepresentation(valueType.cast(value))
                    : value::toString;
        }
    }
}
