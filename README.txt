Polopoly Java Rebel plugin to allow hotswap of content files.

Build:
  git submodule init
  git submodule update
  mvn clean install
  mvn assembly:single -f pp-rebel/pom.xml

Install:
  Copy pp-rebel.xml to $HOME_OF_POLOPOLY_INSTALLATION
  Add to CATALINA_OPTS
    -DPP_HOME=$HOME_OF_POLOPOLY_INSTALLATION
    -Drebel.plugins=$THIS_PATH/pp-rebel/target/pp-rebel-1.1-SNAPSHOT-jar-with-dependencies.jar
    -Drebel.pp-rebel=true

Usage:
  put the pp-rebel.xml file (found in pp-rebel/src/main/resources) in $HOME_OF_POLOPOLY_INSTALLATION 
  and edit it to suit your needs!