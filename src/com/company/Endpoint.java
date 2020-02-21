package com.company;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Queue;

public class Endpoint {
    private String endpoint;
    private String method;
    private String name;
    private HashMap<String, String> formParameters;
    private LinkedHashMap<String, String> pathParameters;
    private HashMap<String, String> headers;

    Endpoint(String endpoint, String method, String name, HashMap<String, String> headers, HashMap<String, String> formParameters, LinkedHashMap<String, String> pathParameters) {

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
        StringBuilder endString = new StringBuilder();
        Queue<String> pathParams = new LinkedList<>();
        getPathParameters().forEach((key, value) -> pathParams.add(value));
        while (pathParams.size() != 0) {
            endString.append("/").append(pathParams.remove());
        }
        return getEndpoint() + endString;
    }

    public boolean hasFormParameters() {
        return getFormParameters().size() > 0;
    }

    public boolean hasHeader() {
        return getHeaders().size() > 0;
    }

    public Queue<String> getFormParamsKeys() {

        Queue<String> pathParams = new LinkedList<>();
        getFormParameters().forEach((key, value) -> pathParams.add(key));
        return pathParams;
    }

    public String getConstructedFormParams() {

        Queue<String> pathParams = new LinkedList<>();
        StringBuilder constructedVal = new StringBuilder();
        getFormParameters().forEach((key, value) -> pathParams.add(key + "=" + value));
        while (pathParams.size() > 0) {
            if (pathParams.size() == 1) {
                constructedVal.append(pathParams.remove());
            } else {
                constructedVal.append(pathParams.remove()).append("&");
            }
        }
        constructedVal = new StringBuilder("--data=\"" + constructedVal + "\"");
        return constructedVal.toString();
    }

    public String getConstructedHeaderParams() {

        Queue<String> pathParams = new LinkedList<>();
        StringBuilder constructedVal = new StringBuilder();
        getHeaders().forEach((key, value) -> pathParams.add(key + ":" + value));
        while (pathParams.size() > 0) {
            if (pathParams.size() == 1) {
                constructedVal.append(pathParams.remove());
            } else {
                constructedVal.append(pathParams.remove()).append("&");
            }
        }
        constructedVal = new StringBuilder("--headers=\"" + constructedVal + "\"");
        return constructedVal.toString();
    }
}
