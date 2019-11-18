package dev.russia9.trainpix;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Class for bootstrapping Manager
 *
 * @author Russia9
 * @since v0.0.1
 */
public class Bootstrap {
    private static final Logger logger = LogManager.getLogger(Bootstrap.class.getName());

    public Bootstrap() {
        new Manager(getToken(), getClientID());
    }

    private String getToken() {
        return  "";
    }

    private String getClientID() {
        return "";
    }
}
