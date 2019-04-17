package com.devschool.utils;

import com.devschool.App;
import com.devschool.data.Preferences;
import com.devschool.module.DayNight;

import org.jetbrains.annotations.NotNull;

public class HtmlRenderer {

    public String renderHtml(String html) {
        return html.replace("<head>", "<head>" + getStyle() + (Preferences.translatePluginEnabled() ? getTranslatePlugin() : ""));
    }

    @NotNull
    private String getStyle() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<style>");

        if (DayNight.getCurrentMode(App.getContext()).equals(DayNight.Mode.DAY)) {
            stringBuilder.append(FileReader.fromAssets("style.css"));
        } else stringBuilder.append(FileReader.fromAssets("dark_style.css"));

        stringBuilder
                .append("* {font-size:").append(getFontSize()).append(";}")
                .append("</style>");

        return stringBuilder.toString();
    }

    private String getFontSize() {
        return Preferences.getFontSize();
    }

    @NotNull
    private String getTranslatePlugin() {
        return FileReader.fromAssets("tt.html");
    }
}
