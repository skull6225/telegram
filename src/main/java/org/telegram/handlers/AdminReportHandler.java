package org.telegram.handlers;

import org.springframework.stereotype.Component;
import org.telegram.handlers.utils.UserRequestHandler;
import org.telegram.models.UserRequest;
import org.telegram.models.dto.Place;
import org.telegram.models.dto.Sales;
import org.telegram.repository.PlaceRepository;
import org.telegram.repository.SalesRepository;
import org.telegram.services.TelegramService;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
public class AdminReportHandler extends UserRequestHandler {

    private final TelegramService telegramService;
    private final PlaceRepository placeRepository;
    private final SalesRepository salesRepository;


    public AdminReportHandler(TelegramService telegramService, PlaceRepository placeRepository,
                               SalesRepository salesRepository) {
        this.telegramService = telegramService;
        this.salesRepository = salesRepository;
        this.placeRepository = placeRepository;
    }

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return isReport(userRequest.getUpdate(), userRequest.getUserSession().isAdmin());
    }

    @Override
    public void handle(UserRequest request) {
        Message message = request.getUpdate().getMessage();
        Place place = placeRepository.findByName(message.getText().replace(" звіт", ""));
        List<Sales> allByPublicationDateAndPlace = salesRepository.findAllByPublicationDateAndPlace(new Date(), place);

        telegramService.sendMessage(request.getChatId(), getMenuMessage(allByPublicationDateAndPlace, place));
    }

    private static String getMenuMessage(List<Sales> sales, Place place) {
        StringBuilder productsInfo = new StringBuilder();
        int totalPrice = 0;
        int totalBasePrice = 0;

        Date currentDate = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd:MM:yyyy");
        String formattedDate = formatter.format(currentDate);

        productsInfo.append("Звіт з точки: ");
        productsInfo.append(place.getName());
        productsInfo.append(" за ");
        productsInfo.append(formattedDate);
        productsInfo.append("\n\n");

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

        productsInfo.append("Загальна сума: ");
        productsInfo.append(totalPrice);
        productsInfo.append("\n");

        productsInfo.append("Загальна сума базової ціни: ");
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
