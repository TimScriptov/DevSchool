package com.devschool.module;

import com.devschool.App;
import com.devschool.data.Preferences;
import com.devschool.utils.FileReader;

import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HtmlRenderer {

    public static String renderHtml(String html) {
        Document document = cleanHtml(Jsoup.parse(html).normalise());

        if (Preferences.translatePluginEnabled()) document.head().append(getTranslatePlugin());

        Element style = document.createElement("style");
        style.append(getStyle());
        document.head().appendChild(style);

        Element div = document.createElement("div");
        div.attr("id", "google_translate_element");
        document.body().prependChild(div);

        return document.outerHtml();
    }

    private static Document cleanHtml(@NotNull Document htmlDocument) {
        htmlDocument.getElementsByClass("w3-container top").remove();
        htmlDocument.select("#googleSearch").remove();
        htmlDocument.select("#google_translate_element").remove();
        htmlDocument.select("#topnav").remove();
        htmlDocument.select("#googleSearch").remove();
        htmlDocument.select("#mainLeaderboard").remove();
        htmlDocument.select(".w3-clear.nextprev").remove();
        htmlDocument.getElementsByAttributeValueStarting("href", "tryit.asp").remove();
        htmlDocument.select("#midcontentadcontainer").remove();
        htmlDocument.select("#w3-exerciseform").remove();
        htmlDocument.select(".sidesection").remove();
        htmlDocument.select(".footer").remove();
        htmlDocument.select(".w3-sidebar").remove();

        String[] arr = {
                "GoogleAnalyticsObject",
                "snhb",
                "sticky"
        };
        Elements jsElements = htmlDocument.select("script");
        for (Element js : jsElements) {
            for (String s : arr) {
                try {
                    if (js.html().contains(s)) js.remove();
                } catch (Exception ignored) {
                }
            }
        }

        htmlDocument.getElementsByAttributeValueContaining("src", "sncmp").remove();
        htmlDocument.getElementsByAttributeValueContaining("src", "snigelweb").remove();
        htmlDocument.getElementsByAttributeValueContaining("src", "snhb").remove();
        htmlDocument.getElementsByAttributeValueContaining("src", "w3schools_footer").remove();
        return htmlDocument;
    }

    @NotNull
    private static String getStyle() {
        StringBuilder stringBuilder = new StringBuilder();
        if (DayNight.getCurrentMode(App.getContext()).equals(DayNight.Mode.DAY)) {
            stringBuilder.append(FileReader.fromAssets("style.css"));
        } else stringBuilder.append(FileReader.fromAssets("style_dark.css"));
        stringBuilder.append("body{font-size:").append(getFontSize()).append("!important;}");
        stringBuilder.append("h1,h2{font-size:1.3em!important;}");
        stringBuilder.append("h3{font-size:1.2em!important;}");
        return stringBuilder.toString();
    }

    private static String getFontSize() {
        return Preferences.getFontSize();
    }

    @NotNull
    private static String getTranslatePlugin() {
        return FileReader.fromAssets("tt.html");
    }
}
