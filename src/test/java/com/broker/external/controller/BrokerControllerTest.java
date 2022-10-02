package com.broker.external.controller;

import com.broker.external.client.dto.BrokerTradeSide;
import com.broker.external.entity.BrokerTradeEntity;
import com.broker.external.entity.BrokerTradeSideEntity;
import com.broker.external.entity.BrokerTradeStatusEntity;
import com.broker.external.service.BrokerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;

@WebMvcTest(controllers = BrokerController.class)
public class BrokerControllerTest {

    @MockBean
    BrokerService brokerService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void test_executeBuyTrade_valid() throws Exception {

        // given
        var url = "/api/buy";
        var request = new BrokerTradeRequest("EUR/USD", 5, BigDecimal.valueOf(1.4));

        doReturn("trade-uuid-dummy").when(brokerService).executeTrade(any(), eq(BrokerTradeSide.BUY));

        // when
        var requestBuilder = MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request));

        var result = mockMvc.perform(requestBuilder);

        // then
        var expectedLocationHeader = "http://localhost/api/trades/trade-uuid-dummy/status";
        result.andExpect(MockMvcResultMatchers.status().isCreated());
        result.andExpect(MockMvcResultMatchers.header().string("Location", expectedLocationHeader));
    }

    @Test
    void test_executeBuyTrade_badRequest() throws Exception {

        // given
        var url = "/api/buy";
        var request = new BrokerTradeRequest("dummy-symbol", -1, BigDecimal.valueOf(1.4));

        // when
        var requestBuilder = MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request));

        var result = mockMvc.perform(requestBuilder);

        // then
        result.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void test_executeSellTrade_valid() throws Exception {

        // given
        var url = "/api/sell";
        var request = new BrokerTradeRequest("EUR/USD", 5, BigDecimal.valueOf(1.4));

        doReturn("trade-uuid-dummy").when(brokerService).executeTrade(any(), eq(BrokerTradeSide.SELL));

        // when
        var requestBuilder = MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request));

        var result = mockMvc.perform(requestBuilder);

        // then
        var expectedLocationHeader = "http://localhost/api/trades/trade-uuid-dummy/status";
        result.andExpect(MockMvcResultMatchers.status().isCreated());
        result.andExpect(MockMvcResultMatchers.header().string("Location", expectedLocationHeader));
    }

    @Test
    void test_executeSellTrade_badRequest() throws Exception {

        // given
        var url = "/api/sell";
        var request = new BrokerTradeRequest("dummy-symbol", -1, BigDecimal.valueOf(1.4));


        // when
        var requestBuilder = MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request));

        var result = mockMvc.perform(requestBuilder);

        // then
        result.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void test_findTradeDetails() throws Exception {

        // given
        var tradeId = "dummy-uuid";
        var url = "/api/trades/" + tradeId;
        var entity = new BrokerTradeEntity(tradeId,
                "dummy-symbol",
                12,
                BrokerTradeSideEntity.BUY,
                BigDecimal.valueOf(2.4),
                BrokerTradeStatusEntity.PENDING_EXECUTION,
                "",
                LocalDateTime.of(1999, 12, 12, 12, 0));

        doReturn(Optional.of(entity)).when(brokerService).findTradeDetails(tradeId);

        // when
        var requestBuilder = MockMvcRequestBuilders.get(url)
                .contentType(MediaType.APPLICATION_JSON);

        var result = mockMvc.perform(requestBuilder);

        // then
        var expected = new BrokerTradeDetailResponse(tradeId,
                "dummy-symbol",
                12,
                BrokerTradeSideEntity.BUY,
                BigDecimal.valueOf(2.4),
                BrokerTradeStatusEntity.PENDING_EXECUTION.name(),
                "",
                LocalDateTime.of(1999, 12, 12, 12, 0));

        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(expected)));
    }

    @Test
    void test_findTradeDetails_notFound() throws Exception {

        // given
        var tradeId = "dummy-uuid";
        var url = "/api/trades/" + tradeId;

        doReturn(Optional.empty()).when(brokerService).findTradeDetails(tradeId);

        // when
        var requestBuilder = MockMvcRequestBuilders.get(url)
                .contentType(MediaType.APPLICATION_JSON);

        var result = mockMvc.perform(requestBuilder);

        // then
        result.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void test_findTradeStatus() throws Exception {

        // given
        var tradeId = "dummy-uuid";
        var url = "/api/trades/" + tradeId + "/status";

        doReturn(Optional.of(BrokerTradeStatusEntity.EXECUTED)).when(brokerService).findTradeStatus(tradeId);

        // when
        var requestBuilder = MockMvcRequestBuilders.get(url)
                .contentType(MediaType.APPLICATION_JSON);

        var result = mockMvc.perform(requestBuilder);

        // then
        result.andExpect(MockMvcResultMatchers.status().isOk());
        result.andExpect(MockMvcResultMatchers.content().string(BrokerTradeStatusEntity.EXECUTED.name()));
    }

    @Test
    void test_findTradeStatus_notFound() throws Exception {

        // given
        var tradeId = "dummy-uuid";
        var url = "/api/trades/" + tradeId + "/status";

        doReturn(Optional.empty()).when(brokerService).findTradeStatus(tradeId);

        // when
        var requestBuilder = MockMvcRequestBuilders.get(url)
                .contentType(MediaType.APPLICATION_JSON);

        var result = mockMvc.perform(requestBuilder);

        // then
        result.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void test_findAllTrades() throws Exception {

        // given
        var url = "/api/trades";
        var entity = new BrokerTradeEntity("dummy-id",
                "dummy-symbol",
                12,
                BrokerTradeSideEntity.BUY,
                BigDecimal.valueOf(2.4),
                BrokerTradeStatusEntity.PENDING_EXECUTION,
                "",
                LocalDateTime.of(1999, 12, 12, 12, 0));

        doReturn(List.of(entity)).when(brokerService).findAllTrades();

        // when
        var requestBuilder = MockMvcRequestBuilders.get(url)
                .contentType(MediaType.APPLICATION_JSON);

        var result = mockMvc.perform(requestBuilder);

        // then
        var expected = List.of(new BrokerTradeDetailResponse("dummy-id",
                "dummy-symbol",
                12,
                BrokerTradeSideEntity.BUY,
                BigDecimal.valueOf(2.4),
                BrokerTradeStatusEntity.PENDING_EXECUTION.name(),
                "",
                LocalDateTime.of(1999, 12, 12, 12, 0)));

        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(expected)));
    }
}
