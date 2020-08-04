package Main;

import Service.SettleSalary;
import model.BalanceEntity;
import model.PayEntity;
import model.TransactionEntity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ali on 20/07/2020.
 */
public class MyThreads implements Runnable {
    List<PayEntity> payEntities = new ArrayList<>();

    public List<PayEntity> getPayEntities() {
        return payEntities;
    }

    public void setPayEntity(List<PayEntity> payEntity) {
        this.payEntities = payEntity;
    }

    @Override
    public void run() {
        List<BalanceEntity> balanceEntities = SettleSalary.getBalanceEntities();
        try {
            SettleSalary.criticalSection(this.threadCriticalSection(balanceEntities));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private synchronized List<BalanceEntity> threadCriticalSection(List<BalanceEntity> balanceEntities){

        for (PayEntity payEntity : getPayEntities()) {
            if (payEntity.getDepositType().equals("creditor")) {
                for (BalanceEntity balanceEntity : balanceEntities) {
                    if (balanceEntity.getDepositNumber().equals(payEntity.getDepositNumber())) {
                        balanceEntity.setAmount(balanceEntity.getAmount().add(payEntity.getAmount()));
                        TransactionEntity transactionEntity = new TransactionEntity();
                        transactionEntity.setDebtorDepositNumber(SettleSalary.getDebtorDepositNumber());
                        transactionEntity.setCreditorDepositNumber(balanceEntity.getDepositNumber());
                        transactionEntity.setAmount(payEntity.getAmount());
                        try {
                            SettleSalary.criticalSection(transactionEntity);
                            SettleSalary.criticalSection(payEntity.getAmount());
                            break;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                }
            }
        }
        return balanceEntities;
    }
}


