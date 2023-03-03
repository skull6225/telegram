package org.telegram.transformer;

import org.springframework.stereotype.Component;
import org.telegram.models.dto.Place;
import org.telegram.models.dto.Product;
import org.telegram.models.dto.ProductPrice;
import org.telegram.models.dto.Sales;

import java.util.Date;

@Component
public class Transformer {
    public Sales transformProductToSales(ProductPrice product, Place place) {
        Sales sales = new Sales();
        sales.setName(product.getProduct().getName());
        sales.setPrice(product.getPrice());
        sales.setBasePrice(product.getBasePrice());
        sales.setCount(product.getProduct().getCount());
        sales.setPlace(place);
        sales.setPublicationDate(new Date());

        return sales;
    }
}
