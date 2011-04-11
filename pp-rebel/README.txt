Build:
	Use package assembly:single to get a a single jar
Install:
  * Copy pp-rebel.xml to $HOME_OF_POLOPOLY_INSTALLATION
  * Add to CATALINA_OPTS
	-DPP_HOME=$HOME_OF_POLOPOLY_INSTALLATION
	-Drebel.plugins=$PATH_TO_REBEL_JAR
