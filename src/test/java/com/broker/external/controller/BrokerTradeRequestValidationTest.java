package com.broker.external.controller;

import org.junit.jupiter.api.Test;

import javax.validation.Validation;
import javax.validation.Validator;
import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;


public class BrokerTradeRequestValidationTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void test_valid() {

        // given
        var request = new BrokerTradeRequest("USD/JPY", 5, BigDecimal.valueOf(1.4));

        // when
        var violations = validator.validate(request);

        // then
        assertThat(violations).isEmpty();
    }

    @Test
    void test_noneValid() {

        // given
        var request = new BrokerTradeRequest("dummy-symbol", -1, BigDecimal.valueOf(-1));

        // when
        var violations = validator.validate(request);

        // then
        assertThat(violations).hasSize(3);
    }

    @Test
    void test_symbol_USDJPY() {

        // given
        var request = new BrokerTradeRequest("USD/JPY", 5, BigDecimal.valueOf(1.4));

        // when
        var violations = validator.validate(request);

        // then
        assertThat(violations).isEmpty();
    }

    @Test
    void test_symbol_EURUSD() {

        // given
        var request = new BrokerTradeRequest("EUR/USD", 5, BigDecimal.valueOf(1.4));

        // when
        var violations = validator.validate(request);

        // then
        assertThat(violations).isEmpty();
    }

    @Test
    void test_symbol_nonValid() {

        // given
        var request = new BrokerTradeRequest("dummy-symbol", 5, BigDecimal.valueOf(1.4));

        // when
        var violations = validator.validate(request);

        // then
        assertThat(violations).hasSize(1);
    }

    @Test
    void test_quantity_lessThanOne() {

        // given
        var request = new BrokerTradeRequest("EUR/USD", 0, BigDecimal.valueOf(1.4));

        // when
        var violations = validator.validate(request);

        // then
        assertThat(violations).hasSize(1);
    }

    @Test
    void test_quantity_moreThanOneMillion() {

        // given
        var request = new BrokerTradeRequest("EUR/USD", 9999999, BigDecimal.valueOf(1.4));

        // when
        var violations = validator.validate(request);

        // then
        assertThat(violations).hasSize(1);
    }

    @Test
    void test_price_negative() {

        // given
        var request = new BrokerTradeRequest("EUR/USD", 1, BigDecimal.valueOf(-1.4));

        // when
        var violations = validator.validate(request);

        // then
        assertThat(violations).hasSize(1);
    }
}
