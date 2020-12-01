package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
/**
 * create a program which invokes the user for their age &
 * resting heart rate
 * determine the target heart rate based on intensities 55-95%
 * target heart rate formula = ((220-age)-restingHr)* intensity)+ restingHr
 * use a loop to increment percentages from 55 to 95
 * do not hard code the percentages
 * age and heart rate must be valid if not exit the program
 * display results in tabular format
 * resting pulse : 65 age:22 (sample input)
 * output
 * intensity    rate
 * 55           138bpm
 * 60           148bpm
 * 65           152 bpm
 */
public class Main {
   private static final Logger logger = Logger.getLogger(Main.class.getSimpleName());
    private static final Pattern numberPattern;
    static {
        numberPattern= Pattern.compile("\\d{2,3}"); // \\d{2,4}
    }
    public static void main(String[] args) {
        printWelcomeMessage();
         mainFunction();
    }
    private static void printWelcomeMessage(){
        String instructionsMessage= "Enter your age and heart rate separated by a comma.The heart rate should be between 55-95.Age should be above 0";
        try {
            System.out.println("Hello there welcome to Heart Rate calculator");
            Thread.sleep(500);
            System.out.println("-----Instructions -----");
            Thread.sleep(800);
            System.out.println(instructionsMessage);
            System.out.println("Example: 22,65");
        }catch (InterruptedException ex){
            logger.log(Level.SEVERE,"The following error occurred:%s", ex.getMessage());
        }
    }
    private static  void mainFunction(){
        CompletableFuture.runAsync(() -> {
            String commandLineInputs;
            int userRate ;
            int userAge ;
            try(BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(System.in))){
                commandLineInputs = bufferedReader.readLine();
                String[] inputs= commandLineInputs.split(",");
                int inputLength = inputs.length;
                boolean isLengthValid = verifyInputLength(inputLength);
                if (!isLengthValid){
                    System.out.println("Invalid input.Please provide two inputs comma separated.\nExample 22,55");
                    mainFunction();
                }
                String age = inputs[0];
                String heartRate = inputs[1];
                if (!numberPattern.matcher(age).matches() &&
                        !numberPattern.matcher(heartRate).matches() &&
                        !verifyHeartRate(Integer.parseInt(heartRate))){
                    System.out.printf("inputs provided %s, %s are invalid please try again",age, heartRate);
                }
                userRate= Integer.parseInt(heartRate);
                userAge = Integer.parseInt(age);
                calculateHeartRate(userRate, userAge);
            }catch (IOException e){
                String errorMessage= parseErrorMessage(e);
                logger.log(Level.SEVERE, errorMessage);
            }
        });

    }
    private static void calculateHeartRate(int inputRate, int age){
         int rate = 55;
         List<Integer> heartRateList = new ArrayList<>();
         heartRateList.add(rate); //initially add the constant rate
        if (inputRate>rate){
            int loopTimes = (inputRate-rate)/5;
            for (int i=0;i<=loopTimes;i++){
                if (rate!=inputRate){
                    rate +=5;
                    heartRateList.add(rate);
                }
            }
        }
        System.out.printf("%-1.15s %-25.10s %n", "Intensity", "Rate");
        heartRateList.forEach(integer -> {
            int calculatedHeartRate = (((220-age)-inputRate)*integer) + inputRate;
            System.out.printf("%-5.10s %-25.10s %n", integer, Math.round(calculatedHeartRate));
        });
    }
    private static boolean verifyHeartRate(int rate){
        return rate >= 55 && rate < 95;

    }
    private static boolean verifyInputLength(int length){
        return length == 2;
    }
    private static String parseErrorMessage(Exception exception){
        return String.format("The following error occurred: %s ",exception.getMessage());
    }


}
