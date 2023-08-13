package ThisIsMyBot.MyOneBot.services;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

public interface IBuildSendMessageService {
    SendMessage createMessage(String chatID, String text, ReplyKeyboard mainMarkup);
}
