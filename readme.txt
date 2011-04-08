* Enable Tomcat Follow Symlinks
  <Context allowLinking="true">

* Enable Velocity Check Modifed
<?xml version="1.0" encoding="UTF-8"?>
<application
   xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
   xmlns="http://www.zeroturnaround.com"
   xsi:schemaLocation="http://www.zeroturnaround.com http://www.zeroturnaround.com/alderaan/rebel-2_0.xsd">
  <classpath>
    <dir name="/home/jwiklund/.usr/src/polopoly/eclipse-build"/>
    <dir name="/home/jwiklund/.usr/src/polopoly/localtest/config"/>
  </classpath>
</application>
  velocity.properties => localtest/config/velocity.properties

  polopoly.resource.loader.modificationCheckInterval = 1
  webapp.resource.loader.modificationCheckInterval = 1

  rm -rf $T_HOME/webapps/ROOT/WEB-INF/velocity/
  ln -s $P_HOME/sites/greenfieldtimes-example/src/web/dispatcher/WEB-INF/velocity $T_HOME/webapps/ROOT/WEB-INF

