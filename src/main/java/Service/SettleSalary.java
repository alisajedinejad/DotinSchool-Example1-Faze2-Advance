package Service;

import Exception.DepositBalanceNotEnough;
import Service.Handler.FileWriters;
import model.BalanceEntity;
import model.PayEntity;
import model.TransactionEntity;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public class SettleSalary implements Runnable {
    private static List<BalanceEntity> balanceEntities;
    private static List<PayEntity> payEntities;
    private static String debtorDepositNumber;
    private PayEntity payEntity;
    public static boolean singleToneParam = true;//for running just once constructor
    public static BigDecimal debtorMoney;

    public SettleSalary() {
        if (singleToneParam) {
            singleToneParam = false;
            for (PayEntity payEntity : SettleSalary.payEntities) {
                if (payEntity.getDepositType().equals("debtor")) {
                    SettleSalary.debtorDepositNumber = payEntity.getDepositNumber();
                    SettleSalary.debtorMoney = payEntity.getAmount();

                }
            }


            for (BalanceEntity balanceEntity : SettleSalary.balanceEntities) {

                if (balanceEntity.getDepositNumber().equals(SettleSalary.debtorDepositNumber)) {
                    balanceEntity.setAmount(balanceEntity.getAmount().subtract(SettleSalary.debtorMoney));
                    try {
                        FileWriters.writeToBalance(balanceEntity);
                    } catch (IOException | DepositBalanceNotEnough e) {
                        e.printStackTrace();
                    }
                }

            }
        }
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

    public void run() {
        if (payEntity.getDepositType().equals("creditor")) {
            String creatorNumber = payEntity.getDepositNumber();
            BigDecimal creatorMoney = payEntity.getAmount();
            for (BalanceEntity balanceEntity : SettleSalary.balanceEntities) {
                if (balanceEntity.getDepositNumber().equals(creatorNumber)) {
                    balanceEntity.setAmount(balanceEntity.getAmount().add(creatorMoney));
                    try {
                        FileWriters.writeToBalance(balanceEntity);
                    } catch (DepositBalanceNotEnough | IOException e) {
                        e.printStackTrace();
                    }
                    TransactionEntity transactionEntity = new TransactionEntity();
                    transactionEntity.setDebtorDepositNumber(debtorDepositNumber);
                    transactionEntity.setCreditorDepositNumber(balanceEntity.getDepositNumber());
                    transactionEntity.setAmount(creatorMoney);
                    try {
                        FileWriters.writeToTransaction(transactionEntity);
                    } catch (DepositBalanceNotEnough | IOException e) {
                        e.printStackTrace();
                    }

                    break;
                }
            }
        }

    }

}



