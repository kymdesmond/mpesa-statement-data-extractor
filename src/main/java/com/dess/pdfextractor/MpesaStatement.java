package com.dess.pdfextractor;

import lombok.Data;

import java.util.List;

@Data
public class MpesaStatement {
    private String customerName;
    private String mobileNumber;
    private String dateOfStatement;
    private String statementPeriod;
    private List<TransactionSummary> transactionSummaryList;
    private List<Transaction> transactions;
}
