/*
 * ObfuscatorFactory.java
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
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import com.github.robtimus.obfuscation.Obfuscator;

/**
 * Utility class that can create {@link Obfuscator Obfuscators} from annotations.
 *
 * @author Rob Spoor
 */
public final class ObfuscatorFactory {

    private ObfuscatorFactory() {
        throw new Error("cannot create instances of " + getClass().getName()); //$NON-NLS-1$
    }

    /**
     * Creates an {@link Obfuscator} based on an {@link ObfuscateAll} annotation.
     *
     * @param annotation The annotation to base the created {@link Obfuscator} on.
     * @return An {@link Obfuscator} based on the given annotation.
     * @throws NullPointerException If the given annotation is null.
     */
    public static Obfuscator createObfuscator(ObfuscateAll annotation) {
        return all(annotation.maskChar());
    }

    /**
     * Creates an {@link Obfuscator} based on an {@link ObfuscateNone} annotation.
     *
     * @param annotation The annotation to base the created {@link Obfuscator} on.
     * @return An {@link Obfuscator} based on the given annotation.
     * @throws NullPointerException If the given annotation is null.
     */
    public static Obfuscator createObfuscator(ObfuscateNone annotation) {
        Objects.requireNonNull(annotation);
        return none();
    }

    /**
     * Creates an {@link Obfuscator} based on an {@link ObfuscateFixedLength} annotation.
     *
     * @param annotation The annotation to base the created {@link Obfuscator} on.
     * @return An {@link Obfuscator} based on the given annotation.
     * @throws NullPointerException If the given annotation is null.
     */
    public static Obfuscator createObfuscator(ObfuscateFixedLength annotation) {
        return fixedLength(annotation.value(), annotation.maskChar());
    }

    /**
     * Creates an {@link Obfuscator} based on an {@link ObfuscateFixedValue} annotation.
     *
     * @param annotation The annotation to base the created {@link Obfuscator} on.
     * @return An {@link Obfuscator} based on the given annotation.
     * @throws NullPointerException If the given annotation is null.
     */
    public static Obfuscator createObfuscator(ObfuscateFixedValue annotation) {
        return fixedValue(annotation.value());
    }

    /**
     * Creates an {@link Obfuscator} based on an {@link ObfuscatePortion} annotation.
     *
     * @param annotation The annotation to base the created {@link Obfuscator} on.
     * @return An {@link Obfuscator} based on the given annotation.
     * @throws NullPointerException If the given annotation is null.
     */
    public static Obfuscator createObfuscator(ObfuscatePortion annotation) {
        return portion()
                .keepAtStart(annotation.keepAtStart())
                .keepAtEnd(annotation.keepAtEnd())
                .atLeastFromStart(annotation.atLeastFromStart())
                .atLeastFromEnd(annotation.atLeastFromEnd())
                .withFixedLength(annotation.fixedLength())
                .withMaskChar(annotation.maskChar())
                .build();
    }

    /**
     * Creates an {@link Obfuscator} based on an {@link ObfuscateUsing} annotation.
     *
     * @param annotation The annotation to base the created {@link Obfuscator} on.
     * @return An {@link Obfuscator} based on the given annotation.
     * @throws NullPointerException  If the given annotation is null.
     * @throws IllegalStateException If the annotation's {@link ObfuscateUsing#value() provider type} does not have a public,
     *                                   no-argument constructor.
     */
    public static Obfuscator createObfuscator(ObfuscateUsing annotation) {
        Class<? extends ObfuscatorProvider> providerClass = annotation.value();
        ObfuscatorProvider provider = ObfuscatorProvider.createInstance(providerClass);
        return provider.obfuscator();
    }

    /**
     * Creates an {@link Obfuscator} based on the used annotations in this package.
     *
     * @param annotatedElement The annotated object for which to check the annotations.
     * @return An {@link Optional} describing the single found {@link Obfuscator} based on the used annotations,
     *         or {@link Optional#empty()} if none of the object's annotations describes an {@link Obfuscator}.
     * @throws IllegalStateException If more than one annotation that describes an {@link Obfuscator} is used,
     *                                   or if {@link ObfuscateUsing} is used but its {@link ObfuscateUsing#value() provider type} does not
     *                                   have a public, no-argument constructor.
     * @see ObfuscateAll
     * @see ObfuscateNone
     * @see ObfuscateFixedLength
     * @see ObfuscateFixedValue
     * @see ObfuscatePortion
     * @see ObfuscateUsing
     */
    public static Optional<Obfuscator> createObfuscator(AnnotatedElement annotatedElement) {
        return createObfuscator(annotatedElement::getAnnotation);
    }

