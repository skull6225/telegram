package org.telegram.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.telegram.models.dto.Category;
import org.telegram.models.dto.Place;
import org.telegram.models.dto.Product;
import org.telegram.models.dto.ProductPrice;
import org.telegram.repository.CategoryRepository;
import org.telegram.repository.ProductPriceRepository;
import org.telegram.repository.ProductRepository;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductPriceRepository productPriceRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository, ProductPriceRepository productPriceRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.productPriceRepository = productPriceRepository;
    }

    public List<Category> getCategoryList() {
        return categoryRepository.findAll();
    }

    public List<Product> getProductList() {
        return productRepository.findAll();
    }

    public ReplyKeyboard getStartedButton() {
        List<KeyboardButton> buttons = new ArrayList<>();

        KeyboardRow clearApplyButtons = new KeyboardRow();
        clearApplyButtons.add(KeyboardButton.builder()
                .text("Озерна")
                .build());
        clearApplyButtons.add(KeyboardButton.builder()
                .text("Прибузька")
                .build());


        KeyboardRow row = new KeyboardRow(clearApplyButtons);

        return getReplyKeyboardMarkup(List.of(row, new KeyboardRow(List.of(KeyboardButton.builder()
                .text("Адмін")
                .build()))));
    }

    public ReplyKeyboard getCategoriesButtons() {
        List<KeyboardButton> buttons = new ArrayList<>();

        getCategoryList().forEach(f -> buttons.add(new KeyboardButton(f.getName())));

        KeyboardRow row = new KeyboardRow(buttons);

        return getReplyKeyboardMarkup(List.of(row));
    }

    public ReplyKeyboard getProductButtons(Category category, Place place) {
        List<KeyboardButton> buttons = new ArrayList<>();

        List<Product> productList = productRepository.findAllByCategory(category);

        List<ProductPrice> productPriceList = productList.stream().parallel()
                .map(m -> productPriceRepository.findByProductAndPlace(m, place))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());


        List<KeyboardRow> productButtons = new ArrayList<>();

        for (int i = 0; i < productPriceList.size(); i++) {
            ProductPrice productPrice = productPriceList.get(i);
            buttons.add(KeyboardButton.builder()
                    .text(productPrice.getProduct().getName())
                    .requestContact(false)
                    .build());
            if (buttons.size() == 3 || i == productList.size() - 1) {
                productButtons.add(new KeyboardRow(buttons));
                buttons = new ArrayList<>();
            }
        }
        if (productPriceList.size() < 3) {
            productButtons.add(new KeyboardRow(buttons));
        }

        KeyboardRow clearApplyButtons = new KeyboardRow();
        clearApplyButtons.add(KeyboardButton.builder()
                .text("Назад")
                .build());
        clearApplyButtons.add(KeyboardButton.builder()
                .text("Очистити")
                .build());
        clearApplyButtons.add(KeyboardButton.builder()
                .text("Підтвердити")
                .build());

        productButtons.add(clearApplyButtons);

        return getReplyKeyboardMarkup(productButtons);
    }

    private static ReplyKeyboardMarkup getReplyKeyboardMarkup(List<KeyboardRow> keyboardRow) {

        return ReplyKeyboardMarkup.builder()
                .keyboard(keyboardRow)
                .selective(true)
                .resizeKeyboard(true)
                .oneTimeKeyboard(false)
                .build();
    }
}
