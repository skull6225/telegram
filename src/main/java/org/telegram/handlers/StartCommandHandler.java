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
public class StartCommandHandler extends UserRequestHandler {


    private final TelegramService telegramService;
    private final ProductService productService;
    private final UserSessionService userSessionService;
    private final PlaceRepository placeRepository;


    public StartCommandHandler(TelegramService telegramService, ProductService productService,
                               UserSessionService userSessionService, PlaceRepository placeRepository) {
        this.telegramService = telegramService;
        this.productService = productService;
        this.userSessionService = userSessionService;
        this.placeRepository = placeRepository;
    }

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return isPlace(userRequest.getUpdate());
    }

    @Override
    public void handle(UserRequest request) {

        UserSession userSession = request.getUserSession();

        telegramService.sendMessage(request.getChatId(), "Оберіть категорію з меню нижче", productService.getCategoriesButtons());

        userSession.setPlace(placeRepository.findByName(request.getUpdate().getMessage().getText()));
        userSessionService.saveSession(userSession.getChatId(), userSession);

    }

    @Override
    public boolean isGlobal() {
        return true;
    }
}
