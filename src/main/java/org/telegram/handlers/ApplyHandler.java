package org.telegram.handlers;

import org.springframework.stereotype.Component;
import org.telegram.handlers.utils.UserRequestHandler;
import org.telegram.models.UserRequest;
import org.telegram.models.UserSession;
import org.telegram.models.dto.Sales;
import org.telegram.repository.SalesRepository;
import org.telegram.services.ProductService;
import org.telegram.services.TelegramService;
import org.telegram.services.UserSessionService;
import org.telegram.transformer.Transformer;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ApplyHandler extends UserRequestHandler {

    private final TelegramService telegramService;
    private final ProductService productService;
    private final UserSessionService userSessionService;
    private final Transformer transformer;
    private final SalesRepository salesRepository;


    public ApplyHandler(TelegramService telegramService, ProductService productService, UserSessionService userSessionService, Transformer transformer, SalesRepository salesRepository) {
        this.telegramService = telegramService;
        this.productService = productService;
        this.userSessionService = userSessionService;
        this.transformer = transformer;
        this.salesRepository = salesRepository;
    }

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return isTextMessage(userRequest.getUpdate(), "Підтвердити");
    }

    @Override
    public void handle(UserRequest userRequest) {
        UserSession userSession = userRequest.getUserSession();

        telegramService.deleteMessage(userRequest.getChatId(), userRequest.getUserSession().getPaginationMessageId());

        List<Sales> salesList = userSession.getProductsPrice().values().stream().parallel().map(m -> transformer.transformProductToSales(m, userSession.getPlace())).collect(Collectors.toList());

        salesRepository.saveAll(salesList);

        telegramService.sendMessage(userRequest.getChatId(), "Замовлення успішне", productService.getCategoriesButtons());

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
