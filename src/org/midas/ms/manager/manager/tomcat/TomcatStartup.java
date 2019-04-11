package org.midas.ms.manager.manager.tomcat;
import java.net.InetAddress;

import org.apache.catalina.Context;
import org.apache.catalina.Engine;
import org.apache.catalina.Host;
import org.apache.catalina.Session;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.realm.MemoryRealm;
import org.apache.catalina.startup.Embedded;
import org.apache.tomcat.util.IntrospectionUtils;

/**
 * @author placson
 */
public class TomcatStartup {

    private String path = null;
    private Embedded embedded = null;
    private Host host = null;
    private Context rootcontext;
    public TomcatStartup() {
    }
    
    /**
     * Set Session scope variable
     *
     * @param name
     * Session variable name
     * @param obj
     * Session variable value
     */
    public void setRootContextSessionAttribute(String name, Object obj) {
        try {
            Session sessions[] = this.rootcontext.getManager().findSessions();

            for (int i = 0, size = sessions.length; i < size; i++) {
                sessions[i].getSession().setAttribute(name, obj);
            }
        } catch (Exception x) {
        }
    }

    /**
     * Get Application scope variable
     *
     * @param name
     * Application variable name
     * @return Application variable value
     */
    public Object getRootContextAttribute(String name) {
        return this.rootcontext.getServletContext().getAttribute(name);
    }

    /**
     * Set Application scope variable
     *
     * @param name
     * Application variable name
     * @param obj
     * Application variable value
     */
    public void setRootContextAttribute(String name, Object obj) {
        this.rootcontext.getServletContext().setAttribute(name, obj);
    }

    /**
     * Remove Application scope variable
     *
     * @param name
     * Application variable name
     */
    public void removeRootContextAttribute(String name) {
        this.rootcontext.getServletContext().removeAttribute(name);
    }

    /**
     * Basic Accessor setting the value of the context path
     *
     * @param path -
     * the path
     */
    public void setPath(String path) {

        this.path = path;
    }

    /**
     * Basic Accessor returning the value of the context path
     *
     * @return - the context path
     */
    public String getPath() {
        return this.path;
    }

    /**
     * This method Starts the Tomcat server.
     */
    public void startTomcat() throws Exception {
        Engine engine = null;

        // Create an embedded server
        this.embedded = new Embedded();
        this.embedded.setCatalinaHome(getPath());

        // set the memory realm
        MemoryRealm memRealm = new MemoryRealm();
        this.embedded.setRealm(memRealm);

        // Create an engine
        engine = this.embedded.createEngine();
        engine.setDefaultHost("localhost");

        // Create a default virtual host
        this.host = this.embedded.createHost("localhost", getPath()
                + "/webapps");
        engine.addChild(this.host);

        // Create the ROOT context
        this.rootcontext = this.embedded.createContext("", getPath()
                + "/webapps/ROOT");
        this.rootcontext.setReloadable(false);
        this.rootcontext.addWelcomeFile("index.jsp");
        this.host.addChild(this.rootcontext);

        // create another application Context
        Context appCtx = this.embedded.createContext("/masserver", getPath() + "/webapps/masserver");
        appCtx.setPrivileged(true);
        this.host.addChild(appCtx); // add context to host
        
	    // create another application Context
        appCtx = this.embedded.createContext("/manager", getPath() + "/webapps/manager");
        appCtx.setPrivileged(true);
        this.host.addChild(appCtx); // add context to host
        
        // create another application Context
        appCtx = this.embedded.createContext("/axis", getPath() + "/webapps/axis");
        appCtx.setPrivileged(true);
        this.host.addChild(appCtx); // add context to host
		
        // Install the assembled container hierarchy
        this.embedded.addEngine(engine);

        int port = 7100;
        String addr = null;
        Connector connector = null;//this.embedded.createConnector(addr, port, false);
        /*
         * embedded.createConnector(...)
         * seems to be broken.. it always returns a null connector.
         * see work around below
         */
        InetAddress address = null;
        try {
            connector = new Connector();
            //httpConnector.setScheme("http");
            connector.setSecure(false);
            address = InetAddress.getLocalHost();
            if (address != null) {
                IntrospectionUtils.setProperty(connector, "address", ""
                        + address);
            }
            IntrospectionUtils.setProperty(connector, "port", "" + port);
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        connector.setEnableLookups(false);

        this.embedded.addConnector(connector);
		
		System.out.println(path);
		
        // Start the embedded server
        this.embedded.start();
    }

    /**
     * Refresh ROOT context
     *
     */
    public void reloadRoot() {
        this.rootcontext.reload();
    }

    /**
     * Remove ROOT context
     *
     */
    public void removeRoot() {
        this.host.removeChild(this.rootcontext);
    }

    /**
     * Add ROOT context
     *
     */
    public void addRoot() {
        this.host.addChild(this.rootcontext);
    }

    /**
     * This method Stops the Tomcat server.
     */
    public void stopTomcat() throws Exception {
        // Stop the embedded server
        this.embedded.stop();
    }

    /**
     * Registers a WAR with the container.
     *
     * @param contextPath -
     * the context path under which the application will be
     * registered
     * @param warFile -
     * the URL of the WAR to be registered.
     */
    public void registerWAR(String contextPath, String absolutePath)
            throws Exception {
        Context context = this.embedded
                .createContext(contextPath, absolutePath);
        context.setReloadable(false);
        this.host.addChild(context);
    }

    /**
     * Unregisters a WAR from the web server.
     *
     * @param contextPath -
     * the context path to be removed
     */
    public void unregisterWAR(String contextPath) throws Exception {

        Context context = this.host.map(contextPath);
        if (context != null) {
            this.embedded.removeContext(context);
        } else {
            throw new Exception("Context does not exist for named path : "
                    + contextPath);
        }
    }
}