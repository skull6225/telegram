package org.telegram.services;

import org.springframework.stereotype.Service;
import org.telegram.models.dto.Sales;
import org.telegram.repository.SalesRepository;

import java.util.List;

@Service
public class SalesService {
    private final SalesRepository salesRepository;

    public SalesService(SalesRepository salesRepository) {
        this.salesRepository = salesRepository;
    }

    public void saveSalesToDb(Sales sales) {
        salesRepository.save(sales);
    }

    public void saveSalesListToDb(List<Sales> sales) {
        salesRepository.saveAll(sales);
    }

}
