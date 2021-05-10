package com.dess.pdfextractor;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import technology.tabula.*;
import technology.tabula.extractors.SpreadsheetExtractionAlgorithm;

@SpringBootApplication
@Slf4j
@AllArgsConstructor
public class PdfExtractorApplication {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(PdfExtractorApplication.class, args);
        readPdf();
    }

    public static void readPdf() throws IOException {
        File file = new File("/Users/desmondkimutai/Documents/statement.pdf");
        PDDocument document = PDDocument.load(file);
        int pages = document.getNumberOfPages();
        log.info("Number of pages ---> {}", pages);
        ObjectExtractor oe = new ObjectExtractor(document);
        SpreadsheetExtractionAlgorithm sea = new SpreadsheetExtractionAlgorithm();

        MpesaStatement statement = new MpesaStatement();
        List<TransactionSummary> transactionSummaries = new ArrayList<>();
        List<Transaction> transactions = new ArrayList<>();
        int count = 1;
        while (count < pages) {
            Page page = oe.extract(count);
            // extract text from the table after detecting
            List<Table> table = sea.extract(page);
            for(Table tables: table) {
                List<List<RectangularTextContainer>> rows = tables.getRows();

                for (int i = 0; i < rows.size(); i++) {
                    List<RectangularTextContainer> cells = rows.get(i);
                    TransactionSummary summary = new TransactionSummary();
                    Transaction transaction = new Transaction();
                    if (i > 0 && cells.size() == 4) { //summary section
                        for (int j = 0; j < cells.size() - 1; j++) {
                            switch (j) {
                                case 0:
                                    summary.setTransactionType(cells.get(j).getText());
                                    break;
                                case 1:
                                    summary.setPaidIn(cells.get(j).getText());
                                    break;
                                case 2:
                                    summary.setPaidOut(cells.get(j).getText());
                                    break;
                            }
                        }
                        log.info(summary.toString());
                        transactionSummaries.add(summary);
                    } else if (i > 0 && cells.size() == 8){ //trx section
                        for (int j = 0; j < cells.size() - 1; j++) {
                            switch (j) {
                                case 0:
                                    transaction.setReceiptNo(cells.get(j).getText());
                                    break;
                                case 1:
                                    transaction.setCompletionTime(cells.get(j).getText());
                                    break;
                                case 2:
                                    //transaction.setDetails(cells.get(j).getText());
                                    break;
                                case 3:
                                    transaction.setTransactionStatus(cells.get(j).getText());
                                    break;
                                case 4:
                                    transaction.setPaidIn(cells.get(j).getText());
                                    break;
                                case 5:
                                    transaction.setWithdrawn(cells.get(j).getText());
                                    break;
                                case 6:
                                    transaction.setBalance(cells.get(j).getText());
                                    break;
                            }
                        }
                        log.info(transaction.toString());
                        transactions.add(transaction);
                    }
                }
            }
            count++;
        }
        statement.setTransactionSummaryList(transactionSummaries);
        statement.setTransactions(transactions);

        PDFTextStripper textStripper = new PDFTextStripper();
        String text = textStripper.getText(document);
        String rows[] = text.split("\\r?\\n");
        for (String row: rows) {
//            log.info(row);
//            log.info("------------------------");
        }
        log.info(statement.toString());
        document.close();
    }

}
