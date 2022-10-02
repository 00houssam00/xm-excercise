package com.broker.external.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class BrokerTradeEntity {

    @Id
    private String id;

    private String symbol;

    private long quantity;

    private BrokerTradeSideEntity side;

    private BigDecimal price;

    private BrokerTradeStatusEntity status;

    private String reason;

    private LocalDateTime createdDate;

    public BrokerTradeEntity() {

    }

    public BrokerTradeEntity(String id, String symbol,
                             long quantity,
                             BrokerTradeSideEntity side,
                             BigDecimal price,
                             BrokerTradeStatusEntity status,
                             String reason,
                             LocalDateTime createdDate) {
        this.id = id;
        this.symbol = symbol;
        this.quantity = quantity;
        this.side = side;
        this.price = price;
        this.status = status;
        this.reason = reason;
        this.createdDate = createdDate;
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

    public BrokerTradeStatusEntity getStatus() {
        return status;
    }

    public String getReason() {
        return reason;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
}
