package dev.russia9.trainpix;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Bootstrap {
    private static final Logger logger = LogManager.getLogger(Bootstrap.class.getName());
    private String token;

    public Bootstrap() {

        new Manager();
    }
}
