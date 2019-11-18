package dev.russia9.trainpix.modules;


import dev.russia9.trainpix.i18n.LocaleManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;

public class ListModule implements BotModule {
    private static final Logger logger = LogManager.getLogger(ListModule.class.getName());
    private LocaleManager localeManager;

    public ListModule(LocaleManager localeManager) {
        this.localeManager = localeManager;
    }

    @Override
    public boolean check(String message) {
        return message.contains("/list");
    }

    @Override
    public void process(MessageCreateEvent event) {
        String[] message = event.getMessageContent().split(" ");
        if (message.length == 2) { // First page
            String searchQuery = message[1];
            EmbedBuilder reply = new EmbedBuilder();
        }
    }
}
