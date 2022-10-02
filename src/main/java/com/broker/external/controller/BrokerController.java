package com.broker.external.controller;

import com.broker.external.client.dto.BrokerTradeSide;
import com.broker.external.entity.BrokerTradeEntity;
import com.broker.external.entity.BrokerTradeStatusEntity;
import com.broker.external.service.BrokerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api")
public class BrokerController {

    @Autowired
    private BrokerService brokerService;

    @PostMapping("/buy")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity executeBuyTrade(@Valid @RequestBody BrokerTradeRequest brokerTradeRequest) {

        var tradeId = brokerService.executeTrade(brokerTradeRequest, BrokerTradeSide.BUY);
        var location = buildTradeStatusUri(tradeId);
        return ResponseEntity.created(location).build();
    }

    @PostMapping("/sell")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity executeSellTrade(@Valid @RequestBody BrokerTradeRequest brokerTradeRequest) {

        var tradeId = brokerService.executeTrade(brokerTradeRequest, BrokerTradeSide.SELL);
        var location = buildTradeStatusUri(tradeId);
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/trades/{tradeId}")
    public ResponseEntity<BrokerTradeDetailResponse> findTradeDetails(@PathVariable String tradeId) {

        return ResponseEntity.of(brokerService.findTradeDetails(tradeId)
                .map(this::convertToBrokerTradeDetailResponse));
    }

    @GetMapping("/trades/{tradeId}/status")
    public ResponseEntity<String> findTradeStatus(@PathVariable String tradeId) {

        return ResponseEntity.of(brokerService.findTradeStatus(tradeId)
                .map(BrokerTradeStatusEntity::name));
    }

    @GetMapping("/trades")
    public List<BrokerTradeDetailResponse> getAllTrades() {

        return brokerService.findAllTrades().stream()
                .map(this::convertToBrokerTradeDetailResponse)
                .toList();
    }

    private URI buildTradeStatusUri(String tradeId) {

        return ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/trades/{tradeId}/status")
                .buildAndExpand(tradeId)
                .toUri();
    }

    private BrokerTradeDetailResponse convertToBrokerTradeDetailResponse(BrokerTradeEntity brokerTradeEntity) {

        return new BrokerTradeDetailResponse(brokerTradeEntity.getId(),
                brokerTradeEntity.getSymbol(),
                brokerTradeEntity.getQuantity(),
                brokerTradeEntity.getSide(),
                brokerTradeEntity.getPrice(),
                brokerTradeEntity.getStatus().name(),
                brokerTradeEntity.getReason(),
                brokerTradeEntity.getCreatedDate());
    }
}
