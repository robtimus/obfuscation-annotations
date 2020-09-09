/*
 * ObfuscatorProvider.java
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
import com.github.robtimus.obfuscation.Obfuscator;

/**
 * A provider for {@link Obfuscator Obfuscators}.
 *
 * @author Rob Spoor
 */
@FunctionalInterface
public interface ObfuscatorProvider {

    /**
     * Provides an {@link Obfuscator}.
     *
     * @return The provided {@link Obfuscator}.
     */
    Obfuscator obfuscator();

    /**
     * Creates an instance of an {@link Obfuscator} provider type.
     *
     * @param providerClass The {@link Obfuscator} provider type.
     * @return The created instance.
     * @throws IllegalStateException If the given provider type does not have a public, no-argument constructor.
     */
    static ObfuscatorProvider createInstance(Class<? extends ObfuscatorProvider> providerClass) {
        try {
            Constructor<? extends ObfuscatorProvider> constructor = providerClass.getConstructor();
            return constructor.newInstance();
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
    }
}
