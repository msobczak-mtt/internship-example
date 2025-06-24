package stock.repository.mem;

import stock.model.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SampleData {
    
    public static List<Stock> stocks = new ArrayList<>();
    public static List<Client> clients = new ArrayList<>();
    public static List<Transaction> transactions = new ArrayList<>();
    public static List<Index> indices = new ArrayList<>();
    
    static {
        initializeData();
    }
    
    private static void initializeData() {
        // Utworzenie przykładowych akcji
        Stock pko = new Stock("PKO", "PKO Bank Polski", new BigDecimal("35.50"));
        pko.setId(1L);
        
        Stock ccc = new Stock("CCC", "CCC S.A.", new BigDecimal("15.20"));
        ccc.setId(2L);
        
        Stock kghm = new Stock("KGHM", "KGHM Polska Miedź", new BigDecimal("85.40"));
        kghm.setId(3L);
        
        Stock pge = new Stock("PGE", "Polska Grupa Energetyczna", new BigDecimal("12.30"));
        pge.setId(4L);
        
        Stock cdr = new Stock("CDR", "CD Projekt", new BigDecimal("125.80"));
        cdr.setId(5L);
        
        stocks.add(pko);
        stocks.add(ccc);
        stocks.add(kghm);
        stocks.add(pge);
        stocks.add(cdr);
        
        // Utworzenie przykładowych klientów
        Client client1 = new Client("Jan", "Kowalski", "jan.kowalski@example.com", new BigDecimal("10000.00"));
        client1.setId(1L);
        
        Client client2 = new Client("Anna", "Nowak", "anna.nowak@example.com", new BigDecimal("25000.00"));
        client2.setId(2L);
        
        clients.add(client1);
        clients.add(client2);
        
        // Utworzenie przykładowych indeksów
        Index wig20 = new Index("WIG20", "Warszawski Indeks Giełdowy 20", "20 największych spółek na GPW");
        wig20.setId(1L);
        wig20.setCurrentValue(new BigDecimal("2250.50"));
        
        Index wigBanki = new Index("WIG-BANKI", "WIG Banki", "Indeks spółek bankowych");
        wigBanki.setId(2L);
        wigBanki.setCurrentValue(new BigDecimal("8650.30"));
        
        Index wigIT = new Index("WIG-IT", "WIG Informatyka", "Indeks spółek informatycznych");
        wigIT.setId(3L);
        wigIT.setCurrentValue(new BigDecimal("1845.70"));
        
        indices.add(wig20);
        indices.add(wigBanki);
        indices.add(wigIT);
        
        // Przypisanie akcji do indeksów
        wig20.addStock(pko);
        wig20.addStock(kghm);
        wig20.addStock(pge);
        wig20.addStock(cdr);
        
        wigBanki.addStock(pko);
        
        wigIT.addStock(cdr);
        
        // Utworzenie przykładowych transakcji
        Transaction trans1 = new Transaction(client1, pko, TransactionType.BUY, 100L, new BigDecimal("35.00"));
        trans1.setId(1L);
        trans1.setTransactionDate(LocalDateTime.now().minusDays(1));
        
        Transaction trans2 = new Transaction(client2, cdr, TransactionType.BUY, 50L, new BigDecimal("120.00"));
        trans2.setId(2L);
        trans2.setTransactionDate(LocalDateTime.now().minusHours(5));
        
        transactions.add(trans1);
        transactions.add(trans2);
    }
    
    public static Long getNextId(List<?> list) {
        return list.stream()
                .mapToLong(item -> {
                    try {
                        return (Long) item.getClass().getMethod("getId").invoke(item);
                    } catch (Exception e) {
                        return 0L;
                    }
                })
                .max()
                .orElse(0L) + 1;
    }
}