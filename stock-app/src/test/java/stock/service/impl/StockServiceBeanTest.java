package stock.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import stock.model.Stock;
import stock.repository.StockDao;

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
    private StockDao stockDao;

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
        when(stockDao.findAll()).thenReturn(stocks);

        // when
        List<Stock> result = stockService.findAll();

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(testStock);
        verify(stockDao).findAll();
    }

    @Test
    void should_find_stock_by_id() {
        // given
        when(stockDao.findById(1L)).thenReturn(Optional.of(testStock));

        // when
        Optional<Stock> result = stockService.findById(1L);

        // then
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(testStock);
        verify(stockDao).findById(1L);
    }

    @Test
    void should_find_stock_by_symbol() {
        // given
        when(stockDao.findBySymbol("PKO")).thenReturn(Optional.of(testStock));

        // when
        Optional<Stock> result = stockService.findBySymbol("PKO");

        // then
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(testStock);
        verify(stockDao).findBySymbol("PKO");
    }

    @Test
    void should_create_new_stock() {
        // given
        when(stockDao.findBySymbol("CCC")).thenReturn(Optional.empty());
        when(stockDao.save(any(Stock.class))).thenAnswer(invocation -> {
            Stock stock = invocation.getArgument(0);
            stock.setId(2L);
            return stock;
        });

        // when
        Stock result = stockService.createStock("CCC", "CCC S.A.", new BigDecimal("15.20"));

        // then
        assertThat(result.getSymbol()).isEqualTo("CCC");
        assertThat(result.getCompanyName()).isEqualTo("CCC S.A.");
        assertThat(result.getCurrentPrice()).isEqualTo(new BigDecimal("15.20"));
        verify(stockDao).findBySymbol("CCC");
        verify(stockDao).save(any(Stock.class));
    }

    @Test
    void should_throw_exception_when_creating_stock_with_existing_symbol() {
        // given
        when(stockDao.findBySymbol("PKO")).thenReturn(Optional.of(testStock));

        // when & then
        assertThatThrownBy(() -> stockService.createStock("PKO", "PKO Bank", new BigDecimal("35.00")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Stock with symbol PKO already exists");

        verify(stockDao).findBySymbol("PKO");
        verify(stockDao, never()).save(any(Stock.class));
    }

    @Test
    void should_update_stock_price() {
        // given
        when(stockDao.findById(1L)).thenReturn(Optional.of(testStock));
        when(stockDao.save(any(Stock.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // when
        Stock result = stockService.updatePrice(1L, new BigDecimal("40.00"));

        // then
        assertThat(result.getCurrentPrice()).isEqualTo(new BigDecimal("40.00"));
        assertThat(result.getLastUpdate()).isNotNull();
        verify(stockDao).findById(1L);
        verify(stockDao).save(testStock);
    }

    @Test
    void should_throw_exception_when_updating_price_of_non_existing_stock() {
        // given
        when(stockDao.findById(999L)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> stockService.updatePrice(999L, new BigDecimal("40.00")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Stock not found with id: 999");

        verify(stockDao).findById(999L);
        verify(stockDao, never()).save(any(Stock.class));
    }

    @Test
    void should_update_recommendation_counts() {
        // given
        when(stockDao.findBySymbol("PKO")).thenReturn(Optional.of(testStock));

        // when
        stockService.updateRecommendationCounts("PKO", 5, 2, 3);

        // then
        assertThat(testStock.getBuyRecommendations()).isEqualTo(5);
        assertThat(testStock.getSellRecommendations()).isEqualTo(2);
        assertThat(testStock.getHoldRecommendations()).isEqualTo(3);
        verify(stockDao).findBySymbol("PKO");
    }

    @Test
    void should_handle_null_recommendation_counts() {
        // given
        when(stockDao.findBySymbol("PKO")).thenReturn(Optional.of(testStock));

        // when
        stockService.updateRecommendationCounts("PKO", null, null, null);

        // then
        assertThat(testStock.getBuyRecommendations()).isEqualTo(0);
        assertThat(testStock.getSellRecommendations()).isEqualTo(0);
        assertThat(testStock.getHoldRecommendations()).isEqualTo(0);
        verify(stockDao).findBySymbol("PKO");
    }
}