package com.company;

import java.io.*;
import java.util.*;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.json.*;


public class Main {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[92m";
    public static final String ANSI_YELLOW = "\u001B[33m";

    public static void main(String[] args) throws IOException {

        String fileData = getFileContent("text.txt");
        JSONArray jsonArr = convertToJsonArray(fileData);
        LinkedList<SQLMapData> report = new LinkedList<>();

        for (int i = 0; i < jsonArr.length(); i++) {
            SQLMapData data;
            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.redirectInput(new File("input.txt"));
            Endpoint endpoint = getEndpointElements(jsonArr.getJSONObject(i));

            String endPoint = endpoint.getConstructedEndpoint();
            String endPointName = endpoint.getEndpoint();
            String method = endpoint.getMethod();
            System.out.println("Endpoint : " + endPoint);
            if (endpoint.hasFormParameters()) {
                Queue<String> formVars = endpoint.getFormParamsKeys();
                if (endpoint.hasHeader()) {
                    while (formVars.size() > 0) {
                        String formVal = formVars.remove();
                        processBuilder.command(
                                "sqlmap",
                                "-u",
                                endPoint,
                                "--method",
                                method,
                                endpoint.getConstructedHeaderParams(),
                                endpoint.getConstructedFormParams(),
                                "-p",
                                formVal,
                                "--dbs");
                        data = getSQLMapDetailsWithForm(processBuilder, endPointName, formVal);
                        report.add(data);
                    }
                } else {
                    while (formVars.size() > 0) {
                        String formVal = formVars.remove();
                        processBuilder.command(
                                "sqlmap",
                                "-u",
                                endPoint,
                                "--method",
                                method,
                                endpoint.getConstructedHeaderParams(),
                                endpoint.getConstructedFormParams(),
                                "-p",
                                formVal,
                                "--dbs");
                        data = getSQLMapDetailsWithForm(processBuilder, endPointName, formVal);
                        report.add(data);
                    }
                }
            } else {
                if (endpoint.hasHeader()) {
                    processBuilder.command(
                            "sqlmap",
                            "-u",
                            endPoint,
                            "--method",
                            method,
                            endpoint.getConstructedHeaderParams(),
                            "--dbs");
                } else {
                    processBuilder.command(
                            "sqlmap",
                            "-u",
                            endPoint,
                            "--method",
                            method,
                            "--dbs");
                }
                data = getSQLMapDetails(processBuilder, endPointName);
                report.add(data);
            }
        }
        printData(report);
    }

    public static void printData(@NotNull LinkedList<SQLMapData> data) throws IOException {
        PrintWriter pw = new PrintWriter(new FileWriter("out.csv"));
        System.out.println("--------------------------------------------------------------------------------------------------------------------------------------");
        System.out.println(ANSI_RED + "                                                            REPORT SUMMARY " + ANSI_RESET);
        System.out.println("--------------------------------------------------------------------------------------------------------------------------------------");
        pw.write("REPORT SUMMARY  \n");
        pw.write("API Name, Form parameter if any, vulnarability \n");
        while (data.size() > 0) {
            SQLMapData entry = data.pop();
            System.out.print(entry.getName() + " ");
            pw.write(entry.getName() + ",");
            if (entry.hasFormParam()) {
                System.out.print(entry.getFormParam() + " ");
                pw.write(entry.getFormParam() + ",");
            }else{
                pw.write(" ,");
            }
            String validity = entry.getValidity();
            if (validity.equals("Not vulnarable")) {
                System.out.println(ANSI_GREEN + "Not vulnarable!" + ANSI_RESET);
                pw.write("Not vulnarable");
            } else if (validity.equals("Vulnarable")) {
                System.out.println(ANSI_RED + "Vulnarable!" + ANSI_RESET);
                pw.write("Vulnarable");
            } else {
                System.out.println(ANSI_YELLOW + "Error with sqlmap querry!" + ANSI_RESET);
                pw.write("Error with sqlmap querry!");
            }
            System.out.println();
        }
        pw.close();
    }

    @NotNull
    public static String getSQLMapValidity(@NotNull ProcessBuilder processBuilder) {

        int status = 0;
        try {
            Process process = processBuilder.start();
            BufferedReader readerTwo =
                    new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = "";
            while ((line = readerTwo.readLine()) != null) {
                System.out.println(line);
                if (line.contains("all tested parameters do not appear to be injectable")) {
                    status = 1;
                } else if (line.contains("fetching all databases")) {
                    status = 2;
                }
            }
            int exitCode = process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println();
        if (status == 1) {
            return "Not vulnarable";
        } else if (status == 2) {
            return "Vulnarable";
        } else {
            return "Error with sqlmap querry";
        }
    }

    @NotNull
    public static SQLMapData getSQLMapDetailsWithForm(ProcessBuilder processBuilder, String endPointName, String formParam) {
        SQLMapData sqlMapData;
        System.out.print("Checking for " + formParam + " parameter -> ");
        String vulnerability = getSQLMapValidity(processBuilder);
        System.out.println("Compleded");
        sqlMapData = new SQLMapData(endPointName, formParam, vulnerability);
        return sqlMapData;
    }

    @NotNull
    public static SQLMapData getSQLMapDetails(ProcessBuilder processBuilder, String endPointName) {
        SQLMapData sqlMapData;
        System.out.print("Checking -> ");
        String vulnerability = getSQLMapValidity(processBuilder);
        System.out.println("Compleded");
        sqlMapData = new SQLMapData(endPointName, vulnerability);
        return sqlMapData;
    }

    public static String getFileContent(String fileName) throws IOException {

        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String data;
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

    @NotNull
    @Contract("_ -> new")
    public static JSONArray convertToJsonArray(String data) {
        return new JSONArray(data);
    }

    @NotNull
    @Contract("_ -> new")
    public static Endpoint getEndpointElements(@NotNull JSONObject jsonObj) {

        Iterator<String> keys = jsonObj.keys();
        String endpoint = null;
        String method = null;
        String name = null;
        HashMap<String, String> formParameters = new HashMap<>();
        LinkedHashMap<String, String> pathParameters = new LinkedHashMap<>();
        HashMap<String, String> headers = new HashMap<>();
        String key;
        while (keys.hasNext()) {
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
                    for (int num = 0; num < formParams.length(); num++) {
                        JSONObject formObj = formParams.getJSONObject(num);
                        Iterator<String> formKeys = formObj.keys();
                        String formKey;
                        String formKeyVal;
                        while (formKeys.hasNext()) {
                            formKey = formKeys.next();
                            formKeyVal = formObj.getString(formKey);
                            formParameters.put(formKey, formKeyVal);
                        }
                    }
                    break;
                case "path parameters":
                    JSONArray pathParams = jsonObj.getJSONArray("path parameters");
                    for (int num = 0; num < pathParams.length(); num++) {
                        JSONObject pathObj = pathParams.getJSONObject(num);
                        Iterator<String> formKeys = pathObj.keys();
                        String formKey;
                        String formKeyVal;
                        while (formKeys.hasNext()) {
                            formKey = formKeys.next();
                            formKeyVal = pathObj.getString(formKey);
                            pathParameters.put(formKey, formKeyVal);
                        }
                    }
                    break;
                case "headers":
                    JSONArray headerParams = jsonObj.getJSONArray("headers");
                    for (int num = 0; num < headerParams.length(); num++) {
                        JSONObject headerObj = headerParams.getJSONObject(num);
                        Iterator<String> formKeys = headerObj.keys();
                        String formKey;
                        String formKeyVal;
                        while (formKeys.hasNext()) {
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
        return new Endpoint(endpoint, method, name, headers, formParameters, pathParameters);
    }
}
