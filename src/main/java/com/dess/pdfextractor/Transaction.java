package com.dess.pdfextractor;

import lombok.Data;

@Data
public class Transaction {
    private String receiptNo;
    private String completionTime;
    private String details;
    private String transactionStatus;
    private String paidIn;
    private String withdrawn;
    private String balance;
}
