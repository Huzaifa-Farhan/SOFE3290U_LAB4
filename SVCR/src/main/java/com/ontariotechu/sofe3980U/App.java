package com.ontariotechu.sofe3980U;

import java.io.FileReader;
import java.util.List;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

public class App {
    public static void main(String[] args) {
        // Array of model file names
        String[] modelFiles = {"model_1.csv", "model_2.csv", "model_3.csv"};
        String bestModel = "";
        double bestMSE = Double.MAX_VALUE;

        // Loop through each model file to evaluate performance
        for (String file : modelFiles) {
            double[] metrics = evaluateModel(file);
            if (metrics == null) continue;

            // Print metrics for each model
            System.out.printf("\n%s -> MSE: %.4f, MAE: %.4f, MARE: %.4f%%\n", file, metrics[0], metrics[1], metrics[2]);

            // Keep track of the best model (lowest MSE)
            if (metrics[0] < bestMSE) {
                bestMSE = metrics[0];
                bestModel = file;
            }
        }

        // Print the best model
        System.out.println("\nBest Model: " + bestModel);
    }

    // Function to compute MSE, MAE, and MARE for a given file
    private static double[] evaluateModel(String filePath) {
        try (CSVReader csvReader = new CSVReaderBuilder(new FileReader(filePath)).withSkipLines(1).build()) {
            List<String[]> data = csvReader.readAll();
            double mse = 0, mae = 0, mare = 0, epsilon = 1e-10;
            int n = data.size();

            // Loop through data and calculate metrics
            for (String[] row : data) {
                double y_true = Double.parseDouble(row[0]);
                double y_pred = Double.parseDouble(row[1]);
                double error = y_true - y_pred;

                mse += error * error;
                mae += Math.abs(error);
                mare += Math.abs(error) / (Math.abs(y_true) + epsilon);
            }

            // Return the computed metrics
            return new double[]{mse / n, mae / n, (mare / n) * 100};
        } catch (Exception e) {
            System.out.println("Error reading file: " + filePath);
            return null;
        }
    }
}
