package dev.russia9.trainpix.i18n;

import java.util.Locale;
import java.util.ResourceBundle;

public class LocaleManager {
    private Locale[] locales;
    private ResourceBundle[] resourceBundles;

    public LocaleManager() {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        locales = new Locale[]{
                new Locale("ru", "RU"),
                new Locale("en", "US")
        };
        resourceBundles = new ResourceBundle[]{
                ResourceBundle.getBundle("locales/trainpix", locales[0], classloader),
                ResourceBundle.getBundle("locales/trainpix", locales[1], classloader)
        };
    }

    public String getString(String locale, String name) {
        switch (locale) {
            case "ru":
                return resourceBundles[0].getString(name);
            default:
                return resourceBundles[1].getString(name);
        }
    }
}
