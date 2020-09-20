/*
 * ObjectFactory.java
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
import static com.github.robtimus.obfuscation.annotation.ObjectFactoryUtils.addIfPresent;
import static com.github.robtimus.obfuscation.annotation.ObjectFactoryUtils.findSingleCharacterRepresentationProvider;
import static com.github.robtimus.obfuscation.annotation.ObjectFactoryUtils.findSingleObfuscator;
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

/**
 * A factory for creating objects. This can be used to easily create {@link Obfuscator Obfuscators} and
 * {@link CharacterRepresentationProvider CharacterRepresentationProviders} from annotations.
 *
 * @author Rob Spoor
 * @since 2.0
 */
@FunctionalInterface
public interface ObjectFactory {

    /**
     * Returns an instance of a specific type.
     *
     * @param <T>  The type to return an instance of.
     * @param type A class representing the type to return an instance of.
     * @return An instance of the given type.
     * @throws NullPointerException If the given type is {@code null}.
     * @throws RuntimeException If no instance could be returned.
     *                              This is often an exception like {@link IllegalStateException} or {@link IllegalArgumentException}, but other
     *                              exceptions are also possible.
     */
    <T> T instance(Class<T> type);

    /**
     * Returns an {@link Obfuscator} based on an {@link ObfuscateAll} annotation.
     *
     * @param annotation The annotation to base the returned {@link Obfuscator} on.
     * @return An {@link Obfuscator} based on the given annotation.
     * @throws NullPointerException If the given annotation is null.
     */
    default Obfuscator obfuscator(ObfuscateAll annotation) {
        return all(annotation.maskChar());
    }

    /**
     * Returns an {@link Obfuscator} based on an {@link ObfuscateNone} annotation.
     *
     * @param annotation The annotation to base the returned {@link Obfuscator} on.
     * @return An {@link Obfuscator} based on the given annotation.
     * @throws NullPointerException If the given annotation is null.
     */
    default Obfuscator obfuscator(ObfuscateNone annotation) {
        Objects.requireNonNull(annotation);
        return none();
    }

    /**
     * Returns an {@link Obfuscator} based on an {@link ObfuscateFixedLength} annotation.
     *
     * @param annotation The annotation to base the returned {@link Obfuscator} on.
     * @return An {@link Obfuscator} based on the given annotation.
     * @throws NullPointerException If the given annotation is null.
     */
    default Obfuscator obfuscator(ObfuscateFixedLength annotation) {
        return fixedLength(annotation.value(), annotation.maskChar());
    }

    /**
     * Returns an {@link Obfuscator} based on an {@link ObfuscateFixedValue} annotation.
     *
     * @param annotation The annotation to base the returned {@link Obfuscator} on.
     * @return An {@link Obfuscator} based on the given annotation.
     * @throws NullPointerException If the given annotation is null.
     */
    default Obfuscator obfuscator(ObfuscateFixedValue annotation) {
        return fixedValue(annotation.value());
    }

    /**
     * Returns an {@link Obfuscator} based on an {@link ObfuscatePortion} annotation.
     *
     * @param annotation The annotation to base the returned {@link Obfuscator} on.
     * @return An {@link Obfuscator} based on the given annotation.
     * @throws NullPointerException If the given annotation is null.
     */
    default Obfuscator obfuscator(ObfuscatePortion annotation) {
        return portion()
                .keepAtStart(annotation.keepAtStart())
                .keepAtEnd(annotation.keepAtEnd())
                .atLeastFromStart(annotation.atLeastFromStart())
                .atLeastFromEnd(annotation.atLeastFromEnd())
                .withFixedTotalLength(annotation.fixedTotalLength())
                .withMaskChar(annotation.maskChar())
                .build();
    }

    /**
     * Returns an {@link Obfuscator} based on an {@link ObfuscateUsing} annotation.
     *
     * @param annotation The annotation to base the returned {@link Obfuscator} on.
     * @return An {@link Obfuscator} based on the given annotation.
     * @throws NullPointerException  If the given annotation is null.
     * @throws IllegalStateException If the annotation's {@link ObfuscateUsing#value() provider type} cannot be {@link #instance(Class) instantiated}.
     */
    default Obfuscator obfuscator(ObfuscateUsing annotation) {
        ObfuscatorProvider provider = obfuscatorProvider(annotation);
        return provider.obfuscator();
    }

