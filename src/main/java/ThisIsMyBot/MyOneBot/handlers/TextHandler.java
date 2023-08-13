package ThisIsMyBot.MyOneBot.handlers;

import ThisIsMyBot.MyOneBot.cahce.Cache;
import ThisIsMyBot.MyOneBot.cahce.UserCache;
import ThisIsMyBot.MyOneBot.entities.User;
import ThisIsMyBot.MyOneBot.sendmessage.MessageSender;
import ThisIsMyBot.MyOneBot.services.BuildButtonsService;
import ThisIsMyBot.MyOneBot.services.BuildInlineButtonsService;
import ThisIsMyBot.MyOneBot.services.BuildSendMessageService;
import com.vdurmont.emoji.EmojiParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;


@Component
public class TextHandler implements Handler<Message> {
    private final MessageSender messageSender;
    private final BuildSendMessageService buildSendMessageService;
    private final BuildInlineButtonsService buildInlineButtonsService; //тестирование встроенных кнопок
    private final BuildButtonsService buildButtonsService;

    private final Cache<User> userCache;

    @Autowired
    public TextHandler(@Lazy MessageSender messageSender, BuildSendMessageService buildSendMessageService, BuildInlineButtonsService buildInlineButtonsService, @Lazy BuildButtonsService buildButtonsService, UserCache userCache) {
        this.messageSender = messageSender;
        this.buildSendMessageService = buildSendMessageService;
        this.buildInlineButtonsService = buildInlineButtonsService;
        this.buildButtonsService = buildButtonsService;
        this.userCache = userCache;
    }

    @Override
    public void handle(Message message) {
        if (message.getText().equals("/start")) {
            buildButtonsService.beforeRegistrationButtons();
            messageSender.messageSend(buildSendMessageService.createMessage(message.getChatId().toString(), "Ура! Вы только что запустили этого бота!", buildButtonsService.getMainMarkup()));
        } else if (message.getText().equals("Я хочу шуточку")) {
            var sendMessage = SendMessage.builder()
                    .text("Вы готовы к моей подборке самых веселых шуток??\nЕсли да, то нажмите кнопку ниже!")
                    .chatId(message.getChatId().toString())
                    .build();
            sendMessage.setReplyMarkup(buildInlineButtonsService.build());
            messageSender.messageSend(sendMessage);
        } else if (message.getText().equals("Привет, бот")) {
            String smile = EmojiParser.parseToUnicode(":smiley:");
            messageSender.messageSend(buildSendMessageService.createMessage(message.getChatId().toString(), "Привет, " + message.getChat().getFirstName() + " " + smile, buildButtonsService.getMainMarkup()));
        } else if (message.getText().equals("Посмотреть мои данные")) {
            User userFromCache = userCache.findBy(message.getChatId());
            messageSender.messageSend(buildSendMessageService.createMessage(message.getChatId().toString(), userFromCache.toString(), buildButtonsService.getMainMarkup()));
        } else if (message.getText().equals("Удалите мои данные")) {
            buildButtonsService.beforeRegistrationButtons();
            userCache.remove(message.getChatId());
            messageSender.messageSend(buildSendMessageService.createMessage(message.getChatId().toString(), "Все данные о вас были удалены", buildButtonsService.getMainMarkup()));
        } else {
            var sendMsg = new SendMessage(message.getChatId().toString(), "Я вас не понял. Попробуйте нажать / написать что-нибудь еще");
            messageSender.messageSend(sendMsg);
        }
    }
}