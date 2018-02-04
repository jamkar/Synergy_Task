package api;

import model.Cell;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class CellIterator implements Iterator<Cell> {

    private List<Cell> cells;
    private int size;
    private int cursor = 0;

    public CellIterator(List<Cell> cells) {
        this.cells = new ArrayList<>(cells);
        this.size = cells.size();
    }

    @Override
    public boolean hasNext() {
        return (cursor < size);
    }

    @Override
    public Cell next() {
        Cell currentCell;
        try {
            currentCell = cells.get(cursor++);
        } catch (IndexOutOfBoundsException ex) {
            throw new NoSuchElementException("There are no more cells in the table");
        }
        return currentCell;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("remove");
    }

}