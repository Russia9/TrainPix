package dev.russia9.trainpix.i18n;

public class LocaleManager {
    // TODO: rewrite
    public String getString(String locale, String name) {
        switch (name) {
            case "train.built":
                if (locale.equals("ru")) {
                    return "Построен: ";
                } else {
                    return "Built: ";
                }
            case "train.built.unknown":
                if (locale.equals("ru")) {
                    return "Построен: Неизвестно";
                } else {
                    return "Built: Unknown";
                }
            case "train.status.working":
                if (locale.equals("ru")) {
                    return "Действующий";
                } else {
                    return "In operation";
                }
            case "train.status.new":
                if (locale.equals("ru")) {
                    return "Новый";
                } else {
                    return "New";
                }
            case "train.status.broken":
                if (locale.equals("ru")) {
                    return "Не работает";
                } else {
                    return "Out of order";
                }
            case "train.status.written_off":
                if (locale.equals("ru")) {
                    return "Списан";
                } else {
                    return "Written off";
                }
            case "train.status.unknown":
                if (locale.equals("ru")) {
                    return "Проебали";
                } else {
                    return "Current location and condition are unknown ";
                }
            case "train.status.moved":
                if (locale.equals("ru")) {
                    return "Передан в другое депо";
                } else {
                    return "Transferred to another depot";
                }
            case "train.status.moved2":
                if (locale.equals("ru")) {
                    return "Передан на другую дорогу";
                } else {
                    return "Passed to an other railway";
                }
            case "train.status.krp":
                if (locale.equals("ru")) {
                    return "КРП/Модернизация";
                } else {
                    return "Modernisation";
                }
            case "train.status.refurbishment":
                if (locale.equals("ru")) {
                    return "Перенумерован";
                } else {
                    return "Refurbishment";
                }
            case "train.status.monument":
                if (locale.equals("ru")) {
                    return "Памятник/музейный экспонат/тренажёр ";
                } else {
                    return "Monument/Museum exhibit/Trainer ";
                }
            case "list.results":
                if (locale.equals("ru")) {
                    return "Результаты: ";
                } else {
                    return "Results: ";
                }
        }
        return "";
    }
}
