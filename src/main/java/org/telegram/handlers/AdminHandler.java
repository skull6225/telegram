package org.telegram.handlers;

import org.springframework.stereotype.Component;
import org.telegram.handlers.utils.UserRequestHandler;
import org.telegram.models.UserRequest;
import org.telegram.models.UserSession;
import org.telegram.models.dto.Sales;
import org.telegram.models.dto.WhiteList;
import org.telegram.repository.WhiteListRepository;
import org.telegram.services.AdminService;
import org.telegram.services.ProductService;
import org.telegram.services.TelegramService;
import org.telegram.services.UserSessionService;

import java.util.List;
import java.util.Objects;

@Component
public class AdminHandler extends UserRequestHandler {

    private final TelegramService telegramService;
    private final ProductService productService;
    private final AdminService adminService;
    private final UserSessionService userSessionService;
    private final WhiteListRepository whiteListRepository;


    public AdminHandler(TelegramService telegramService, ProductService productService,
                        AdminService adminService, UserSessionService userSessionService,
                        WhiteListRepository whiteListRepository) {
        this.telegramService = telegramService;
        this.productService = productService;
        this.adminService = adminService;
        this.userSessionService = userSessionService;
        this.whiteListRepository = whiteListRepository;
    }

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return isTextMessage(userRequest.getUpdate(), "Адмін");
    }

    @Override
    public void handle(UserRequest request) {
        UserSession userSession = request.getUserSession();
        WhiteList name = whiteListRepository.findByName(request.getUpdate().getMessage().getFrom().getUserName());

        if (Objects.isNull(name) || (Objects.nonNull(name.getRole()) && !name.getRole().equals("admin"))) {
            telegramService.sendMessage(request.getChatId(), "У вас нема прав доступу", productService.getStartedButton());
            userSession.setAdmin(false);
        } else {
            telegramService.sendMessage(request.getChatId(), "Вітаю, обери з якої точки хочеш отримати звіт", adminService.getStartedButton());
            userSession.setAdmin(true);
            userSessionService.saveSession(userSession.getChatId(), userSession);

        }
    }

    private static String getMenuMessage(List<Sales> sales) {
        StringBuilder productsInfo = new StringBuilder();
        int totalPrice = 0;
        int totalBasePrice = 0;

        for (Sales product : sales) {
            productsInfo.append(product.getName());
            productsInfo.append("\t\t\tКількість: ");
            productsInfo.append(product.getCount());
            productsInfo.append("\tЦіна: ");
            productsInfo.append(product.getPrice());
            productsInfo.append(" грн");


            productsInfo.append("\n\n");
            totalPrice = totalPrice + product.getPrice();
            totalBasePrice = totalBasePrice + product.getBasePrice();
        }
        productsInfo.append("\n");

        productsInfo.append("Загальна сума базової ціни: ");
        productsInfo.append(totalPrice);
        productsInfo.append("\n");

        productsInfo.append("Загальна сума: ");
        productsInfo.append(totalBasePrice);
        productsInfo.append("\n");

        productsInfo.append("Чистяк: ");
        productsInfo.append(totalPrice - totalBasePrice);

        return productsInfo.toString();
    }


    @Override
    public boolean isGlobal() {
        return true;
    }
}
