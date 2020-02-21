package com.company;

public class SQLMapData {
    private String name;
    private String formParam;
    private String validity;

    SQLMapData(String name, String validity) {
        this.name = name;
        this.validity = validity;
    }

    SQLMapData(String name, String formParam, String validity) {
        this.name = name;
        this.formParam = formParam;
        this.validity = validity;
    }

    public String getName() {
        return name;
    }

    public String getValidity() {
        return validity;
    }

    public String getFormParam() {
        return formParam;
    }

    public boolean hasFormParam() {
        return formParam != null;
    }
}