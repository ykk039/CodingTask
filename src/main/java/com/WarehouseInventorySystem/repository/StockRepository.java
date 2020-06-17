package com.WarehouseInventorySystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.WarehouseInventorySystem.model.Stock;
import com.WarehouseInventorySystem.model.StockKey;

public interface StockRepository extends JpaRepository<Stock, StockKey> {
  List<Stock> findByLocationContaining(String location);
  List<Stock> findByCodeContaining(String code);
}