# Instancio subtype mapping using jandex

Instancio cannot create a concrete object of an interface if no subtype class is
specified. This can be solved by specifying the subtype e.g.
```java
IFoo foo = Instancio.of(IFoo.class).subtype(all(IFoo.class), Foo.class).create();
```
However, this is a time-consuming manual process.

### Using properties file
An alternative is the specification of subtyping via properties file:
>Properties prefixed with subtype are used to specify default implementations 
>for abstract types, or map types to subtypes in general. 
>This is the same mechanism as subtype mapping, but configured via properties.
from https://www.instancio.org/user-guide/#overriding-settings-using-a-properties-file

### Using custom type resolver
The most efficient way is to provide your own provider for your own implementation of TypeResolver.\
An example project of Instancio shows this can be achieved using ClassGraph on Runtime.\
In this project it is shown how a TypeResolver that uses a previously compiled Jandex index may be implemented.

1. Generate jandex index:
```bash
mvn package -P generate-jandex-idx -DskipTests=true
```
2. Link the custom implemented provider (e.g. JandexProvider) via file:  
`/src/test/resources/META-INF/services/org.instancio.spi.InstancioServiceProvider`.