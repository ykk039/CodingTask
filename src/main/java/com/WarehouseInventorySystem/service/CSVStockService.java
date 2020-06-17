package com.WarehouseInventorySystem.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.WarehouseInventorySystem.csv_handler.CSVStockHelper;
import com.WarehouseInventorySystem.model.Stock;
import com.WarehouseInventorySystem.repository.StockRepository;

@Service
public class CSVStockService {
  @Autowired
  StockRepository repository;

  public void save(MultipartFile file) {
    try {
      List<Stock> stocks = CSVStockHelper.csvToStocks(file.getInputStream());
      repository.saveAll(stocks);
    } catch (IOException e) {
      throw new RuntimeException("fail to store csv data: " + e.getMessage());
    }
  }

  public ByteArrayInputStream load() {
    List<Stock> stocks = repository.findAll();

    ByteArrayInputStream in = CSVStockHelper.stocksToCSV(stocks);
    return in;
  }

}