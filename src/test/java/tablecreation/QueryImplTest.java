package tablecreation;

import CRUD.customQuery.QueryImpl;
import CRUD.customQuery.SubQuery;
import org.junit.Assert;
import org.junit.Test;
import tablecreation.classesintesting.PersonWithSimpleProperColumns;

public class QueryImplTest {
    private QueryImpl query = new QueryImpl(PersonWithSimpleProperColumns.class);
    private SubQuery subQuery = new SubQuery(PersonWithSimpleProperColumns.class);

    @Test
    public void selectTest() {
        String expected = "SELECT * FROM PersonWithSimpleProperColumns";
        String actual = query.select().toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void whereTest() {
        String expected = " WHERE ";
        String actual = query.where(query.getLimits()).toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void groupByTest() {
        String expected = " GROUP BY ";
        String actual = query.groupBy(query.getLimits()).toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void orderByTest() {
        String expected = " ORDER BY ";
        String actual = query.orderBy(subQuery.builder(), query.getLimits()).toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void havingByTest() {
        String expected = " HAVING  ";
        String actual = query.buildQuery().having(query.getAggregates(), query.getLimits()).toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void objectIsNotNullTest() {
        Assert.assertNotNull(query.getLimits());
        Assert.assertNotNull(query.getAggregates());
    }

    @Test
    public void fetchTest() {
        String expected = ";";
        String actual = query.fetch().toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void selectWhereEqualTest() {
        String expected = "SELECT * FROM PersonWithSimpleProperColumns WHERE p_id='1';";
        String actual = query.select().where(query.getLimits().equals("id", "1")).fetch().toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void selectWhereMoreThenTest() {
        String expected = "SELECT * FROM PersonWithSimpleProperColumns WHERE p_id > 1;";
        String actual = query.select().where(query.getLimits().equals("id").more("1")).fetch().toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void selectWhereLessThen() {
        String expected = "SELECT * FROM PersonWithSimpleProperColumns WHERE p_id < 1;";
        String actual = query.select().where(query.getLimits().equals("id").less("1")).fetch().toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void selectWhereLessOrEqualThenTest() {
        String expected = "SELECT * FROM PersonWithSimpleProperColumns WHERE p_id <= 1;";
        String actual = query.select().where(query.getLimits().equals("id").lessOrEqual("1")).fetch().toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void selectWhereMoreOrEqualThen() {
        String expected = "SELECT * FROM PersonWithSimpleProperColumns WHERE p_id >= 1;";
        String actual = query.select().where(query.getLimits().equals("id").moreOrEqual("1")).fetch().toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void selectWhereNotEqualTest() {
        String expected = "SELECT * FROM PersonWithSimpleProperColumns WHERE p_id <> 1;";
        String actual = query.select().where(query.getLimits().equals("id").notEqual("1")).fetch().toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void selectWhereBetweenTest() {
        String expected = "SELECT * FROM PersonWithSimpleProperColumns WHERE p_id BETWEEN '1' AND '23';";
        String actual = query.select().where(query.getLimits().equals("id").between("1", "23"))
                .fetch().toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void selectWhereNotBetweenTest() {
        String expected = "SELECT * FROM PersonWithSimpleProperColumns WHERE p_id NOT  BETWEEN '10' AND '20';";
        String actual = query.buildQuery().select().where(query.getLimits().equals("id").not().between("10", "20"))
                .fetch().toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void selectWhereNotBetweenOrderByTest() {
        String expected = "SELECT * FROM PersonWithSimpleProperColumns WHERE p_id NOT  BETWEEN '10' AND '20' ORDER BY p_id;";
        String actual = query.buildQuery().select().where(query.getLimits().equals("id").not().between("10", "20")).orderBy(query.getLimits().builder().equals("id"))
                .fetch().toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void selectWhereNotBetweenAndNotInTest() {
        String expected = "SELECT * FROM PersonWithSimpleProperColumns WHERE p_id NOT  BETWEEN '10' AND '20' AND  NOT p_id IN ('1', '2', '3');";
        String actual = query.buildQuery().select().where(query.getLimits().equals("id").not().between("10", "20")
                .and().not().equals("id").in("1", "2", "3")).fetch().toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void selectWhereLikeTest() {
        QueryImpl query1 = new QueryImpl(PersonWithSimpleProperColumns.class);
        QueryImpl query2 = new QueryImpl(PersonWithSimpleProperColumns.class);
        QueryImpl query3 = new QueryImpl(PersonWithSimpleProperColumns.class);
        QueryImpl query4 = new QueryImpl(PersonWithSimpleProperColumns.class);
        QueryImpl query5 = new QueryImpl(PersonWithSimpleProperColumns.class);


        String expected = "SELECT * FROM PersonWithSimpleProperColumns WHERE p_id LIKE 's%';";
        String expected1 = "SELECT * FROM PersonWithSimpleProperColumns WHERE p_id LIKE '%s';";
        String expected2 = "SELECT * FROM PersonWithSimpleProperColumns WHERE p_id LIKE '%s%';";
        String expected3 = "SELECT * FROM PersonWithSimpleProperColumns WHERE p_id LIKE '_s%';";
        String expected4 = "SELECT * FROM PersonWithSimpleProperColumns WHERE p_id LIKE 's_%';";
        String expected5 = "SELECT * FROM PersonWithSimpleProperColumns WHERE p_id LIKE 's%d';";

        String actual = query.buildQuery().select().where(query.getLimits().equals("id").like("starts", "s"))
                .fetch().toString();
        String actual1 = query1.buildQuery().select().where(query1.getLimits().equals("id").like("ends", "s"))
                .fetch().toString();
        String actual2 = query2.buildQuery().select().where(query2.getLimits().equals("id").like("any position", "s"))
                .fetch().toString();
        String actual3 = query3.buildQuery().select().where(query3.getLimits().equals("id").like("position after", "2", "s"))
                .fetch().toString();
        String actual4 = query4.buildQuery().select().where(query4.getLimits().equals("id").like("position before", "2", "s"))
                .fetch().toString();
        String actual5 = query5.buildQuery().select().where(query5.getLimits().equals("id").like("starts and ends", "s", "d"))
                .fetch().toString();

        Assert.assertEquals(expected, actual);
        Assert.assertEquals(expected1, actual1);
        Assert.assertEquals(expected2, actual2);
        Assert.assertEquals(expected3, actual3);
        Assert.assertEquals(expected4, actual4);
        Assert.assertEquals(expected5, actual5);
    }

    @Test
    public void selectInTest() {
        String expected = "SELECT * FROM PersonWithSimpleProperColumns WHERE p_id IN ('Germany', 'France', 'UK');";
        String actual = query.buildQuery().select().where(query.getLimits().equals("id").in("Germany", "France", "UK")).fetch().toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void selectNotInTest() {
        String expected = "SELECT * FROM PersonWithSimpleProperColumns WHERE p_id NOT  IN ('Germany', 'France', 'UK');";
        String actual = query.buildQuery().select().where(query.getLimits().equals("id").not().in("Germany", "France", "UK")).fetch().toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void selectInWithCompoundTest() {
        String expected = "SELECT * FROM PersonWithSimpleProperColumns WHERE p_id IN (SELECT p_id FROM PersonWithSimpleProperColumns WHERE p_id > 5);";
        String actual = query.buildQuery().select().where(query.getLimits().builder().equals("id")
                .inSubQuery(subQuery.selectWhere("id"), subQuery.getLimits().equals("id").more("5"))).fetch().toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void selectNotInWithCompoundTest() {
        String expected = "SELECT * FROM PersonWithSimpleProperColumns WHERE p_id NOT  IN (SELECT p_id FROM PersonWithSimpleProperColumns WHERE p_id > 5);";
        String actual = query.buildQuery().select().where(query.getLimits().builder().equals("id").not()
                .inSubQuery(subQuery.selectWhere("id"), subQuery.getLimits().equals("id").more("5"))).fetch().toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void selectCountTest() {
        String expected = "SELECT  COUNT (p_id), p_id FROM PersonWithSimpleProperColumns;";
        String actual = query.buildQuery().select(query.getAggregates().builder().count("id", "id")).fetch().toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void selectCountGroupByTest() {
        String expected = "SELECT  COUNT (p_id), p_id FROM PersonWithSimpleProperColumns GROUP BY p_id;";
        String actual = query.buildQuery().select(query.getAggregates().builder().count("id", "id"))
                .groupBy(query.getLimits().equals("id")).fetch().toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void selectCountGroupByOrderByTest() {
        String expected = "SELECT  COUNT (p_id), p_id FROM PersonWithSimpleProperColumns GROUP BY p_id ORDER BY  COUNT (p_id) DESC;";
        String actual = query.buildQuery().select(query.getAggregates().builder().count("id", "id"))
                .groupBy(query.getLimits().equals("id")).orderBy(query.getAggregates().builder().count("id"), subQuery.desc()).fetch().toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void selectCountGroupByHavingOrderByTest() {
        String expected = "SELECT  COUNT (p_id), p_id FROM PersonWithSimpleProperColumns GROUP BY p_id HAVING  COUNT (p_id)  > 5 ORDER BY  COUNT (p_id) DESC;";
        String actual = query.buildQuery().select(query.getAggregates().builder().count("id", "id"))
                .groupBy(query.getLimits().equals("id")).having(query.getAggregates().builder().count("id"), query.getLimits().builder()
                        .more("5")).orderBy(query.getAggregates().builder().count("id"), subQuery.desc()).fetch().toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void selectMinTest() {
        String expected = "SELECT MIN(p_id) FROM PersonWithSimpleProperColumns;";
        String actual = query.buildQuery().select(query.getAggregates().builder().selectMin("id")).fetch().toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void selectMaxTest() {
        String expected = "SELECT MAX(p_id) FROM PersonWithSimpleProperColumns;";
        String actual = query.buildQuery().select(query.getAggregates().builder().selectMax("id")).fetch().toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void selectAvgTest() {
        String expected = "SELECT AVG(p_id) FROM PersonWithSimpleProperColumns;";
        String actual = query.buildQuery().select(query.getAggregates().builder().avg("id")).fetch().toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void selectSumTest() {
        String expected = "SELECT SUM(p_id) FROM PersonWithSimpleProperColumns;";
        String actual = query.buildQuery().select(query.getAggregates().builder().sum("id")).fetch().toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void selectSumIsNullTest() {
        String expected = "SELECT SUM(p_id) IS  NULL  FROM PersonWithSimpleProperColumns;";
        String actual = query.buildQuery().selectMath(query.getAggregates().builder().sum("id"), query.getLimits().builder().is().nuLL()).fetch().toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void selectSumIsNullOrNotNullTest() {
        String expected = "SELECT SUM(p_id) IS  NULL  OR  NOT  NULL  FROM PersonWithSimpleProperColumns;";
        String actual = query.buildQuery().selectMath(query.getAggregates().builder().sum("id"), query.getLimits().builder().is().nuLL().or().not().nuLL()).fetch().toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void selectWhereNotAndNotTest() {
        String expected = "SELECT * FROM PersonWithSimpleProperColumns WHERE  NOT p_id='1' AND  NOT p_id='2';";
        String actual = query.buildQuery().select().where(query.getLimits().builder().not().equals("id", "1").and().not().equals("id", "2")).fetch().toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void selectOrderByAscAndDescTest() {
        String expected = "SELECT * FROM PersonWithSimpleProperColumns ORDER BY p_id ASC, p_id DESC;";
        String actual = query.buildQuery().select().orderBy(subQuery.builder().ascAndDesc("id", "id")).fetch().toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void selectOrderByAscTest() {
        String expected = "SELECT * FROM PersonWithSimpleProperColumns ORDER BY p_id ASC;";
        String actual = query.buildQuery().select().orderBy(subQuery.builder().asc("id")).fetch().toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void selectOrderByDescTest() {
        String expected = "SELECT * FROM PersonWithSimpleProperColumns ORDER BY p_id DESC;";
        String actual = query.buildQuery().select().orderBy(subQuery.builder().desc("id")).fetch().toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void selectCountGroupByOrderByAscTest() {
        String expected = "SELECT  COUNT (p_id), p_id FROM PersonWithSimpleProperColumns GROUP BY p_id ORDER BY  COUNT (p_id) ASC;";
        String actual = query.buildQuery().select(query.getAggregates().builder().count("id", "id"))
                .groupBy(query.getLimits().equals("id")).orderBy(query.getAggregates().builder().count("id"), subQuery.asc()).fetch().toString();
        Assert.assertEquals(expected, actual);
    }
}
