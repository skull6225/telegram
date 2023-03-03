package org.telegram.handlers;

import org.springframework.stereotype.Component;
import org.telegram.handlers.utils.UserRequestHandler;
import org.telegram.models.UserRequest;
import org.telegram.models.UserSession;
import org.telegram.models.dto.Product;
import org.telegram.models.dto.ProductPrice;
import org.telegram.repository.ProductPriceRepository;
import org.telegram.repository.ProductRepository;
import org.telegram.services.TelegramService;
import org.telegram.services.UserSessionService;

import java.util.HashMap;
import java.util.Map;

@Component
public class ProductHandler extends UserRequestHandler {

    private final TelegramService telegramService;
    private final UserSessionService userSessionService;
    private final ProductRepository productRepository;
    private final ProductPriceRepository productPriceRepository;


    public ProductHandler(TelegramService telegramService,
                          UserSessionService userSessionService, ProductRepository productRepository,
                          ProductPriceRepository productPriceRepository) {
        this.telegramService = telegramService;
        this.userSessionService = userSessionService;
        this.productRepository = productRepository;
        this.productPriceRepository = productPriceRepository;
    }

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return isProduct(userRequest.getUpdate());
    }

    @Override
    public void handle(UserRequest userRequest) {
        UserSession userSession = userRequest.getUserSession();
        String productName = userRequest.getUpdate().getMessage().getText();

        Product product = productRepository.findByName(productName);
        ProductPrice productPrice = productPriceRepository.findByProductAndPlace(product, userSession.getPlace());

        if (userSession.getProductsPrice() == null) {
            userSession.setProductsPrice(new HashMap<>());
            userSession.getProductsPrice().put(productName, productPrice);
        } else if (userSession.getProductsPrice().get(productName) == null) {
            userSession.getProductsPrice().put(productName, productPrice);
        } else if (userSession.getProductsPrice().get(productName) != null) {
            ProductPrice selectedProduct = userSession.getProductsPrice().get(productName);
            selectedProduct.getProduct().setCount(selectedProduct.getProduct().getCount() + 1);
            selectedProduct.setPrice(selectedProduct.getPrice() + productPrice.getPrice());
            selectedProduct.setBasePrice(selectedProduct.getBasePrice() + productPrice.getBasePrice());

        }

        String menuMessage = getMenuMessage(userSession.getProductsPrice());

        telegramService.deleteMessage(userRequest.getChatId(), userRequest.getUpdate().getMessage().getMessageId());

        telegramService.editMessage(userRequest.getChatId(), menuMessage, userSession.getPaginationMessageId());

        userSession.setText(menuMessage);
        userSessionService.saveSession(userSession.getChatId(), userSession);
    }

    private static String getMenuMessage(Map<String, ProductPrice> productMap) {
        StringBuilder productsInfo = new StringBuilder();
        int totalPrice = 0;
        for (ProductPrice product : productMap.values()) {
            productsInfo.append(product.getProduct().getName());
            productsInfo.append("\t\t\tКількість: ");
            productsInfo.append(product.getProduct().getCount());
            productsInfo.append("\tЦіна: ");
            productsInfo.append(product.getPrice());
            productsInfo.append(" грн");


            productsInfo.append("\n\n");
            totalPrice = totalPrice + product.getPrice();
        }
        productsInfo.append("\n");

        productsInfo.append("Загальна сума: ");
        productsInfo.append(totalPrice);

        return productsInfo.toString();
    }

    @Override
    public boolean isGlobal() {
        return true;
    }
}