    /**
     * Returns an {@link Obfuscator} based on the used annotations in this package.
     *
     * @param annotatedElement The annotated object for which to check the annotations.
     * @return An {@link Optional} describing the single found {@link Obfuscator} based on the used annotations,
     *         or {@link Optional#empty()} if none of the object's annotations describes an {@link Obfuscator}.
     * @throws IllegalStateException If more than one annotation that describes an {@link Obfuscator} is used,
     *                                   or if {@link ObfuscateUsing} is used but its {@link ObfuscateUsing#value() provider type} cannot be
     *                                   {@link #instance(Class) instantiated}.
     * @see ObfuscateAll
     * @see ObfuscateNone
     * @see ObfuscateFixedLength
     * @see ObfuscateFixedValue
     * @see ObfuscatePortion
     * @see ObfuscateUsing
     */
    default Optional<Obfuscator> obfuscator(AnnotatedElement annotatedElement) {
        return obfuscator(annotatedElement::getAnnotation);
    }

    /**
     * Returns an {@link Obfuscator} based on the used annotations in this package.
     * This method provides a generic way of checking annotations, in case a lookup other than reflection is used.
     *
     * @param annotationLookup The function that maps an annotation type to the used annotation of that type,
     *                             or {@code null} if the annotation is not used.
     * @return An {@link Optional} describing the single found {@link Obfuscator} based on the used annotations,
     *         or {@link Optional#empty()} if the function does not return any non-{@code null} value for any of the annotations in this package.
     * @throws IllegalStateException If more than one annotation that describes an {@link Obfuscator} is used,
     *                                   or if {@link ObfuscateUsing} is used but its {@link ObfuscateUsing#value() provider type} cannot be
     *                                   {@link #instance(Class) instantiated}.
     * @see ObfuscateAll
     * @see ObfuscateNone
     * @see ObfuscateFixedLength
     * @see ObfuscateFixedValue
     * @see ObfuscatePortion
     * @see ObfuscateUsing
     */
    default Optional<Obfuscator> obfuscator(Function<Class<? extends Annotation>, Annotation> annotationLookup) {
        List<Annotation> obfuscatorAnnotations = new ArrayList<>(1);
        List<Obfuscator> obfuscators = new ArrayList<>(1);

        addIfPresent(ObfuscateAll.class, annotationLookup, this::obfuscator, obfuscatorAnnotations, obfuscators);
        addIfPresent(ObfuscateNone.class, annotationLookup, this::obfuscator, obfuscatorAnnotations, obfuscators);
        addIfPresent(ObfuscateFixedLength.class, annotationLookup, this::obfuscator, obfuscatorAnnotations, obfuscators);
        addIfPresent(ObfuscateFixedValue.class, annotationLookup, this::obfuscator, obfuscatorAnnotations, obfuscators);
        addIfPresent(ObfuscatePortion.class, annotationLookup, this::obfuscator, obfuscatorAnnotations, obfuscators);
        addIfPresent(ObfuscateUsing.class, annotationLookup, this::obfuscator, obfuscatorAnnotations, obfuscators);

        return findSingleObfuscator(obfuscators, obfuscatorAnnotations);
    }

    /**
     * Returns an {@link Obfuscator} based on multiple annotations.
     *
     * @param annotations The annotations to base the returned {@link Obfuscator} on.
     * @return An {@link Optional} describing the single found {@link Obfuscator} based on the given annotations,
     *         or {@link Optional#empty()} if none of the annotations describes an {@link Obfuscator}.
     * @throws NullPointerException If any of the annotations is {@code null}.
     * @throws IllegalStateException If the given annotations contain more than one annotation that describes an {@link Obfuscator},
     *                                   or if the given annotations contain an {@link ObfuscateUsing} annotation but its
     *                                   {@link ObfuscateUsing#value() provider type} cannot be {@link #instance(Class) instantiated}.
     * @see ObfuscateAll
     * @see ObfuscateNone
     * @see ObfuscateFixedLength
     * @see ObfuscateFixedValue
     * @see ObfuscatePortion
     * @see ObfuscateUsing
     */
    default Optional<Obfuscator> obfuscator(Annotation... annotations) {
        return obfuscator(Arrays.asList(annotations));
    }

