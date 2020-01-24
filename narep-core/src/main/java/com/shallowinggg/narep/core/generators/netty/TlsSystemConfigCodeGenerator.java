package com.shallowinggg.narep.core.generators.netty;

import com.shallowinggg.narep.core.annotation.Generator;
import com.shallowinggg.narep.core.generators.ClassCodeGenerator;
import com.shallowinggg.narep.core.lang.FieldInfo;

import java.util.ArrayList;
import java.util.List;

import static com.shallowinggg.narep.core.lang.Modifier.PUBLIC_STATIC;
import static com.shallowinggg.narep.core.lang.Modifier.PUBLIC_STATIC_FINAL;


/**
 * @author shallowinggg
 */
@Generator
public class TlsSystemConfigCodeGenerator extends ClassCodeGenerator {
    private static final String CLASS_NAME = "TlsSystemConfig";
    private static final String SUB_PACKAGE = "netty";

    public TlsSystemConfigCodeGenerator() {
        super(CLASS_NAME, null, SUB_PACKAGE);

        List<FieldInfo> fields = new ArrayList<>(30);
        fields.add(new FieldInfo(PUBLIC_STATIC_FINAL, "String", "TLS_SERVER_MODE", "\"tls.server.mode\""));
        fields.add(new FieldInfo(PUBLIC_STATIC_FINAL, "String", "TLS_ENABLE", "\"tls.enable\""));
        fields.add(new FieldInfo(PUBLIC_STATIC_FINAL, "String", "TLS_CONFIG_FILE", "\"tls.config.file\""));
        fields.add(new FieldInfo(PUBLIC_STATIC_FINAL, "String", "TLS_TEST_MODE_ENABLE", "\"tls.test.mode.enable\""));

        fields.add(new FieldInfo(PUBLIC_STATIC_FINAL, "String", "TLS_SERVER_NEED_CLIENT_AUTH", "\"tls.server.need.client.auth\""));
        fields.add(new FieldInfo(PUBLIC_STATIC_FINAL, "String", "TLS_SERVER_KEYPATH", "\"tls.server.keyPath\""));
        fields.add(new FieldInfo(PUBLIC_STATIC_FINAL, "String", "TLS_SERVER_KEYPASSWORD", "\"tls.server.keyPassword\""));
        fields.add(new FieldInfo(PUBLIC_STATIC_FINAL, "String", "TLS_SERVER_CERTPATH", "\"tls.server.certPath\""));
        fields.add(new FieldInfo(PUBLIC_STATIC_FINAL, "String", "TLS_SERVER_AUTHCLIENT", "\"tls.server.authClient\""));
        fields.add(new FieldInfo(PUBLIC_STATIC_FINAL, "String", "TLS_SERVER_TRUSTCERTPATH", "\"tls.server.trustCertPath\""));

        fields.add(new FieldInfo(PUBLIC_STATIC_FINAL, "String", "TLS_CLIENT_KEYPATH", "\"tls.client.keyPath\""));
        fields.add(new FieldInfo(PUBLIC_STATIC_FINAL, "String", "TLS_CLIENT_KEYPASSWORD", "\"tls.client.keyPassword\""));
        fields.add(new FieldInfo(PUBLIC_STATIC_FINAL, "String", "TLS_CLIENT_CERTPATH", "\"tls.client.certPath\""));
        fields.add(new FieldInfo(PUBLIC_STATIC_FINAL, "String", "TLS_CLIENT_AUTHSERVER", "\"tls.client.authServer\""));
        fields.add(new FieldInfo(PUBLIC_STATIC_FINAL, "String", "TLS_CLIENT_TRUSTCERTPATH", "\"tls.client.trustCertPath\""));

        fields.add(new FieldInfo.Builder().modifier(PUBLIC_STATIC)
                .type("boolean").name("tlsEnable").initValue("Boolean.parseBoolean(System.getProperty(TLS_ENABLE, \"false\"))")
                .comment("    /**\n" +
                        "     * To determine whether use SSL in client-side, include SDK client and BrokerOuterAPI\n" +
                        "     */").build());
        fields.add(new FieldInfo.Builder().modifier(PUBLIC_STATIC)
                .type("boolean").name("tlsTestModeEnable").initValue("Boolean.parseBoolean(System.getProperty(TLS_TEST_MODE_ENABLE, \"false\"))")
                .comment("    /**\n" +
                        "     * To determine whether use test mode when initialize TLS context\n" +
                        "     */").build());
        fields.add(new FieldInfo.Builder().modifier(PUBLIC_STATIC)
                .type("String").name("tlsServerNeedClientAuth").initValue("System.getProperty(TLS_SERVER_NEED_CLIENT_AUTH, \"none\")")
                .comment("    /**\n" +
                        "     * Indicates the state of the {@link javax.net.ssl.SSLEngine} with respect to client authentication.\n" +
                        "     * This configuration item really only applies when building the server-side {@link SslContext},\n" +
                        "     * and can be set to none, require or optional.\n" +
                        "     */").build());
        fields.add(new FieldInfo.Builder().modifier(PUBLIC_STATIC)
                .type("String").name("tlsServerKeyPath").initValue("System.getProperty(TLS_SERVER_KEYPATH, null)")
                .comment("    /**\n" +
                        "     * The store path of server-side private key\n" +
                        "     */").build());
        fields.add(new FieldInfo.Builder().modifier(PUBLIC_STATIC)
                .type("String").name("tlsServerKeyPassword").initValue("System.getProperty(TLS_SERVER_KEYPASSWORD, null)")
                .comment("    /**\n" +
                        "     * The password of the server-side private key\n" +
                        "     */").build());
        fields.add(new FieldInfo.Builder().modifier(PUBLIC_STATIC)
                .type("String").name("tlsServerCertPath").initValue("System.getProperty(TLS_SERVER_CERTPATH, null)")
                .comment("    /**\n" +
                        "     * The store path of server-side X.509 certificate chain in PEM format\n" +
                        "     */").build());
        fields.add(new FieldInfo.Builder().modifier(PUBLIC_STATIC)
                .type("boolean").name("tlsServerAuthClient").initValue("Boolean.parseBoolean(System.getProperty(TLS_SERVER_AUTHCLIENT, \"false\"))")
                .comment("    /**\n" +
                        "     * To determine whether verify the client endpoint's certificate strictly\n" +
                        "     */").build());
        fields.add(new FieldInfo.Builder().modifier(PUBLIC_STATIC)
                .type("String").name("tlsServerTrustCertPath").initValue("System.getProperty(TLS_SERVER_TRUSTCERTPATH, null)")
                .comment("    /**\n" +
                        "     * The store path of trusted certificates for verifying the client endpoint's certificate\n" +
                        "     */").build());
        fields.add(new FieldInfo.Builder().modifier(PUBLIC_STATIC)
                .type("String").name("tlsClientKeyPath").initValue("System.getProperty(TLS_CLIENT_KEYPATH, null)")
                .comment("    /**\n" +
                        "     * The store path of client-side private key\n" +
                        "     */").build());
        fields.add(new FieldInfo.Builder().modifier(PUBLIC_STATIC)
                .type("String").name("tlsClientKeyPassword").initValue("System.getProperty(TLS_CLIENT_KEYPASSWORD, null)")
                .comment("    /**\n" +
                        "     * The password of the client-side private key\n" +
                        "     */").build());
        fields.add(new FieldInfo.Builder().modifier(PUBLIC_STATIC)
                .type("String").name("tlsClientCertPath").initValue("System.getProperty(TLS_CLIENT_CERTPATH, null)")
                .comment("    /**\n" +
                        "     * The store path of client-side X.509 certificate chain in PEM format\n" +
                        "     */").build());
        fields.add(new FieldInfo.Builder().modifier(PUBLIC_STATIC)
                .type("boolean").name("tlsClientAuthServer").initValue("Boolean.parseBoolean(System.getProperty(TLS_CLIENT_AUTHSERVER, \"false\"))")
                .comment("    /**\n" +
                        "     * To determine whether verify the server endpoint's certificate strictly\n" +
                        "     */").build());
        fields.add(new FieldInfo.Builder().modifier(PUBLIC_STATIC)
                .type("String").name("tlsClientTrustCertPath").initValue("System.getProperty(TLS_CLIENT_TRUSTCERTPATH, null)")
                .comment("    /**\n" +
                        "     * The store path of trusted certificates for verifying the server endpoint's certificate\n" +
                        "     */").build());
        fields.add(new FieldInfo.Builder().modifier(PUBLIC_STATIC)
                .type("TlsMode").name("tlsMode").initValue("TlsMode.parse(System.getProperty(TLS_SERVER_MODE, \"permissive\"))")
                .comment("    /**\n" +
                        "     * For server, three SSL modes are supported: disabled, permissive and enforcing.\n" +
                        "     * For client, use {@link TlsSystemConfig#tlsEnable} to determine whether use SSL.\n" +
                        "     * <ol>\n" +
                        "     *     <li><strong>disabled:</strong> SSL is not supported; any incoming SSL handshake will be rejected, causing connection closed.</li>\n" +
                        "     *     <li><strong>permissive:</strong> SSL is optional, aka, server in this mode can serve client connections with or without SSL;</li>\n" +
                        "     *     <li><strong>enforcing:</strong> SSL is required, aka, non SSL connection will be rejected.</li>\n" +
                        "     * </ol>\n" +
                        "     */").build());
        fields.add(new FieldInfo.Builder().modifier(PUBLIC_STATIC)
                .type("String").name("tlsConfigFile").initValue("System.getProperty(TLS_CONFIG_FILE, \"/etc/rocketmq/tls.properties\")")
                .comment("    /**\n" +
                        "     * A config file to store the above TLS related configurations,\n" +
                        "     * except {@link TlsSystemConfig#tlsMode} and {@link TlsSystemConfig#tlsEnable}\n" +
                        "     */").build());
        setFields(fields);
    }
}
