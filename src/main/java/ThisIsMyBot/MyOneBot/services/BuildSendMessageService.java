package ThisIsMyBot.MyOneBot.services;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

@Service
public class BuildSendMessageService implements IBuildSendMessageService {

    public SendMessage createMessage(String chatID, String text, ReplyKeyboard mainMarkup) {
        SendMessage sendThisMessage = new SendMessage();
        sendThisMessage.setChatId(chatID);
        sendThisMessage.setReplyMarkup(mainMarkup);
        sendThisMessage.setText(text);
        return sendThisMessage;
    }
}