# Instancio subtype mapping using jandex

Instancio cannot create a concrete object of an interface if no subtype class is
specified. This can be solved by specifying the subtype e.g.
```java
IFoo foo = Instancio.of(IFoo.class).subtype(all(IFoo.class), Foo.class).create();
```
However, this is a time-consuming manual process.
An alternative is the specification of subtyping via properties file:
>Properties prefixed with subtype are used to specify default implementations 
>for abstract types, or map types to subtypes in general. 
>This is the same mechanism as subtype mapping, but configured via properties.

from https://www.instancio.org/user-guide/#overriding-settings-using-a-properties-file

This project shows how [jandex](https://github.com/smallrye/jandex) can be used to automatically generate the mapping inside the instancio.properties from the given class structure.

Generate jandex index and run propertie file generation:
```bash
mvn package -P generate-instancio -DskipTests=true
```

The profile `generate-instancio` first creates the jandex idx and then generates the instancio properties using maven exec plugin and running `ExecJandexToInstancioProperties`.