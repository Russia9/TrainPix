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
 *
 * @author Russia9
 * @since 0.1
 */
public class ListModule implements BotModule {
    private static final Logger logger = LogManager.getLogger("TrainPix");
    private LocaleManager localeManager;
    private String[] aliases = {
            "list",
            "l"
    };

    public ListModule(LocaleManager localeManager) {
        this.localeManager = localeManager;
    }

    @Override
    public boolean check(String message) {
        logger.trace("Check `" + message + "` for List");
        for (String alias : aliases) {
            if (message.contains(alias)) return true;
        }
        return false;
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
                while (i <= size && currentTrain <= table.children().size() - 2) {
                    logger.trace(currentTrain + " " + i);
                    logger.trace(size + " " + table.children().size());
                    Element train = trains.get(currentTrain);

                    StringBuilder head = new StringBuilder();
                    StringBuilder body = new StringBuilder();

                    boolean process = false;

                    // Model detection
                    String model = train.getElementsByTag("a").text();

                    String page = "https://trainpix.org" + train.getElementsByTag("a").attr("href");
                    Document trainPage = getPage(page, lang);

                    // Build date detection
                    Elements built = trainPage.getElementsContainingOwnText(localeManager.getString(lang, "train.built"));
                    String buildDate = localeManager.getString(lang, "train.built.unknown");
                    if (built.parents().get(0).children().size() > 0) {
                        buildDate = localeManager.getString(lang, "train.built") + " " + built.parents().get(0).getElementsByTag("b").text();
                    }


                    // Depot detection
                    Elements depot = trainPage.getElementsContainingOwnText(localeManager.getString(lang, "train.depot"));
                    String depotName = localeManager.getString(lang, "train.depot.unknown");
                    if (built.parents().get(0).children().size() > 0) {
                        depotName = depot.parents().get(0).getElementsByTag("a").text();
                    }

                    // Road detection
                    Elements road = trainPage.getElementsContainingOwnText(localeManager.getString(lang, "train.road"));
                    String roadName = localeManager.getString(lang, "train.road.unknown");
                    if (road.parents().get(0).children().size() > 0) {
                        roadName = road.parents().get(0).getElementsByTag("a").text();
                    }

                    head.append(model);
                    head.append(" | ").append(roadName);

                    body.append(buildDate);
                    body.append(" | ").append(depotName);

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
                reply.setFooter(localeManager.getString(lang, "list.results") + i + "/" + count);
            } catch (IOException e) {
                logger.warn(e);
            }

            event.getChannel().sendMessage(reply);
        }
    }
}
