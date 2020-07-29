package Service;

import Service.Handler.FileWriters;
import model.BalanceEntity;
import model.PayEntity;
import model.TransactionEntity;

import java.io.BufferedWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class SettleSalary {
    private static List<BalanceEntity> balanceEntities;
    private static List<PayEntity> payEntities;
    private static List<TransactionEntity> transactionEntities;
    private static String debtorDepositNumber;
    private static int indexOfDebtorDeposit;
    private PayEntity payEntity;
    public static boolean singleToneParam = true;//for running just once constructor
    private static BigDecimal debtorMoney;
    private static int counter = 0;

    public SettleSalary() {

        if (singleToneParam) {
            singleToneParam = false;
            int index = 0;

            for (PayEntity payEntity : SettleSalary.payEntities) {
                if (payEntity.getDepositType().equals("debtor")) {

                    SettleSalary.debtorDepositNumber = payEntity.getDepositNumber();
                    SettleSalary.debtorMoney = payEntity.getAmount();
                    SettleSalary.indexOfDebtorDeposit = index;
                    break;
                }
                index++;
            }
        }
    }

    public static List<TransactionEntity> getTransactionEntities() {
        return transactionEntities;
    }

    public static void setTransactionEntities(List<TransactionEntity> transactionEntities) {
        SettleSalary.transactionEntities = transactionEntities;
    }


    public static int getIndexOfDebtorDeposit() {
        return indexOfDebtorDeposit;
    }

    public static void setIndexOfDebtorDeposit(int indexOfDebtorDeposit) {
        SettleSalary.indexOfDebtorDeposit = indexOfDebtorDeposit;
    }

    public static String getDebtorDepositNumber() {
        return debtorDepositNumber;
    }

    public static void setDebtorDepositNumber(String debtorDepositNumber) {
        SettleSalary.debtorDepositNumber = debtorDepositNumber;
    }

    public static BigDecimal getDebtorMoney() {
        return debtorMoney;
    }

    public static void setDebtorMoney(BigDecimal debtorMoney) {
        SettleSalary.debtorMoney = debtorMoney;
    }

    public static List<BalanceEntity> getBalanceEntities() {
        return balanceEntities;
    }

    public static List<PayEntity> getPayEntities() {
        return payEntities;
    }

    public static void setPayEntities(List<PayEntity> payEntities) {
        SettleSalary.payEntities = payEntities;
    }

    public static void setBalanceEntities(List<BalanceEntity> balanceEntities) {
        SettleSalary.balanceEntities = balanceEntities;
    }

    public void setPayEntity(PayEntity payEntity) {
        this.payEntity = payEntity;
    }

    @Override
    public String toString() {
        return "SettleSalary{" +
                "balanceEntities=" + balanceEntities +
                ", payEntities=" + payEntity
                +
                '}';
    }


    public static synchronized void criticalSection(List<BalanceEntity> balanceEntities) throws IOException {
        FileWriters.writeToBalance(balanceEntities);

    }

    public static synchronized void criticalSection(BigDecimal bigDecimal) {
        SettleSalary.balanceEntities.get(0).setAmount(SettleSalary.balanceEntities.get(0).getAmount().subtract(bigDecimal));
    }

    public static synchronized void criticalSection(TransactionEntity transactionEntity) throws IOException {
        FileWriters.writeToTransaction(transactionEntity);
    }

    public static synchronized void criticalSection(String payNumber) throws IOException {
        List<PayEntity> payEntities = SettleSalary.getPayEntities();

        for (PayEntity payEntity : payEntities) {
            if (payEntity.getDepositNumber().equals(payNumber)) {
                BigDecimal setterForPayDebtor = payEntities.get(SettleSalary.indexOfDebtorDeposit).getAmount();
                setterForPayDebtor = setterForPayDebtor.subtract(payEntity.getAmount());
                payEntity.setAmount(BigDecimal.ZERO);
                payEntities.get(SettleSalary.indexOfDebtorDeposit).setAmount(setterForPayDebtor);
                counter++;
            }
        }
        if (counter == SettleSalary.payEntities.size() - 1) {
            Path filePathObj0 = Paths.get("DataBase/pay.txt");
            BufferedWriter writer0;
            writer0 = Files.newBufferedWriter(filePathObj0);
            writer0.write("");
            writer0.flush();
            FileWriters.writeToPay(payEntities);
        }
    }
}



