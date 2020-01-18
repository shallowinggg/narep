package com.shallowinggg.narep.core.generators.netty;

import com.shallowinggg.narep.core.common.CodeGeneratorHelper;
import com.shallowinggg.narep.core.common.FieldMetaData;
import com.shallowinggg.narep.core.generators.ClassCodeGenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.shallowinggg.narep.core.common.FieldMetaData.Modifier.PRIVATE_STATIC;
import static com.shallowinggg.narep.core.common.FieldMetaData.Modifier.PRIVATE_STATIC_FINAL;

/**
 * @author shallowinggg
 */
public class TlsHelperCodeGenerator extends ClassCodeGenerator {
    private static final String CLASS_NAME = "TlsHelper";
    private static final String SUB_PACKAGE = "netty";
    private List<FieldMetaData> fields = new ArrayList<>(1);
    private static final List<String> DEPENDENCIES = Arrays.asList("RemotingHelper.java", "TlsSystemConfig.java");

    public TlsHelperCodeGenerator() {
        super(CLASS_NAME, null, SUB_PACKAGE);
        setDependenciesName(DEPENDENCIES);

        fields.add(new FieldMetaData(PRIVATE_STATIC_FINAL, "Logger", "log", CodeGeneratorHelper.buildLoggerField(CLASS_NAME)));
        fields.add(new FieldMetaData(PRIVATE_STATIC, "DecryptionStrategy", "decryptionStrategy", "new DecryptionStrategy() {\n" +
                "        @Override\n" +
                "        public InputStream decryptPrivateKey(final String privateKeyEncryptPath,\n" +
                "                                             final boolean forClient) throws IOException {\n" +
                "            return new FileInputStream(privateKeyEncryptPath);\n" +
                "        }\n" +
                "    }"));
    }

    @Override
    public String buildImports() {
        StringBuilder builder = new StringBuilder(600);
        CodeGeneratorHelper.buildDependencyImports(builder, getDependencies().subList(0, 1));
        builder.append("import io.netty.handler.ssl.*;\n" +
                "import io.netty.handler.ssl.util.InsecureTrustManagerFactory;\n" +
                "import io.netty.handler.ssl.util.SelfSignedCertificate;\n" +
                "import org.apache.logging.log4j.LogManager;\n" +
                "import org.apache.logging.log4j.Logger;\n" +
                "\n" +
                "import java.io.File;\n" +
                "import java.io.FileInputStream;\n" +
                "import java.io.IOException;\n" +
                "import java.io.InputStream;\n" +
                "import java.security.cert.CertificateException;\n" +
                "import java.util.Properties;\n\n");
        CodeGeneratorHelper.buildStaticImports(builder, getDependencies().subList(1, 2));

        return builder.toString();
    }

    @Override
    public String buildFields() {
        return CodeGeneratorHelper.buildFieldsByMetaData(fields);
    }

    @Override
    public String buildMethods() {
        return innerClass() +
                registerDecryptionStrategy() +
                buildSslContext() +
                extractTlsConfigFromFile() +
                logTheFinalUsedTlsConfig() +
                parseClientAuthMode() +
                isNullOrEmpty();
    }

    // methods

    private String innerClass() {
        return "    public interface DecryptionStrategy {\n" +
                "        /**\n" +
                "         * Decrypt the target encrpted private key file.\n" +
                "         *\n" +
                "         * @param privateKeyEncryptPath A pathname string\n" +
                "         * @param forClient             tells whether it's a client-side key file\n" +
                "         * @return An input stream for a decrypted key file\n" +
                "         * @throws IOException if an I/O error has occurred\n" +
                "         */\n" +
                "        InputStream decryptPrivateKey(String privateKeyEncryptPath, boolean forClient) throws IOException;\n" +
                "    }\n\n";
    }

    private String registerDecryptionStrategy() {
        return "    public static void registerDecryptionStrategy(final DecryptionStrategy decryptionStrategy) {\n" +
                "        TlsHelper.decryptionStrategy = decryptionStrategy;\n" +
                "    }\n\n";
    }

