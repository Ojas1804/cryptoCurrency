package dev.ojas.cryptoCurrency.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class Ledger {
    private int payerId;
    private int receiverId;
    private float amount;
    private float minerReward;

    @Override
    public String toString() {
        return String.format(payerId + " " + receiverId + " " + amount + " " + minerReward);
    }
}
