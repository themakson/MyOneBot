package ThisIsMyBot.MyOneBot.processors;

import ThisIsMyBot.MyOneBot.cahce.Cache;
import ThisIsMyBot.MyOneBot.entities.Position;
import ThisIsMyBot.MyOneBot.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

public abstract class Processor {

    abstract void handleText(Update update);

    abstract void handleCallBackQuery(CallbackQuery update);

    abstract void handleSaveToCache(Message message);

    private Cache<User> userCache;

    @Autowired
    public void setUserCache(Cache<User> userCache) {
        this.userCache = userCache;
    }

    public void direct(Update update) {
        //активная регистрация
        if (update.hasCallbackQuery()) {
            handleCallBackQuery(update.getCallbackQuery());
        } else {
            User userFromCache = userCache.findBy(update.getMessage().getChatId());
            if ((userFromCache != null) && !userFromCache.getPosition().equals(Position.NONE)) {
                switch (userFromCache.getPosition()) {
                    case PHONE_NUMBER:
                    case AGE:
                        handleSaveToCache(update.getMessage());
                        return; //так что это не перейдет к строкам if
                }
            }
        }
        if ((update.getMessage() != null) && (update.getMessage().getText() != null) && (update.getMessage().getText().equals("Временно сохраните мою информацию в кэше"))) {
            handleSaveToCache(update.getMessage());
        } else if ((update.getMessage() != null) && (update.getMessage().getText() != null)) {
            handleText(update);
        }
    }
}