package org.telegram.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.telegram.models.dto.Category;
import org.telegram.models.dto.WhiteList;

public interface WhiteListRepository extends JpaRepository<WhiteList, Integer> {
    WhiteList findByName(String name);

}
