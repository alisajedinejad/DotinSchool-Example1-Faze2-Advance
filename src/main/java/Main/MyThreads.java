package Main;

import Exception.DepositBalanceNotEnough;
import Service.SettleSalary;
import model.BalanceEntity;
import model.PayEntity;
import model.TransactionEntity;

import java.io.IOException;
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
        SettleSalary settleSalary = new SettleSalary();
        if (!this.payEntity.getDepositNumber().equals(SettleSalary.getDebtorDepositNumber())) {
            List<BalanceEntity> balanceEntities2 = SettleSalary.getBalanceEntities();
            List<TransactionEntity> transactionEntities = SettleSalary.getTransactionEntities();
            for (BalanceEntity balanceEntity : balanceEntities2) {

                if (balanceEntity.getDepositNumber().equals(this.payEntity.getDepositNumber())) {

                    balanceEntity.setAmount(balanceEntity.getAmount().add(payEntity.getAmount()));
                    SettleSalary.subFromDebtor(payEntity.getAmount());
                    try {
                        SettleSalary.setTransaction(payEntity);
                    } catch (DepositBalanceNotEnough | IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }
    }
}
