package stock.service;

import stock.model.Stock;

import java.math.BigDecimal;
import java.util.List;

public interface StockService {

    List<Stock> getAllStocks();
    
    Stock getStockById(Long id);
    
    Stock getStockBySymbol(String symbol);
    
    List<Stock> getStocksByCompanyName(String companyName);
    
    Stock addStock(Stock stock);
    
    Stock updatePrice(Long id, BigDecimal newPrice);
}