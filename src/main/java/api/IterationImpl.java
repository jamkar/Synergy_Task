package api;

import model.Cell;
import model.Table;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class IterationImpl implements Iteration {

    private Map<String, Table> tables;

    public IterationImpl(Map<String, Table>  tables) {
        this.tables = new ConcurrentHashMap<>(tables);
    }

    @Override
    public Set<String> getTableNames() {
        return tables.keySet();
    }

    @Override
    public Iterator<Cell> getDataCellsIterator(String tableName) {
        return new CellIterator(tables.get(tableName).getDataCells());
    }

    @Override
    public Iterator<Cell> getHeaderCellsIterator(String tableName) {
        return new CellIterator(tables.get(tableName).getHeaderCells());
    }

    @Override
    public int getColumnsCount(String tableName) {
        return tables.get(tableName).getColumnsCount();
    }
}
