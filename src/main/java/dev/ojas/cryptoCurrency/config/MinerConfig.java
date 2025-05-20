package dev.ojas.cryptoCurrency.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class MinerConfig {
    @Value("${miner.difficulty}")
    private int difficulty;
}
