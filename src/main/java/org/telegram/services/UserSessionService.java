package org.telegram.services;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;
import org.telegram.models.UserSession;

@Component
public class UserSessionService {

  private Map<Long, UserSession> userSessionMap = new HashMap<>();

  public UserSession getSession(Long chatId) {
    return userSessionMap.getOrDefault(chatId, UserSession
        .builder()
        .chatId(chatId)
        .build());
  }

  public UserSession saveSession(Long chatId, UserSession session) {
    return userSessionMap.put(chatId, session);
  }
}
