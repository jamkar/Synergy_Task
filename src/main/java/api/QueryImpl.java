package api;

import model.Cell;
import model.Measure;
import model.Table;
import model.TableName;
import util.DbUtils;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class QueryImpl implements Query {

    private Map<String, Table> tables;

    private Connection connection;
    private Statement statement;
    private ResultSet rs;

    @Override
    public void execute(TableName categoryOnTable, TableName categoryOnColumn,
                        TableName categoryOnRow, Measure measure) {

        if (categoryOnTable == categoryOnColumn || categoryOnTable == categoryOnRow || categoryOnColumn == categoryOnRow) {
            throw new IllegalArgumentException("model.Table names are incorrect");
        }

        Map<String, Integer> categoryOnTableNamesAndIds =
                getNamesIdsFromCategoryOnTable(categoryOnTable);

        // Execute query for each table
        Map<String, Table> tables = new HashMap<>();
        for (String tableName : categoryOnTableNamesAndIds.keySet()) {
            tables.put(tableName, executeAndGetPivotTable(categoryOnTable.getName(), categoryOnTableNamesAndIds.get(tableName), tableName,
                    categoryOnColumn.getName(), categoryOnRow.getName(), measure.getName()));
        }

        this.tables = tables;
    }

    private Map<String, Integer> getNamesIdsFromCategoryOnTable(TableName categoryOnTable) {
        Map<String, Integer> categoryOnTableNamesIds = new HashMap<>();
        try {
            connection = DbUtils.getConnection();
            statement = connection.createStatement();
            rs = statement.executeQuery("SELECT Id, Name FROM " + categoryOnTable.getName());

            while (rs.next()) {
                categoryOnTableNamesIds.put(rs.getString("Name"), rs.getInt("Id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return categoryOnTableNamesIds;
    }

    private Table executeAndGetPivotTable(String categoryOnTable, int categoryOnTableId,
                                          String categoryOnTableName, String categoryOnColumn,
                                          String categoryOnRow, String measure) {

        String query = createPivotQuery(categoryOnTable, categoryOnTableId, categoryOnColumn, categoryOnRow, measure);
//        System.out.println(query);

        Table table = new Table(categoryOnTableName);
        try {
            connection = DbUtils.getConnection();
            statement = connection.createStatement();
            rs = statement.executeQuery(query);
            ResultSetMetaData rsMeta = rs.getMetaData();
            int columnCount = rsMeta.getColumnCount();

            extractHeaderAndFirstRowAndAddToTable(categoryOnTableName, table, rsMeta, columnCount);
            extractCellsAndAddToTable(categoryOnTableName, table, rsMeta, columnCount);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }

        return table;
    }

    private void extractHeaderAndFirstRowAndAddToTable(String categoryOnTableName, Table table, ResultSetMetaData rsMeta, int columnCount) throws SQLException {
        String rowName;
        rs.next();
        rowName = rs.getString(1);
        for (int i = 1; i <= columnCount; i++) {
            Cell headerCell;
            if (i == 1) {
                headerCell = new Cell(categoryOnTableName, categoryOnTableName, categoryOnTableName, categoryOnTableName);
            } else {
                headerCell = new Cell(categoryOnTableName, categoryOnTableName, rsMeta.getColumnName(i), rsMeta.getColumnName(i));
            }
            table.addHeaderCell(headerCell);
            String data = rs.getString(i);
            data = (data == null) ? "" : data;
            Cell dataCell = new Cell(categoryOnTableName, rowName, rsMeta.getColumnName(i), data);
            table.addDataCell(dataCell);
        }
    }

    private void extractCellsAndAddToTable(String categoryOnTableName, Table table, ResultSetMetaData rsMeta, int columnCount) throws SQLException {
        String rowName;
        while (rs.next()) {
            rowName = rs.getString(1);
            for (int i = 1; i <= columnCount; i++) {
                String data = rs.getString(i);
                data = (data == null) ? "" : data;
                Cell cell = new Cell(categoryOnTableName, rowName, rsMeta.getColumnName(i), data);
                table.addDataCell(cell);
            }
        }
    }

    private String createPivotQuery(String categoryOnTable, int categoryOnTableId, String categoryOnColumn, String categoryOnRow, String measure) {

//************************************ api.Query Example *****************************************//
//        SELECT Product.Name,
//                sum(CASE WHEN Year.Id = 1 THEN TotalCost ELSE NULL END) as '2001',
//                sum(CASE WHEN Year.Id = 2 THEN TotalCost ELSE NULL END) as '2002',
//                sum(CASE WHEN Year.Id = 3 THEN TotalCost ELSE NULL END) as '2003',
//                sum(CASE WHEN Year.Id = 4 THEN TotalCost ELSE NULL END) as '2004',
//                sum(CASE WHEN Year.Id = 5 THEN TotalCost ELSE NULL END) as '2005',
//                sum(CASE WHEN Year.Id = 6 THEN TotalCost ELSE NULL END) as '2006'
//        FROM Sales
//        INNER JOIN Country ON Sales.CountryId = Country.Id
//        INNER JOIN Year ON Sales.YearId = Year.Id
//        INNER JOIN Client ON Sales.ClientId = Client.Id
//        INNER JOIN Product ON Sales.ProductId = Product.Id
//        LEFT JOIN ProductType ON Product.ProductTypeId = ProductType.Id
//        WHERE Country.Id = 1
//        GROUP BY Product.Name
//********************************************************************************************//

        StringBuilder query = new StringBuilder();
        query.append("SELECT ").append(categoryOnRow).append(".Name, ");
        query.append(getQueryPartForColumnNames(categoryOnColumn, measure));
        query.append("FROM Sales ")
                .append("inner join Country on Sales.CountryId = Country.Id ")
                .append("inner join Year on Sales.YearId = Year.Id ")
                .append("inner join Client on Sales.ClientId = Client.Id ")
                .append("inner join Product on Sales.ProductId = Product.Id ")
                .append("left join ProductType on Product.ProductTypeId = ProductType.Id ")
                .append("WHERE ").append(categoryOnTable).append(".Id = ").append(categoryOnTableId)
                .append(" GROUP BY ").append(categoryOnRow).append(".Name");

        return query.toString();
    }

    private String getQueryPartForColumnNames(String categoryOnColumn, String measure) {
        StringBuilder query = new StringBuilder();
        try {
            connection = DbUtils.getConnection();
            statement = connection.createStatement();
            rs = statement.executeQuery("SELECT Id, Name FROM " + categoryOnColumn);

            while (rs.next()) {
                query.append("sum(CASE WHEN ").append(categoryOnColumn).append(".Id = ")
                        .append(rs.getString("Id")).append(" THEN ").append(measure)
                        .append(" ELSE NULL END) as \'").append(rs.getString("Name"))
                        .append("\', ");
            }
            query.deleteCharAt(query.length() - 2); // Remove last comma
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return query.toString();
    }

    @Override
    public Map<String, Table> getResult() {
        return new HashMap<>(tables);
    }

    @Override
    public Iteration getIteration() {
        return new IterationImpl(tables);
    }

    private void close() {
        try {
            if (rs != null)
                rs.close();
            if (statement != null)
                statement.close();
            if (connection != null)
                connection.close();
        } catch (SQLException ex) { }
    }
}
