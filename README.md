# obfuscation-annotations

Contains general purpose annotations for [obfuscation-core](https://robtimus.github.io/obfuscation-core). Theses annotations can be used by framework-specific processors to apply these obfuscators.

## Annotations

### Annotations for creating Obfuscators

* `ObfuscateAll` represents [Obfuscator.all](https://robtimus.github.io/obfuscation-core/apidocs/com/github/robtimus/obfuscation/Obfuscator.html#all-char-).
* `ObfuscateNone` represents [Obfuscator.none](https://robtimus.github.io/obfuscation-core/apidocs/com/github/robtimus/obfuscation/Obfuscator.html#none--).
* `ObfuscateFixedLength` represents [Obfuscator.fixedLength](https://robtimus.github.io/obfuscation-core/apidocs/com/github/robtimus/obfuscation/Obfuscator.html#fixedLength-int-char-).
* `ObfuscateFixedValue` represents [Obfuscator.fixedValue](https://robtimus.github.io/obfuscation-core/apidocs/com/github/robtimus/obfuscation/Obfuscator.html#fixedValue-java.lang.String-).
* `ObfuscatePortion` represents [Obfuscator.portion](https://robtimus.github.io/obfuscation-core/apidocs/com/github/robtimus/obfuscation/Obfuscator.html#portion--).
* `ObfuscateUsing` allows you to provide custom `Obfuscator` implementations. This is done through implementations of [ObfuscatorProvider](https://robtimus.github.io/obfuscation-annotations/apidocs/com/github/robtimus/obfuscation/annotation/ObfuscatorProvider.html).

To help with converting these annotations to `Obfuscator` instances you can use [ObfuscatorFactory](https://robtimus.github.io/obfuscation-annotations/apidocs/com/github/robtimus/obfuscation/annotation/ObfuscatorFactory.html)

### Annotations for obfuscating objects

* `RepresentedBy` allows you to provide a custom string representation. This can be used with [Obfuscator.obfuscateObject](https://robtimus.github.io/obfuscation-core/apidocs/com/github/robtimus/obfuscation/Obfuscator.html#obfuscateObject-T-java.util.function.Supplier-). This is done through implementations of [StringRepresentationProvider](https://robtimus.github.io/obfuscation-annotations/apidocs/com/github/robtimus/obfuscation/annotation/StringRepresentationProvider.html).

## Known framework support

* [obfuscation-jackson-databind](https://robtimus.github.io/obfuscation-jackson-databind/) provides integration with [jackson-databind](https://github.com/FasterXML/jackson-databind).
