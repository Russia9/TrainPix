package dev.russia9.trainpix.modules;

import dev.russia9.trainpix.i18n.LocaleManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;

/**
 * /help command module
 *
 * @author Russia9
 * @since 0.1
 */
public class HelpModule implements BotModule {
    private static final Logger logger = LogManager.getLogger("TrainPix");
    private LocaleManager localeManager;
    private String[] aliases = {
            "help"
    };

    public HelpModule(LocaleManager localeManager) {
        this.localeManager = localeManager;
    }

    @Override
    public String[] getAliases() {
        return aliases;
    }

    @Override
    public void process(MessageCreateEvent event) {
        EmbedBuilder reply = new EmbedBuilder();

        String lang = "en";
        if (event.getServer().get().getRegion().getKey().equals("russia")) {
            lang = "ru";
        }
        logger.trace("Detected LANG: " + lang);

        reply.setAuthor("TrainPix");

        reply.addField(localeManager.getString(lang, "list.help.title"), localeManager.getString(lang, "list.help.description"));
        reply.addField(localeManager.getString(lang, "photo.help.title"), localeManager.getString(lang, "photo.help.description"));

        reply.setFooter("(c) Russia9 2019");
        event.getChannel().sendMessage(reply);
    }
}
