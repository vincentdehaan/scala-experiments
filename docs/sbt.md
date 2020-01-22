Notes on sbt
===

How to build sbt launcher locally?
---

Build sbt launcher (commit `fbe82523`) like Travis does it:

```
sudo docker run -it -v $(pwd):/launcher azul/zulu-openjdk:7u222

apt-get update # necessary?
apt-get install curl
curl -L --fail https://piccolo.link/sbt-0.13.17.tgz > sbt-0.13.17.tgz
tar zxf ./sbt-0.13.17.tgz -C $HOME/
export PATH="$HOME/sbt/bin:$PATH"
export SBT_OPTS="-Xms2048M -Xmx2048M -Xss2M -XX:MaxPermSize=512M"
cd launcher
sbt package
```
