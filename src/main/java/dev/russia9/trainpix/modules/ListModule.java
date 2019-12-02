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

import static dev.russia9.trainpix.lib.ParseHelper.getPage;

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
    public String[] getAliases() {
        return aliases;
    }

    @Override
    public void process(MessageCreateEvent event) {
        String[] message = event.getMessageContent().split(" ");
        if (message.length == 2) { // First page
            String searchQuery = message[1];
            EmbedBuilder reply = new EmbedBuilder();

            String lang = "en";
            if (event.isServerMessage() && event.getServer().get().getRegion().getKey().equals("russia")) {
                lang = "ru";
            }
            logger.trace("Detected LANG: " + lang);

            try {
                String searchUrl = "https://trainpix.org/vsearch.php?cid=0&did=0&mid=0&zid=0&serial_type=&works_number=&id_number=&anybuilt=1&anywritt=1&state=0&note=&info=&order=0&num=" + URLEncoder.encode(searchQuery, "UTF-8");
                Document document = getPage(searchUrl, lang);

                if (document.getElementsContainingOwnText(localeManager.getString(lang, "list.nothing")).isEmpty()) {
                    Element table = document.getElementsByClass("main").get(0).getElementsByTag("table").get(0).child(0);
                    Elements trains = table.children();

                    int count = Integer.parseInt(document.getElementsByClass("main").get(0).getElementsByTag("p").get(0).getElementsByTag("b").get(0).text());

                    int size = table.children().size() - 3;
                    if (size > Reference.maxListSize) size = Reference.maxListSize - 1;

                    int i = 0;
                    int currentTrain = 2;
                    while (i <= size && currentTrain <= table.children().size() - 2) {
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
                        String buildDate = localeManager.getString(lang, "train.built") + " " + localeManager.getString(lang, "unknown");

                        if (!built.isEmpty() && built.parents().get(0).children().size() > 0) {
                            buildDate = localeManager.getString(lang, "train.built") + " " + built.parents().get(0).getElementsByTag("b").text();
                        }

                        // Depot detection
                        Elements depot = trainPage.getElementsContainingOwnText(localeManager.getString(lang, "train.depot"));
                        String depotName = localeManager.getString(lang, "train.depot") + " " + localeManager.getString(lang, "unknown");

                        if (!depot.isEmpty() && depot.parents().get(0).children().size() > 0) {
                            depotName = depot.parents().get(0).getElementsByTag("a").text();
                        }

                        // Road detection
                        Elements road = trainPage.getElementsContainingOwnText(localeManager.getString(lang, "train.road"));
                        String roadName = localeManager.getString(lang, "train.road") + " " + localeManager.getString(lang, "unknown");

                        if (!road.isEmpty() && road.parents().get(0).children().size() > 0) {
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
                            default:
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
                } else {
                    reply.setAuthor("TrainPix");
                    reply.setTitle(localeManager.getString(lang, "errors.404.header"));
                    reply.addField(localeManager.getString(lang, "errors.404.title"), localeManager.getString(lang, "errors.404.description"));
                }
            } catch (IOException e) {
                logger.warn(e);
                reply.setTitle(localeManager.getString(lang, "errors.500.header"));
                reply.addField(localeManager.getString(lang, "errors.500.title"), localeManager.getString(lang, "errors.500.description"));
            }

            event.getChannel().sendMessage(reply);
        }
    }
}
