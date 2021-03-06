package dev.russia9.trainpix.listeners;

import dev.russia9.trainpix.i18n.LocaleManager;
import dev.russia9.trainpix.lib.Reference;
import dev.russia9.trainpix.modules.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.javacord.api.event.message.MessageCreateEvent;

/**
 * Message creation Listener
 *
 * @author Russia9
 * @since v0.0.1
 */
public class MessageCreateListener implements org.javacord.api.listener.message.MessageCreateListener {
    private static final Logger logger = LogManager.getLogger("TrainPix");
    private final BotModule[] modules;

    public MessageCreateListener(LocaleManager localeManager) {
        modules = new BotModule[]{
                new ListModule(localeManager),
                new PhotoModule(localeManager),
                new HelpModule(localeManager),
                new RandomModule(localeManager)
        };
    }

    @Override
    public void onMessageCreate(MessageCreateEvent messageCreateEvent) {
        String messageContent = messageCreateEvent.getMessageContent();

        for (BotModule module : modules) {
            logger.trace("Check `" + messageContent + "` for " + module.getClass().getName());

            String[] aliases = module.getAliases();
            for (String alias : aliases) {
                if (messageContent.split(" ").length > 0 && messageContent.split(" ")[0].equals(Reference.botPrefix + alias)) {
                    logger.debug(module.getClass().getName() + " Processing `" + messageContent + "`");
                    module.process(messageCreateEvent);
                    break;
                }
            }
        }
    }
}
