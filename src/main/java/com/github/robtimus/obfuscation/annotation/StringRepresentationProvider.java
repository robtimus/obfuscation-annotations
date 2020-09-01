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
import java.util.function.Function;
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
     * Returns function that acts as the string representation for values.
     * <p>
     * This default implementation delegates to {@link #stringRepresentation(Object)}, then calls {@link Supplier#get()} on it.
     *
     * @return A function that acts as the string representation for values.
     * @since 1.2
     */
    default Function<Object, ? extends CharSequence> stringRepresentation() {
        return t -> stringRepresentation(t).get();
    }

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
        public Function<Object, ? extends CharSequence> stringRepresentation() {
            return Object::toString;
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
    final class BooleanArrayToString extends TypeSpecific<boolean[]> {

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
        public Function<Object, ? extends CharSequence> stringRepresentation() {
            return t -> t instanceof boolean[]
                    ? Arrays.toString((boolean[]) t)
                    : t.toString();
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
    final class CharArrayToString extends TypeSpecific<char[]> {

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
        public Function<Object, ? extends CharSequence> stringRepresentation() {
            return t -> t instanceof char[]
                    ? Arrays.toString((char[]) t)
                    : t.toString();
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
    final class ByteArrayToString extends TypeSpecific<byte[]> {

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
        public Function<Object, ? extends CharSequence> stringRepresentation() {
            return t -> t instanceof byte[]
                    ? Arrays.toString((byte[]) t)
                    : t.toString();
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
    final class ShortArrayToString extends TypeSpecific<short[]> {

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
        public Function<Object, ? extends CharSequence> stringRepresentation() {
            return t -> t instanceof short[]
                    ? Arrays.toString((short[]) t)
                    : t.toString();
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
    final class IntArrayToString extends TypeSpecific<int[]> {

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
        public Function<Object, ? extends CharSequence> stringRepresentation() {
            return t -> t instanceof int[]
                    ? Arrays.toString((int[]) t)
                    : t.toString();
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
    final class LongArrayToString extends TypeSpecific<long[]> {

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
        public Function<Object, ? extends CharSequence> stringRepresentation() {
            return t -> t instanceof long[]
                    ? Arrays.toString((long[]) t)
                    : t.toString();
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
    final class FloatArrayToString extends TypeSpecific<float[]> {

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
        public Function<Object, ? extends CharSequence> stringRepresentation() {
            return t -> t instanceof float[]
                    ? Arrays.toString((float[]) t)
                    : t.toString();
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
    final class DoubleArrayToString extends TypeSpecific<double[]> {

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
        public Function<Object, ? extends CharSequence> stringRepresentation() {
            return t -> t instanceof double[]
                    ? Arrays.toString((double[]) t)
                    : t.toString();
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
    final class ObjectArrayToString extends TypeSpecific<Object[]> {

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
        public Function<Object, ? extends CharSequence> stringRepresentation() {
            return t -> t instanceof Object[]
                    ? Arrays.toString((Object[]) t)
                    : t.toString();
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
    final class ObjectArrayDeepToString extends TypeSpecific<Object[]> {

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
        public Function<Object, ? extends CharSequence> stringRepresentation() {
            return t -> t instanceof Object[]
                    ? Arrays.deepToString((Object[]) t)
                    : t.toString();
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
    abstract class TypeSpecific<T> implements StringRepresentationProvider {

        private final Class<T> type;

        protected TypeSpecific(Class<T> type) {
            this.type = type;
        }

        protected abstract Supplier<? extends CharSequence> typeSpecificStringRepresentation(T value);

        @Override
        public Supplier<? extends CharSequence> stringRepresentation(Object value) {
            Objects.requireNonNull(value);
            return type.isInstance(value)
                    ? typeSpecificStringRepresentation(type.cast(value))
                    : value::toString;
        }
    }
}
