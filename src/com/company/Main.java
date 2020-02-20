package com.company;

import java.io.*;
import java.util.Iterator;
import org.json.*;


public class Main {

    public static void main(String[] args) throws IOException {

           String fileData = getFileContent("text.txt");

            JSONArray jsonArr = new JSONArray(fileData);

            for (int i = 0; i < jsonArr.length(); i++)
            {
                JSONObject jsonObj = jsonArr.getJSONObject(i);
                String name = jsonObj.getString("name");
                String endpoint = jsonObj.getString("endpoint");
                JSONArray parameters;
                try{
                    parameters = jsonObj.getJSONArray("parameters");
                    for (int j = 0; j < parameters.length();j++){
                        JSONObject jsonObjTwo = parameters.getJSONObject(j);
                        Iterator<String> keys = jsonObjTwo.keys();
                        while (keys.hasNext()) {
                            String key = keys.next();
                            System.out.print(key + " ");
                            System.out.println(jsonObjTwo.getString(key));
                        }
                    }
                }catch (Exception e){

                }

                System.out.println(endpoint+"/"+name+"?");
                System.out.println();
//
            }
//
//
//        ProcessBuilder processBuilder = new ProcessBuilder();
//
//        processBuilder.command("sqlmap", "-u", "http://www.bible-history.com/subcat.php?id=2","--dbs");
//
//        try {
//
//            Process process = processBuilder.start();
//
//            BufferedReader readerTwo =
//                    new BufferedReader(new InputStreamReader(process.getInputStream()));
//
//            String line;
//            while ((line = readerTwo.readLine()) != null) {
//                System.out.println(line);
//            }
//
//            int exitCode = process.waitFor();
//            System.out.println("\nExited with error code : " + exitCode);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        }

        public static String getFileContent(String fileName) throws IOException {

            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String data = "";
            try {
                StringBuilder stringBuilder = new StringBuilder();
                String line = reader.readLine();

                while (line != null) {
                    stringBuilder.append(line);
                    stringBuilder.append("\n");
                    line = reader.readLine();
                }
                data = stringBuilder.toString();
            } finally {
                reader.close();
            }
            return data;
        }

    }

