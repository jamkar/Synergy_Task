package api;

import model.Cell;
import model.Measure;
import model.TableName;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class QueryTest {

    @Before
    public void init() {
        QueryTestHelper.createTables();
        QueryTestHelper.insertValues();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExecuteWrongArguments() {
        Query query = new QueryImpl();
        query.execute(TableName.YEAR, TableName.YEAR, TableName.PRODUCT, Measure.TOTAL_COST);
    }

    @Test
    public void testExecute() {
        Set<String> expectedTableNames = new HashSet<>();
        expectedTableNames.add("USA");
        expectedTableNames.add("Canada");

        List<String> expHeadValues = new ArrayList<>();
        expHeadValues.add("USA");
        expHeadValues.add("2001");
        expHeadValues.add("2002");
        expHeadValues.add("2003");
        expHeadValues.add("2004");
        expHeadValues.add("2005");
        expHeadValues.add("2006");

        List<String> expDataValues = new ArrayList<>();
        expDataValues.add("Butter");
        expDataValues.add("");
        expDataValues.add("");
        expDataValues.add("");
        expDataValues.add("");
        expDataValues.add("");
        expDataValues.add("426");
        expDataValues.add("Wine");
        expDataValues.add("564");
        expDataValues.add("12.5");
        expDataValues.add("");
        expDataValues.add("");
        expDataValues.add("");
        expDataValues.add("");

        Query query = new QueryImpl();
        query.execute(TableName.COUNTRY, TableName.YEAR, TableName.PRODUCT, Measure.TOTAL_COST);
        Iteration iteration = query.getIteration();
        Set<String> actualTableNames = iteration.getTableNames();

        Iterator<Cell> headIter = iteration.getHeaderCellsIterator("USA");
        List<String> actualHeadValues = new ArrayList<>();
        while (headIter.hasNext()) {
            actualHeadValues.add(headIter.next().getValue());
        }

        Iterator<Cell> dataIter = iteration.getDataCellsIterator("USA");
        List<String> actualDataValues = new ArrayList<>();
        while (dataIter.hasNext()) {
            actualDataValues.add(dataIter.next().getValue());
        }

        assertThat(actualTableNames, is(expectedTableNames));
        assertThat(actualHeadValues, is(expHeadValues));
        assertThat(actualDataValues, is(expDataValues));
    }
}
