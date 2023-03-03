package org.telegram.services.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;

@Slf4j
@Component
public class HelpBotSender extends DefaultAbsSender {

  @Value("${telegram.token}")
  private String botToken;

  protected HelpBotSender() {
    super(new DefaultBotOptions());
  }

  @Override
  public String getBotToken() {
    return botToken;
  }
}