    private String buildSslContext() {
        return "    public static SslContext buildSslContext(boolean forClient) throws IOException, CertificateException {\n" +
                "        File configFile = new File(TlsSystemConfig.tlsConfigFile);\n" +
                "        extractTlsConfigFromFile(configFile);\n" +
                "        logTheFinalUsedTlsConfig();\n" +
                "\n" +
                "        SslProvider provider;\n" +
                "        if (OpenSsl.isAvailable()) {\n" +
                "            provider = SslProvider.OPENSSL;\n" +
                "            LOGGER.info(\"Using OpenSSL provider\");\n" +
                "        } else {\n" +
                "            provider = SslProvider.JDK;\n" +
                "            LOGGER.info(\"Using JDK SSL provider\");\n" +
                "        }\n" +
                "\n" +
                "        if (forClient) {\n" +
                "            if (tlsTestModeEnable) {\n" +
                "                return SslContextBuilder\n" +
                "                        .forClient()\n" +
                "                        .sslProvider(SslProvider.JDK)\n" +
                "                        .trustManager(InsecureTrustManagerFactory.INSTANCE)\n" +
                "                        .build();\n" +
                "            } else {\n" +
                "                SslContextBuilder sslContextBuilder = SslContextBuilder.forClient().sslProvider(SslProvider.JDK);\n" +
                "\n" +
                "\n" +
                "                if (!tlsClientAuthServer) {\n" +
                "                    sslContextBuilder.trustManager(InsecureTrustManagerFactory.INSTANCE);\n" +
                "                } else {\n" +
                "                    if (!isNullOrEmpty(tlsClientTrustCertPath)) {\n" +
                "                        sslContextBuilder.trustManager(new File(tlsClientTrustCertPath));\n" +
                "                    }\n" +
                "                }\n" +
                "\n" +
                "                return sslContextBuilder.keyManager(\n" +
                "                        !isNullOrEmpty(tlsClientCertPath) ? new FileInputStream(tlsClientCertPath) : null,\n" +
                "                        !isNullOrEmpty(tlsClientKeyPath) ? decryptionStrategy.decryptPrivateKey(tlsClientKeyPath, true) : null,\n" +
                "                        !isNullOrEmpty(tlsClientKeyPassword) ? tlsClientKeyPassword : null)\n" +
                "                        .build();\n" +
                "            }\n" +
                "        } else {\n" +
                "            if (tlsTestModeEnable) {\n" +
                "                SelfSignedCertificate selfSignedCertificate = new SelfSignedCertificate();\n" +
                "                return SslContextBuilder\n" +
                "                        .forServer(selfSignedCertificate.certificate(), selfSignedCertificate.privateKey())\n" +
                "                        .sslProvider(SslProvider.JDK)\n" +
                "                        .clientAuth(ClientAuth.OPTIONAL)\n" +
                "                        .build();\n" +
                "            } else {\n" +
                "                SslContextBuilder sslContextBuilder = SslContextBuilder.forServer(\n" +
                "                        !isNullOrEmpty(tlsServerCertPath) ? new FileInputStream(tlsServerCertPath) : null,\n" +
                "                        !isNullOrEmpty(tlsServerKeyPath) ? decryptionStrategy.decryptPrivateKey(tlsServerKeyPath, false) : null,\n" +
                "                        !isNullOrEmpty(tlsServerKeyPassword) ? tlsServerKeyPassword : null)\n" +
                "                        .sslProvider(provider);\n" +
                "\n" +
                "                if (!tlsServerAuthClient) {\n" +
                "                    sslContextBuilder.trustManager(InsecureTrustManagerFactory.INSTANCE);\n" +
                "                } else {\n" +
                "                    if (!isNullOrEmpty(tlsServerTrustCertPath)) {\n" +
                "                        sslContextBuilder.trustManager(new File(tlsServerTrustCertPath));\n" +
                "                    }\n" +
                "                }\n" +
                "\n" +
                "                sslContextBuilder.clientAuth(parseClientAuthMode(tlsServerNeedClientAuth));\n" +
                "                return sslContextBuilder.build();\n" +
                "            }\n" +
                "        }\n" +
                "    }\n\n";
    }

