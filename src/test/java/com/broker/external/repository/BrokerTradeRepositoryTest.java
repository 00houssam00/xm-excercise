package com.broker.external.repository;

import com.broker.external.entity.BrokerTradeEntity;
import com.broker.external.entity.BrokerTradeSideEntity;
import com.broker.external.entity.BrokerTradeStatusEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class BrokerTradeRepositoryTest {

    @Autowired
    BrokerTradeRepository brokerTradeRepository;

    @BeforeEach
    void before() {

        var entity = new BrokerTradeEntity("dummy-uiid",
                "EUR/USD",
                4,
                BrokerTradeSideEntity.BUY,
                BigDecimal.valueOf(1.5),
                BrokerTradeStatusEntity.PENDING_EXECUTION,
                "",
                LocalDateTime.now());

        brokerTradeRepository.saveAndFlush(entity);
    }

    @Test
    void updateStatus() {

        // given
        brokerTradeRepository.updateStatus(BrokerTradeStatusEntity.EXECUTED, "dummy-uiid");

        // when
        var result = brokerTradeRepository.findById("dummy-uiid")
                .get()
                .getStatus();

        // then
        assertThat(result).isEqualTo(BrokerTradeStatusEntity.EXECUTED);
    }

    @Test
    void updateStatusWithReason() {

        // given
        brokerTradeRepository.updateStatus(BrokerTradeStatusEntity.EXECUTED, "Trade expired", "dummy-uiid");

        // when
        var result = brokerTradeRepository.findById("dummy-uiid");
        var actualStatus = result.get().getStatus();
        var actualReason = result.get().getReason();

        // then
        assertThat(actualStatus).isEqualTo(BrokerTradeStatusEntity.EXECUTED);
        assertThat(actualReason).isEqualTo("Trade expired");
    }
}
