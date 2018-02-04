package api;

import model.Cell;

import java.util.Iterator;
import java.util.Set;

public interface Iteration {

    Set<String> getTableNames();

    Iterator<Cell> getDataCellsIterator(String tableName);

    Iterator<Cell> getHeaderCellsIterator(String tableName);

    int getColumnsCount(String tableName);

}
