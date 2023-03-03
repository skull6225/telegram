package org.telegram.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.telegram.models.dto.Place;
import org.telegram.models.dto.Product;
import org.telegram.models.dto.Sales;

import java.util.Date;
import java.util.List;

public interface SalesRepository extends JpaRepository<Sales, Integer> {
    List<Sales> findAllByPublicationDateAndPlace(Date publicationDate, Place place);

}