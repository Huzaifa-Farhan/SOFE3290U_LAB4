package com.ontariotechu.sofe3980U;

import java.io.FileReader;
import java.util.List;
import com.opencsv.*;

public class App {
    public static void main(String[] args) {
        String filePath = "model.csv";  // Path to the CSV file
        FileReader filereader;
        List<String[]> allData;

        // Reading the CSV file
        try {
            filereader = new FileReader(filePath);
            CSVReader csvReader = new CSVReaderBuilder(filereader).withSkipLines(1).build();
            allData = csvReader.readAll();
        } catch (Exception e) {
            System.out.println("Error reading the CSV file");
            return;
        }

        // Variables for confusion matrix and metrics
        int TP = 0, FP = 0, TN = 0, FN = 0;
        double totalLogLoss = 0;
        int count = 0;

        // Iterating over all rows in the CSV file
        for (String[] row : allData) {
            int y_true = Integer.parseInt(row[0]);  // True label
            float[] y_predicted = new float[5];

            // Iterate over the 5 predicted values for the row (model outputs)
            for (int i = 0; i < 5; i++) {
                y_predicted[i] = Float.parseFloat(row[i + 1]);  // Predicted value for this model

                // Using a threshold of 0.5 to classify prediction as 1 (positive) or 0 (negative)
                int y_pred = (y_predicted[i] >= 0.5) ? 1 : 0;

                // Compute Confusion Matrix
                if (y_pred == 1 && y_true == 1) {
                    TP++;  // True Positive
                } else if (y_pred == 1 && y_true == 0) {
                    FP++;  // False Positive
                } else if (y_pred == 0 && y_true == 0) {
                    TN++;  // True Negative
                } else if (y_pred == 0 && y_true == 1) {
                    FN++;  // False Negative
                }

                // Calculate Cross Entropy (Log Loss)
                // Ensure the prediction values are not exactly 0 or 1 to avoid math errors
                double predictedProb = Math.max(0.0001, Math.min(0.9999, y_predicted[i]));  // Clamp to avoid log(0) or log(1)
                double logLoss = -(y_true * Math.log(predictedProb) + (1 - y_true) * Math.log(1 - predictedProb));
                totalLogLoss += logLoss;
            }

            count++;
            // You can remove this limit if you want to process all rows
            if (count == 10) {
                break;  // Limiting to the first 10 rows for simplicity (optional)
            }
        }

        // Calculate metrics
        double accuracy = (double) (TP + TN) / (TP + TN + FP + FN);
        double precision = (TP + FP) == 0 ? Double.NaN : (double) TP / (TP + FP);
        double recall = (TP + FN) == 0 ? Double.NaN : (double) TP / (TP + FN);
        double f1Score = (precision + recall) == 0 ? Double.NaN : 2 * (precision * recall) / (precision + recall);
        double logLoss = totalLogLoss / (TP + FP + TN + FN);  // Average log loss

        // Print the metrics
        System.out.println("\nConfusion Matrix:");
        System.out.println("TP (True Positive): " + TP);
        System.out.println("FP (False Positive): " + FP);
        System.out.println("TN (True Negative): " + TN);
        System.out.println("FN (False Negative): " + FN);
        System.out.println("Accuracy: " + accuracy);
        System.out.println("Precision: " + precision);
        System.out.println("Recall: " + recall);
        System.out.println("F1 Score: " + f1Score);
        System.out.println("Average Log Loss: " + logLoss);
    }
}
