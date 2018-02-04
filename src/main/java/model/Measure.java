package model;

public enum Measure {
    COUNT ("Count"),
    TOTAL_COST ("TotalCost");

    private final String name;

    Measure(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
