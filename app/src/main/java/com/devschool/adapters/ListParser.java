package com.devschool.adapters;

import com.devschool.App;
import com.devschool.R;
import com.devschool.data.Constants;
import com.devschool.view.MainView;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class ListParser {
    private ArrayList<String> itemsText = new ArrayList<>();
    private ArrayList<String> itemsSrc = new ArrayList<>();
    private MainView mainView;

    public ListParser(Type type, MainView mainView) {
        this.mainView = mainView;
        if (itemsText.size() == 0) {
            try {
                int raw_id = 0;
                switch (type) {
                    case HTML:
                        raw_id = R.raw.html_items;
                        break;
                    case CSS:
                        raw_id = R.raw.css_items;
                }
                InputStream is = App.getContext().getResources().openRawResource(raw_id);
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = dbf.newDocumentBuilder();
                Document doc = builder.parse(is);
                is.close();
                NodeList items = doc.getElementsByTagName("item");
                for (int x = 0; x < items.getLength(); x++) {
                    this.itemsText.add(items.item(x).getTextContent());
                    itemsSrc.add(Constants.SERVER + items.item(x).getAttributes().getNamedItem("src").getNodeValue());
                }
            } catch (Exception ignored) {
            }
        }
    }

    public ListAdapter getListAdapter() {
        return new ListAdapter(itemsText, itemsSrc, mainView);
    }

    public enum Type {
        HTML, CSS
    }
}
