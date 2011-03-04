function servlet(src) {
    return "  <servlet>\n" + 
        "    <servlet-name>polopoly-development</servlet-name>\n" + 
        "    <servlet-class>com.polopoly.cm.servlet.PolopolyDevelopmentServlet</servlet-class>\n" + 
        "    <init-param>\n" + 
        "      <param-name>cssbase</param-name>\n" + 
        "      <param-value>" + src + "/assemblies/admin-gui-war/src/main/lesscss</param-value>\n" + 
        "    </init-param>\n" + 
        "    <init-param>\n" + 
        "      <param-name>jsbase</param-name>\n" + 
        "      <param-value>" + src + "/assemblies/admin-gui-war/src/main/javascript</param-value>\n" + 
        "    </init-param>\n" + 
        "    <init-param>\n" + 
        "      <param-name>compress</param-name>\n" + 
        "      <param-value>false</param-value>\n" + 
        "    </init-param>\n" + 
        "    <init-param>\n" + 
        "      <param-name>cache</param-name>\n" + 
        "      <param-value>false</param-value>\n" + 
        "    </init-param>\n" + 
        "  </servlet>"
}
function mapping() {
    return "  <servlet-mapping>\n" + 
        "    <servlet-name>polopoly-development</servlet-name>\n" + 
        "    <url-pattern>/stylesheets/polopoly.css</url-pattern>\n" + 
        "  </servlet-mapping>\n" + 
        "  <servlet-mapping>\n" + 
        "    <servlet-name>polopoly-development</servlet-name>\n" + 
        "    <url-pattern>/script/polopoly-core.js</url-pattern>\n" + 
        "  </servlet-mapping>\n" + 
        "  <servlet-mapping>\n" + 
        "    <servlet-name>polopoly-development</servlet-name>\n" + 
        "    <url-pattern>/script/polopoly-ui.js</url-pattern>\n" + 
        "  </servlet-mapping>\n" + 
        "  <servlet-mapping>\n" + 
        "    <servlet-name>polopoly-development</servlet-name>\n" + 
        "    <url-pattern>/script/polopoly-search.js</url-pattern>\n" + 
        "  </servlet-mapping>\n" + 
        "  <servlet-mapping>\n" + 
        "    <servlet-name>polopoly-development</servlet-name>\n" + 
        "    <url-pattern>/script/polopoly-searchframe.js</url-pattern>\n" + 
        "  </servlet-mapping>"
}
process.argv.forEach(function (val, index) {
    if (val == 'mapping') {
	console.log(mapping())
    } else if (val == 'servlet') {
	console.log(servlet(process.argv[index+1]))
    }
})