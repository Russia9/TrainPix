package dev.russia9.trainpix;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

/**
 * Launch class
 *
 * @author Russia9
 * @since v0.0.1
 */
public class TrainPix {
    private static final Logger logger = LogManager.getLogger("TrainPix");

    public static void main(String[] args) {
        if (System.getenv("LEVEL") != null) {
            String level = System.getenv("LEVEL");
            switch (level) {
                case "OFF":
                    Configurator.setLevel("TrainPix", Level.OFF);
                    break;
                case "FATAL":
                    Configurator.setLevel("TrainPix", Level.FATAL);
                    break;
                case "ERROR":
                    Configurator.setLevel("TrainPix", Level.ERROR);
                    break;
                case "WARN":
                    Configurator.setLevel("TrainPix", Level.WARN);
                    break;
                case "INFO":
                    Configurator.setLevel("TrainPix", Level.INFO);
                    break;
                case "DEBUG":
                    Configurator.setLevel("TrainPix", Level.DEBUG);
                    break;
                case "TRACE":
                    Configurator.setLevel("TrainPix", Level.TRACE);
                    break;
            }
        }
        for (String arg : args) {
            if (arg.equals("Debug=true")) {
                Configurator.setLevel("TrainPix", Level.DEBUG);
            } else if (arg.equals("Trace=true")) {
                Configurator.setLevel("TrainPix", Level.TRACE);
            }
        }

        logger.info("TrainPix v0.1 by Russia9");
        logger.info("For more information: https://github.com/Russia9/TrainPix");

        new Bootstrap();
    }
}
