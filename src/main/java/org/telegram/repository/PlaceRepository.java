package org.telegram.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.telegram.models.dto.Place;
import org.telegram.models.dto.Product;
import org.telegram.models.dto.ProductPrice;

public interface PlaceRepository extends JpaRepository<Place, Long> {
    Place findByName(String name);
}