    /**
     * Returns an {@link Obfuscator} based on multiple annotations.
     *
     * @param annotations The annotations to base the returned {@link Obfuscator} on.
     * @return An {@link Optional} describing the single found {@link Obfuscator} based on the given annotations,
     *         or {@link Optional#empty()} if none of the annotations describes an {@link Obfuscator}.
     * @throws NullPointerException If any of the annotations is {@code null}.
     * @throws IllegalStateException If the given annotations contain more than one annotation that describes an {@link Obfuscator},
     *                                   or if the given annotations contain an {@link ObfuscateUsing} annotation but its
     *                                   {@link ObfuscateUsing#value() provider type} cannot be {@link #instance(Class) instantiated}.
     * @see ObfuscateAll
     * @see ObfuscateNone
     * @see ObfuscateFixedLength
     * @see ObfuscateFixedValue
     * @see ObfuscatePortion
     * @see ObfuscateUsing
     */
    default Optional<Obfuscator> obfuscator(Collection<? extends Annotation> annotations) {
        List<Annotation> obfuscatorAnnotations = new ArrayList<>(1);
        List<Obfuscator> obfuscators = new ArrayList<>(1);
        for (Annotation annotation : annotations) {
            Class<? extends Annotation> annotationType = annotation.annotationType();
            if (annotationType == ObfuscateAll.class) {
                obfuscatorAnnotations.add(annotation);
                obfuscators.add(obfuscator((ObfuscateAll) annotation));
            } else if (annotationType == ObfuscateNone.class) {
                obfuscatorAnnotations.add(annotation);
                obfuscators.add(obfuscator((ObfuscateNone) annotation));
            } else if (annotationType == ObfuscateFixedLength.class) {
                obfuscatorAnnotations.add(annotation);
                obfuscators.add(obfuscator((ObfuscateFixedLength) annotation));
            } else if (annotationType == ObfuscateFixedValue.class) {
                obfuscatorAnnotations.add(annotation);
                obfuscators.add(obfuscator((ObfuscateFixedValue) annotation));
            } else if (annotationType == ObfuscatePortion.class) {
                obfuscatorAnnotations.add(annotation);
                obfuscators.add(obfuscator((ObfuscatePortion) annotation));
            } else if (annotationType == ObfuscateUsing.class) {
                obfuscatorAnnotations.add(annotation);
                obfuscators.add(obfuscator((ObfuscateUsing) annotation));
            }
        }
        return findSingleObfuscator(obfuscators, obfuscatorAnnotations);
    }

