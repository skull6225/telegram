package org.telegram.handlers;

import org.springframework.stereotype.Component;
import org.telegram.handlers.utils.UserRequestHandler;
import org.telegram.models.UserRequest;
import org.telegram.models.UserSession;
import org.telegram.services.ProductService;
import org.telegram.services.TelegramService;
import org.telegram.services.UserSessionService;

@Component
public class ClearHandler extends UserRequestHandler {

    private final TelegramService telegramService;
    private final ProductService productService;
    private final UserSessionService userSessionService;


    public ClearHandler(TelegramService telegramService, ProductService productService, UserSessionService userSessionService) {
        this.telegramService = telegramService;
        this.productService = productService;
        this.userSessionService = userSessionService;
    }

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return isTextMessage(userRequest.getUpdate(), "Очистити");
    }

    @Override
    public void handle(UserRequest userRequest) {

        UserSession userSession = userRequest.getUserSession();

        telegramService.deleteMessage(userRequest.getChatId(), userRequest.getUserSession().getPaginationMessageId());

        telegramService.sendMessage(userRequest.getChatId(), "замовлення очищено", productService.getCategoriesButtons());

        userSession.setText(null);
        userSession.setPaginationMessageId(null);
        userSession.setProductsPrice(null);

        userSessionService.saveSession(userSession.getChatId(), userSession);
    }

    @Override
    public boolean isGlobal() {
        return true;
    }
}
