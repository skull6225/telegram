package org.telegram.handlers;

import org.springframework.stereotype.Component;
import org.telegram.handlers.utils.UserRequestHandler;
import org.telegram.models.UserRequest;
import org.telegram.models.UserSession;
import org.telegram.repository.PlaceRepository;
import org.telegram.services.ProductService;
import org.telegram.services.TelegramService;
import org.telegram.services.UserSessionService;

@Component
public class StartHandler extends UserRequestHandler {

    private static String command = "/start";

    private final TelegramService telegramService;
    private final ProductService productService;
    private final UserSessionService userSessionService;
    private final PlaceRepository placeRepository;

    public StartHandler(ProductService productService, UserSessionService userSessionService,
                        PlaceRepository placeRepository, TelegramService telegramService) {
        this.productService = productService;
        this.userSessionService = userSessionService;
        this.placeRepository = placeRepository;
        this.telegramService = telegramService;
    }

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return isCommand(userRequest.getUpdate(), command);
    }

    @Override
    public void handle(UserRequest request) {

        UserSession userSession = UserSession.builder().build();
        placeRepository.findByName(request.getUpdate().getMessage().getText());
        userSessionService.saveSession(userSession.getChatId(), userSession);

        telegramService.sendMessage(request.getChatId(), "Оберіть категорію з меню нижче", productService.getStartedButton());

    }

    @Override
    public boolean isGlobal() {
        return true;
    }
}
