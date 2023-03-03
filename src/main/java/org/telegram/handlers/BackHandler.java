package org.telegram.handlers;

import org.springframework.stereotype.Component;
import org.telegram.handlers.utils.UserRequestHandler;
import org.telegram.models.dto.Product;
import org.telegram.models.UserRequest;
import org.telegram.models.UserSession;
import org.telegram.services.ProductService;
import org.telegram.services.TelegramService;
import org.telegram.services.UserSessionService;

@Component
public class BackHandler extends UserRequestHandler {

    private final TelegramService telegramService;
    private final ProductService productService;
    private final UserSessionService userSessionService;


    public BackHandler(TelegramService telegramService, ProductService productService, UserSessionService userSessionService) {
        this.telegramService = telegramService;
        this.productService = productService;
        this.userSessionService = userSessionService;
    }

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return isTextMessage(userRequest.getUpdate(), "Назад");
    }

    @Override
    public void handle(UserRequest userRequest) {
        String productName = userRequest.getUpdate().getMessage().getText();

        Product product = productService.getProductList().stream().filter(f -> f.getName().equals(productName)).findFirst().orElse(null);
        UserSession userSession = userRequest.getUserSession();

        telegramService.deleteMessage(userRequest.getChatId(), userRequest.getUserSession().getPaginationMessageId());

        telegramService.sendMessage(userRequest.getChatId(), userSession.getText(), productService.getCategoriesButtons());

        telegramService.deleteMessage(userRequest.getChatId(), userRequest.getUpdate().getMessage().getMessageId());

        userSessionService.saveSession(userSession.getChatId(), userSession);
    }

    @Override
    public boolean isGlobal() {
        return true;
    }
}