    /**
     * Creates an {@link Obfuscator} based on the used annotations in this package.
     * This method provides a generic way of checking annotations, in case a lookup other than reflection is used.
     *
     * @param annotationLookup The function that maps an annotation type to the used annotation of that type,
     *                             or {@code null} if the annotation is not used.
     * @return An {@link Optional} describing the single found {@link Obfuscator} based on the used annotations,
     *         or {@link Optional#empty()} if none of the object's annotations describes an {@link Obfuscator}.
     * @throws IllegalStateException If more than one annotation that describes an {@link Obfuscator} is used,
     *                                   or if {@link ObfuscateUsing} is used but its {@link ObfuscateUsing#value() provider type} does not
     *                                   have a public, no-argument constructor.
     * @see ObfuscateAll
     * @see ObfuscateNone
     * @see ObfuscateFixedLength
     * @see ObfuscateFixedValue
     * @see ObfuscatePortion
     * @see ObfuscateUsing
     */
    public static Optional<Obfuscator> createObfuscator(Function<Class<? extends Annotation>, Annotation> annotationLookup) {
        List<Annotation> obfuscatorAnnotations = new ArrayList<>(1);
        List<Obfuscator> obfuscators = new ArrayList<>(1);

        addIfPresent(ObfuscateAll.class, annotationLookup, ObfuscatorFactory::createObfuscator, obfuscatorAnnotations, obfuscators);
        addIfPresent(ObfuscateNone.class, annotationLookup, ObfuscatorFactory::createObfuscator, obfuscatorAnnotations, obfuscators);
        addIfPresent(ObfuscateFixedLength.class, annotationLookup, ObfuscatorFactory::createObfuscator, obfuscatorAnnotations, obfuscators);
        addIfPresent(ObfuscateFixedValue.class, annotationLookup, ObfuscatorFactory::createObfuscator, obfuscatorAnnotations, obfuscators);
        addIfPresent(ObfuscatePortion.class, annotationLookup, ObfuscatorFactory::createObfuscator, obfuscatorAnnotations, obfuscators);
        addIfPresent(ObfuscateUsing.class, annotationLookup, ObfuscatorFactory::createObfuscator, obfuscatorAnnotations, obfuscators);

        return findSingleObfuscator(obfuscators, obfuscatorAnnotations);
    }

    private static <A extends Annotation> void addIfPresent(Class<A> annotationType,
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

    /**
     * Creates an {@link Obfuscator} based on multiple annotations.
     *
     * @param annotations The annotations to base the created {@link Obfuscator} on.
     * @return An {@link Optional} describing the single found {@link Obfuscator} based on the given annotations,
     *         or {@link Optional#empty()} if none of the annotations describes an {@link Obfuscator}.
     * @throws NullPointerException If any of the annotations is {@code null}.
     * @throws IllegalStateException If the given annotations contain more than one annotation that describes an {@link Obfuscator},
     *                                   or if the given annotations contain an {@link ObfuscateUsing} annotation but its
     *                                   {@link ObfuscateUsing#value() provider type} does not have a public, no-argument constructor.
     * @see ObfuscateAll
     * @see ObfuscateNone
     * @see ObfuscateFixedLength
     * @see ObfuscateFixedValue
     * @see ObfuscatePortion
     * @see ObfuscateUsing
     */
    public static Optional<Obfuscator> createObfuscator(Annotation... annotations) {
        return createObfuscator(Arrays.asList(annotations));
    }

    /**
     * Creates an {@link Obfuscator} based on multiple annotations.
     *
     * @param annotations The annotations to base the created {@link Obfuscator} on.
     * @return An {@link Optional} describing the single found {@link Obfuscator} based on the given annotations,
     *         or {@link Optional#empty()} if none of the annotations describes an {@link Obfuscator}.
     * @throws NullPointerException If any of the annotations is {@code null}.
     * @throws IllegalStateException If the given annotations contain more than one annotation that describes an {@link Obfuscator},
     *                                   or if the given annotations contain an {@link ObfuscateUsing} annotation but its
     *                                   {@link ObfuscateUsing#value() provider type} does not have a public, no-argument constructor.
     * @see ObfuscateAll
     * @see ObfuscateNone
     * @see ObfuscateFixedLength
     * @see ObfuscateFixedValue
     * @see ObfuscatePortion
     * @see ObfuscateUsing
     */
    public static Optional<Obfuscator> createObfuscator(Collection<? extends Annotation> annotations) {
        List<Annotation> obfuscatorAnnotations = new ArrayList<>(1);
        List<Obfuscator> obfuscators = new ArrayList<>(1);
        for (Annotation annotation : annotations) {
            Class<? extends Annotation> annotationType = annotation.annotationType();
            if (annotationType == ObfuscateAll.class) {
                obfuscatorAnnotations.add(annotation);
                obfuscators.add(createObfuscator((ObfuscateAll) annotation));
            } else if (annotationType == ObfuscateNone.class) {
                obfuscatorAnnotations.add(annotation);
                obfuscators.add(createObfuscator((ObfuscateNone) annotation));
            } else if (annotationType == ObfuscateFixedLength.class) {
                obfuscatorAnnotations.add(annotation);
                obfuscators.add(createObfuscator((ObfuscateFixedLength) annotation));
            } else if (annotationType == ObfuscateFixedValue.class) {
                obfuscatorAnnotations.add(annotation);
                obfuscators.add(createObfuscator((ObfuscateFixedValue) annotation));
            } else if (annotationType == ObfuscatePortion.class) {
                obfuscatorAnnotations.add(annotation);
                obfuscators.add(createObfuscator((ObfuscatePortion) annotation));
            } else if (annotationType == ObfuscateUsing.class) {
                obfuscatorAnnotations.add(annotation);
                obfuscators.add(createObfuscator((ObfuscateUsing) annotation));
            }
        }
        return findSingleObfuscator(obfuscators, obfuscatorAnnotations);
    }

    private static Optional<Obfuscator> findSingleObfuscator(List<Obfuscator> obfuscators, List<Annotation> obfuscatorAnnotations) {
        switch (obfuscators.size()) {
        case 0: return Optional.empty();
        case 1: return Optional.of(obfuscators.get(0));
        default: throw new IllegalStateException(Messages.multipleObfuscatorAnnotationsFound.get(obfuscatorAnnotations));
        }
    }
}
