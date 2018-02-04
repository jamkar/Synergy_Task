package model;

public class Cell {

    private String tableName;
    private String rowName;
    private String columnName;
    private String value;

    public Cell(String tableName, String rowName, String columnName, String value) {
        this.tableName = tableName;
        this.rowName = rowName;
        this.columnName = columnName;
        this.value = value;
    }

    public String getTableName() {
        return tableName;
    }

    public String getRowName() {
        return rowName;
    }

    public String getColumnName() {
        return columnName;
    }

    public String getValue() {
        return value;
    }
}
