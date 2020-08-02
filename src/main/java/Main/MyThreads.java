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

    public List<PayEntity> getPayEntity() {
        return payEntities;
    }

    public void setPayEntity(List<PayEntity> payEntity) {
        this.payEntities = payEntity;
    }

    @Override
    public synchronized void run() {
        List<BalanceEntity> balanceEntities = SettleSalary.getBalanceEntities();
        for (int i = 0; i < this.payEntities.size(); i++) {
            if (this.payEntities.get(i).getDepositType().equals("creditor")) {
                for (BalanceEntity balanceEntity : balanceEntities) {
                    if (balanceEntity.getDepositNumber().equals(this.payEntities.get(i).getDepositNumber())) {
                        balanceEntity.setAmount(balanceEntity.getAmount().add(payEntities.get(i).getAmount()));
                        TransactionEntity transactionEntity = new TransactionEntity();
                        transactionEntity.setDebtorDepositNumber(SettleSalary.getDebtorDepositNumber());
                        transactionEntity.setCreditorDepositNumber(balanceEntity.getDepositNumber());
                        transactionEntity.setAmount(payEntities.get(i).getAmount());
                        try {
                            SettleSalary.criticalSection(transactionEntity);
                            SettleSalary.criticalSection(getPayEntity().get(i).getAmount());
                            break;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                }
            }
        }
        try {
            SettleSalary.criticalSection(balanceEntities);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}


