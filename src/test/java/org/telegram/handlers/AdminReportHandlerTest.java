package org.telegram.handlers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.telegram.models.UserRequest;
import org.telegram.models.dto.Place;
import org.telegram.models.dto.Sales;
import org.telegram.repository.PlaceRepository;
import org.telegram.repository.SalesRepository;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AdminReportHandlerTest {

    @Autowired
    AdminReportHandler adminReportHandler;

    @Mock
    PlaceRepository placeRepository;
    @Mock
    SalesRepository salesRepository;

    @Test
    public void reportGenerateTest() {
        Place place = new Place();
        place.setId(1);
        place.setName("Озерна");

        List<Sales> salesList = getSalesList();

        when(placeRepository.findByName("Озерна звіт")).thenReturn(place);
        when(salesRepository.findAllByPublicationDateAndPlace(any(Date.class), any(Place.class)))
                .thenReturn(salesList);
        adminReportHandler.handle(getUserRequest());

    }

    private UserRequest getUserRequest() {
        Update update = new Update();
        Message message = new Message();
        message.setText("Озерна звіт");

        update.setMessage(message);

        return UserRequest.builder()
                .update(update)
                .build();
    }

    private List<Sales> getSalesList() {

        Place place = new Place();
        place.setId(1);
        place.setName("Озерна");

        List<Sales> salesList = List.of(
                new Sales(), new Sales(), new Sales()
        );

        salesList.get(0).setId(1);
        salesList.get(0).setName("americano");
        salesList.get(0).setCount(2);
        salesList.get(0).setPrice(3);
        salesList.get(0).setBasePrice(2);
        salesList.get(0).setPublicationDate(new Date());
        salesList.get(0).setPlace(place);

        salesList.get(1).setId(2);
        salesList.get(1).setName("latte");
        salesList.get(1).setCount(3);
        salesList.get(1).setPrice(4);
        salesList.get(1).setBasePrice(3);
        salesList.get(1).setPublicationDate(new Date());
        salesList.get(1).setPlace(place);

        salesList.get(2).setId(3);
        salesList.get(2).setName("americano");
        salesList.get(2).setCount(1);
        salesList.get(2).setPrice(3);
        salesList.get(2).setBasePrice(2);
        salesList.get(2).setPublicationDate(new Date());
        salesList.get(2).setPlace(place);
        return salesList;
    }
}