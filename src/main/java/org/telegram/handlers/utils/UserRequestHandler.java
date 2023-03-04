package org.telegram.handlers.utils;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.models.dto.Category;
import org.telegram.models.dto.Product;
import org.telegram.models.UserRequest;
import org.telegram.repository.PlaceRepository;
import org.telegram.services.ProductService;
import org.telegram.telegrambots.meta.api.objects.Update;

public abstract class UserRequestHandler {

  public abstract boolean isApplicable(UserRequest request);

  public abstract void handle(UserRequest dispatchRequest);

  public abstract boolean isGlobal();

  @Autowired
  ProductService productService;
  @Autowired
  PlaceRepository placeRepository;

  public boolean isCommand(Update update, String command) {
    return update.hasMessage() && update.getMessage().isCommand()
        && update.getMessage().getText().equals(command);
  }

  public boolean isCategory(Update update) {

    for (Category category : productService.getCategoryList()) {
      if (update.hasMessage() && update.getMessage().hasText() &&
              update.getMessage().getText().equals(category.getName())) {
        return true;
      }
    }
    return false;
  }

  public boolean isProduct(Update update) {

    for (Product product : productService.getProductList()) {
      if (update.hasMessage() && update.getMessage().hasText() &&
              update.getMessage().getText().equals(product.getName())) {
        return true;
      }
    }
    return false;
  }

  public boolean isPlace(Update update) {
    return update.hasMessage() && update.getMessage().hasText() && (
            Objects.nonNull(placeRepository.findByName(update.getMessage().getText())));
  }

  public boolean isReport(Update update, boolean isAdmin) {
    return update.hasMessage() && update.getMessage().hasText() && isAdmin && (
            update.getMessage().getText().equals("Озерна звіт") || update.getMessage().getText().equals("Прибузька звіт"));
  }

  public boolean isTextMessage(Update update, String text) {
    return update.hasMessage() && update.getMessage().hasText() &&
        update.getMessage().getText().equals(text);
  }

  public boolean isCallbackQuery(Update update, String text) {
    return Objects.isNull(update.getMessage()) && Objects.nonNull(update.getCallbackQuery()) &&
        update.getCallbackQuery().getData().contains(text);
  }

  public boolean isPreCheckoutQuery(Update update) {
    return Objects.nonNull(update.getPreCheckoutQuery());
  }

  public boolean isShippingQuery(Update update) {
    return Objects.nonNull(update.getShippingQuery());
  }

  public boolean isSuccessCheckoutQuery(Update update) {
    return Objects.nonNull(update.getMessage().getSuccessfulPayment());
  }

  public boolean isPhotoMessage(Update update) {
    return update.hasMessage() && Objects.nonNull(update.getMessage().getPhoto()) &&
        !update.getMessage().getPhoto().isEmpty();
  }
}