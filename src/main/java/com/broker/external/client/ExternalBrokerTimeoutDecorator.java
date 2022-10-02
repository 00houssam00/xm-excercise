package com.broker.external.client;

import com.broker.external.client.dto.BrokerTrade;

import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class ExternalBrokerTimeoutDecorator {

    private static long MINUTES_IN_MILLI = 60000;

    private final BrokerResponseCallback callback;

    public ExternalBrokerTimeoutDecorator(BrokerResponseCallback callback) {
        this.callback = callback;
    }

    public void execute(final BrokerTrade brokerTrade) {

        var timer = new Timer();

        new ExternalBroker(new BrokerResponseCallback() {
            @Override
            public void successful(UUID tradeId) {
                timer.cancel();
                callback.successful(tradeId);
            }

            @Override
            public void unsuccessful(UUID tradeId, String reason) {
                timer.cancel();
                callback.unsuccessful(tradeId, reason);
            }
        }).execute(brokerTrade);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                callback.unsuccessful(brokerTrade.getId(), "Trade expired");
            }
        }, MINUTES_IN_MILLI);
    }

}
