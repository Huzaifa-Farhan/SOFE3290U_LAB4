package com.ontariotechu.sofe3980U;

import java.io.FileReader;
import java.util.List;
import com.opencsv.*;

public class App {
    public static void main(String[] args) {
        String[] models = {"model_1.csv", "model_2.csv", "model_3.csv"};
        String bestModel = "";
        double bestF1Score = 0;

        // Loop through each model file
        for (String filePath : models) {
            int TP = 0, FP = 0, TN = 0, FN = 0; // Confusion matrix counters

            try {
                // Read the CSV file
                FileReader filereader = new FileReader(filePath);
                CSVReader csvReader = new CSVReaderBuilder(filereader).withSkipLines(1).build();
                List<String[]> allData = csvReader.readAll();

                // Calculate confusion matrix values
                for (String[] row : allData) {
                    int y_true = Integer.parseInt(row[0]); // Ground truth
                    float y_predicted = Float.parseFloat(row[1]); // Predicted value
                    
                    // Convert prediction to binary (0 or 1)
                    int y_pred_binary = (y_predicted >= 0.5) ? 1 : 0;

                    // Update confusion matrix
                    if (y_true == 1 && y_pred_binary == 1) TP++;
                    if (y_true == 0 && y_pred_binary == 1) FP++;
                    if (y_true == 0 && y_pred_binary == 0) TN++;
                    if (y_true == 1 && y_pred_binary == 0) FN++;
                }

                // Compute evaluation metrics
                double accuracy = (double) (TP + TN) / (TP + TN + FP + FN);
                double precision = (TP + FP == 0) ? 0 : (double) TP / (TP + FP);
                double recall = (TP + FN == 0) ? 0 : (double) TP / (TP + FN);
                double f1Score = (precision + recall == 0) ? 0 : (2 * precision * recall) / (precision + recall);

                // Print metrics for each model
                System.out.printf("%s -> Accuracy: %.4f, Precision: %.4f, Recall: %.4f, F1 Score: %.4f%n",
                        filePath, accuracy, precision, recall, f1Score);

                // Track the best model based on F1 Score
                if (f1Score > bestF1Score) {
                    bestF1Score = f1Score;
                    bestModel = filePath;
                }

            } catch (Exception e) {
                System.out.println("Error reading the CSV file: " + filePath);
            }
        }

        // Print the best model based on the highest F1 Score
        System.out.println("Best Model: " + bestModel);
    }
}