    /**
     * Returns an {@link ObfuscatorProvider} based on an {@link ObfuscateUsing} annotation.
     *
     * @param annotation The annotation to base the returned {@link ObfuscatorProvider} on.
     * @return An {@link ObfuscatorProvider} based on the given annotation.
     * @throws NullPointerException  If the given annotation is null.
     * @throws IllegalStateException If the annotation's {@link ObfuscateUsing#value() provider type} cannot be {@link #instance(Class) instantiated}.
     */
    default ObfuscatorProvider obfuscatorProvider(ObfuscateUsing annotation) {
        try {
            return obfuscatorProvider(annotation.value());
        } catch (RuntimeException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Returns an instance of a specific {@link ObfuscatorProvider} type.
     *
     * @param providerClass The class representing the {@link ObfuscatorProvider} type.
     * @return An instance of the given {@link ObfuscatorProvider} type.
     * @throws NullPointerException If the given class is {@code null}.
     * @throws RuntimeException If no instance could be returned.
     * @see #instance(Class)
     */
    default ObfuscatorProvider obfuscatorProvider(Class<? extends ObfuscatorProvider> providerClass) {
        return instance(providerClass);
    }

    /**
     * Returns a {@link CharacterRepresentationProvider} based on the used annotations in this package.
     *
     * @param annotatedElement The annotated object for which to check the annotations.
     * @return An {@link Optional} describing the {@link CharacterRepresentationProvider} based on the used annotations,
     *         or {@link Optional#empty()} if the element is not annotated with {@link RepresentedBy}.
     * @throws IllegalStateException If the element is annotated with {@link RepresentedBy}, but its {@link RepresentedBy#value() provider type}
     *                                   cannot be {@link #instance(Class) instantiated}.
     */
    default Optional<CharacterRepresentationProvider> characterRepresentationProvider(AnnotatedElement annotatedElement) {
        return characterRepresentationProvider(annotatedElement::getAnnotation);
    }

    /**
     * Returns a {@link CharacterRepresentationProvider} based on the used annotations in this package.
     * This method provides a generic way of checking annotations, in case a lookup other than reflection is used.
     *
     * @param annotationLookup The function that maps an annotation type to the used annotation of that type,
     *                             or {@code null} if the annotation is not used.
     * @return An {@link Optional} describing the {@link CharacterRepresentationProvider} based on the used annotations,
     *         or {@link Optional#empty()} if the function does not return any non-{@code null} value for {@link RepresentedBy}.
     * @throws IllegalStateException If {@link RepresentedBy} is used but its {@link RepresentedBy#value() provider type} cannot be
     *                                   {@link #instance(Class) instantiated}.
     */
    default Optional<CharacterRepresentationProvider> characterRepresentationProvider(
            Function<Class<? extends Annotation>, Annotation> annotationLookup) {

        RepresentedBy representedBy = (RepresentedBy) annotationLookup.apply(RepresentedBy.class);
        return Optional.ofNullable(representedBy)
                .map(this::characterRepresentationProvider);
    }

    /**
     * Returns a {@link CharacterRepresentationProvider} based on multiple annotations.
     *
     * @param annotations The annotations to base the returned {@link CharacterRepresentationProvider} on.
     * @return An {@link Optional} describing the single found {@link CharacterRepresentationProvider} based on the given annotations,
     *         or {@link Optional#empty()} if none of the annotations is a {@link RepresentedBy} annotation.
     * @throws NullPointerException If any of the annotations is {@code null}.
     * @throws IllegalStateException If the given annotations contain more than one {@link RepresentedBy} annotation,
     *                                   or if the given annotations contain a {@link RepresentedBy} annotation but its
     *                                   {@link RepresentedBy#value() provider type} cannot be {@link #instance(Class) instantiated}.
     */
    default Optional<CharacterRepresentationProvider> characterRepresentationProvider(Annotation... annotations) {
        return characterRepresentationProvider(Arrays.asList(annotations));
    }

    /**
     * Returns a {@link CharacterRepresentationProvider} based on multiple annotations.
     *
     * @param annotations The annotations to base the returned {@link CharacterRepresentationProvider} on.
     * @return An {@link Optional} describing the single found {@link CharacterRepresentationProvider} based on the given annotations,
     *         or {@link Optional#empty()} if none of the annotations is a {@link RepresentedBy} annotation.
     * @throws NullPointerException If any of the annotations is {@code null}.
     * @throws IllegalStateException If the given annotations contain more than one {@link RepresentedBy} annotation,
     *                                   or if the given annotations contain a {@link RepresentedBy} annotation but its
     *                                   {@link RepresentedBy#value() provider type} cannot be {@link #instance(Class) instantiated}.
     */
    default Optional<CharacterRepresentationProvider> characterRepresentationProvider(Collection<? extends Annotation> annotations) {
        List<Annotation> providerAnnotations = new ArrayList<>(1);
        List<CharacterRepresentationProvider> providers = new ArrayList<>(1);
        for (Annotation annotation : annotations) {
            Class<? extends Annotation> annotationType = annotation.annotationType();
            if (annotationType == RepresentedBy.class) {
                providerAnnotations.add(annotation);
                providers.add(characterRepresentationProvider((RepresentedBy) annotation));
            }
        }
        return findSingleCharacterRepresentationProvider(providers, providerAnnotations);
    }

    /**
     * Returns a {@link CharacterRepresentationProvider} based on a {@link RepresentedBy} annotation.
     *
     * @param annotation The annotation to base the returned {@link CharacterRepresentationProvider} on.
     * @return A {@link CharacterRepresentationProvider} based on the given annotation.
     * @throws NullPointerException  If the given annotation is null.
     * @throws IllegalStateException If the annotation's {@link RepresentedBy#value() provider type} cannot be {@link #instance(Class) instantiated}.
     * @see #characterRepresentationProvider(Class)
     */
    default CharacterRepresentationProvider characterRepresentationProvider(RepresentedBy annotation) {
        try {
            return characterRepresentationProvider(annotation.value());
        } catch (RuntimeException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Returns an instance of a specific {@link CharacterRepresentationProvider} type.
     *
     * @param providerClass The class representing the {@link CharacterRepresentationProvider} type.
     * @return An instance of the given {@link CharacterRepresentationProvider} type.
     * @throws NullPointerException If the given class is {@code null}.
     * @throws RuntimeException If no instance could be returned.
     * @see #instance(Class)
     */
    default CharacterRepresentationProvider characterRepresentationProvider(Class<? extends CharacterRepresentationProvider> providerClass) {
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
        return instance(providerClass);
    }

    /**
     * Returns an object factory that uses reflection to create instances.
     * This requires that each type to be instantiated has a public, no-argument constructor. If this is not the case, then attempting to instantiate
     * a type will result in an {@link IllegalStateException}.
     *
     * @return An object factory that uses reflection to create instances.
     */
    static ObjectFactory usingReflection() {
        return ObjectFactoryUtils.USING_REFLECTION;
    }
}
