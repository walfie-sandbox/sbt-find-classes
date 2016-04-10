# sbt-find-classes

sbt plugin to search `fullClasspath` for classes with matching names.

## Quick start

For sbt versions >=0.13.11, Add to `~/.sbt/0.13/plugins/build.sbt`:

```scala
resolvers += Resolver.bintrayIvyRepo("walfie", "sbt-plugins")

addSbtPlugin("com.github.walfie" % "sbt-find-classes" % "0.0.1")
```

For sbt versions before 0.13.11, `Resolver.bintrayIvyRepo` doesn't exist,
so use the following instead:

```scala
resolvers += Resolver.url(
  "bintray/walfie",
  url("https://dl.bintray.com/walfie/sbt-plugins")
)(Resolver.ivyStylePatterns)
```

Then in an sbt prompt, for example,

```
> findClasses scala con future
scala.concurrent.Future
scala.concurrent.FutureTaskRunner
scala.concurrent.impl.Future
```

* Spaces in search terms are replaced with regex wildcard `.*`
* If all search terms are lowercase, performs a case-insensitive search `(?i)`

So the above will search for classes matching the regex `(?i).*scala.*con.*future.*`.

