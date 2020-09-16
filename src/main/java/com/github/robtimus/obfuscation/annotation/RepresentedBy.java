/*
 * RepresentedBy.java
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

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import com.github.robtimus.obfuscation.Obfuscator;

/**
 * Indicates that objects should get a custom character representation when being obfuscated using
 * {@link Obfuscator#obfuscateObject(Object, Supplier)}, {@link Obfuscator#obfuscateCollection(Collection, Function)},
 * {@link Obfuscator#obfuscateList(List, Function)}, {@link Obfuscator#obfuscateSet(Set, Function)} or {@link Obfuscator#obfuscateMap(Map, Function)}.
 * {@link Obfuscator#obfuscateObject(Object, Supplier)}.
 *
 * @author Rob Spoor
 */
@Documented
@Target({ ElementType.ANNOTATION_TYPE, ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface RepresentedBy {

    /**
     * The character representation provider type.
     */
    Class<? extends CharacterRepresentationProvider> value();
}
