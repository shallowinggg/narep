package com.shallowinggg.narep.core.generators.defaults;

import com.shallowinggg.narep.core.CodeGenerator;
import com.shallowinggg.narep.core.annotation.Generator;
import com.shallowinggg.narep.core.common.ConfigInfos;
import com.shallowinggg.narep.core.util.FileUtils;
import com.shallowinggg.narep.core.util.StringTinyUtils;

import java.io.IOException;

import static com.shallowinggg.narep.core.lang.JLSConstants.FILE_SEPARATOR;

/**
 * @author shallowinggg
 */
@Generator
public class PomCodeGenerator implements CodeGenerator {
    protected String parentModuleName = "test";
    protected String parentVersion = "1.0-SNAPSHOT";
    protected String relativePath;

    protected String modelVersion = "4.0.0";
    protected String groupId;
    protected String packaging = "jar";
    protected String moduleName = "remoting";
    protected String version = "1.0-SNAPSHOT";
    protected String description;
    protected String url = "www.example.com";

    protected String compilerSource = "1.8";
    protected String compilerTarget = "1.8";


    @Override
    public String fileName() {
        return "pom.xml";
    }

    @Override
    public void write() throws IOException {
        String location = ConfigInfos.getInstance().storeLocation() + FILE_SEPARATOR + fileName();
        String content = header() +
                basic() +
                properties() +
                dependencies() +
                build() +
                footer();
        FileUtils.writeFile(location, content, CodeGenerator.DEFAULT_CHARSET);
    }

