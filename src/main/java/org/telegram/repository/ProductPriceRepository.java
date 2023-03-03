package org.telegram.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.telegram.models.dto.Category;
import org.telegram.models.dto.Place;
import org.telegram.models.dto.Product;
import org.telegram.models.dto.ProductPrice;

public interface ProductPriceRepository extends JpaRepository<ProductPrice, Long> {
    ProductPrice findByProductAndPlace(Product product, Place place);
}

