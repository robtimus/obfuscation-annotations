/*
 * ObfuscatePortion.java
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
import com.github.robtimus.obfuscation.Obfuscator;

/**
 * Indicates that obfuscation should be done in a similar way to using {@link Obfuscator#portion()}.
 *
 * @author Rob Spoor
 */
@Documented
@Target({ ElementType.ANNOTATION_TYPE, ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ObfuscatePortion {

    /**
     * The number of characters to skip at the start.
     */
    int keepAtStart() default 0;

    /**
     * The number of characters to skip at the end.
     */
    int keepAtEnd() default 0;

    /**
     * The minimum number of characters from the start that need to be obfuscated.
     * This will overrule any value set with {@link #keepAtStart()} or {@link #keepAtEnd()}.
     */
    int atLeastFromStart() default 0;

    /**
     * The minimum number of characters from the end that need to be obfuscated.
     * This will overrule any value set with {@link #keepAtStart()} or {@link #keepAtEnd()}.
     */
    int atLeastFromEnd() default 0;

    /**
     * The fixed total length to use for obfuscated contents, or a negative value to use the actual length.
     * When obfuscating, the result will have {@link #maskChar() mask characters} added until this total length has been reached.
     * <p>
     * Note: when used in combination with {@link #keepAtStart()} and/or {@link #keepAtEnd()}, this total length must be at least the sum of both
     * other values. When used in combination with both, parts of the input may be repeated in the obfuscated content if the input's length is less
     * than the combined number of characters to keep.
     *
     * @since 1.1
     */
    int fixedTotalLength() default -1;

    /**
     * The fixed number of {@link #maskChar() mask characters}, or a negative value to use the actual length.
     * <p>
     * This setting will be ignored if the {@link #fixedTotalLength() fixed total length} is set.
     *
     * @deprecated The total length of obfuscated contents can very when using this setting, making it possible in certain cases to find the original
     *             value that was obfuscated. Use {@link #fixedTotalLength()} instead.
     */
    @Deprecated
    int fixedLength() default -1;

    /**
     * The character to replace with.
     */
    char maskChar() default '*';
}
