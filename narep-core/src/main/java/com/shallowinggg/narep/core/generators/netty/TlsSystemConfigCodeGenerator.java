package com.shallowinggg.narep.core.generators.netty;

import com.shallowinggg.narep.core.common.CodeGeneratorHelper;
import com.shallowinggg.narep.core.common.FieldMetaData;
import com.shallowinggg.narep.core.common.GenericBuilder;
import com.shallowinggg.narep.core.generators.ClassCodeGenerator;

import java.util.ArrayList;
import java.util.List;

import static com.shallowinggg.narep.core.common.FieldMetaData.Modifier.PUBLIC_STATIC;
import static com.shallowinggg.narep.core.common.FieldMetaData.Modifier.PUBLIC_STATIC_FINAL;

/**
 * @author shallowinggg
 */
public class TlsSystemConfigCodeGenerator extends ClassCodeGenerator {
    private static final String CLASS_NAME = "TlsSystemConfig";
    private static final String SUB_PACKAGE = "netty";
    private List<FieldMetaData> fields = new ArrayList<>(30);

    public TlsSystemConfigCodeGenerator() {
        super(CLASS_NAME, null, SUB_PACKAGE);
        fields.add(new FieldMetaData(PUBLIC_STATIC_FINAL, "String", "TLS_SERVER_MODE", "\"tls.server.mode\""));
        fields.add(new FieldMetaData(PUBLIC_STATIC_FINAL, "String", "TLS_ENABLE", "\"tls.enable\""));
        fields.add(new FieldMetaData(PUBLIC_STATIC_FINAL, "String", "TLS_CONFIG_FILE", "\"tls.config.file\""));
        fields.add(new FieldMetaData(PUBLIC_STATIC_FINAL, "String", "TLS_TEST_MODE_ENABLE", "\"tls.test.mode.enable\""));

        fields.add(new FieldMetaData(PUBLIC_STATIC_FINAL, "String", "TLS_SERVER_NEED_CLIENT_AUTH", "\"tls.server.need.client.auth\""));
        fields.add(new FieldMetaData(PUBLIC_STATIC_FINAL, "String", "TLS_SERVER_KEYPATH", "\"tls.server.keyPath\""));
        fields.add(new FieldMetaData(PUBLIC_STATIC_FINAL, "String", "TLS_SERVER_KEYPASSWORD", "\"tls.server.keyPassword\""));
        fields.add(new FieldMetaData(PUBLIC_STATIC_FINAL, "String", "TLS_SERVER_CERTPATH", "\"tls.server.certPath\""));
        fields.add(new FieldMetaData(PUBLIC_STATIC_FINAL, "String", "TLS_SERVER_AUTHCLIENT", "\"tls.server.authClient\""));
        fields.add(new FieldMetaData(PUBLIC_STATIC_FINAL, "String", "TLS_SERVER_TRUSTCERTPATH", "\"tls.server.trustCertPath\""));

        fields.add(new FieldMetaData(PUBLIC_STATIC_FINAL, "String", "TLS_CLIENT_KEYPATH", "\"tls.client.keyPath\""));
        fields.add(new FieldMetaData(PUBLIC_STATIC_FINAL, "String", "TLS_CLIENT_KEYPASSWORD", "\"tls.client.keyPassword\""));
        fields.add(new FieldMetaData(PUBLIC_STATIC_FINAL, "String", "TLS_CLIENT_CERTPATH", "\"tls.client.certPath\""));
        fields.add(new FieldMetaData(PUBLIC_STATIC_FINAL, "String", "TLS_CLIENT_AUTHSERVER", "\"tls.client.authServer\""));
        fields.add(new FieldMetaData(PUBLIC_STATIC_FINAL, "String", "TLS_CLIENT_TRUSTCERTPATH", "\"tls.client.trustCertPath\""));

        fields.add(GenericBuilder.of(FieldMetaData::new).with(FieldMetaData::setModifier, PUBLIC_STATIC)
                .with(FieldMetaData::setClazz, "boolean")
                .with(FieldMetaData::setName, "tlsEnable")
                .with(FieldMetaData::setDefaultValue, "Boolean.parseBoolean(System.getProperty(TLS_ENABLE, \"false\"))")
                .with(FieldMetaData::setComment, "    /**\n" +
                        "     * To determine whether use SSL in client-side, include SDK client and BrokerOuterAPI\n" +
                        "     */").build());
        fields.add(GenericBuilder.of(FieldMetaData::new).with(FieldMetaData::setModifier, PUBLIC_STATIC)
                .with(FieldMetaData::setClazz, "boolean")
                .with(FieldMetaData::setName, "tlsTestModeEnable")
                .with(FieldMetaData::setDefaultValue, "Boolean.parseBoolean(System.getProperty(TLS_TEST_MODE_ENABLE, \"false\"))")
                .with(FieldMetaData::setComment, "    /**\n" +
                        "     * To determine whether use test mode when initialize TLS context\n" +
                        "     */").build());
        fields.add(GenericBuilder.of(FieldMetaData::new).with(FieldMetaData::setModifier, PUBLIC_STATIC)
                .with(FieldMetaData::setClazz, "String")
                .with(FieldMetaData::setName, "tlsServerNeedClientAuth")
                .with(FieldMetaData::setDefaultValue, "System.getProperty(TLS_SERVER_NEED_CLIENT_AUTH, \"none\")")
                .with(FieldMetaData::setComment, "    /**\n" +
                        "     * Indicates the state of the {@link javax.net.ssl.SSLEngine} with respect to client authentication.\n" +
                        "     * This configuration item really only applies when building the server-side {@link SslContext},\n" +
                        "     * and can be set to none, require or optional.\n" +
                        "     */").build());
        fields.add(GenericBuilder.of(FieldMetaData::new).with(FieldMetaData::setModifier, PUBLIC_STATIC)
                .with(FieldMetaData::setClazz, "String")
                .with(FieldMetaData::setName, "tlsServerKeyPath")
                .with(FieldMetaData::setDefaultValue, "System.getProperty(TLS_SERVER_KEYPATH, null)")
                .with(FieldMetaData::setComment, "    /**\n" +
                        "     * The store path of server-side private key\n" +
                        "     */").build());
        fields.add(GenericBuilder.of(FieldMetaData::new).with(FieldMetaData::setModifier, PUBLIC_STATIC)
                .with(FieldMetaData::setClazz, "String")
                .with(FieldMetaData::setName, "tlsServerKeyPassword")
                .with(FieldMetaData::setDefaultValue, "System.getProperty(TLS_SERVER_KEYPASSWORD, null)")
                .with(FieldMetaData::setComment, "    /**\n" +
                        "     * The password of the server-side private key\n" +
                        "     */").build());
        fields.add(GenericBuilder.of(FieldMetaData::new).with(FieldMetaData::setModifier, PUBLIC_STATIC)
                .with(FieldMetaData::setClazz, "String")
                .with(FieldMetaData::setName, "tlsServerCertPath")
                .with(FieldMetaData::setDefaultValue, "System.getProperty(TLS_SERVER_CERTPATH, null)")
                .with(FieldMetaData::setComment, "    /**\n" +
                        "     * The store path of server-side X.509 certificate chain in PEM format\n" +
                        "     */").build());
        fields.add(GenericBuilder.of(FieldMetaData::new).with(FieldMetaData::setModifier, PUBLIC_STATIC)
                .with(FieldMetaData::setClazz, "boolean")
                .with(FieldMetaData::setName, "tlsServerAuthClient")
                .with(FieldMetaData::setDefaultValue, "Boolean.parseBoolean(System.getProperty(TLS_SERVER_AUTHCLIENT, \"false\"))")
                .with(FieldMetaData::setComment, "    /**\n" +
                        "     * To determine whether verify the client endpoint's certificate strictly\n" +
                        "     */").build());
        fields.add(GenericBuilder.of(FieldMetaData::new).with(FieldMetaData::setModifier, PUBLIC_STATIC)
                .with(FieldMetaData::setClazz, "String")
                .with(FieldMetaData::setName, "tlsServerTrustCertPath")
                .with(FieldMetaData::setDefaultValue, "System.getProperty(TLS_SERVER_TRUSTCERTPATH, null)")
                .with(FieldMetaData::setComment, "    /**\n" +
                        "     * The store path of trusted certificates for verifying the client endpoint's certificate\n" +
                        "     */").build());
        fields.add(GenericBuilder.of(FieldMetaData::new).with(FieldMetaData::setModifier, PUBLIC_STATIC)
                .with(FieldMetaData::setClazz, "String")
                .with(FieldMetaData::setName, "tlsClientKeyPath")
                .with(FieldMetaData::setDefaultValue, "System.getProperty(TLS_CLIENT_KEYPATH, null)")
                .with(FieldMetaData::setComment, "    /**\n" +
                        "     * The store path of client-side private key\n" +
                        "     */").build());
        fields.add(GenericBuilder.of(FieldMetaData::new).with(FieldMetaData::setModifier, PUBLIC_STATIC)
                .with(FieldMetaData::setClazz, "String")
                .with(FieldMetaData::setName, "tlsClientKeyPassword")
                .with(FieldMetaData::setDefaultValue, "System.getProperty(TLS_CLIENT_KEYPASSWORD, null)")
                .with(FieldMetaData::setComment, "    /**\n" +
                        "     * The password of the client-side private key\n" +
                        "     */").build());
        fields.add(GenericBuilder.of(FieldMetaData::new).with(FieldMetaData::setModifier, PUBLIC_STATIC)
                .with(FieldMetaData::setClazz, "String")
                .with(FieldMetaData::setName, "tlsClientCertPath")
                .with(FieldMetaData::setDefaultValue, "System.getProperty(TLS_CLIENT_CERTPATH, null)")
                .with(FieldMetaData::setComment, "    /**\n" +
                        "     * The store path of client-side X.509 certificate chain in PEM format\n" +
                        "     */").build());
        fields.add(GenericBuilder.of(FieldMetaData::new).with(FieldMetaData::setModifier, PUBLIC_STATIC)
                .with(FieldMetaData::setClazz, "boolean")
                .with(FieldMetaData::setName, "tlsClientAuthServer")
                .with(FieldMetaData::setDefaultValue, "Boolean.parseBoolean(System.getProperty(TLS_CLIENT_AUTHSERVER, \"false\"))")
                .with(FieldMetaData::setComment, "    /**\n" +
                        "     * To determine whether verify the server endpoint's certificate strictly\n" +
                        "     */").build());
        fields.add(GenericBuilder.of(FieldMetaData::new).with(FieldMetaData::setModifier, PUBLIC_STATIC)
                .with(FieldMetaData::setClazz, "String")
                .with(FieldMetaData::setName, "tlsClientTrustCertPath")
                .with(FieldMetaData::setDefaultValue, "System.getProperty(TLS_CLIENT_TRUSTCERTPATH, null)")
                .with(FieldMetaData::setComment, "    /**\n" +
                        "     * The store path of trusted certificates for verifying the server endpoint's certificate\n" +
                        "     */").build());
        fields.add(GenericBuilder.of(FieldMetaData::new).with(FieldMetaData::setModifier, PUBLIC_STATIC)
                .with(FieldMetaData::setClazz, "TlsMode")
                .with(FieldMetaData::setName, "tlsMode")
                .with(FieldMetaData::setDefaultValue, "TlsMode.parse(System.getProperty(TLS_SERVER_MODE, \"permissive\"))")
                .with(FieldMetaData::setComment, "    /**\n" +
                        "     * For server, three SSL modes are supported: disabled, permissive and enforcing.\n" +
                        "     * For client, use {@link TlsSystemConfig#tlsEnable} to determine whether use SSL.\n" +
                        "     * <ol>\n" +
                        "     *     <li><strong>disabled:</strong> SSL is not supported; any incoming SSL handshake will be rejected, causing connection closed.</li>\n" +
                        "     *     <li><strong>permissive:</strong> SSL is optional, aka, server in this mode can serve client connections with or without SSL;</li>\n" +
                        "     *     <li><strong>enforcing:</strong> SSL is required, aka, non SSL connection will be rejected.</li>\n" +
                        "     * </ol>\n" +
                        "     */").build());
        fields.add(GenericBuilder.of(FieldMetaData::new).with(FieldMetaData::setModifier, PUBLIC_STATIC)
                .with(FieldMetaData::setClazz, "String")
                .with(FieldMetaData::setName, "tlsConfigFile")
                .with(FieldMetaData::setDefaultValue, "System.getProperty(TLS_CONFIG_FILE, \"/etc/rocketmq/tls.properties\")")
                .with(FieldMetaData::setComment, "    /**\n" +
                        "     * A config file to store the above TLS related configurations,\n" +
                        "     * except {@link TlsSystemConfig#tlsMode} and {@link TlsSystemConfig#tlsEnable}\n" +
                        "     */").build());
    }

    @Override
    public String buildFields() {
        return CodeGeneratorHelper.buildFieldsByMetaData(fields);
    }
}
