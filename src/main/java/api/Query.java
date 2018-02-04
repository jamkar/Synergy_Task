package api;

import model.Measure;
import model.Table;
import model.TableName;

import java.util.Map;

public interface Query {

    void execute(TableName catOnTable, TableName catOnColumn, TableName catOnRow, Measure measure);

    Map<String, Table> getResult();

    Iteration getIteration();
}
