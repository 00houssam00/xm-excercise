package com.broker.external.service;

import com.broker.external.entity.BrokerTradeEntity;
import com.broker.external.entity.BrokerTradeSideEntity;
import com.broker.external.entity.BrokerTradeStatusEntity;
import com.broker.external.repository.BrokerTradeRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

@MockitoSettings
public class BrokerServiceTest {

    @Mock
    BrokerTradeRepository brokerTradeRepository;

    @InjectMocks
    BrokerService brokerService;

    @Test
    void test_findTradeDetails() throws Exception {

        // given
        var tradeId = "dummy-uuid";
        var entity = new BrokerTradeEntity(tradeId,
                "dummy-symbol",
                12,
                BrokerTradeSideEntity.BUY,
                BigDecimal.valueOf(2.4),
                BrokerTradeStatusEntity.PENDING_EXECUTION,
                "",
                LocalDateTime.of(1999, 12, 12, 12, 0));

        doReturn(Optional.of(entity)).when(brokerTradeRepository).findById(tradeId);

        // when
        var result = brokerService.findTradeDetails(tradeId);

        // then
        assertThat(result).isNotEmpty().get().isEqualTo(entity);
    }

    @Test
    void test_findTradeDetails_notFound() throws Exception {

        // given
        var tradeId = "dummy-uuid";
        doReturn(Optional.empty()).when(brokerTradeRepository).findById(tradeId);

        // when
        var result = brokerService.findTradeDetails(tradeId);

        // then
        assertThat(result).isEmpty();
    }

    @Test
    void test_findTradeStatus() throws Exception {

        // given
        var tradeId = "dummy-uuid";
        var entity = new BrokerTradeEntity("dummy-id",
                "dummy-symbol",
                12,
                BrokerTradeSideEntity.BUY,
                BigDecimal.valueOf(2.4),
                BrokerTradeStatusEntity.PENDING_EXECUTION,
                "",
                LocalDateTime.of(1999, 12, 12, 12, 0));

        doReturn(Optional.of(entity)).when(brokerTradeRepository).findById(tradeId);

        // when
        var result = brokerService.findTradeStatus(tradeId);

        // then
        assertThat(result).isNotEmpty().get().isEqualTo(BrokerTradeStatusEntity.PENDING_EXECUTION);
    }

    @Test
    void test_findTradeStatus_notFound() throws Exception {

        // given
        var tradeId = "dummy-uuid";
        doReturn(Optional.empty()).when(brokerTradeRepository).findById(tradeId);

        // when
        var result = brokerService.findTradeStatus(tradeId);

        // then
        assertThat(result).isEmpty();
    }

    @Test
    void test_findAllTrades() {

        // given
        var entity = new BrokerTradeEntity("dummy-id",
                "dummy-symbol",
                12,
                BrokerTradeSideEntity.BUY,
                BigDecimal.valueOf(2.4),
                BrokerTradeStatusEntity.PENDING_EXECUTION,
                "",
                LocalDateTime.of(1999, 12, 12, 12, 0));

        doReturn(List.of(entity)).when(brokerTradeRepository).findAll();

        // when
        var result = brokerService.findAllTrades();

        // then
        assertThat(result).usingRecursiveComparison().isEqualTo(List.of(entity));
    }
}
