package org.telegram.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.telegram.models.dto.Category;
import org.telegram.models.dto.Product;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findAllByCategory(Category category);
    Product findByName(String name);;

}