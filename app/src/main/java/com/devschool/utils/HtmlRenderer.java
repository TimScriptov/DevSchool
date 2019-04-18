package com.devschool.utils;

import com.devschool.App;
import com.devschool.data.Preferences;
import com.devschool.module.DayNight;

import org.jetbrains.annotations.NotNull;

public class HtmlRenderer {

    public static String renderHtml(String html) {
        return html.replace("<head>", "<head>" + getStyle() + (Preferences.translatePluginEnabled() ? getTranslatePlugin() : ""));
    }

    @NotNull
    private static String getStyle() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<style>");

        if (DayNight.getCurrentMode(App.getContext()).equals(DayNight.Mode.DAY)) {
            stringBuilder.append(FileReader.fromAssets("style.css"));
        } else stringBuilder.append(FileReader.fromAssets("dark_style.css"));

        stringBuilder
                .append("*,h1,h2,h3,h4,h5h,h6,p,li,table,pre,code,abbr,span,.w3-example{font-size:").append(getFontSize()).append(";}")
                .append("</style>");

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
