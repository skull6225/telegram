package org.telegram.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.telegram.models.dto.Place;
import org.telegram.models.dto.Product;
import org.telegram.models.dto.Sales;

import java.util.Date;
import java.util.List;

public interface SalesRepository extends JpaRepository<Sales, Integer> {
    @Query("SELECT new org.telegram.models.dto.Sales(s.name, SUM(s.count), SUM(s.price), SUM(s.basePrice), s.place, s.publicationDate) " +
            "FROM Sales s " +
            "WHERE s.publicationDate = :publicationDate AND s.place = :place " +
            "GROUP BY s.name")
    List<Sales> findAllByPublicationDateAndPlace(Date publicationDate, Place place);

}