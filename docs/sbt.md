Notes on sbt
===

How to build sbt launcher locally?
---

Build sbt launcher (commit `fbe82523`) like Travis does it:

```
docker run -it -v $(pwd):/data azul/zulu-openjdk:7u222

apt-get update
apt-get install -y curl
curl -L --fail https://piccolo.link/sbt-0.13.17.tgz > sbt-0.13.17.tgz
tar zxf ./sbt-0.13.17.tgz -C $HOME/
export PATH="$HOME/sbt/bin:$PATH"
export SBT_OPTS="-Xms2048M -Xmx2048M -Xss2M -XX:MaxPermSize=512M"
cd data/launcher
sbt package
```

Locate the original `sbt-launch.jar` and copy `/sbt` and `sbt.boot.properties*` to the newly generated jar. Run the jar using:

```
sbt --sbt-jar ~/dev/launcher/target/sbt-launch-1.1.2-SNAPSHOT.jar
```

Resolving an unknown sbt version
---
If a project is built using an sbt version that is unknown on the current machine, it takes a while to download the new sbt version. (One may trigger this by `mv ~/.ivy2/cache ~/.ivy2/cache_bk`.) Sbt displays the elusive message:

```
[info] [launcher] getting org.scala-sbt sbt 0.13.16  (this may take some time)...
```

At this time, Ivy downloads the right version of sbt _and all necessary dependencies_. This can be shown by commenting out this line in `Update.scala` in the `launcher` package:

```scala
resolveOptions.setLog(LogOptions.LOG_DOWNLOAD_ONLY)
```

To turn on even more logging, change the log level in this line in `Update.scala` (for example, to `MSG_DEBUG`):

```scala
private final class SbtIvyLogger(logWriter: PrintWriter) extends DefaultMessageLogger(Message.MSG_INFO)
```

These logs show that in order to resolve one dependency, multiple HTTP calls need to be made: Ivy tries both the `.pom` and the `.jar` at various repositories. In some cases, it needs three or four tries (times two) before finding the right file.