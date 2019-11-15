package main;

import com.darthShana.encrypt.AzureEnvelopKeyManager;
import com.darthShana.rest.CustomerResource;
import io.ebean.DatabaseFactory;
import io.ebean.config.DatabaseConfig;
import io.ebean.datasource.DataSourceConfig;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

public class Application {

    private int serverPort;

    public Application(int port) throws Exception {
        this.serverPort = port;
        Server server = configureServer();
        server.start();
        server.join();
    }

    private Server configureServer() throws IOException {

        initialiseSystemProperties();
        initialiseEbean();
        AzureEnvelopKeyManager keyManager = new AzureEnvelopKeyManager();
        keyManager.initialise();
        keyManager.verifyDataEncryptionKeys();

        ResourceConfig resourceConfig = new ResourceConfig();
        resourceConfig.packages(CustomerResource.class.getPackage().getName());
        resourceConfig.register(JacksonFeature.class);
        ServletContainer servletContainer = new ServletContainer(resourceConfig);
        ServletHolder sh = new ServletHolder(servletContainer);
        Server server = new Server(serverPort);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        context.addServlet(sh, "/*");
        server.setHandler(context);
        return server;
    }

    private void initialiseSystemProperties() throws IOException {
        Properties p = new Properties();
        p.load(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("app.properties")));
        for (String name : p.stringPropertyNames()) {
            String value = p.getProperty(name);
            System.setProperty(name, value);
        }
    }

    private void initialiseEbean() {
        DatabaseConfig config = new DatabaseConfig();

        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setUrl("jdbc:postgresql://localhost:5432/book-store");
        dataSourceConfig.setDriver("org.postgresql.Driver");
        dataSourceConfig.setUsername("postgres");
        dataSourceConfig.setPassword("password");

        config.setDataSourceConfig(dataSourceConfig);
        config.addPackage("com.darthShana.model");
        config.setRunMigration(true);
        config.setEncryptKeyManager(new AzureEnvelopKeyManager());

        DatabaseFactory.create(config);
    }

    public static void main(String[] args) throws Exception {
        new Application(8080);
    }
}
