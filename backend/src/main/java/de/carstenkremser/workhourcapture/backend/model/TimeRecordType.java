package de.carstenkremser.workhourcapture.backend.model;

public enum TimeRecordType {
    WORKSTART("workstart"),
    WORKEND("workend");

    private final String value;
    TimeRecordType(String value) {this.value = value;}
    public String getValue() {return value;}
}
