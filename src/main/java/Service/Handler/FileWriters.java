package Service.Handler;

import Exception.DepositBalanceNotEnough;
import model.BalanceEntity;
import model.PayEntity;
import model.TransactionEntity;

import java.io.BufferedWriter;
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

    public static void writeToBalance(List<BalanceEntity> balanceEntities) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        for (BalanceEntity balanceEntity : balanceEntities) {
            stringBuilder.append(balanceEntity.getDepositNumber());
            stringBuilder.append("\t");
            stringBuilder.append(balanceEntity.getAmount());
            stringBuilder.append("\n");
        }
        Path filePathObj = Paths.get("DataBase/balance.txt");
        try {
            Files.write(filePathObj, stringBuilder.toString().getBytes(), StandardOpenOption.CREATE);
        } catch (IOException ioExceptionObj) {
            ioExceptionObj.printStackTrace();
        }
    }

    public static  void writeToBalance(BalanceEntity balanceEntity) throws DepositBalanceNotEnough, IOException {
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
            Files.write(filePathObj, stringBuilder.toString().getBytes(), StandardOpenOption.APPEND);
        } catch (IOException ioExceptionObj) {
            ioExceptionObj.printStackTrace();
        }
    }

    public static  void writeToTransaction(List<TransactionEntity> transactionEntities) throws IOException {

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
            Files.write(filePathObj, stringBuilder.toString().getBytes(), StandardOpenOption.APPEND);
        } catch (IOException ioExceptionObj) {
            ioExceptionObj.printStackTrace();
        }
    }

    public static  void writeToTransaction(TransactionEntity transactionEntity) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(transactionEntity.getDebtorDepositNumber());
            stringBuilder.append("\t");
            stringBuilder.append(transactionEntity.getCreditorDepositNumber());
            stringBuilder.append("\t");
            stringBuilder.append(transactionEntity.getAmount());
            stringBuilder.append("\n");
        Path filePathObj = Paths.get("DataBase/transactions.txt");
        try {
            Files.write(filePathObj, stringBuilder.toString().getBytes(), StandardOpenOption.APPEND);
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
            Files.write(filePathObj, stringBuilder.toString().getBytes(), StandardOpenOption.CREATE);
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



    public static void clearAllFiles() throws IOException {

        Path filePathObj0 = Paths.get("DataBase/balance.txt");
        Path filePathObj1 = Paths.get("DataBase/pay.txt");
        Path filePathObj2 = Paths.get("DataBase/transactions.txt");
        BufferedWriter writer0 = Files.newBufferedWriter(filePathObj0);
        writer0.write("");
        writer0.flush();
        BufferedWriter writer1 = Files.newBufferedWriter(filePathObj1);
        writer1.write("");
        writer1.flush();
        BufferedWriter writer2 = Files.newBufferedWriter(filePathObj2);
        writer2.write("");
        writer2.flush();
//        }
    }
}
