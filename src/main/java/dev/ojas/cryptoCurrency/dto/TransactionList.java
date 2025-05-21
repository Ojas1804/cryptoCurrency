package dev.ojas.cryptoCurrency.dto;

import dev.ojas.cryptoCurrency.model.Transaction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
public class TransactionList {
    private int userId;
    private List<Transaction> transactions;
}
