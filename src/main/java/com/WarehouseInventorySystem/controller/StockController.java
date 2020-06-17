package com.WarehouseInventorySystem.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;

import com.WarehouseInventorySystem.model.Stock;
import com.WarehouseInventorySystem.model.StockKey;
import com.WarehouseInventorySystem.repository.StockRepository;
//import com.WarehouseInventorySystem.model.Product;
import com.WarehouseInventorySystem.csv_handler.CSVStockHelper;
import com.WarehouseInventorySystem.service.CSVStockService;
import com.WarehouseInventorySystem.response_message.ResponseMessage;


@RestController
@RequestMapping("/api")
public class StockController {
  
  @Autowired
  StockRepository stockRepository;

  @Autowired
  CSVStockService fileService;

  @GetMapping("/stocks")
  public ResponseEntity<List<Stock>> getAllStocks(@RequestParam(required = false) String code) {
    try {
      List<Stock> stocks = new ArrayList<Stock>();

      if (code == null)
        stockRepository.findAll().forEach(stocks::add);
      else
        stockRepository.findByCodeContaining(code).forEach(stocks::add);

      if (stocks.isEmpty()) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
      }

      return new ResponseEntity<>(stocks, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping("/stocks")
  public ResponseEntity<Stock> createStock(@RequestBody Stock stock) {
    try {
      Stock _stock = stockRepository
          .save(new Stock(stock.getLocation(), stock.getCode(), stock.getQuantity()));
      return new ResponseEntity<>(_stock, HttpStatus.CREATED);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.EXPECTATION_FAILED);
    }
  }

  @PutMapping("/stocks/{code}/{from}/{to}/{quantity}")
  public ResponseEntity<List<Stock>> transferStock(@PathVariable("code") String code,
  	@PathVariable("from") String from, @PathVariable("to") String to, @PathVariable("quantity") long quantity) {

  	StockKey stockKey1 = new StockKey(from, code);
    Optional<Stock> stockData1 = stockRepository.findById(stockKey1);

    StockKey stockKey2 = new StockKey(to, code);
    Optional<Stock> stockData2 = stockRepository.findById(stockKey2);

    if (stockData1.isPresent() && stockData2.isPresent()) {
      Stock _stock1 = stockData1.get();
      _stock1.tranferMinusQuantity(quantity);
      Stock _stock2 = stockData2.get();
      _stock2.tranferAddQuantity(quantity);

      List<Stock> stocks = new ArrayList<Stock>();

      stocks.add(stockRepository.save(_stock1));
      stocks.add(stockRepository.save(_stock2));

      return new ResponseEntity<>(stocks, HttpStatus.OK);
    } else if(stockData1.isPresent() && !stockData2.isPresent()) {
      Stock _stock1 = stockData1.get();
      _stock1.tranferMinusQuantity(quantity);

      Stock _stock2 = stockRepository.save(new Stock(to, code, quantity));

      List<Stock> stocks = new ArrayList<Stock>();

      stocks.add(stockRepository.save(_stock1));
      stocks.add(stockRepository.save(_stock2));

      return new ResponseEntity<>(stocks, HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @PutMapping("/stocks/updateQt/{code}/{location}")
  public ResponseEntity<Stock> updateStockQuantity(@PathVariable("code") String code,
    @PathVariable("location") String location, @RequestBody Stock stock) {

    StockKey stockKey = new StockKey(location, code);
    Optional<Stock> stockData = stockRepository.findById(stockKey);

    if (stockData.isPresent()) {
      Stock _stock = stockData.get();
      _stock.setQuantity(stock.getQuantity());
      return new ResponseEntity<>(stockRepository.save(_stock), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @GetMapping("/stocks/{code}/{location}")
  public ResponseEntity<Stock> getStockById(@PathVariable("code") String code, 
  	@PathVariable("location") String location) {
  	StockKey stockKey = new StockKey(location, code);
    Optional<Stock> stockData = stockRepository.findById(stockKey);

    if (stockData.isPresent()) {
      return new ResponseEntity<>(stockData.get(), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @DeleteMapping("/stocks/{code}/{location}")
  public ResponseEntity<HttpStatus> deleteStock(@PathVariable("code") String code, 
  	@PathVariable("location") String location) {
  	StockKey stockKey = new StockKey(location, code);
    try {
      stockRepository.deleteById(stockKey);
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
    }
  }

  @DeleteMapping("/stocks")
  public ResponseEntity<HttpStatus> deleteAllStocks() {
    try {
      stockRepository.deleteAll();
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
    }

  }

  @PostMapping("/stocks/upload")
  public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
    String message = "";

    if (CSVStockHelper.hasCSVFormat(file)) {
      try {
        fileService.save(file);

        message = "Uploaded the file successfully: " + file.getOriginalFilename();
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
      } catch (Exception e) {
        message = "Could not upload the file: " + file.getOriginalFilename() + "!";
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
      }
    }

    message = "Please upload a csv file!";
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
  }

  @GetMapping("/stocks/download")
  public ResponseEntity<Resource> getFile() {
    String filename = "stocks.csv";
    InputStreamResource file = new InputStreamResource(fileService.load());

    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
        .contentType(MediaType.parseMediaType("application/csv"))
        .body(file);
  }

}