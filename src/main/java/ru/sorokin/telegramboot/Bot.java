package ru.sorokin.telegramboot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;

public class Bot extends TelegramLongPollingBot {
    private final String BOT_NAME = System.getenv("BOT_NAME");
    private final String BOT_TOKEN = System.getenv("BOT_TOKEN");
    Storage storage;
    ReplyKeyboardMarkup replyKeyboardMarkup;

    public Bot() {
        this.storage = new Storage();
        initKeyboard();
    }

    private void initKeyboard() {
        //Создаем объект будущей клавиатуры и выставляем нужные настройки
        replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true); //подгоняем размер
        replyKeyboardMarkup.setOneTimeKeyboard(false); //скрываем после использования
        //Создаем список с рядами кнопок
        ArrayList<KeyboardRow> keyboardRows = new ArrayList<>();
        //Создаем один ряд кнопок и добавляем его в список
        KeyboardRow keyboardRow = new KeyboardRow();
        KeyboardRow keyboardRow2 = new KeyboardRow();
        keyboardRows.add(keyboardRow);
        keyboardRows.add(keyboardRow2);
        //Добавляем одну кнопку с текстом "Просвети" наш ряд
        keyboardRow.add(new KeyboardButton("Просвети"));
        keyboardRow.add(new KeyboardButton("Или"));
        keyboardRow2.add(new KeyboardButton("1"));
        //добавляем лист с одним рядом кнопок в главный объект
        replyKeyboardMarkup.setKeyboard(keyboardRows);
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            if (update.hasMessage() && update.getMessage().hasText()) {
                Message inMassega = update.getMessage();
                String chatId = inMassega.getChatId().toString();
                String response = parseMessage(inMassega.getText());
                SendMessage outMessage = new SendMessage();
                outMessage.setChatId(chatId);
                outMessage.setText(response);
                outMessage.setReplyMarkup(replyKeyboardMarkup);
                execute(outMessage);
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private String parseMessage(String text) {
        String response;
        if (text.equals("/start")) {
            response = "Приветствую, бот знает много цитат. Жми /get, чтобы получить случайную из них";
        } else if (text.equals("/get") || text.equals("Просвети")) {
            response = storage.getRandQuote();
        } else if (text.equals("Или")) {
            response = "Что сразу \"ИЛИ\" просвещаться не хотим \n;)\n";
        } else if (text.equals("1")) {
            response = storage.getQuoteList().get(0);
        } else {
            response = "Сообщение не распознано";
        }
        return response;
    }

    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

}
