package org.telegram.handlers;

import org.springframework.stereotype.Component;
import org.telegram.handlers.utils.WhiteListUserRequestHandler;
import org.telegram.models.UserRequest;
import org.telegram.services.TelegramService;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Component
public class WhiteListHandler extends WhiteListUserRequestHandler {


    private final TelegramService telegramService;

    public WhiteListHandler(TelegramService telegramService) {
        this.telegramService = telegramService;
    }

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return isWhitelistUser(userRequest.getUpdate());
    }

    @Override
    public void handle(UserRequest request) {

        List<KeyboardButton> buttons = new ArrayList<>();
        buttons.add(new KeyboardButton("/start"));
        KeyboardRow row = new KeyboardRow(buttons);

        telegramService.sendMessage(request.getChatId(), "Тут закрита вечірка, у вас нема доступу", ReplyKeyboardMarkup.builder()
                .keyboard(List.of(row))
                .selective(true)
                .resizeKeyboard(true)
                .oneTimeKeyboard(false)
                .build());

    }

    @Override
    public boolean isGlobal() {
        return true;
    }
}
