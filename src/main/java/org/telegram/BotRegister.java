package org.telegram;

import java.io.IOException;
import java.util.Objects;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.Dispatcher;
import org.telegram.models.UserRequest;
import org.telegram.models.UserSession;
import org.telegram.services.UserSessionService;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@Component
public class BotRegister extends TelegramLongPollingBot {

  @Value("${telegram.token}")
  private String botToken;

  @Value("${telegram.username}")
  private String botUsername;

  private final Dispatcher dispatcher;
  private final UserSessionService userSessionService;

  public BotRegister(Dispatcher dispatcher, UserSessionService userSessionService) {
    this.dispatcher = dispatcher;
    this.userSessionService = userSessionService;
  }

  @Override
  public void onUpdateReceived(Update update) {

    if (update.hasMessage() && update.getMessage().hasText()) {
      Long userId = update.getMessage().getFrom().getId();
      String userFirstName = update.getMessage().getFrom().getFirstName();
      String textFromUser = update.getMessage().getText();
      log.info("[{}, {}] : {}", userId, userFirstName, textFromUser);
    }

    if (update.getCallbackQuery() != null && update.getCallbackQuery().getMessage().hasText()) {
      Long userId = update.getCallbackQuery().getFrom().getId();
      String userFirstName = update.getCallbackQuery().getFrom().getFirstName();
      String textFromUser = update.getCallbackQuery().getData();
      log.info("[{}, {}] : {}", userId, userFirstName, textFromUser);
    }

    Long chatId;
    if (Objects.nonNull(update.getMessage())) {
      chatId = update.getMessage().getChatId();
    } else if (Objects.nonNull(update.getCallbackQuery())) {
      chatId = update.getCallbackQuery().getMessage().getChatId();
    } else if (Objects.nonNull(update.getPreCheckoutQuery())) {
      chatId = update.getPreCheckoutQuery().getFrom().getId();
    } else {
      chatId = update.getShippingQuery().getFrom().getId();
    }
    UserSession session = userSessionService.getSession(chatId);

    UserRequest userRequest = UserRequest
        .builder()
        .update(update)
        .userSession(session)
        .chatId(chatId)
        .build();

    boolean dispatched = dispatcher.dispatch(userRequest);

    if (!dispatched) {
      log.warn("Unexpected update from user");
    }
  }


  @Override
  public String getBotUsername() {
    // username which you give to your bot bia BotFather (without @)
    return botUsername;
  }

  @Override
  public String getBotToken() {
    // do not expose the token to the repository,
    // always provide it externally(for example as environmental variable)
    return botToken;
  }
}
