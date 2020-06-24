/*
 * ObfuscatorFactoryTest.java
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
import static com.github.robtimus.obfuscation.annotation.ObfuscatorFactory.createObfuscator;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import com.github.robtimus.obfuscation.Obfuscator;

@SuppressWarnings("nls")
class ObfuscatorFactoryTest {

    @Nested
    @DisplayName("createObfuscator(Annotation...), createObfuscator(AnnotatedElement)")
    class CreateObfuscatorFromAnnotationsTest {

        @Test
        @DisplayName("@ObfuscateAll")
        void testObfuscateAll() {
            Field field = getField("obfuscateAll");
            Annotation[] annotations = field.getAnnotations();
            Obfuscator obfuscator = all('x');
            assertAll(
                    () -> assertEquals(Optional.of(obfuscator), createObfuscator(annotations)),
                    () -> assertEquals(Optional.of(obfuscator), createObfuscator(field)));
        }

        @Test
        @DisplayName("@ObfuscateNone")
        void testObfuscateNone() {
            Field field = getField("obfuscateNone");
            Annotation[] annotations = field.getAnnotations();
            Obfuscator obfuscator = none();
            assertAll(
                    () -> assertEquals(Optional.of(obfuscator), createObfuscator(annotations)),
                    () -> assertEquals(Optional.of(obfuscator), createObfuscator(field)));
        }

        @Test
        @DisplayName("@ObfuscateFixedLength")
        void testObfuscateFixedLength() {
            Field field = getField("obfuscateFixedLength");
            Annotation[] annotations = field.getAnnotations();
            Obfuscator obfuscator = fixedLength(3, 'x');
            assertAll(
                    () -> assertEquals(Optional.of(obfuscator), createObfuscator(annotations)),
                    () -> assertEquals(Optional.of(obfuscator), createObfuscator(field)));
        }

        @Test
        @DisplayName("@ObfuscateFixedValue")
        void testObfuscateFixedValue() {
            Field field = getField("obfuscateFixedValue");
            Annotation[] annotations = field.getAnnotations();
            Obfuscator obfuscator = fixedValue("fixed");
            assertAll(
                    () -> assertEquals(Optional.of(obfuscator), createObfuscator(annotations)),
                    () -> assertEquals(Optional.of(obfuscator), createObfuscator(field)));
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
                    .withFixedLength(5)
                    .withMaskChar('x')
                    .build();
            assertAll(
                    () -> assertEquals(Optional.of(obfuscator), createObfuscator(annotations)),
                    () -> assertEquals(Optional.of(obfuscator), createObfuscator(field)));
        }

        @Test
        @DisplayName("@ObfuscateUsing")
        void testObfuscateUsing() {
            Field field = getField("obfuscateUsing");
            Annotation[] annotations = field.getAnnotations();
            Obfuscator obfuscator = fixedLength(8);
            assertAll(
                    () -> assertEquals(Optional.of(obfuscator), createObfuscator(annotations)),
                    () -> assertEquals(Optional.of(obfuscator), createObfuscator(field)));
        }

        @Test
        @DisplayName("multiple annotations")
        void testMultipleAnnotations() {
            Field field = getField("multipleAnnotations");
            Annotation[] annotations = field.getAnnotations();
            IllegalStateException exception = assertThrows(IllegalStateException.class, () -> createObfuscator(annotations));
            assertEquals(Messages.multipleObfuscatorAnnotationsFound.get(Arrays.asList(annotations)), exception.getMessage());
        }

        @Test
        @DisplayName("no annotations")
        void testNoAnnotations() {
            Field field = getField("noAnnotations");
            Annotation[] annotations = field.getAnnotations();
            assertAll(
                    () -> assertEquals(Optional.empty(), createObfuscator(annotations)),
                    () -> assertEquals(Optional.empty(), createObfuscator(field)));
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

        @ObfuscatePortion(keepAtStart = 1, keepAtEnd = 2, atLeastFromStart = 3, atLeastFromEnd = 4, fixedLength = 5, maskChar = 'x')
        private String obfuscatePortion;

        @ObfuscateUsing(Provider.class)
        private String obfuscateUsing;

        @ObfuscateAll
        @ObfuscateNone
        private String multipleAnnotations;

        private String noAnnotations;
    }

    public static final class Provider implements ObfuscatorProvider {

        @Override
        public Obfuscator obfuscator() {
            return Obfuscator.fixedLength(8);
        }
    }
}
