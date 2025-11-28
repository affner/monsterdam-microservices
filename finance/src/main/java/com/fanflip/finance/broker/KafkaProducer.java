package com.monsterdam.finance.broker;

import java.util.function.Supplier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(prefix = "app.features", name = "kafka-enabled", havingValue = "true")
public class KafkaProducer implements Supplier<String> {

    @Override
    public String get() {
        return "kakfa_producer";
    }
}
