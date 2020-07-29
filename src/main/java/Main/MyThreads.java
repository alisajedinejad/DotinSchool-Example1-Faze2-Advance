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
    public void run() {
        List<BalanceEntity> balanceEntities = SettleSalary.getBalanceEntities();
        if (!this.payEntities.get(0).getDepositNumber().equals(SettleSalary.getDebtorDepositNumber())) {

            for (BalanceEntity balanceEntity : balanceEntities) {
                if (balanceEntity.getDepositNumber().equals(this.payEntities.get(0).getDepositNumber())) {
                    balanceEntity.setAmount(balanceEntity.getAmount().add(payEntities.get(0).getAmount()));
                    TransactionEntity transactionEntity = new TransactionEntity();
                    transactionEntity.setDebtorDepositNumber(SettleSalary.getDebtorDepositNumber());
                    transactionEntity.setCreditorDepositNumber(balanceEntity.getDepositNumber());
                    transactionEntity.setAmount(payEntities.get(0).getAmount());
                    SettleSalary.criticalSection(payEntities.get(0).getAmount());
                    try {
                        SettleSalary.criticalSection(transactionEntity);
                        SettleSalary.criticalSection(getPayEntity().get(0).getDepositNumber());
                        break;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }
        if (!this.payEntities.get(1).getDepositNumber().equals(SettleSalary.getDebtorDepositNumber())) {
            for (BalanceEntity balanceEntity : balanceEntities) {
                if (balanceEntity.getDepositNumber().equals(this.payEntities.get(1).getDepositNumber())) {
                    balanceEntity.setAmount(balanceEntity.getAmount().add(payEntities.get(1).getAmount()));
                    TransactionEntity transactionEntity = new TransactionEntity();
                    transactionEntity.setDebtorDepositNumber(SettleSalary.getDebtorDepositNumber());
                    transactionEntity.setCreditorDepositNumber(balanceEntity.getDepositNumber());
                    transactionEntity.setAmount(payEntities.get(1).getAmount());
                    SettleSalary.criticalSection(payEntities.get(1).getAmount());
                    try {
                        SettleSalary.criticalSection(transactionEntity);
                        SettleSalary.criticalSection(getPayEntity().get(1).getDepositNumber());
                        break;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
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


