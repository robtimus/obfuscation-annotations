/*
 * ObfuscatorProviderTest.java
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

import static com.github.robtimus.obfuscation.annotation.ObfuscatorProvider.createInstance;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import com.github.robtimus.obfuscation.Obfuscator;

class ObfuscatorProviderTest {

    @Nested
    @DisplayName("createInstance(Class<? extends StringRepresentationProvider<?>>)")
    class CreateInstance {

        @Test
        void testWithValidImplemenation() {
            ObfuscatorProvider provider = createInstance(ValidImplementation.class);
            assertEquals(Obfuscator.fixedLength(3), provider.obfuscator());
        }

        @Test
        void testWithInvalidImplemenation() {
            IllegalStateException exception = assertThrows(IllegalStateException.class, () -> createInstance(InvalidImplementation.class));
            assertThat(exception.getCause(), instanceOf(ReflectiveOperationException.class));
        }
    }

    public static final class ValidImplementation implements ObfuscatorProvider {

        @Override
        public Obfuscator obfuscator() {
            return Obfuscator.fixedLength(3);
        }
    }

    private static final class InvalidImplementation implements ObfuscatorProvider {

        @Override
        public Obfuscator obfuscator() {
            throw new UnsupportedOperationException();
        }
    }
}
