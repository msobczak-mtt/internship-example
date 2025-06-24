package stock.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/stocks")
public class StockController {

    @GetMapping
    public Map<String, String> getStocks() {
        return Map.of("message", "Stock API is working!", "status", "OK");
    }
}