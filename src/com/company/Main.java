package com.company;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

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
            System.out.print(jsonStr);
        }
    }

