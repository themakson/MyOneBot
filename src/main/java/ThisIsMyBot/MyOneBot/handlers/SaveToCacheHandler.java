package ThisIsMyBot.MyOneBot.handlers;

import ThisIsMyBot.MyOneBot.entities.Position;

import ThisIsMyBot.MyOneBot.cahce.Cache;
import ThisIsMyBot.MyOneBot.entities.User;
import ThisIsMyBot.MyOneBot.sendmessage.MessageSender;
import ThisIsMyBot.MyOneBot.services.BuildButtonsService;
import ThisIsMyBot.MyOneBot.services.IBuildSendMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;


@Slf4j
@Component
public class SaveToCacheHandler implements Handler<Message> {
    private final IBuildSendMessageService ibuildSendMessageService;
    private final Cache<User> userCache;
    private final MessageSender messageSender;
    private final BuildButtonsService buildButtonsService;

    @Autowired
    public SaveToCacheHandler(Cache<User> userCache, MessageSender messageSender, BuildButtonsService buildButtonsService, IBuildSendMessageService ibuildSendMessageService) {
        this.ibuildSendMessageService = ibuildSendMessageService;
        this.userCache = userCache;
        this.messageSender = messageSender;
        this.buildButtonsService = buildButtonsService;
    }

    private User generateDefaultUserInformationFromMessage(Message message) {
        User newUser = new User(message.getChatId(), message.getFrom().getUserName(),
                message.getFrom().getFirstName(), Position.PHONE_NUMBER);
        buildButtonsService.addingPhoneNumberButton(); //кнопка добавления номера телефона
        messageSender.messageSend(ibuildSendMessageService.createMessage(message.getChatId().toString(), "Пожалуйста, нажмите на кнопку \"Номер телефона\".", buildButtonsService.getMainMarkup()));
        return newUser;
    }

    private void registerRestUserData(User user, Message message) {
        switch (user.getPosition()) {
            case PHONE_NUMBER: //фаза 1
                if (message.hasContact()) {
                    user.setPhoneNumber(message.getContact().getPhoneNumber());
                    log.debug("\nУ этого пользователя вот такой номер телефона: " + message.getContact().getPhoneNumber().toString());
                    messageSender.messageSend(ibuildSendMessageService.createMessage(message.getChatId().toString(), "Пожалуйста, введите свой возраст в этом чате (0-99)", new ReplyKeyboardRemove(Boolean.TRUE)));
                    user.setPosition(Position.AGE);
                } else if (message.getText().equals("Я не хочу разглашать номер телефона")) {
                    ReplyKeyboardRemove replyKeyboardRemove = new ReplyKeyboardRemove(); //удаляет клавиатуру с номером телефона
                    replyKeyboardRemove.setRemoveKeyboard(true);
                    messageSender.messageSend(ibuildSendMessageService.createMessage(message.getChatId().toString(), "Пожалуйста, введите свой возраст в этом чате (0-99)", replyKeyboardRemove));
                    user.setPosition(Position.AGE);
                } else {
                    var newMessage = SendMessage.builder().
                            text("Вы не поделились своим номером телефона!").
                            chatId(message.getChatId().toString()).build();
                    messageSender.messageSend(newMessage);

                    buildButtonsService.addingPhoneNumberButton();
                }
                break;
            case AGE:
                if (message.getText().matches("\\d{1,2}")) {
                    user.setAge(message.getText());
                    user.setPosition(Position.NONE);
                    buildButtonsService.afterRegistrationButtons();
                    messageSender.messageSend(ibuildSendMessageService.createMessage(message.getChatId().toString(), "ok", buildButtonsService.getMainMarkup()));
                    messageSender.messageSend(ibuildSendMessageService.createMessage(message.getChatId().toString(), "Ваши данные были сохранены в кэш.", buildButtonsService.getMainMarkup()));
                    log.debug("\nВот этому пользователю вот столько лет: " + message.getText().toString());

                } else {
                    SendMessage newMessage = new SendMessage();
                    newMessage.setText("Пожалуйста, введите цифру от 0 до 99");
                    newMessage.setChatId(user.getId().toString());

                    messageSender.messageSend(newMessage);
                }
                System.out.println(user);
                break;
        }
    }

    //точка входа
    @Override
    public void handle(Message message) {
        //если пользователь не найден в реестре (кэше), запустите регистрацию нового пользователя
        User userFromCache = userCache.findBy(message.getChatId());
        if (userFromCache == null) {
            User newUser = generateDefaultUserInformationFromMessage(message);
            userCache.add(newUser);
        } else if (userFromCache.getPosition() == Position.NONE) {
            messageSender.messageSend(new SendMessage(message.getChatId().toString(), "Эй. Вы уже находитесь в системе." +
                    " Вместо того чтобы дублировать свои данные, сделайте что-нибудь полезное в своей жизни"));
        } else {
            registerRestUserData(userFromCache, message);
        }
    }
}
