package dev.ojas.cryptoCurrency.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class BlockConfig {
    @Value("${block.size}")
    private int blockSize;
}
