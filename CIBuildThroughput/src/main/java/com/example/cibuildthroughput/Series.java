package com.example.cibuildthroughput;

import java.util.List;

public class Series {
    private String name;
    private String group;
    private List<Integer> values;

    public Series(String name, List<Integer> values) {
        this.name = name;
        this.group = "a";
        this.values = values;
    }

    public String getName() {
        return name;
    }

    public String getGroup() {
        return group;
    }

    public List<Integer> getValues() {
        return values;
    }
}
