package ru.sorokin.telegramboot;

import lombok.Getter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Getter
public class Storage {
    private List<String> quoteList;
    private String uri = "https://citatnica.ru/citaty/mudrye-tsitaty-velikih-lyudej";
    private String className = "su-note-inner su-u-clearfix su-u-trim";

    public Storage() {
        this.quoteList = new ArrayList<>();
        parser(uri, className);
    }

    public String getRandQuote() {
        int randomValue = (int) (Math.random() * quoteList.size());
        return quoteList.get(randomValue);
    }

    private void parser(String uri, String className) {
        Document document = null;
        try {
            document = Jsoup.connect(uri).maxBodySize(0).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Elements elementsQuote = document.getElementsByClass(className);
        elementsQuote.forEach(element -> quoteList.add(element.text()));
    }
}
