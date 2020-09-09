/*
 * ObjectFactoryUtils.java
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

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import com.github.robtimus.obfuscation.Obfuscator;

final class ObjectFactoryUtils {

    static final ObjectFactory USING_REFLECTION = ObjectFactoryUtils::createInstance;

    private ObjectFactoryUtils() {
        throw new IllegalStateException("cannot create instances of " + getClass().getName()); //$NON-NLS-1$
    }

    private static <T> T createInstance(Class<T> type) {
        try {
            Constructor<? extends T> constructor = type.getConstructor();
            return constructor.newInstance();
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
    }

    static <A extends Annotation> void addIfPresent(Class<A> annotationType,
            Function<Class<? extends Annotation>, Annotation> annotationLookup, Function<A, Obfuscator> obfuscatorFactory,
            List<Annotation> obfuscatorAnnotations, List<Obfuscator> obfuscators) {

        @SuppressWarnings("unchecked")
        A annotation = (A) annotationLookup.apply(annotationType);
        if (annotation != null) {
            Obfuscator obfuscator = obfuscatorFactory.apply(annotation);
            obfuscatorAnnotations.add(annotation);
            obfuscators.add(obfuscator);
        }
    }

    static Optional<Obfuscator> findSingleObfuscator(List<Obfuscator> obfuscators, List<Annotation> obfuscatorAnnotations) {
        return findSingleElement(obfuscators, obfuscatorAnnotations, Messages.multipleObfuscatorAnnotationsFound::get);
    }

    static Optional<CharacterRepresentationProvider> findSingleCharacterRepresentationProvider(List<CharacterRepresentationProvider> providers,
            List<Annotation> providerAnnotations) {

        return findSingleElement(providers, providerAnnotations, Messages.multipleRepresentedByAnnotationsFound::get);
    }

    private static <T> Optional<T> findSingleElement(List<T> elements, List<Annotation> annotations, Function<Object, String> errorMessage) {
        switch (elements.size()) {
        case 0: return Optional.empty();
        case 1: return Optional.of(elements.get(0));
        default: throw new IllegalStateException(errorMessage.apply(annotations));
        }
    }
}
