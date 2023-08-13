package ThisIsMyBot.MyOneBot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.Collections;

@Service
public class BuildButtonsService {
    private final ArrayList<KeyboardRow> arrayOfKeyboardRows;
    private final ReplyKeyboardMarkup mainMarkup;
    ReplyKeyboardRemove replyKeyboardRemove ; //удаляет клавиатуру с номером телефона

    public ReplyKeyboardMarkup getMainMarkup() {
        return mainMarkup;
    }

    @Autowired
    public BuildButtonsService() {
        this.arrayOfKeyboardRows = new ArrayList<>();
        this.mainMarkup = new ReplyKeyboardMarkup();
        this.replyKeyboardRemove = new ReplyKeyboardRemove(Boolean.TRUE);

        //украшает пуговицы
        this.mainMarkup.setKeyboard(arrayOfKeyboardRows);
        this.mainMarkup.setResizeKeyboard(true);
    }

    public void beforeRegistrationButtons() {
        arrayOfKeyboardRows.clear();

        var row2 = new KeyboardRow();
        var button3 = new KeyboardButton("Временно сохраните мою информацию в кэше");
        row2.add(button3);

        Collections.addAll(arrayOfKeyboardRows, createCommonButtonsRow(), row2);
    }


    //срабатывает, если мы регистрируем нового пользователя
    public void addingPhoneNumberButton() {
        arrayOfKeyboardRows.clear();

        var phoneNumberButton = KeyboardButton.builder().text("Номер телефона").requestContact(Boolean.TRUE).build();
        var declineSharingPhoneNumber = KeyboardButton.builder().text("Я не хочу разглашать номер телефона").build();
        var row1 = new KeyboardRow();
        Collections.addAll(row1, phoneNumberButton, declineSharingPhoneNumber);

        arrayOfKeyboardRows.add(row1);
    }

    public void afterRegistrationButtons() {
        arrayOfKeyboardRows.clear();
        var row2 = new KeyboardRow();
        var button3 = new KeyboardButton("Посмотреть мои данные");
        var button4 = new KeyboardButton("Удалите мои данные");
        row2.add(button3);
        row2.add(button4);

        Collections.addAll(arrayOfKeyboardRows, createCommonButtonsRow(), row2);
    }


    //эти кнопки используются до и после регистрации
    private KeyboardRow createCommonButtonsRow() {
        var row1 = new KeyboardRow();
        row1.add("Я хочу шуточку");
        row1.add("Привет, бот");
        return row1;
    }
}