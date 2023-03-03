package org.telegram.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.telegram.models.dto.Category;
import org.telegram.models.dto.Product;
import org.telegram.models.dto.Sales;
import org.telegram.repository.CategoryRepository;
import org.telegram.repository.ProductRepository;
import org.telegram.repository.SalesRepository;

import java.util.Date;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductServiceTest {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ProductService productService;
    @Autowired
    private SalesRepository salesRepository;

    @Test
    public void writeProductsTestData() {

        System.out.println(1);
    }

}