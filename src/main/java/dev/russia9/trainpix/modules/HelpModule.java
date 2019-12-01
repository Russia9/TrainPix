package dev.russia9.trainpix.modules;

import dev.russia9.trainpix.i18n.LocaleManager;
import dev.russia9.trainpix.lib.Reference;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;

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
    public boolean check(String message) {
        logger.trace("Check `" + message + "` for " + this.getClass().getName());
        for (String alias : aliases) {
            if (message.startsWith(Reference.botPrefix + alias)) return true;
        }
        return false;
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
        reply.setTitle(localeManager.getString(lang, "help.title"));
        reply.addField(localeManager.getString(lang, "help.list.description"), localeManager.getString(lang, "help.photo.description"));
        reply.setFooter("(c) Russia9 2019");
        event.getChannel().sendMessage(reply);
    }
}
