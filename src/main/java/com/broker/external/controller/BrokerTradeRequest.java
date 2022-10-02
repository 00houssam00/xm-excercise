package com.broker.external.controller;

import javax.validation.constraints.*;
import java.math.BigDecimal;

public class BrokerTradeRequest {

    @NotBlank(message = "{validation.symbol}")
    @Pattern(regexp = "USD/JPY|EUR/USD", message = "{validation.symbol}")
    private final String symbol;

    @Min(value = 1l, message = "{validation.quantity}")
    @Max(value = 1000000l, message = "{validation.quantity}")
    private final long quantity;

    @NotNull(message = "{validation.price}")
    @Positive(message = "{validation.price}")
    private final BigDecimal price;

    public BrokerTradeRequest(String symbol, long quantity, BigDecimal price) {
        this.symbol = symbol;
        this.quantity = quantity;
        this.price = price;
    }

    public String getSymbol() {
        return symbol;
    }

    public long getQuantity() {
        return quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
