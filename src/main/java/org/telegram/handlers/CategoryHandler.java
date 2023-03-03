package org.telegram.handlers;

import org.springframework.stereotype.Component;
import org.telegram.handlers.utils.UserRequestHandler;
import org.telegram.models.UserRequest;
import org.telegram.models.UserSession;
import org.telegram.repository.CategoryRepository;
import org.telegram.services.ProductService;
import org.telegram.services.TelegramService;
import org.telegram.services.UserSessionService;

@Component
public class CategoryHandler extends UserRequestHandler {

    private final TelegramService telegramService;
    private final ProductService productService;
    private final UserSessionService userSessionService;
    private final CategoryRepository categoryRepository;


    public CategoryHandler(TelegramService telegramService, ProductService productService,
                           UserSessionService userSessionService, CategoryRepository categoryRepository) {
        this.telegramService = telegramService;
        this.productService = productService;
        this.userSessionService = userSessionService;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return isCategory(userRequest.getUpdate());
    }

    @Override
    public void handle(UserRequest userRequest) {
        UserSession userSession = userRequest.getUserSession();
        String categoryName = userRequest.getUpdate().getMessage().getText();

        telegramService.sendMessage(userRequest.getChatId(),
                "Якщо щось наклацали не так то очистіть замовлення, якщо все гуд натискайте Підтвердити",
                productService.getProductButtons(categoryRepository.findByName(categoryName), userSession.getPlace()));

        if (userRequest.getUserSession().getText() == null) {
            telegramService.sendMessage(userRequest.getChatId(), "Яку каву бажаєте?");
        } else {
            telegramService.sendMessage(userRequest.getChatId(), userRequest.getUserSession().getText());
        }

        userSession.setPaginationMessageId(userRequest.getUpdate().getMessage().getMessageId() + 2);
        userSessionService.saveSession(userSession.getChatId(), userSession);
    }

    @Override
    public boolean isGlobal() {
        return true;
    }
}