    private String extractTlsConfigFromFile() {
        return "    private static void extractTlsConfigFromFile(final File configFile) {\n" +
                "        if (!(configFile.exists() && configFile.isFile() && configFile.canRead())) {\n" +
                "            LOGGER.info(\"Tls config file doesn't exist, skip it\");\n" +
                "            return;\n" +
                "        }\n" +
                "\n" +
                "        Properties properties;\n" +
                "        properties = new Properties();\n" +
                "        InputStream inputStream = null;\n" +
                "        try {\n" +
                "            inputStream = new FileInputStream(configFile);\n" +
                "            properties.load(inputStream);\n" +
                "        } catch (IOException ignore) {\n" +
                "        } finally {\n" +
                "            if (null != inputStream) {\n" +
                "                try {\n" +
                "                    inputStream.close();\n" +
                "                } catch (IOException ignore) {\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "\n" +
                "        tlsTestModeEnable = Boolean.parseBoolean(properties.getProperty(TLS_TEST_MODE_ENABLE, String.valueOf(tlsTestModeEnable)));\n" +
                "        tlsServerNeedClientAuth = properties.getProperty(TLS_SERVER_NEED_CLIENT_AUTH, tlsServerNeedClientAuth);\n" +
                "        tlsServerKeyPath = properties.getProperty(TLS_SERVER_KEYPATH, tlsServerKeyPath);\n" +
                "        tlsServerKeyPassword = properties.getProperty(TLS_SERVER_KEYPASSWORD, tlsServerKeyPassword);\n" +
                "        tlsServerCertPath = properties.getProperty(TLS_SERVER_CERTPATH, tlsServerCertPath);\n" +
                "        tlsServerAuthClient = Boolean.parseBoolean(properties.getProperty(TLS_SERVER_AUTHCLIENT, String.valueOf(tlsServerAuthClient)));\n" +
                "        tlsServerTrustCertPath = properties.getProperty(TLS_SERVER_TRUSTCERTPATH, tlsServerTrustCertPath);\n" +
                "\n" +
                "        tlsClientKeyPath = properties.getProperty(TLS_CLIENT_KEYPATH, tlsClientKeyPath);\n" +
                "        tlsClientKeyPassword = properties.getProperty(TLS_CLIENT_KEYPASSWORD, tlsClientKeyPassword);\n" +
                "        tlsClientCertPath = properties.getProperty(TLS_CLIENT_CERTPATH, tlsClientCertPath);\n" +
                "        tlsClientAuthServer = Boolean.parseBoolean(properties.getProperty(TLS_CLIENT_AUTHSERVER, String.valueOf(tlsClientAuthServer)));\n" +
                "        tlsClientTrustCertPath = properties.getProperty(TLS_CLIENT_TRUSTCERTPATH, tlsClientTrustCertPath);\n" +
                "    }\n\n";
    }

    private String logTheFinalUsedTlsConfig() {
        return "    private static void logTheFinalUsedTlsConfig() {\n" +
                "        LOGGER.info(\"Log the final used tls related configuration\");\n" +
                "        LOGGER.info(\"{} = {}\", TLS_TEST_MODE_ENABLE, tlsTestModeEnable);\n" +
                "        LOGGER.info(\"{} = {}\", TLS_SERVER_NEED_CLIENT_AUTH, tlsServerNeedClientAuth);\n" +
                "        LOGGER.info(\"{} = {}\", TLS_SERVER_KEYPATH, tlsServerKeyPath);\n" +
                "        LOGGER.info(\"{} = {}\", TLS_SERVER_KEYPASSWORD, tlsServerKeyPassword);\n" +
                "        LOGGER.info(\"{} = {}\", TLS_SERVER_CERTPATH, tlsServerCertPath);\n" +
                "        LOGGER.info(\"{} = {}\", TLS_SERVER_AUTHCLIENT, tlsServerAuthClient);\n" +
                "        LOGGER.info(\"{} = {}\", TLS_SERVER_TRUSTCERTPATH, tlsServerTrustCertPath);\n" +
                "\n" +
                "        LOGGER.info(\"{} = {}\", TLS_CLIENT_KEYPATH, tlsClientKeyPath);\n" +
                "        LOGGER.info(\"{} = {}\", TLS_CLIENT_KEYPASSWORD, tlsClientKeyPassword);\n" +
                "        LOGGER.info(\"{} = {}\", TLS_CLIENT_CERTPATH, tlsClientCertPath);\n" +
                "        LOGGER.info(\"{} = {}\", TLS_CLIENT_AUTHSERVER, tlsClientAuthServer);\n" +
                "        LOGGER.info(\"{} = {}\", TLS_CLIENT_TRUSTCERTPATH, tlsClientTrustCertPath);\n" +
                "    }\n\n";
    }

    private String parseClientAuthMode() {
        return "    private static ClientAuth parseClientAuthMode(String authMode) {\n" +
                "        if (null == authMode || authMode.trim().isEmpty()) {\n" +
                "            return ClientAuth.NONE;\n" +
                "        }\n" +
                "\n" +
                "        for (ClientAuth clientAuth : ClientAuth.values()) {\n" +
                "            if (clientAuth.name().equals(authMode.toUpperCase())) {\n" +
                "                return clientAuth;\n" +
                "            }\n" +
                "        }\n" +
                "\n" +
                "        return ClientAuth.NONE;\n" +
                "    }\n\n";
    }

    private String isNullOrEmpty() {
        return "    private static boolean isNullOrEmpty(String s) {\n" +
                "        return s == null || s.isEmpty();\n" +
                "    }\n\n";
    }
}
