package dev.russia9.trainpix;

import dev.russia9.trainpix.lib.Reference;
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
                case "DEBUG":
                    Configurator.setLevel("TrainPix", Level.DEBUG);
                    break;
                case "TRACE":
                    Configurator.setLevel("TrainPix", Level.TRACE);
                    break;
                default:
                    Configurator.setLevel("TrainPix", Level.INFO);
                    break;
            }
        }
        for (String arg : args) {
            if ("Debug=true".equals(arg)) {
                Configurator.setLevel("TrainPix", Level.DEBUG);
            } else if ("Trace=true".equals(arg)) {
                Configurator.setLevel("TrainPix", Level.TRACE);
            }
        }

        logger.info(Reference.botName + " " + Reference.botVersion + " by Russia9");
        logger.info("For more information: " + Reference.botSourcesLink);

        new Bootstrap();
    }
}
