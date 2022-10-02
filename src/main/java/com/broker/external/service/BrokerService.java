package com.broker.external.service;

import com.broker.external.client.BrokerResponseCallback;
import com.broker.external.client.ExternalBrokerTimeoutDecorator;
import com.broker.external.client.dto.BrokerTrade;
import com.broker.external.client.dto.BrokerTradeSide;
import com.broker.external.controller.BrokerTradeRequest;
import com.broker.external.entity.BrokerTradeEntity;
import com.broker.external.entity.BrokerTradeSideEntity;
import com.broker.external.entity.BrokerTradeStatusEntity;
import com.broker.external.repository.BrokerTradeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BrokerService {

    private Logger logger = LoggerFactory.getLogger(BrokerService.class);

    private BrokerTradeRepository brokerTradeRepository;

    public BrokerService(BrokerTradeRepository brokerTradeRepository){
        this.brokerTradeRepository = brokerTradeRepository;
    }

    public String executeTrade(BrokerTradeRequest brokerTradeRequest, BrokerTradeSide brokerTradeSide) {
        var brokerTrade = createTrade(brokerTradeRequest, brokerTradeSide);

        new ExternalBrokerTimeoutDecorator(new BrokerResponseCallback() {
            @Override
            public void successful(UUID tradeId) {

                brokerTradeRepository.updateStatus(BrokerTradeStatusEntity.EXECUTED, tradeId.toString());

                logger.trace("Updated successful trade status with id trade '{}'", brokerTrade.getId().toString());
            }

            @Override
            public void unsuccessful(UUID tradeId, String reason) {

                brokerTradeRepository.updateStatus(BrokerTradeStatusEntity.NOT_EXECUTED, reason, tradeId.toString());

                logger.trace("Updated unsuccessful trade status with id trade '{}'", brokerTrade.getId().toString());
            }
        }).execute(brokerTrade);

        return brokerTrade.getId().toString();
    }

    private BrokerTrade createTrade(BrokerTradeRequest brokerTradeRequest, BrokerTradeSide brokerTradeSide) {

        var brokerTrade = buildBrokerTrade(brokerTradeRequest.getSymbol(),
                brokerTradeRequest.getQuantity(),
                brokerTradeSide,
                brokerTradeRequest.getPrice());

        brokerTradeRepository.save(createBrokerTradeEntity(brokerTrade));

        logger.trace("Created new trade with id '{}'", brokerTrade.getId().toString());

        return brokerTrade;
    }

    public List<BrokerTradeEntity> findAllTrades() {

        return brokerTradeRepository.findAll();
    }

    public Optional<BrokerTradeEntity> findTradeDetails(String id) {
        return brokerTradeRepository.findById(id);
    }

    public Optional<BrokerTradeStatusEntity> findTradeStatus(String id) {
        return brokerTradeRepository.findById(id).map(BrokerTradeEntity::getStatus);
    }

    private BrokerTrade buildBrokerTrade(String symbol, long quantity, BrokerTradeSide side, BigDecimal price) {

        return new BrokerTrade(UUID.randomUUID(),
                symbol,
                quantity,
                side,
                price);
    }

    private BrokerTradeEntity createBrokerTradeEntity(BrokerTrade brokerTrade){

        var side = switch (brokerTrade.getSide()) {
            case BUY -> BrokerTradeSideEntity.BUY;
            case SELL -> BrokerTradeSideEntity.SELL;
            default -> throw new RuntimeException("No defined value exist");
        };

        return new BrokerTradeEntity(brokerTrade.getId().toString(),
                brokerTrade.getSymbol(),
                brokerTrade.getQuantity(),
                side,
                brokerTrade.getPrice(),
                BrokerTradeStatusEntity.PENDING_EXECUTION,
                "",
                LocalDateTime.now());
    }
}
