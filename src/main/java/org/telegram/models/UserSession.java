package org.telegram.models;

import java.util.Map;

import lombok.Builder;
import lombok.Data;
import org.telegram.models.dto.Place;
import org.telegram.models.dto.Product;
import org.telegram.models.dto.ProductPrice;

@Data
@Builder
public class UserSession {
  private Long chatId;
  private String text;
  private Place place;
  private Map<String, ProductPrice> productsPrice;
  private Integer paginationMessageId;
  private Integer lastPageNumber;
  private Product selectionProduct;
  private boolean isAdmin;

}
