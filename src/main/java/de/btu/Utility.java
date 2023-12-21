package de.btu;

import lombok.SneakyThrows;

import java.io.File;
import java.net.URISyntaxException;

public class Utility {
    /**
     * returns the directory of the .jar file that is used to run this program
     */
    @SneakyThrows(URISyntaxException.class)
    public static File getDirectoryOfExecutable() {
        return new File(Utility.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParentFile();
    }
}
