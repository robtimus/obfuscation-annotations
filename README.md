# obfuscation-annotations

Contains general purpose annotations for [obfuscation-core](https://robtimus.github.io/obfuscation-core). Theses annotations can be used by framework-specific processors to apply these obfuscators.

## Annotations

### Annotations for creating Obfuscators

* [ObfuscateAll](https://robtimus.github.io/obfuscation-annotations/apidocs/com/github/robtimus/obfuscation/annotation/ObfuscateAll.html) represents [Obfuscator.all](https://robtimus.github.io/obfuscation-core/apidocs/com/github/robtimus/obfuscation/Obfuscator.html#all-char-).
* [ObfuscateNone](https://robtimus.github.io/obfuscation-annotations/apidocs/com/github/robtimus/obfuscation/annotation/ObfuscateNone.html) represents [Obfuscator.none](https://robtimus.github.io/obfuscation-core/apidocs/com/github/robtimus/obfuscation/Obfuscator.html#none--).
* [ObfuscateFixedLength](https://robtimus.github.io/obfuscation-annotations/apidocs/com/github/robtimus/obfuscation/annotation/ObfuscateFixedLength.html) represents [Obfuscator.fixedLength](https://robtimus.github.io/obfuscation-core/apidocs/com/github/robtimus/obfuscation/Obfuscator.html#fixedLength-int-char-).
* [ObfuscateFixedValue](https://robtimus.github.io/obfuscation-annotations/apidocs/com/github/robtimus/obfuscation/annotation/ObfuscateFixedValue.html) represents [Obfuscator.fixedValue](https://robtimus.github.io/obfuscation-core/apidocs/com/github/robtimus/obfuscation/Obfuscator.html#fixedValue-java.lang.String-).
* [ObfuscatePortion](https://robtimus.github.io/obfuscation-annotations/apidocs/com/github/robtimus/obfuscation/annotation/ObfuscatePortion.html) represents [Obfuscator.portion](https://robtimus.github.io/obfuscation-core/apidocs/com/github/robtimus/obfuscation/Obfuscator.html#portion--).
* [ObfuscateUsing](https://robtimus.github.io/obfuscation-annotations/apidocs/com/github/robtimus/obfuscation/annotation/ObfuscateUsing.html) allows you to provide custom [Obfuscator](https://robtimus.github.io/obfuscation-core/apidocs/com/github/robtimus/obfuscation/Obfuscator.html) implementations. This is done through implementations of [ObfuscatorProvider](https://robtimus.github.io/obfuscation-annotations/apidocs/com/github/robtimus/obfuscation/annotation/ObfuscatorProvider.html).

To help with converting these annotations to `Obfuscator` instances you can use [ObjectFactory](https://robtimus.github.io/obfuscation-annotations/apidocs/com/github/robtimus/obfuscation/annotation/ObjectFactory.html)

### Annotations for obfuscating objects

* [RepresentedBy](https://robtimus.github.io/obfuscation-annotations/apidocs/com/github/robtimus/obfuscation/annotation/RepresentedBy.html) allows you to provide a custom character representation. This can be used with [Obfuscator.obfuscateObject](https://robtimus.github.io/obfuscation-core/apidocs/com/github/robtimus/obfuscation/Obfuscator.html#obfuscateObject-T-java.util.function.Supplier-), [Obfuscator.obfuscateList](https://robtimus.github.io/obfuscation-core/apidocs/com/github/robtimus/obfuscation/Obfuscator.html#obfuscateList-java.util.List-java.util.function.Function-), [Obfuscator.obfuscateSet](https://robtimus.github.io/obfuscation-core/apidocs/com/github/robtimus/obfuscation/Obfuscator.html#obfuscateSet-java.util.Set-java.util.function.Function-), [Obfuscator.obfuscateCollection](https://robtimus.github.io/obfuscation-core/apidocs/com/github/robtimus/obfuscation/Obfuscator.html#obfuscateCollection-java.util.Collection-java.util.function.Function-) and [Obfuscator.obfuscateMap](https://robtimus.github.io/obfuscation-core/apidocs/com/github/robtimus/obfuscation/Obfuscator.html#obfuscateMap-java.util.Map-java.util.function.Function-). This is done through implementations of [CharacterRepresentationProvider](https://robtimus.github.io/obfuscation-annotations/apidocs/com/github/robtimus/obfuscation/annotation/CharacterRepresentationProvider.html).

## Known framework support

* [obfuscation-jackson-databind](https://robtimus.github.io/obfuscation-jackson-databind/) provides integration with [jackson-databind](https://github.com/FasterXML/jackson-databind).
