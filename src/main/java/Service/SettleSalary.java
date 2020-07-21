package Service;

import Exception.DepositBalanceNotEnough;
import Service.Handler.FileWriters;
import model.BalanceEntity;
import model.PayEntity;
import model.TransactionEntity;

import java.io.IOException;
import java.math.BigDecimal;
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
    private static BigDecimal sum = BigDecimal.ZERO;

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

    public static BigDecimal getSum() {
        return sum;
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

    public static synchronized void subFromDebtor(BigDecimal subMoney) {

        sum = sum.add(subMoney);
        if (sum.equals(payEntities.get(payEntities.size() - 1).getAmount())) {

            balanceEntities.get(0).setAmount(balanceEntities.get(0).getAmount().subtract(sum));

        }
    }

    public static synchronized void setTransaction(PayEntity payEntity) throws DepositBalanceNotEnough, IOException {
        TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.setAmount(payEntity.getAmount());
        transactionEntity.setCreditorDepositNumber(payEntity.getDepositNumber());
        transactionEntity.setDebtorDepositNumber(SettleSalary.getDebtorDepositNumber());
        FileWriters.writeToTransaction(transactionEntity);
    }
}



