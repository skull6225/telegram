package org.telegram.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.telegram.models.dto.Category;
import org.telegram.models.dto.Place;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Category findByName(String name);

}
