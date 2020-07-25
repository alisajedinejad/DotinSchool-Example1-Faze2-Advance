package Main;


import Exception.DepositBalanceNotEnough;
import Service.Handler.FileReader;
import Service.Handler.FileWriters;
import Service.SettleSalary;
import Service.Validation.CheckDepositBalanceNotEnough;
import model.BalanceEntity;
import model.PayEntity;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class Main {
    private static final Logger log = Logger.getLogger(Main.class.getName());

    public static void main(String args[]) throws InterruptedException, IOException {

        ExecutorService executorService = Executors.newFixedThreadPool(50);
        log.info("Start create 1000 Random Pay And Balance .");
        createRandomPayAndBalance();
        log.info("1000 Random Pay And Balance generated .");
        long start = System.currentTimeMillis();
        List<PayEntity> payEntities = FileReader.getPaysEntities();
        List<BalanceEntity> balanceEntities = FileReader.getBalanceEntities();


        try {
            CheckDepositBalanceNotEnough checkNegativeDepositBalance = new CheckDepositBalanceNotEnough();
            checkNegativeDepositBalance.check(payEntities, balanceEntities);
        } catch (DepositBalanceNotEnough depositBalanceNotEnough) {
            depositBalanceNotEnough.printStackTrace();
            return;
        }

        SettleSalary.setBalanceEntities(balanceEntities);
        SettleSalary.setPayEntities(payEntities);
        SettleSalary settleSalary = new SettleSalary();//Just for run constructor

        for (PayEntity payEntity : payEntities) {
            MyThreads myThreads = new MyThreads();
            myThreads.setPayEntity(payEntity);
            executorService.execute(myThreads);
        }
        executorService.shutdown();
        while (!executorService.isTerminated()) {

        }
        log.info("Finished all threads");
        long end = System.currentTimeMillis();
        log.info("time of exe is : " + (end - start));
    }

    public static void createRandomPayAndBalance() {

        List<BalanceEntity> balanceEntities = new ArrayList<BalanceEntity>();
        List<PayEntity> payEntities = new ArrayList<PayEntity>();
        Random rand = new Random();
        BigDecimal sumOfSalary = BigDecimal.ZERO;
        BalanceEntity debtor = new BalanceEntity();
        debtor.setAmount(new BigDecimal("10000000"));
        debtor.setDepositNumber("1.10.100.1");
        balanceEntities.add(debtor);
        for (int i = 0; i < 1000; i++) {
            int depositNumberPartOne = rand.nextInt(7) + 2;
            int depositNumberPartTwo = rand.nextInt(899) + 100;
            int depositNumberPartThree = rand.nextInt(899) + 100;
            int depositNumberPartFour = rand.nextInt(89) + 10;

            BigDecimal max = new BigDecimal("100000");
            BigDecimal randFromDouble = new BigDecimal(Math.random());
            BigDecimal amount = randFromDouble.multiply(max);
            amount = amount
                    .setScale(0, BigDecimal.ROUND_DOWN);
            BigDecimal max2 = new BigDecimal("1000");
            BigDecimal randFromDouble2 = new BigDecimal(Math.random());
            BigDecimal creatorSalary = randFromDouble2.multiply(max2);
            creatorSalary = creatorSalary
                    .setScale(0, BigDecimal.ROUND_DOWN);
            sumOfSalary = sumOfSalary.add(creatorSalary);
            BalanceEntity balanceEntity = new BalanceEntity();
            balanceEntity.setDepositNumber(depositNumberPartOne + "." + depositNumberPartTwo + "." + depositNumberPartThree + "." + depositNumberPartFour);
            balanceEntity.setAmount(amount);
//            balanceEntity.setAmount(BigDecimal.ZERO);
            balanceEntities.add(balanceEntity);
            PayEntity payEntity = new PayEntity();
            payEntity.setDepositNumber(depositNumberPartOne + "." + depositNumberPartTwo + "." + depositNumberPartThree + "." + depositNumberPartFour);
            payEntity.setDepositType("creditor");
            payEntity.setAmount(creatorSalary);
//            payEntity.setAmount(BigDecimal.TEN);
            payEntities.add(payEntity);
        }
        PayEntity debtorForPay = new PayEntity();
        debtorForPay.setAmount(sumOfSalary);
        debtorForPay.setDepositType("debtor");
        debtorForPay.setDepositNumber("1.10.100.1");
        payEntities.add(debtorForPay);
        try {
            FileWriters.clearAllFiles();
            FileWriters.writeToBalance(balanceEntities);
            FileWriters.writeToPay(payEntities);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
