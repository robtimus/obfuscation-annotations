/*
 * ObjectFactoryTest.java
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

import static com.github.robtimus.obfuscation.Obfuscator.all;
import static com.github.robtimus.obfuscation.Obfuscator.fixedLength;
import static com.github.robtimus.obfuscation.Obfuscator.fixedValue;
import static com.github.robtimus.obfuscation.Obfuscator.none;
import static com.github.robtimus.obfuscation.Obfuscator.portion;
import static com.github.robtimus.obfuscation.annotation.ObjectFactory.usingReflection;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Function;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import com.github.robtimus.obfuscation.Obfuscator;
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
class ObjectFactoryTest {

    private final ObjectFactory factory = usingReflection();

    @Nested
    @DisplayName("obfuscator(Annotation...), obfuscator(AnnotatedElement)")
    class ObfuscatorFromAnnotations {

        @Test
        @DisplayName("@ObfuscateAll")
        void testObfuscateAll() {
            Field field = getField("obfuscateAll");
            Annotation[] annotations = field.getAnnotations();
            Obfuscator obfuscator = all('x');
            assertAll(
                    () -> assertEquals(Optional.of(obfuscator), factory.obfuscator(annotations)),
                    () -> assertEquals(Optional.of(obfuscator), factory.obfuscator(field)));
        }

        @Test
        @DisplayName("@ObfuscateNone")
        void testObfuscateNone() {
            Field field = getField("obfuscateNone");
            Annotation[] annotations = field.getAnnotations();
            Obfuscator obfuscator = none();
            assertAll(
                    () -> assertEquals(Optional.of(obfuscator), factory.obfuscator(annotations)),
                    () -> assertEquals(Optional.of(obfuscator), factory.obfuscator(field)));
        }

        @Test
        @DisplayName("@ObfuscateFixedLength")
        void testObfuscateFixedLength() {
            Field field = getField("obfuscateFixedLength");
            Annotation[] annotations = field.getAnnotations();
            Obfuscator obfuscator = fixedLength(3, 'x');
            assertAll(
                    () -> assertEquals(Optional.of(obfuscator), factory.obfuscator(annotations)),
                    () -> assertEquals(Optional.of(obfuscator), factory.obfuscator(field)));
        }

        @Test
        @DisplayName("@ObfuscateFixedValue")
        void testObfuscateFixedValue() {
            Field field = getField("obfuscateFixedValue");
            Annotation[] annotations = field.getAnnotations();
            Obfuscator obfuscator = fixedValue("fixed");
            assertAll(
                    () -> assertEquals(Optional.of(obfuscator), factory.obfuscator(annotations)),
                    () -> assertEquals(Optional.of(obfuscator), factory.obfuscator(field)));
        }

        @Test
        @DisplayName("@ObfuscatePortion")
        void testObfuscatePortion() {
            Field field = getField("obfuscatePortion");
            Annotation[] annotations = field.getAnnotations();
            Obfuscator obfuscator = portion()
                    .keepAtStart(1)
                    .keepAtEnd(2)
                    .atLeastFromStart(3)
                    .atLeastFromEnd(4)
                    .withFixedTotalLength(8)
                    .withMaskChar('x')
                    .build();
            assertAll(
                    () -> assertEquals(Optional.of(obfuscator), factory.obfuscator(annotations)),
                    () -> assertEquals(Optional.of(obfuscator), factory.obfuscator(field)));
        }

        @Test
        @DisplayName("@ObfuscatePortion with fixedLength")
        @SuppressWarnings("deprecation")
        void testObfuscatePortionWithFixedLength() {
            Field field = getField("obfuscatePortionWithFixedLength");
            Annotation[] annotations = field.getAnnotations();
            Obfuscator obfuscator = portion()
                    .keepAtStart(1)
                    .keepAtEnd(2)
                    .atLeastFromStart(3)
                    .atLeastFromEnd(4)
                    .withFixedLength(5)
                    .withMaskChar('x')
                    .build();
            assertAll(
                    () -> assertEquals(Optional.of(obfuscator), factory.obfuscator(annotations)),
                    () -> assertEquals(Optional.of(obfuscator), factory.obfuscator(field)));
        }

        @Nested
        @DisplayName("@ObfuscateUsing")
        class ObfuscateUsingFromAnnotations {

            @Test
            @DisplayName("valid implementation")
            void testValidImplementation() {
                Field field = getField("obfuscateUsingValidImplementation");
                Annotation[] annotations = field.getAnnotations();
                Obfuscator obfuscator = fixedLength(8);
                assertAll(
                        () -> assertEquals(Optional.of(obfuscator), factory.obfuscator(annotations)),
                        () -> assertEquals(Optional.of(obfuscator), factory.obfuscator(field)));
            }

            @Test
            @DisplayName("invalid implementation")
            void testInvalidImplementation() {
                Field field = getField("obfuscateUsingInvalidImplementation");
                Annotation[] annotations = field.getAnnotations();
                assertAll(
                        () -> testInvalidImplementation(factory::obfuscator, annotations),
                        () -> testInvalidImplementation(factory::obfuscator, field));
            }

            private <T> void testInvalidImplementation(Function<T, Optional<Obfuscator>> method, T input) {
                IllegalStateException exception = assertThrows(IllegalStateException.class, () -> method.apply(input));
                assertThat(exception.getCause(), instanceOf(IllegalStateException.class));
                assertThat(exception.getCause().getCause(), instanceOf(ReflectiveOperationException.class));
            }
        }

        @Test
        @DisplayName("multiple annotations")
        void testMultipleAnnotations() {
            Field field = getField("multipleAnnotations");
            Annotation[] annotations = field.getAnnotations();
            assertAll(
                    () -> testMultipleAnnotations(factory::obfuscator, annotations, annotations),
                    () -> testMultipleAnnotations(factory::obfuscator, field, annotations));
        }

        private <T> void testMultipleAnnotations(Function<T, Optional<Obfuscator>> method, T input, Annotation[] annotations) {
            IllegalStateException exception = assertThrows(IllegalStateException.class, () -> method.apply(input));
            assertEquals(Messages.multipleObfuscatorAnnotationsFound.get(Arrays.asList(annotations)), exception.getMessage());
        }

        @Test
        @DisplayName("no annotations")
        void testNoAnnotations() {
            Field field = getField("noAnnotations");
            Annotation[] annotations = field.getAnnotations();
            assertAll(
                    () -> assertEquals(Optional.empty(), factory.obfuscator(annotations)),
                    () -> assertEquals(Optional.empty(), factory.obfuscator(field)));
        }
    }

    @Nested
    @DisplayName("obfuscatorProvider(Class<? extends ObfuscatorProvider>)")
    class ObfuscatorProviderFromClass {

        @Test
        @DisplayName("valid implementation")
        void testWithValidImplemenation() {
            ObfuscatorProvider provider = factory.obfuscatorProvider(ValidImplementation.class);
            assertEquals(Obfuscator.fixedLength(8), provider.obfuscator());
        }

        @Test
        @DisplayName("invalid implementation")
        void testWithInvalidImplemenation() {
            IllegalStateException exception = assertThrows(IllegalStateException.class,
                    () -> factory.obfuscatorProvider(InvalidImplementation.class));
            assertThat(exception.getCause(), instanceOf(ReflectiveOperationException.class));
        }
    }

    @Nested
    @DisplayName("characterRepresentationProvider(Annotation...), characterRepresentationProvider(AnnotatedElement)")
    class CharacterRepresentationProviderFromAnnotations {

        @Test
        @DisplayName("valid implementation")
        void testValidImplementation() {
            Field field = getField("representedByValidImplementation");
            Annotation[] annotations = field.getAnnotations();
            assertAll(
                    () -> testValidImplementation(factory::characterRepresentationProvider, annotations),
                    () -> testValidImplementation(factory::characterRepresentationProvider, field));
        }

        private <T> void testValidImplementation(Function<T, Optional<CharacterRepresentationProvider>> method, T input) {
            Optional<CharacterRepresentationProvider> optional = method.apply(input);
            assertNotEquals(Optional.empty(), optional);
            assertThat(optional.get(), instanceOf(ValidImplementation.class));
        }

        @Test
        @DisplayName("invalid implementation")
        void testInvalidImplementation() {
            Field field = getField("representedByInvalidImplementation");
            Annotation[] annotations = field.getAnnotations();
            assertAll(
                    () -> testInvalidImplementation(factory::characterRepresentationProvider, annotations),
                    () -> testInvalidImplementation(factory::characterRepresentationProvider, field));
        }

        private <T> void testInvalidImplementation(Function<T, Optional<CharacterRepresentationProvider>> method, T input) {
            IllegalStateException exception = assertThrows(IllegalStateException.class, () -> method.apply(input));
            assertThat(exception.getCause(), instanceOf(IllegalStateException.class));
            assertThat(exception.getCause().getCause(), instanceOf(ReflectiveOperationException.class));
        }

        @Test
        @DisplayName("multiple annotations")
        void testMultipleAnnotations() {
            Field field = getField("representedByValidImplementation");
            Annotation[] annotations = { field.getAnnotation(RepresentedBy.class), field.getAnnotation(RepresentedBy.class), };
            IllegalStateException exception = assertThrows(IllegalStateException.class, () -> factory.characterRepresentationProvider(annotations));
            assertEquals(Messages.multipleRepresentedByAnnotationsFound.get(Arrays.asList(annotations)), exception.getMessage());
        }

        @Test
        @DisplayName("no annotations")
        void testNoAnnotations() {
            Field field = getField("noAnnotations");
            Annotation[] annotations = field.getAnnotations();
            assertAll(
                    () -> assertEquals(Optional.empty(), factory.characterRepresentationProvider(annotations)),
                    () -> assertEquals(Optional.empty(), factory.characterRepresentationProvider(field)));
        }
    }

    @Nested
    @DisplayName("characterRepresentationProvider(Class<? extends CharacterRepresentationProvider<?>>)")
    class CharacterRepresentationProviderFromClass {

        @Test
        @DisplayName("ToString.class")
        void testWithToString() {
            assertSame(ToString.INSTANCE, factory.characterRepresentationProvider(ToString.class));
        }

        @Test
        @DisplayName("BooleanArrayToString.class")
        void testWithBooleanArrayToString() {
            assertSame(BooleanArrayToString.INSTANCE, factory.characterRepresentationProvider(BooleanArrayToString.class));
        }

        @Test
        @DisplayName("CharArrayToString.class")
        void testWithCharArrayToString() {
            assertSame(CharArrayToString.INSTANCE, factory.characterRepresentationProvider(CharArrayToString.class));
        }

        @Test
        @DisplayName("ByteArrayToString.class")
        void testWithByteArrayToString() {
            assertSame(ByteArrayToString.INSTANCE, factory.characterRepresentationProvider(ByteArrayToString.class));
        }

        @Test
        @DisplayName("ShortArrayToString.class")
        void testShortArrayToString() {
            assertSame(ShortArrayToString.INSTANCE, factory.characterRepresentationProvider(ShortArrayToString.class));
        }

        @Test
        @DisplayName("IntArrayToString.class")
        void testWithIntArrayToString() {
            assertSame(IntArrayToString.INSTANCE, factory.characterRepresentationProvider(IntArrayToString.class));
        }

        @Test
        @DisplayName("LongArrayToString.class")
        void testWithLongArrayToString() {
            assertSame(LongArrayToString.INSTANCE, factory.characterRepresentationProvider(LongArrayToString.class));
        }

        @Test
        @DisplayName("FloatArrayToString.class")
        void testWithFloatArrayToString() {
            assertSame(FloatArrayToString.INSTANCE, factory.characterRepresentationProvider(FloatArrayToString.class));
        }

        @Test
        @DisplayName("DoubleArrayToString.class")
        void testWithDoubleArrayToString() {
            assertSame(DoubleArrayToString.INSTANCE, factory.characterRepresentationProvider(DoubleArrayToString.class));
        }

        @Test
        @DisplayName("ObjectArrayToString.class")
        void testWithObjectArrayToString() {
            assertSame(ObjectArrayToString.INSTANCE, factory.characterRepresentationProvider(ObjectArrayToString.class));
        }

        @Test
        @DisplayName("ObjectArrayDeepToString.class")
        void testWithObjectArrayDeepToString() {
            assertSame(ObjectArrayDeepToString.INSTANCE, factory.characterRepresentationProvider(ObjectArrayDeepToString.class));
        }

        @Test
        @DisplayName("valid implementation")
        void testWithValidImplemenation() {
            CharacterRepresentationProvider provider = factory.characterRepresentationProvider(ValidImplementation.class);
            Object value = new Object();
            CharSequence representation = provider.toCharSequence(value);
            assertEquals(value.toString(), representation);
        }

        @Test
        @DisplayName("invalid implementation")
        void testWithInvalidImplemenation() {
            IllegalStateException exception = assertThrows(IllegalStateException.class,
                    () -> factory.characterRepresentationProvider(InvalidImplementation.class));
            assertThat(exception.getCause(), instanceOf(ReflectiveOperationException.class));
        }
    }

    private Field getField(String name) {
        return assertDoesNotThrow(() -> ClassWithAnnotations.class.getDeclaredField(name));
    }

    @SuppressWarnings("unused")
    private static final class ClassWithAnnotations {

        @ObfuscateAll(maskChar = 'x')
        private String obfuscateAll;

        @ObfuscateNone
        private String obfuscateNone;

        @ObfuscateFixedLength(value = 3, maskChar = 'x')
        private String obfuscateFixedLength;

        @ObfuscateFixedValue("fixed")
        private String obfuscateFixedValue;

        @ObfuscatePortion(keepAtStart = 1, keepAtEnd = 2, atLeastFromStart = 3, atLeastFromEnd = 4, fixedTotalLength = 8, maskChar = 'x')
        private String obfuscatePortion;

        @ObfuscatePortion(keepAtStart = 1, keepAtEnd = 2, atLeastFromStart = 3, atLeastFromEnd = 4, fixedLength = 5, maskChar = 'x')
        private String obfuscatePortionWithFixedLength;

        @ObfuscateUsing(ValidImplementation.class)
        private String obfuscateUsingValidImplementation;

        @ObfuscateUsing(InvalidImplementation.class)
        private String obfuscateUsingInvalidImplementation;

        @ObfuscateAll
        @ObfuscateNone
        private String multipleAnnotations;

        private String noAnnotations;

        @RepresentedBy(ValidImplementation.class)
        private String representedByValidImplementation;

        @RepresentedBy(InvalidImplementation.class)
        private String representedByInvalidImplementation;
    }

    public static final class ValidImplementation implements ObfuscatorProvider, CharacterRepresentationProvider {

        @Override
        public Obfuscator obfuscator() {
            return Obfuscator.fixedLength(8);
        }

        @Override
        public CharSequence toCharSequence(Object value) {
            return value.toString();
        }
    }

    private static final class InvalidImplementation implements ObfuscatorProvider, CharacterRepresentationProvider {

        @Override
        public Obfuscator obfuscator() {
            throw new UnsupportedOperationException();
        }

        @Override
        public CharSequence toCharSequence(Object value) {
            throw new UnsupportedOperationException();
        }
    }
}
