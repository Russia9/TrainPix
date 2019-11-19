package dev.russia9.trainpix.modules;


import dev.russia9.trainpix.i18n.LocaleManager;
import dev.russia9.trainpix.lib.Reference;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URLEncoder;

import static dev.russia9.trainpix.lib.Lib.getPage;

/**
 * /list command module
 */
public class ListModule implements BotModule {
    private static final Logger logger = LogManager.getLogger("TrainPix");
    private LocaleManager localeManager;

    public ListModule(LocaleManager localeManager) {
        this.localeManager = localeManager;
    }

    @Override
    public boolean check(String message) {
        logger.trace("Check `" + message + "`");
        return message.contains("/list");
    }

    @Override
    public void process(MessageCreateEvent event) {
        logger.debug("Processing `" + event.getMessageContent() + "`");
        String[] message = event.getMessageContent().split(" ");
        if (message.length == 2) { // First page
            String searchQuery = message[1];
            EmbedBuilder reply = new EmbedBuilder();

            try {
                String searchUrl = "https://trainpix.org/vsearch.php?cid=0&did=0&mid=0&zid=0&serial_type=&works_number=&id_number=&anybuilt=1&anywritt=1&state=0&note=&info=&order=0&num=" + URLEncoder.encode(searchQuery, "UTF-8");

                String lang = "en";
                if (event.getServer().get().getRegion().getKey().equals("russia")) {
                    lang = "ru";
                }
                logger.trace("Detected LANG: " + lang);

                Document document = getPage(searchUrl, lang);

                Element table = document.getElementsByClass("main").get(0).getElementsByTag("table").get(0).child(0);
                Elements trains = table.children();

                int count = Integer.parseInt(document.getElementsByClass("main").get(0).getElementsByTag("p").get(0).getElementsByTag("b").get(0).text());

                int size = table.children().size() - 3;
                if (size > Reference.maxListSize) size = Reference.maxListSize;

                int i = 0, currentTrain = 2;
                while (i < size) {
                    Element train = trains.get(currentTrain);

                    StringBuilder head = new StringBuilder();
                    StringBuilder body = new StringBuilder();

                    boolean process = false;

                    head.append(train.getElementsByTag("a").text()); // Train number

                    if (document.getElementsContainingOwnText(localeManager.getString(lang, "train.serial")).size() > 0) { // Train build date
                        if (!train.child(3).text().equals(""))
                            body.append(localeManager.getString(lang, "train.built")).append(train.child(3).text());
                        else body.append(localeManager.getString(lang, "train.built.unknown"));
                    } else {
                        if (!train.child(2).text().equals(""))
                            body.append(localeManager.getString(lang, "train.built")).append(train.child(2).text());
                        else body.append(localeManager.getString(lang, "train.built.unknown"));
                    }

                    // TODO: depot detection
                    // TODO: road detection

                    switch (train.className()) { // Checking state
                        case "s1":
                        case "s11":
                            head.append(" | ").append(localeManager.getString(lang, "train.status.working"));
                            ++i;
                            process = true;
                            break;
                        case "s2":
                        case "s12":
                            head.append(" | ").append(localeManager.getString(lang, "train.status.new"));
                            ++i;
                            process = true;
                            break;
                        case "s3":
                        case "s13":
                            head.append(" | ").append(localeManager.getString(lang, "train.status.broken"));
                            ++i;
                            process = true;
                            break;
                        case "s4":
                        case "s14":
                            head.append(" | ").append(localeManager.getString(lang, "train.status.written_off"));
                            ++i;
                            process = true;
                            break;
                        case "s5":
                        case "s15":
                            head.append(" | ").append(localeManager.getString(lang, "train.status.unknown"));
                            ++i;
                            process = true;
                            break;
                        case "s7":
                        case "s17":
                            head.append(" | ").append(localeManager.getString(lang, "train.status.krp"));
                            ++i;
                            process = true;
                            break;
                        case "s9":
                        case "s19":
                            head.append(" | ").append(localeManager.getString(lang, "train.status.monument"));
                            ++i;
                            process = true;
                            break;
                    }

                    if (process) { // If it is current state
                        reply.addField(head.toString(), body.toString());
                    }
                    ++currentTrain;
                }

                reply.setAuthor("TrainPix");
                reply.setTitle(searchQuery);
                reply.setFooter(localeManager.getString(lang, "list.results") + size + "/" + count);
            } catch (IOException e) {
                logger.warn(e);
            }

            event.getChannel().sendMessage(reply);
        }
    }
}
