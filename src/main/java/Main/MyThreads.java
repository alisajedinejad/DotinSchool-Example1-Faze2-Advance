package Main;

import Service.SettleSalary;
import model.BalanceEntity;
import model.PayEntity;
import model.TransactionEntity;

import java.util.List;

/**
 * Created by ali on 20/07/2020.
 */
public class MyThreads implements Runnable {
    PayEntity payEntity = new PayEntity();

    public PayEntity getPayEntity() {
        return payEntity;
    }

    public void setPayEntity(PayEntity payEntity) {
        this.payEntity = payEntity;
    }

    @Override
    public void run() {
        if (!this.payEntity.getDepositNumber().equals(SettleSalary.getDebtorDepositNumber())) {
            List<BalanceEntity> balanceEntities = SettleSalary.getBalanceEntities();
            for (BalanceEntity balanceEntity : balanceEntities) {
                if (balanceEntity.getDepositNumber().equals(this.payEntity.getDepositNumber())) {
                    balanceEntity.setAmount(balanceEntity.getAmount().add(payEntity.getAmount()));
                    TransactionEntity transactionEntity = new TransactionEntity();
                    transactionEntity.setDebtorDepositNumber(SettleSalary.getDebtorDepositNumber());
                    transactionEntity.setCreditorDepositNumber(balanceEntity.getDepositNumber());
                    transactionEntity.setAmount(payEntity.getAmount());
                    SettleSalary.criticalSection(payEntity.getAmount());
                    SettleSalary.criticalSection(balanceEntities);
                    SettleSalary.criticalSection(transactionEntity);
                    SettleSalary.criticalSection(payEntity.getDepositNumber());
                    break;
                }
            }


        }
    }
}
