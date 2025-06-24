package stock.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import stock.model.Stock;
import stock.repository.StockRepository;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StockServiceBeanTest {

    @Mock
    private StockRepository stockRepository;

    @InjectMocks
    private StockServiceBean stockService;

    private Stock testStock;

    @BeforeEach
    void setUp() {
        testStock = new Stock("PKO", "PKO Bank Polski", new BigDecimal("35.50"));
        testStock.setId(1L);
    }

    @Test
    void should_find_all_stocks() {
        // given
        List<Stock> stocks = Arrays.asList(testStock);
        when(stockRepository.findAll()).thenReturn(stocks);

        // when
        List<Stock> result = stockService.getAllStocks();

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(testStock);
        verify(stockRepository).findAll();
    }

    @Test
    void should_find_stock_by_id() {
        // given
        when(stockRepository.findById(1L)).thenReturn(Optional.of(testStock));

        // when
        Stock result = stockService.getStockById(1L);

        // then
        assertThat(result).isEqualTo(testStock);
        verify(stockRepository).findById(1L);
    }

    @Test
    void should_find_stock_by_symbol() {
        // given
        when(stockRepository.findBySymbol("PKO")).thenReturn(Optional.of(testStock));

        // when
        Stock result = stockService.getStockBySymbol("PKO");

        // then
        assertThat(result).isEqualTo(testStock);
        verify(stockRepository).findBySymbol("PKO");
    }

    @Test
    void should_add_new_stock() {
        // given
        when(stockRepository.save(any(Stock.class))).thenAnswer(invocation -> {
            Stock stock = invocation.getArgument(0);
            stock.setId(2L);
            return stock;
        });

        Stock newStock = new Stock("CCC", "CCC S.A.", new BigDecimal("15.20"));

        // when
        Stock result = stockService.addStock(newStock);

        // then
        assertThat(result.getSymbol()).isEqualTo("CCC");
        assertThat(result.getCompanyName()).isEqualTo("CCC S.A.");
        assertThat(result.getCurrentPrice()).isEqualTo(new BigDecimal("15.20"));
        assertThat(result.getLastUpdate()).isNotNull();
        verify(stockRepository).save(any(Stock.class));
    }

    @Test
    void should_update_stock_price() {
        // given
        when(stockRepository.findById(1L)).thenReturn(Optional.of(testStock));
        when(stockRepository.save(any(Stock.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // when
        Stock result = stockService.updatePrice(1L, new BigDecimal("40.00"));

        // then
        assertThat(result.getCurrentPrice()).isEqualTo(new BigDecimal("40.00"));
        assertThat(result.getLastUpdate()).isNotNull();
        verify(stockRepository).findById(1L);
        verify(stockRepository).save(testStock);
    }

    @Test
    void should_throw_exception_when_updating_price_of_non_existing_stock() {
        // given
        when(stockRepository.findById(999L)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> stockService.updatePrice(999L, new BigDecimal("40.00")))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Stock with id 999 not found");

        verify(stockRepository).findById(999L);
        verify(stockRepository, never()).save(any(Stock.class));
    }
}