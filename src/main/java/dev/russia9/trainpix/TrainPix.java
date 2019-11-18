package dev.russia9.trainpix;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

public class TrainPix {
    private static final Logger logger = LogManager.getLogger(TrainPix.class.getName());

    public static void main(String[] args) {
        for (String arg : args) {
            if (arg.equals("Debug=true")) {
                Configurator.setRootLevel(Level.DEBUG);
            } else if (arg.equals("Trace=true")) {
                Configurator.setRootLevel(Level.TRACE);
            }
        }

        logger.info("TrainPix v0.1 by Russia9");
        logger.info("For more information: https://github.com/Russia9/TrainPix");

        new Bootstrap();
    }
}
