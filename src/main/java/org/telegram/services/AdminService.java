package org.telegram.services;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdminService {

    public ReplyKeyboard getStartedButton() {

        KeyboardRow clearApplyButtons = new KeyboardRow();
        clearApplyButtons.add(KeyboardButton.builder()
                .text("Озерна звіт")
                .build());
        clearApplyButtons.add(KeyboardButton.builder()
                .text("Прибузька звіт")
                .build());

        KeyboardRow row = new KeyboardRow(clearApplyButtons);

        return getReplyKeyboardMarkup(List.of(row));
    }
    private static ReplyKeyboardMarkup getReplyKeyboardMarkup(List<KeyboardRow> keyboardRow) {

        return ReplyKeyboardMarkup.builder()
                .keyboard(keyboardRow)
                .selective(true)
                .resizeKeyboard(true)
                .oneTimeKeyboard(false)
                .build();
    }
}
