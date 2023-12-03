package de.btu;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.io.File;

public class ConfigFileReader {
    private static Configuration config;
    private static final File configFile = new File(Utility.getDirectoryOfExecutable(), "config.properties");

    private static Configuration readConfigFile() throws ConfigurationException {
        System.out.println("reading config from " + ConfigFileReader.configFile.getAbsolutePath());
        Parameters params = new Parameters();
        FileBasedConfigurationBuilder<FileBasedConfiguration> builder =
                new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
                        .configure(params.properties().setFile(ConfigFileReader.configFile));
        return builder.getConfiguration();
    }

    public static Configuration getConfig() {
        if (config == null) {
            try {
                config = readConfigFile();
            } catch (ConfigurationException e) {
                throw new RuntimeException("can't read config file (path: " + configFile.getAbsolutePath() + ")!");
            }
        }
        return config;
    }
}
