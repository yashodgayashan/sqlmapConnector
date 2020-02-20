package com.company;

import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import org.json.*;


public class Main {

        public static void main(String[] args) throws IOException {

            String fileData = getFileContent("text.txt");
            JSONArray jsonArr = convertToJsonArray(fileData);

            for (int i = 0; i < jsonArr.length(); i++)
            {
                Endpoint endpoint = getEndpointElements(jsonArr.getJSONObject(i));
                System.out.println(endpoint.getName());
                System.out.println(endpoint.getConstructedEndpoint());
                System.out.println();
            }
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

        public static Endpoint getEndpointElements(JSONObject jsonObj){

            Iterator<String> keys = jsonObj.keys();
            String endpoint = null;
            String method = null;
            String name = null;
            HashMap<String, String > formParameters = new HashMap<>();
            LinkedHashMap<String, String > pathParameters = new LinkedHashMap<>();
            HashMap<String, String> headers = new HashMap<>();
            String key;
            while (keys.hasNext()){
                key = keys.next();
                switch (key) {
                    case "name":
                        name = jsonObj.getString("name");
                        break;
                    case "method":
                        method = jsonObj.getString("method");
                        break;
                    case "endpoint":
                        endpoint = jsonObj.getString("endpoint");
                        break;
                    case "form parameters":
                        JSONArray formParams = jsonObj.getJSONArray("form parameters");
                        for (int num = 0; num < formParams.length(); num++){
                            JSONObject formObj = formParams.getJSONObject(num);
                            Iterator<String> formKeys = formObj.keys();
                            String formKey;
                            String formKeyVal;
                            while (formKeys.hasNext()){
                                formKey = formKeys.next();
                                formKeyVal = formObj.getString(formKey);
                                formParameters.put(formKey, formKeyVal);
                            }
                        }
                        break;
                    case "path parameters":
                        JSONArray pathParams = jsonObj.getJSONArray("path parameters");
                        for (int num = 0; num < pathParams.length(); num++){
                            JSONObject pathObj = pathParams.getJSONObject(num);
                            Iterator<String> formKeys = pathObj.keys();
                            String formKey;
                            String formKeyVal;
                            while (formKeys.hasNext()){
                                formKey = formKeys.next();
                                formKeyVal = pathObj.getString(formKey);
                                pathParameters.put(formKey, formKeyVal);
                            }
                        }
                        break;
                    case "headers":
                        JSONArray headerParams = jsonObj.getJSONArray("headers");
                        for (int num = 0; num < headerParams.length(); num++){
                            JSONObject headerObj = headerParams.getJSONObject(num);
                            Iterator<String> formKeys = headerObj.keys();
                            String formKey;
                            String formKeyVal;
                            while (formKeys.hasNext()){
                                formKey = formKeys.next();
                                formKeyVal = headerObj.getString(formKey);
                                headers.put(formKey, formKeyVal);
                            }
                        }
                        break;
                    default:
                        break;
                }
            }
            return new Endpoint(endpoint,method,name,headers,formParameters,pathParameters);
        }
    }

    class Endpoint {
        String endpoint;
        String method;
        String name;
        HashMap<String, String > formParameters = new HashMap<>();
        LinkedHashMap<String, String > pathParameters = new LinkedHashMap<>();
        HashMap<String, String> headers = new HashMap<>();

        Endpoint(String endpoint, String method, String name, HashMap<String, String > headers, HashMap<String, String > formParameters, LinkedHashMap<String, String > pathParameters) {

            this.endpoint = endpoint;
            this.method = method;
            this.name = name;
            this.headers = headers;
            this.formParameters = formParameters;
            this.pathParameters = pathParameters;
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

        public HashMap<String, String> getHeaders() {
            return headers;
        }

        public HashMap<String, String> getFormParameters() {
            return formParameters;
        }

        public HashMap<String, String> getPathParameters() {
            return pathParameters;
        }

        public String getConstructedEndpoint() {
            String endString = "";
            Queue<String> pathParams = new LinkedList<>();
            getPathParameters().forEach((key,value)->{
                pathParams.add(value);
            });
            while (pathParams.size() != 0){
                endString +="/"+pathParams.remove();
            }
            return getEndpoint()+endString;
        }

        public boolean hasFormParameters() {
            if (getFormParameters().size() > 0) {
                return true;
            } else {
                return false;
            }
        }

        public boolean hasHeader() {
            if (getHeaders().size() > 0) {
                return true;
            } else {
                return false;
            }
        }
    }


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

