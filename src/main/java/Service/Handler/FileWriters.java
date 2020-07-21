package Service.Handler;

import Exception.DepositBalanceNotEnough;
import model.BalanceEntity;
import model.PayEntity;
import model.TransactionEntity;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class FileWriters {
    private static List<BalanceEntity> balanceEntities = new ArrayList<BalanceEntity>();
    private static List<TransactionEntity> transactionEntities = new ArrayList<TransactionEntity>();

    public static synchronized void writeToBalance(List<BalanceEntity> balanceEntities) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        for (BalanceEntity balanceEntity : balanceEntities) {
            stringBuilder.append(balanceEntity.getDepositNumber());
            stringBuilder.append("\t");
            stringBuilder.append(balanceEntity.getAmount());
            stringBuilder.append("\n");
        }
        Path filePathObj = Paths.get("DataBase/balance.txt");
        try {
            Files.write(filePathObj, stringBuilder.toString().getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException ioExceptionObj) {
            ioExceptionObj.printStackTrace();
        }
    }

    public static synchronized void writeToBalance(BalanceEntity balanceEntity) throws DepositBalanceNotEnough, IOException {
        StringBuilder stringBuilder = new StringBuilder();
        for (BalanceEntity balanceEntity1 : FileWriters.balanceEntities) {
            if (balanceEntity1.getDepositNumber().equals(balanceEntity.getDepositNumber())) {
                balanceEntity1.setAmount(balanceEntity.getAmount());
                break;
            }
        }
        for (BalanceEntity balanceEntity2 : FileWriters.balanceEntities) {
            stringBuilder.append(balanceEntity2.getDepositNumber());
            stringBuilder.append("\t");
            stringBuilder.append(balanceEntity2.getAmount());
            stringBuilder.append("\n");
        }
        Path filePathObj = Paths.get("DataBase/balance.txt");
        try {
            Files.write(filePathObj, stringBuilder.toString().getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException ioExceptionObj) {
            ioExceptionObj.printStackTrace();
        }
    }

    public static synchronized void writeToTransaction(List<TransactionEntity> transactionEntities) throws IOException {

        StringBuilder stringBuilder = new StringBuilder();
        for (TransactionEntity transactionEntity : transactionEntities) {
            stringBuilder.append(transactionEntity.getDebtorDepositNumber());
            stringBuilder.append("\t");
            stringBuilder.append(transactionEntity.getCreditorDepositNumber());
            stringBuilder.append("\t");
            stringBuilder.append(transactionEntity.getAmount());
            stringBuilder.append("\n");
        }
        Path filePathObj = Paths.get("DataBase/transactions.txt");
        try {
            Files.write(filePathObj, stringBuilder.toString().getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException ioExceptionObj) {
            ioExceptionObj.printStackTrace();
        }
    }

    public static synchronized void writeToTransaction(TransactionEntity transactionEntity) throws DepositBalanceNotEnough, IOException {
        StringBuilder stringBuilder = new StringBuilder();
        FileWriters.transactionEntities.add(transactionEntity);
        for (TransactionEntity transactionEntity1 : FileWriters.transactionEntities) {
            stringBuilder.append(transactionEntity1.getDebtorDepositNumber());
            stringBuilder.append("\t");
            stringBuilder.append(transactionEntity1.getCreditorDepositNumber());
            stringBuilder.append("\t");
            stringBuilder.append(transactionEntity1.getAmount());
            stringBuilder.append("\n");
        }
        Path filePathObj = Paths.get("DataBase/transactions.txt");
        try {
            Files.write(filePathObj, stringBuilder.toString().getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException ioExceptionObj) {
            ioExceptionObj.printStackTrace();
        }
    }

    public static synchronized void writeToPay(List<PayEntity> payEntities) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        for (PayEntity payEntity : payEntities) {
            stringBuilder.append(payEntity.getDepositType());
            stringBuilder.append("\t");
            stringBuilder.append(payEntity.getDepositNumber());
            stringBuilder.append("\t");
            stringBuilder.append(payEntity.getAmount());
            stringBuilder.append("\n");
        }
        Path filePathObj = Paths.get("DataBase/pay.txt");
        try {
            Files.write(filePathObj, stringBuilder.toString().getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException ioExceptionObj) {
            ioExceptionObj.printStackTrace();
        }
    }

    public static List<BalanceEntity> getBalanceEntities() {
        return balanceEntities;
    }

    public static void setBalanceEntities(List<BalanceEntity> balanceEntities) {
        FileWriters.balanceEntities = balanceEntities;
    }
}
