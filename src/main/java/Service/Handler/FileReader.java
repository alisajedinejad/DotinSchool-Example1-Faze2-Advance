package Service.Handler;

import model.BalanceEntity;
import model.PayEntity;
import model.TransactionEntity;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

//import org.apache.log4j.Logger;

public class FileReader {
    static Logger log = Logger.getLogger(FileReader.class.getName());

    public static synchronized List<PayEntity> getPaysEntities() {
        List<PayEntity> payEntities = new ArrayList<PayEntity>();
        Path path = Paths.get("DataBase/pay.txt");
        try (BufferedReader reader = Files.newBufferedReader(path, Charset.forName("UTF-8"))) {
            String currentLine = null;
            while ((currentLine = reader.readLine()) != null) {//while there is content on the current line
                String line = (currentLine); // print the current line
                String[] thisLine = line.split("\t");
                PayEntity payEntity = new PayEntity();
                payEntity.setAmount(new BigDecimal(thisLine[2]));
                payEntity.setDepositNumber(thisLine[1]);
                payEntity.setDepositType(thisLine[0]);
                payEntities.add(payEntity);
            }
            log.info("All files successfully read .");
        } catch (IOException ex) {
            ex.printStackTrace(); //handle an exception here
        }

        return payEntities;
    }

    public static synchronized List<BalanceEntity> getBalanceEntities() {
        List<BalanceEntity> balanceEntities = new ArrayList<BalanceEntity>();
        Path path = Paths.get("DataBase/balance.txt");
        try (BufferedReader reader = Files.newBufferedReader(path, Charset.forName("UTF-8"))) {
            String currentLine = null;
            while ((currentLine = reader.readLine()) != null) {//while there is content on the current line
                String line = (currentLine); // print the current line
                String[] thisLine = line.split("\t");
                BalanceEntity balanceEntity = new BalanceEntity();
                balanceEntity.setAmount(new BigDecimal(thisLine[1]));
                balanceEntity.setDepositNumber(thisLine[0]);
                balanceEntities.add(balanceEntity);
            }
        } catch (IOException ex) {
            ex.printStackTrace(); //handle an exception here
        }

        return balanceEntities;
    }

    public static synchronized String getDebtorNumber(List<PayEntity> payEntities) {
        for (PayEntity payEntity : payEntities) {
            if (payEntity.getDepositType().equals("debtor")) {
                return payEntity.getDepositNumber();
            }
        }
        return null;
    }

    public static synchronized BigDecimal getDebtorMoney(List<PayEntity> payEntities) {
        for (PayEntity payEntity : payEntities) {
            if (payEntity.getDepositType().equals("debtor")) {
                return payEntity.getAmount();
            }
        }
        return null;
    }

    public static synchronized List<TransactionEntity> getTransactionEntities() {
        List<TransactionEntity> transactionEntities = new ArrayList<TransactionEntity>();
        Path path = Paths.get("DataBase/transactions.txt");
        try (BufferedReader reader = Files.newBufferedReader(path, Charset.forName("UTF-8"))) {
            String currentLine = null;
            while ((currentLine = reader.readLine()) != null) {
                String line = (currentLine);
                String[] thisLine = line.split("\t");
                TransactionEntity transactionEntity = new TransactionEntity();
                transactionEntity.setAmount(null);
                transactionEntity.setDebtorDepositNumber(thisLine[1]);
                transactionEntity.setCreditorDepositNumber(thisLine[0]);
                transactionEntities.add(transactionEntity);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return transactionEntities;
    }
}
