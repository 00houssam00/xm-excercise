package com.broker.external.controller;

import com.broker.external.entity.BrokerTradeSideEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class BrokerTradeDetailResponse {

    private final String id;

    private final String symbol;

    private final long quantity;

    private final BrokerTradeSideEntity side;

    private final BigDecimal price;

    private final String status;

    private final String reason;

    private final LocalDateTime timestamp;

    public BrokerTradeDetailResponse(String id,
                                     String symbol,
                                     long quantity,
                                     BrokerTradeSideEntity side,
                                     BigDecimal price,
                                     String status,
                                     String reason,
                                     LocalDateTime timestamp) {
        this.id = id;
        this.symbol = symbol;
        this.quantity = quantity;
        this.side = side;
        this.price = price;
        this.status = status;
        this.reason = reason;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public String getSymbol() {
        return symbol;
    }

    public long getQuantity() {
        return quantity;
    }

    public BrokerTradeSideEntity getSide() {
        return side;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getStatus() {
        return status;
    }

    public String getReason() {
        return reason;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
