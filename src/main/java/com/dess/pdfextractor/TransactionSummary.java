package com.dess.pdfextractor;

import lombok.Data;

@Data
public class TransactionSummary {
    private String transactionType;
    private String paidIn;
    private String paidOut;
}
