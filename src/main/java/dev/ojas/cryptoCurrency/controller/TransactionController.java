package dev.ojas.cryptoCurrency.controller;

import dev.ojas.cryptoCurrency.dto.Ledger;
import dev.ojas.cryptoCurrency.dto.TransactionList;
import dev.ojas.cryptoCurrency.model.Transaction;
import dev.ojas.cryptoCurrency.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class TransactionController {
    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/transaction")
    @ResponseBody
    public Transaction createNewTransaction(Ledger ledger) throws Exception {
        return transactionService.addTransaction(ledger);
    }

    @GetMapping("/transaction/{transactionId}")
    @ResponseBody
    public Transaction getTransactionByTransactionId(@PathVariable int transactionId) {
        return transactionService.getTransactionById(transactionId);
    }

    @GetMapping("/transaction/payer/{payerId}")
    @ResponseBody
    public TransactionList getAllTransactionsByPayer(@PathVariable int payerId) {
        return new TransactionList(payerId, transactionService.getTransactionsForPayerById(payerId));
    }

    @GetMapping("/transaction/receiver/{receiverId}")
    @ResponseBody
    public TransactionList getAllTransactionsByReceiver(@PathVariable int receiverId) {
        return new TransactionList(receiverId, transactionService.getTransactionsForReceiverById(receiverId));
    }

    @GetMapping("/transaction")
    @ResponseBody
    public Page<Transaction> getAllTransactionsPaginated(@RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "10") int size) {
        return transactionService.getAllTransaction(page, size);
    }
}
