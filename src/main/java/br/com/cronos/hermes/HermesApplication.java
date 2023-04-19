package br.com.cronos.hermes;

import org.apache.camel.builder.RouteBuilder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;

@SpringBootApplication
public class HermesApplication {
    private static final Logger log = LoggerFactory.getLogger(HermesApplication.class);

    public static void main(String[] args) {
        var app = new SpringApplication(HermesApplication.class);
        Environment env = app.run(args).getEnvironment();
        logApplicationStartup(env);
    }

    private static void logApplicationStartup(Environment env) {
        String protocol = "http";
        if (env.getProperty("server.ssl.key-store") != null) {
            protocol = "https";
        }
        String serverPort = env.getProperty("server.port");
        String contextPath = env.getProperty("server.servlet.context-path");
        if (StringUtils.isBlank(contextPath)) {
            contextPath = "/";
        }
        String hostAddress = "localhost";
        try {
            hostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.warn("The host name could not be determined, using `localhost` as fallback");
        }
        log.info("\n----------------------------------------------------------\n\t" +
                        "Application '{}' is running! Access URLs:\n\t" +
                        "Local: \t\t{}://localhost:{}{}\n\t" +
                        "External: \t{}://{}:{}{}\n\t" +
                        "Profile(s): \t{}\n----------------------------------------------------------",
                env.getProperty("spring.application.name"),
                protocol,
                serverPort,
                contextPath,
                protocol,
                hostAddress,
                serverPort,
                contextPath,
                env.getActiveProfiles());
    }

    @Component
    class DebeziumRoute extends RouteBuilder {
        private String offsetStorageFilename = "C:\\projetos\\portifolio\\data\\apache-camel\\offset-file.dat";
        private String host = "localhost";
        private String username = "postgres";
        private String password = "postgres";
        private String db = "postgres";


        @Override
        public void configure() throws Exception {
            from(
                "debezium-postgres:dbz-postgres?offsetStorageFilename=" + offsetStorageFilename
                    + "&databaseHostName=" + host
                    + "&databaseUser=" + username
                    + "&databasePassword=" + password
                    + "&databaseServerName=" + db
                    + "&databaseDbname=" + db
                    + "&pluginName=pgoutput")
            .log("EVENTO: ${body}")
            .log(" identificador: ${headers.CamelDebeziumIdentifier}")
            .log(" source metadata: ${headers.CamelDebeziumSourceMetadata}")
            .log(" operacao: ${headers.CamelDebeziumOperation}")
            .log(" base: ${headers.CamelDebeziumSourceMetadata[db]}")
            .log(" tabela: ${headers.CamelDebeziumSourceMetadata[table]}")
            .log(" chave primaria: ${headers.CamelDebeziumKey}");
        }
    }
}
