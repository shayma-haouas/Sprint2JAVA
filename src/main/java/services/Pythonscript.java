package services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Pythonscript {
    public static void main(String[] args) {
        try {
            // Path to your Python script
            String scriptPath = "src/main/resources/recsystem.py";

            // Execute the Python script and capture its output
            ProcessBuilder processBuilder = new ProcessBuilder("python3", scriptPath);
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            // Read and display the output of the Python script
            String line;
            while ((line = reader.readLine())!= null) {
                System.out.println(line);
            }

            reader.close();
            process.waitFor();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}

