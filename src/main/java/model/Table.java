package model;

import java.util.ArrayList;
import java.util.List;

public class Table {

    private String name;
    private int columnsCount = 0;
    private List<Cell> headerCells;
    private List<Cell> dataCells;

    public Table(String name) {
        headerCells = new ArrayList<>();
        dataCells = new ArrayList<>();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getColumnsCount() {
        return columnsCount;
    }

    public void addHeaderCell(Cell cell) {
        headerCells.add(cell);
        columnsCount++;
    }

    public void addDataCell(Cell cell) {
        dataCells.add(cell);
    }

    public List<Cell> getHeaderCells() {
        return headerCells;
    }

    public List<Cell> getDataCells() {
        return dataCells;
    }

}
