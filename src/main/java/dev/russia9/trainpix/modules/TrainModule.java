package dev.russia9.trainpix.modules;

import dev.russia9.trainpix.i18n.LocaleManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.javacord.api.event.message.MessageCreateEvent;

/**
 * /train command module
 *
 * @author Russia9
 * @since 0.8
 */
public class TrainModule implements BotModule {
    private static final Logger logger = LogManager.getLogger("TrainPix");
    private LocaleManager localeManager;
    private String[] aliases = {
            "trains",
            "t"
    };

    public TrainModule(LocaleManager localeManager) {
        this.localeManager = localeManager;
    }

    @Override
    public String[] getAliases() {
        return aliases;
    }

    @Override
    public void process(MessageCreateEvent event) {

    }
}