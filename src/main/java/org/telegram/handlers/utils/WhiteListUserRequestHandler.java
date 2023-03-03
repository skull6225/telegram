package org.telegram.handlers.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.models.UserRequest;
import org.telegram.models.dto.Category;
import org.telegram.models.dto.Product;
import org.telegram.repository.PlaceRepository;
import org.telegram.repository.WhiteListRepository;
import org.telegram.services.ProductService;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Objects;

public abstract class WhiteListUserRequestHandler {

  public abstract boolean isApplicable(UserRequest request);

  public abstract void handle(UserRequest dispatchRequest);

  public abstract boolean isGlobal();

  @Autowired
  WhiteListRepository whiteListRepository;

  public boolean isWhitelistUser(Update update) {
    return update.hasMessage() && update.getMessage() != null && update.getMessage().getFrom() != null
            && Objects.nonNull(whiteListRepository.findByName(update.getMessage().getFrom().getUserName()));
  }
}