    protected String header() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "\n" +
                "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                "         xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                "\n";
    }

    protected String parent() {
        StringBuilder builder = new StringBuilder(350);
        builder.append("    <parent>\n");

        String groupId = ConfigInfos.getInstance().basePackage();
        int pos = groupId.lastIndexOf('.');
        groupId = groupId.substring(0, pos);
        if(StringTinyUtils.isNotEmpty(parentModuleName)) {
            builder.append("        <artifactId>").append(parentModuleName).append("</artifactId>\n");
        }
        builder.append("        <groupId>").append(groupId).append("</groupId>\n");
        if(StringTinyUtils.isNotEmpty(parentVersion)) {
            builder.append("        <version>").append(parentVersion).append("</version>\n");
        }
        if(StringTinyUtils.isNotEmpty(relativePath)) {
            builder.append("        <relativePath>").append(relativePath).append("</relativePath>\n");
        }
        builder.append("    </parent>\n");
        return builder.toString();
    }

    protected String modelVersion() {
        return "    <modelVersion>" + modelVersion + "</modelVersion>\n";
    }

    protected String groupId() {
        if(StringTinyUtils.isNotEmpty(groupId)) {
            return "    <groupId>" + groupId + "</groupId>\n";
        }
        return "";
    }

    protected String artifactId() {
        return "    <artifactId>" + moduleName + "</artifactId>\n";
    }

    protected String packaging() {
        if(StringTinyUtils.isNotEmpty(packaging)) {
            return "    <packaging>" + packaging + "</packaging>\n";
        }
        return "";
    }

    protected String name() {
        if(StringTinyUtils.isNotEmpty(moduleName)) {
            return "    <name>"+ moduleName +"</name>\n";
        }
        return "";
    }

    protected String version() {
        if(StringTinyUtils.isNotEmpty(version)) {
            return "    <version>" + version + "</version>\n";
        }
        return "";
    }

    protected String description() {
        if(StringTinyUtils.isNotEmpty(description)) {
            return "    <description>" + description + "</description>\n";
        }
        return "";
    }

    protected String url() {
        if(StringTinyUtils.isNotEmpty(url)) {
            return "    <url>" + url + "</url>\n";
        }
        return "";
    }

    protected String basic() {
        String groupId = ConfigInfos.getInstance().basePackage();
        int pos = groupId.lastIndexOf('.');
        groupId = groupId.substring(0, pos);
        return "    <parent>\n" +
                "        <artifactId>" + parentModuleName + "</artifactId>\n" +
                "        <groupId>" + groupId + "</groupId>\n" +
                "        <version>" + version + "</version>\n" +
                "    </parent>\n" +
                "    <modelVersion>4.0.0</modelVersion>" +
                "\n" +
                "    <artifactId>" + moduleName + "</artifactId>\n" +
                "    <name>" + moduleName + "</name>\n" +
                "    <url>" + url + "</url>\n" +
                "\n";
    }

    protected String properties() {
        return "    <properties>\n" +
                "        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>\n" +
                "        <maven.compiler.source>" + compilerSource + "</maven.compiler.source>\n" +
                "        <maven.compiler.target>" + compilerTarget + "</maven.compiler.target>\n" +
                "    </properties>\n" +
                "\n";
    }

    protected String dependencies() {
        return "    <dependencies>\n" +
                "        <dependency>\n" +
                "            <groupId>com.alibaba</groupId>\n" +
                "            <artifactId>fastjson</artifactId>\n" +
                "            <version>1.2.51</version>\n" +
                "        </dependency>\n" +
                "        <dependency>\n" +
                "            <groupId>io.netty</groupId>\n" +
                "            <artifactId>netty-all</artifactId>\n" +
                "            <version>4.0.42.Final</version>\n" +
                "        </dependency>\n" +
                "        <dependency>\n" +
                "            <groupId>io.netty</groupId>\n" +
                "            <artifactId>netty-tcnative-boringssl-static</artifactId>\n" +
                "            <version>1.1.33.Fork26</version>\n" +
                "        </dependency>\n" +
                "        <dependency>\n" +
                "            <groupId>org.apache.logging.log4j</groupId>\n" +
                "            <artifactId>log4j-api</artifactId>\n" +
                "            <version>2.11.0</version>\n" +
                "        </dependency>\n" +
                "        <dependency>\n" +
                "            <groupId>org.apache.logging.log4j</groupId>\n" +
                "            <artifactId>log4j-core</artifactId>\n" +
                "            <version>2.11.0</version>\n" +
                "        </dependency>\n" +
                "    </dependencies>\n" +
                "\n";
    }

    protected String build() {
        return "    <build>\n" +
                "        <pluginManagement><!-- lock down plugins versions to avoid using Maven defaults (may be moved to parent pom) -->\n" +
                "            <plugins>\n" +
                "                <plugin>\n" +
                "                    <artifactId>maven-clean-plugin</artifactId>\n" +
                "                    <version>3.1.0</version>\n" +
                "                </plugin>\n" +
                "                <!-- see http://maven.apache.org/ref/current/maven-core/default-bindings.html#Plugin_bindings_for_war_packaging -->\n" +
                "                <plugin>\n" +
                "                    <artifactId>maven-resources-plugin</artifactId>\n" +
                "                    <version>3.0.2</version>\n" +
                "                </plugin>\n" +
                "                <plugin>\n" +
                "                    <artifactId>maven-compiler-plugin</artifactId>\n" +
                "                    <version>3.8.0</version>\n" +
                "                </plugin>\n" +
                "                <plugin>\n" +
                "                    <artifactId>maven-surefire-plugin</artifactId>\n" +
                "                    <version>2.22.1</version>\n" +
                "                </plugin>\n" +
                "                <plugin>\n" +
                "                    <artifactId>maven-war-plugin</artifactId>\n" +
                "                    <version>3.2.2</version>\n" +
                "                </plugin>\n" +
                "                <plugin>\n" +
                "                    <artifactId>maven-install-plugin</artifactId>\n" +
                "                    <version>2.5.2</version>\n" +
                "                </plugin>\n" +
                "                <plugin>\n" +
                "                    <artifactId>maven-deploy-plugin</artifactId>\n" +
                "                    <version>2.8.2</version>\n" +
                "                </plugin>\n" +
                "            </plugins>\n" +
                "        </pluginManagement>\n" +
                "    </build>\n";
    }

    protected String footer() {
        return "</project>\n";
    }
}
