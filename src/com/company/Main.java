package com.company;

import java.io.*;
import java.util.Iterator;
import java.util.Set;

import org.json.*;


public class Main {

    public static void main(String[] args) throws IOException {

            String fileData = getFileContent("text.txt");
            JSONArray jsonArr = convertToJsonArray(fileData);

            for (int i = 0; i < jsonArr.length(); i++)
            {
                constructURLElements(jsonArr.getJSONObject(i));

            }
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

        public static JSONArray convertToJsonArray(String data) {
            return new JSONArray(data);
        }

        public static void constructURLElements(JSONObject jsonObj){
            Iterator<String> keys = jsonObj.keys();
            while (keys.hasNext()){
                System.out.println(keys.next());
            }
            System.out.println();
        }
    }

    class Endpoint {
        String[] headers;
        String endpoint;
        String method;
        String name;
        String[] formParameters;
        String[] pathParameters;
        String[] queryParameters;

        Endpoint(String endpoint, String method, String name, String[] headers, String[] formParameters, String[] pathParameters, String[] queryParameters) {

            this.endpoint = endpoint;
            this.method = method;
            this.name = name;
            if (headers.length != 0) {
                this.headers = headers;
            }
            if (formParameters.length != 0) {
                this.formParameters = formParameters;
            }
            if (pathParameters.length != 0) {
                this.pathParameters = pathParameters;
            }
            if (queryParameters.length != 0) {
                this.queryParameters = queryParameters;
            }
        }

        public String getEndpoint() {
            return endpoint;
        }

        public String getMethod() {
            return method;
        }

        public String getName() {
            return name;
        }

        public String[] getHeaders() {
            return headers;
        }

        public String[] getFormParameters() {
            return formParameters;
        }

        public String[] getPathParameters() {
            return pathParameters;
        }

        public String[] getQueryParameters() {
            return queryParameters;
        }
    }

