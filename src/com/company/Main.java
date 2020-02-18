package com.company;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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
        }
    }

