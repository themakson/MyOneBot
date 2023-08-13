package ThisIsMyBot.MyOneBot.handlers;

import ThisIsMyBot.MyOneBot.sendmessage.MessageSender;
import ThisIsMyBot.MyOneBot.services.BuildInlineButtonsService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.*;


@Component
public class CallBackQueryHandler implements Handler<CallbackQuery> {
    private final MessageSender messageSender;
    private final BuildInlineButtonsService buildInlineButtonsService; //тестирование встроенных кнопок
    private List<String> jokes;
    private int prevNumber = -1;

    public CallBackQueryHandler(@Lazy MessageSender messageSender, BuildInlineButtonsService buildInlineButtonsService) {
        this.messageSender = messageSender;
        this.buildInlineButtonsService = buildInlineButtonsService;
    }

    @Override
    public void handle(CallbackQuery inlineButtonPressed) {
        if (inlineButtonPressed.getData().equals("next_action")) {

            var editMessageText = EditMessageText.builder()
                    .text(randomJoke())
                    .chatId(inlineButtonPressed.getMessage().getChatId().toString())
                    .messageId(inlineButtonPressed.getMessage().getMessageId())
                    .replyMarkup(buildInlineButtonsService.build())
                    .build();

            messageSender.editMessageSend(editMessageText);
        }
    }

    private String randomJoke() {
        jokes = new ArrayList<>(Arrays.asList(

                " Черный юмор-как еда в Африке.\n\n"
                        + " не до каждого доходит",

                "Медведь чудом выбрался из машины,\n"
                        + "но вспомнил что чудес не бывает,\n"
                        + "сел обратно в машину и сгорел.",

                "В семье каннибалов умер отец\n\n"
                        + "Но всеравно остался кормильцем",

                "Евреи подарили миру множество\n"
                + "научных открытий. Но поскольку\n"
                + "евреи ничего бесплатного не дарят,\n"
                + "для них придумали Нобелевскую премию",

                "Что будет с вейпером, если он упадет с самолета?\n\n"
                + "-Он начнет парить в воздухе",

                "Научил я как то попугая 'Спасибо' говорить\n\n"
                + "Он был благодарен",

                "-а давайте поиграем в Римских императоров?\n"
                + "-меня тогда зовут Ленус\n"
                + "-а меня Дашус\n"
                + "-а я не буду играть!\n"
                + "-Ань, ты куда?",

                "Кто всегда работает только на удаленке?\n"
                + "-Снайпер.",

                "Как называются гвозди с суицидальными\n"
                + "наклонностями?\n\n"
                + "-Саморезы",

                "Митинг косоглазых состоялся на\n"
                + "сорок метров левее здания\n"
                + "городской администрации\n"
                + "P.S(из комментариев:) А главное что у каждого своя точка зрения",
//10
                "-Знаеш почему нельзя играть с\n"
                + "мухами в карты, особенно на деньги?\n\n"
                + "Потому что они мухлюют" +"\uD83D\uDE43",

                "Девочка, которой в детсве говорили,\n"
                + "что за ней все будут бегать выросла и\n"
                + "стала водиетелем автобуса" +"\uD83D\uDE8C" +"\uD83E\uDD72",

                "Похоронил пацанчик друга своего\n"
                + "захотел с горя покурить, а сигарет\n"
                + "нет. Приходит в табачный магазин, а\n"
                + "с порога:\n"
                + "- Кента нет.",

                "Он: я приседаю с весом 25 раз.\n"
                + "Я, которая присела с весом на 10 лет!\n",

                "-Извините, а почему в вашем\n"
                + "строительном магазине только\n"
                + "китайские рулеточки?\n"
                + "-Знаете, у нас раньше были в\n"
                + "продаже и русские рулетки, но\n"
                + "покупатели их как то побаивались.",

                "Однажды, ковры больше не\n"
                + "захотели летать.\n"
                + "С тех пор люди периодически бьют\n"
                + "их палками в надежде, что магия\n"
                + "вернется",

                "Черепашки-ниндзя нападали\n"
                + "в четвером на одного, потому что у\n"
                + "них тренер был крыса\n",

                "ак называют человека, который\n"
                + "продал свою печень?\n\n"
                + "Обеспеченный",

                "купил однажды физрук шляпу, а она\n"
                + "ему как раз два три раз два три\n"
                +"закончили\n",

                "как то раз каннибал решил стать\n"
                + "вегетарианцем, на следущий день\n"
                + "он съел садовника\n",
//20
                "Идет наркоман, шатается. Вдруг чувствует сильынй удар\n"
                + "в спину. Оборачивается, а это асфальт.",

                "Стоит Мужик, собрался прыгать\n"
                + "с крыши. В это время около окна\n"
                + "стоит голая девушка.\n\n"
                + "Мужик летит\n"
                + "-АААААА, Ух ты!, ААААААААА",

                "Выходит мужик на балкон, а балкона\n"
                + "нет\n" + "\uD83D\uDDFF",

                "сидят два нарика обкуренные и\n"
                + "смотрят на куру гриль.\n"
                + "наконец один выдает: это курица?\n"
                + "второй ему отвечает: нет, это\n"
                + "хаваеца",

                "Несут два грузина медведя.\n"
                + "Увидел их другой грузин и\n"
                + "спрашивает: 'Гризли?'\n"
                + "-Нэт, так задущилы!!!\n",

                "покупатель говорит продавцу:\n"
                + "- 'яйцо отборное' у кого\n"
                + "отобрали то?\n"
                + "продавец отвечает:\n"
                + "-как у кого? у кур,разумеется.\n"
                + "-а они что,не сопротивлялись\n?"
                + "-те, кто сопротивлялись в\n"
                + "другом отделе.",

                "в библиотеке:\n"
                + "-Где я могу найти книги о\n"
                + "самоубийствах?\n"
                + "-На пятой полке слева.\n"
                + "-Но там нет ни одной книги.\n"
                + "Да,их почему то не возвращают...",

                "как называется домик на кладбище у\n"
                + "охраника? -живой уголок..\n",

                "пуля производит\n"
                + "удивительные изменения\n"
                + "в голове,даже если\n"
                + "попадет в задницу\n",

                "Рожу, трех девочек. Веру,надежду\n"
                + "любовь. И когда они вырастут\n"
                + "будем прыгать с парашютом, и это\n"
                + "будет прыжок с верой, надеждой и\n"
                + "любовью" + "\uD83D\uDC97"
//30

                ));
        int randNumb = (int) (Math.random() * jokes.size());
        while(randNumb == prevNumber) {
            randNumb = (int) (Math.random() * jokes.size());
        }
        prevNumber = randNumb;

        return jokes.get(randNumb);
    }

}