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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class Main {
    private static final Logger log = Logger.getLogger(Main.class.getName());

    public static void main(String args[]) throws InterruptedException, IOException {

        int countOfThreads = 5;
        int sizeOfPayEntities = 1000;
        int countOfPartition = countOfThreads * ((int) Math.log10(sizeOfPayEntities) - 1) * 10;
        if (countOfPartition < 1) {
            countOfPartition = countOfThreads;
        }
        ExecutorService executorService = Executors.newFixedThreadPool(countOfThreads);
        log.info("Start create " + sizeOfPayEntities + " Random Pay And Balance .");
        createRandomPayAndBalance(sizeOfPayEntities);
        log.info(sizeOfPayEntities + " Random Pay And Balance generated .");
        long start = System.currentTimeMillis();
        List<PayEntity> payEntities = FileReader.getPaysEntities();
        List<BalanceEntity> balanceEntities = FileReader.getBalanceEntities();


        try {
            CheckDepositBalanceNotEnough checkNegativeDepositBalance = new CheckDepositBalanceNotEnough();
            checkNegativeDepositBalance.check(payEntities, balanceEntities);
            SettleSalary.setBalanceEntities(balanceEntities);
            SettleSalary.setPayEntities(payEntities);
            SettleSalary settleSalary = new SettleSalary();//Just for run constructor

            int payCounter = payEntities.size();

            while (payCounter > 0) {

                if (payCounter >= countOfPartition) {
                    MyThreads myThreads = new MyThreads();
                    List<PayEntity> payEntity = new ArrayList<>();
                    for (int i = 0; i < countOfPartition; i++) {
                        payEntity.add(payEntities.get(payCounter - 1));
                        payCounter--;
                    }
                    myThreads.setPayEntity(payEntity);
                    executorService.execute(myThreads);
                } else {
                    MyThreads myThreads = new MyThreads();
                    List<PayEntity> payEntity = new ArrayList<>();
                    for (int i = 0; i < payEntities.size() % countOfPartition; i++) {
                        payEntity.add(payEntities.get(i));
                        payCounter--;
                    }
                    myThreads.setPayEntity(payEntity);
                    executorService.execute(myThreads);
                }


            }


            executorService.shutdown();
            executorService.awaitTermination(10000, TimeUnit.MILLISECONDS);

            log.info("Finished all threads");
            long end = System.currentTimeMillis();
            log.info("time of exe is : " + (end - start));

        } catch (DepositBalanceNotEnough depositBalanceNotEnough) {
            depositBalanceNotEnough.printStackTrace();
        }


    }

    public static void createRandomPayAndBalance(int size) {

        List<BalanceEntity> balanceEntities = new ArrayList<>();
        List<PayEntity> payEntities = new ArrayList<>();
        Random rand = new Random();
        BigDecimal sumOfSalary = BigDecimal.ZERO;
        BalanceEntity debtor = new BalanceEntity();
        debtor.setAmount(new BigDecimal("10000000"));
        debtor.setDepositNumber("1.10.100.1");
        balanceEntities.add(debtor);
        for (int i = 0; i < size - 1; i++) {
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
            balanceEntities.add(balanceEntity);
            PayEntity payEntity = new PayEntity();
            payEntity.setDepositNumber(depositNumberPartOne + "." + depositNumberPartTwo + "." + depositNumberPartThree + "." + depositNumberPartFour);
            payEntity.setDepositType("creditor");
            payEntity.setAmount(creatorSalary);
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
