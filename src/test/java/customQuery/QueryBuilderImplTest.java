package customQuery;

import org.junit.Assert;
import org.junit.Test;
import tablecreation.classesintesting.PersonWithSimpleProperColumns;


public class QueryBuilderImplTest {

    @Test
    public void whereTest() {
         QueryBuilderImpl query = new QueryBuilderImpl(PersonWithSimpleProperColumns.class);
        String expected = " WHERE ";
        String actual = query.where(query.getLimits()).toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void groupByTest() {
        QueryBuilderImpl query = new QueryBuilderImpl(PersonWithSimpleProperColumns.class);
        String expected = " GROUP BY ";
        String actual = query.groupBy(query.getLimits()).toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void orderByTest() {
        QueryBuilderImpl query = new QueryBuilderImpl(PersonWithSimpleProperColumns.class);
        String expected = " ORDER BY ";
        SubQuery subQuery = new SubQuery(PersonWithSimpleProperColumns.class);
        String actual = query.orderBy(subQuery.builder(), query.getLimits()).toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void havingByTest() {
        QueryBuilderImpl query = new QueryBuilderImpl(PersonWithSimpleProperColumns.class);
        String expected = " HAVING  ";
        String actual = query.having(query.getAggregates(), query.getLimits()).toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void objectIsNotNullTest() {
        QueryBuilderImpl query = new QueryBuilderImpl(PersonWithSimpleProperColumns.class);
        Assert.assertNotNull(query.getLimits());
        Assert.assertNotNull(query.getAggregates());
    }

    @Test
    public void fetchTest() {
        QueryBuilderImpl query = new QueryBuilderImpl(PersonWithSimpleProperColumns.class);
        String expected = ";";
        String actual = query.fetch();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void selectWhereEqualTest() {
        String expected = "SELECT * FROM person WHERE p_id='1';";

        QueryBuilderImpl query = new QueryBuilderImpl(PersonWithSimpleProperColumns.class);

        String actual = query.select().where(query.getLimits().equals("id", "1")).fetch();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void selectWhereMoreThenTest() {
        String expected = "SELECT * FROM person WHERE p_id > 1;";

        QueryBuilderImpl query = new QueryBuilderImpl(PersonWithSimpleProperColumns.class);
        String actual = query.select().where(query.getLimits().equals("id").more("1")).fetch();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void selectWhereLessThen() {
        String expected = "SELECT * FROM person WHERE p_id < 1;";

        QueryBuilderImpl query = new QueryBuilderImpl(PersonWithSimpleProperColumns.class);

        String actual = query.select().where(query.getLimits().equals("id").less("1")).fetch();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void selectWhereLessOrEqualThenTest() {
        QueryBuilderImpl query = new QueryBuilderImpl(PersonWithSimpleProperColumns.class);
        String expected = "SELECT * FROM person WHERE p_id <= 1;";
        String actual = query.select().where(query.getLimits().equals("id").lessOrEqually("1")).fetch();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void selectWhereMoreOrEqualThen() {
        QueryBuilderImpl query = new QueryBuilderImpl(PersonWithSimpleProperColumns.class);
        String expected = "SELECT * FROM person WHERE p_id >= 1;";
        String actual = query.select().where(query.getLimits().equals("id").moreOrEqually("1")).fetch();


        Assert.assertEquals(expected, actual);
    }

    @Test
    public void selectWhereNotEqualTest() {
        QueryBuilderImpl query = new QueryBuilderImpl(PersonWithSimpleProperColumns.class);
        String expected = "SELECT * FROM person WHERE p_id <> 1;";
        String actual = query.select().where(query.getLimits().equals("id").notEqually("1")).fetch();



        Assert.assertEquals(expected, actual);
    }

    @Test
    public void selectWhereBetweenTest() {

        String expected = "SELECT * FROM person WHERE p_id BETWEEN '1' AND '23';";

        QueryBuilderImpl query = new QueryBuilderImpl(PersonWithSimpleProperColumns.class);
        String actual = query.select().where(query.getLimits().equals("id").between("1", "23"))
                .fetch();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void selectWhereNotBetweenOrderByTest() {
        QueryBuilderImpl query = new QueryBuilderImpl(PersonWithSimpleProperColumns.class);
        String expected = "SELECT * FROM person WHERE p_id NOT  BETWEEN '10' AND '20' ORDER BY p_id;";
        String actual = query.select().where(query.getLimits().equals("id").not().
                between("10", "20")).orderBy(query.getLimits().builder().equals("id"))  .fetch();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void selectWhereNotBetweenAndNotInTest() {
        String expected = "SELECT * FROM person WHERE p_id NOT  BETWEEN '10' AND '20' AND  NOT p_id IN ('1', '2', '3');";
        QueryBuilderImpl query = new QueryBuilderImpl(PersonWithSimpleProperColumns.class);
        String actual = query.select().where(query.getLimits().equals("id").not().between("10", "20")
                .and().not().equals("id").in("1", "2", "3")).fetch();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void selectWhereLikeTest() {
        QueryBuilderImpl query = new QueryBuilderImpl(PersonWithSimpleProperColumns.class);
        QueryBuilderImpl query1 = new QueryBuilderImpl(PersonWithSimpleProperColumns.class);
        QueryBuilderImpl query2 = new QueryBuilderImpl(PersonWithSimpleProperColumns.class);
        QueryBuilderImpl query3 = new QueryBuilderImpl(PersonWithSimpleProperColumns.class);
        QueryBuilderImpl query4 = new QueryBuilderImpl(PersonWithSimpleProperColumns.class);
        QueryBuilderImpl query5 = new QueryBuilderImpl(PersonWithSimpleProperColumns.class);


        String expected = "SELECT * FROM person WHERE p_id LIKE 's%';";
        String expected1 = "SELECT * FROM person WHERE p_id LIKE '%s';";
        String expected2 = "SELECT * FROM person WHERE p_id LIKE '%s%';";
        String expected3 = "SELECT * FROM person WHERE p_id LIKE '_s%';";
        String expected4 = "SELECT * FROM person WHERE p_id LIKE 's_%';";
        String expected5 = "SELECT * FROM person WHERE p_id LIKE 's%d';";

        String actual = query.select().where(query.getLimits().equals("id").like("starts", "s"))
                .fetch();
        String actual1 = query1.select().where(query1.getLimits().equals("id").like("ends", "s"))
                .fetch();
        String actual2 = query2.select().where(query2.getLimits().equals("id").like("any position", "s"))
                .fetch();
        String actual3 = query3.select().where(query3.getLimits().equals("id").like("position after", "2", "s"))
                .fetch();
        String actual4 = query4.select().where(query4.getLimits().equals("id").like("position before", "2", "s"))
                .fetch();
        String actual5 = query5.select().where(query5.getLimits().equals("id").like("starts and ends", "s", "d"))
                .fetch();

        Assert.assertEquals(expected, actual);
        Assert.assertEquals(expected1, actual1);
        Assert.assertEquals(expected2, actual2);
        Assert.assertEquals(expected3, actual3);
        Assert.assertEquals(expected4, actual4);
        Assert.assertEquals(expected5, actual5);
    }

    @Test
    public void selectInTest() {

        String expected = "SELECT * FROM person WHERE p_id IN ('Germany', 'France', 'UK');";
        QueryBuilderImpl query = new QueryBuilderImpl(PersonWithSimpleProperColumns.class);

        String actual = query.select().where(query.getLimits().equals("id").in("Germany", "France", "UK")).fetch();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void selectNotInTest() {

        String expected = "SELECT * FROM person WHERE p_id NOT  IN ('Germany', 'France', 'UK');";

        QueryBuilderImpl query = new QueryBuilderImpl(PersonWithSimpleProperColumns.class);
        String actual = query.select().where(query.getLimits().equals("id").not().in("Germany", "France", "UK")).fetch();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void selectInWithCompoundTest() {
        String expected = "SELECT * FROM person WHERE p_id IN (SELECT p_id FROM person WHERE p_id > 5);";

        QueryBuilderImpl query = new QueryBuilderImpl(PersonWithSimpleProperColumns.class);
        SubQuery subQuery = new SubQuery(PersonWithSimpleProperColumns.class);

        String actual = query.select().where(query.getLimits().builder().equals("id")
                .inSubQuery(subQuery.select("id").where(subQuery.getLimits().equals("id").more("5")))).fetch();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void selectNotInWithCompoundTest() {

        String expected = "SELECT * FROM person WHERE p_id NOT  IN (SELECT p_id FROM person WHERE p_id > 5);";
        QueryBuilderImpl query = new QueryBuilderImpl(PersonWithSimpleProperColumns.class);
        SubQuery subQuery = new SubQuery(PersonWithSimpleProperColumns.class);
   
        String actual = query.select().where(query.getLimits().builder().equals("id").not()
                .inSubQuery(subQuery.select("id").where(subQuery.getLimits().equals("id").more("5")))).fetch();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void selectCountTest() {

        String expected = "SELECT  COUNT (p_id), p_id FROM person;";

        QueryBuilderImpl query = new QueryBuilderImpl(PersonWithSimpleProperColumns.class);

        String actual = query.select(query.getAggregates().builder().count("id", "id")).fetch();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void selectCountGroupByTest() {

        String expected = "SELECT  COUNT (p_id), p_id FROM person GROUP BY p_id;";

        QueryBuilderImpl query = new QueryBuilderImpl(PersonWithSimpleProperColumns.class);

        String actual = query.select(query.getAggregates().builder().count("id", "id"))
                .groupBy(query.getLimits().equals("id")).fetch();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void selectCountGroupByOrderByTest() {
        String expected = "SELECT  COUNT (p_id), p_id FROM person GROUP BY p_id ORDER BY  COUNT (p_id) DESC;";

        QueryBuilderImpl query = new QueryBuilderImpl(PersonWithSimpleProperColumns.class);
        SubQuery subQuery = new SubQuery(PersonWithSimpleProperColumns.class);

        String actual = query.select(query.getAggregates().builder().count("id", "id"))
                .groupBy(query.getLimits().equals("id")).orderBy(query.getAggregates().builder().count("id"), subQuery.desc()).fetch();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void selectCountGroupByHavingOrderByTest() {
        String expected = "SELECT  COUNT (p_id), p_id FROM person GROUP BY p_id HAVING  COUNT (p_id)  > 5 ORDER BY  COUNT (p_id) DESC;";

        QueryBuilderImpl query = new QueryBuilderImpl(PersonWithSimpleProperColumns.class);
        SubQuery subQuery = new SubQuery(PersonWithSimpleProperColumns.class);

        String actual = query.select(query.getAggregates().builder().count("id", "id"))
                .groupBy(query.getLimits().equals("id")).having(query.getAggregates().builder().count("id"), query.getLimits().builder()
                        .more("5")).orderBy(query.getAggregates().builder().count("id"), subQuery.desc()).fetch();
         Assert.assertEquals(expected, actual);
    }

    @Test
    public void selectMinTest() {
        QueryBuilderImpl query = new QueryBuilderImpl(PersonWithSimpleProperColumns.class);
        String expected = "SELECT MIN(p_id) FROM person;";
        String actual = query.select(query.getAggregates().builder().selectMin("id")).fetch();


        Assert.assertEquals(expected, actual);
    }

    @Test
    public void selectMaxTest() {
        QueryBuilderImpl query = new QueryBuilderImpl(PersonWithSimpleProperColumns.class);
        String expected = "SELECT MAX(p_id) FROM person;";
        String actual = query.select(query.getAggregates().builder().selectMax("id")).fetch();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void selectAvgTest() {

        QueryBuilderImpl query = new QueryBuilderImpl(PersonWithSimpleProperColumns.class);
        String expected = "SELECT AVG(p_id) FROM person;";
        String actual = query.select(query.getAggregates().builder().avg("id")).fetch();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void selectSumTest() {
        QueryBuilderImpl query = new QueryBuilderImpl(PersonWithSimpleProperColumns.class);
        String expected = "SELECT SUM(p_id) FROM person;";
        String actual = query.select(query.getAggregates().builder().sum("id")).fetch();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void selectSumIsNullTest() {
        String expected = "SELECT SUM(p_id) IS  NULL  FROM person;";

        QueryBuilderImpl query = new QueryBuilderImpl(PersonWithSimpleProperColumns.class);

        String actual = query.selectMath(query.getAggregates().builder().sum("id"), query.getLimits().builder().is().nuLL()).fetch();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void selectSumIsNullOrNotNullTest() {

        String expected = "SELECT SUM(p_id) IS  NULL  OR  NOT  NULL  FROM person;";
        QueryBuilderImpl query = new QueryBuilderImpl(PersonWithSimpleProperColumns.class);

        String actual = query.selectMath(query.getAggregates().builder().sum("id"), query.getLimits()
                .builder().is().nuLL().or().not().nuLL()).fetch();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void selectWhereNotAndNotTest() {
        String expected = "SELECT * FROM person WHERE  NOT p_id='1' AND  NOT p_id='2';";

        QueryBuilderImpl query = new QueryBuilderImpl(PersonWithSimpleProperColumns.class);

        String actual = query.select().where(query.getLimits().builder().not().equals("id", "1")
                .and().not().equals("id", "2")).fetch();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void selectOrderByAscAndDescTest() {
        QueryBuilderImpl query = new QueryBuilderImpl(PersonWithSimpleProperColumns.class);
        String expected = "SELECT * FROM person ORDER BY p_id ASC, p_id DESC;";
        String actual = query.select().orderBy().ascAndDesc("id", "id").fetch();


        Assert.assertEquals(expected, actual);
    }

    @Test
    public void selectOrderByAscTest() {
        QueryBuilderImpl query = new QueryBuilderImpl(PersonWithSimpleProperColumns.class);
        String expected = "SELECT * FROM person ORDER BY p_id ASC;";
        String actual = query.select().orderBy().asc("id").fetch();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void selectOrderByDescTest() {
        QueryBuilderImpl query = new QueryBuilderImpl(PersonWithSimpleProperColumns.class);
        String expected = "SELECT * FROM person ORDER BY p_id DESC;";
        String actual = query.select().orderBy().desc("id").fetch();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void selectCountGroupByOrderByAscTest() {
        String expected = "SELECT  COUNT (p_id), p_id FROM person GROUP BY p_id ORDER BY  COUNT (p_id) ASC;";

        QueryBuilderImpl query = new QueryBuilderImpl(PersonWithSimpleProperColumns.class);
        SubQuery subQuery = new SubQuery(PersonWithSimpleProperColumns.class);

        String actual = query.select(query.getAggregates().builder().count("id", "id"))
                .groupBy(query.getLimits().equals("id")).orderBy(query.getAggregates().builder().count("id"), subQuery.asc()).fetch();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void test() {//todo check actual SELECT person.p_id, person.name, person.age, person.bd FROM person INNER  JOIN person ON person.p_id=person.p_id WHERE person.p_id=person.p_idp_id > 5 IN (SELECT p_id FROM person WHERE p_id);
       // QueryBuilderImpl query = new QueryBuilderImpl(PersonWithSimpleProperColumns.class);
       // SubQuery subQuery = new SubQuery(PersonWithSimpleProperColumns.class);
       // String expected = "SELECT person.p_id, person.name, person.age, " +
       //         "person.bd FROM person INNER  JOIN person " +
       //         "ON person.p_id=person.p_id WHERE p_id > 5 IN (SELECT p_id FROM person WHERE p_id);";
       // String actual = query.select().innerJoin(PersonWithSimpleProperColumns.class).on(query.getLimits()
       //         .equals("id", PersonWithSimpleProperColumns.class, "id", PersonWithSimpleProperColumns.class))
       //         .where(query.getLimits().equals("id").more("5").inSubQuery(subQuery.select("id").where(subQuery.getLimits().equals("id")))).fetch();
       // Assert.assertEquals(expected, actual);
    }

    @Test
    public void innerJoinTest() {
        QueryBuilderImpl query = new QueryBuilderImpl(PersonWithSimpleProperColumns.class);
        String expected = "SELECT person.p_id, person.name, person.age," +
                " person.bd FROM person INNER  JOIN person " +
                "ON person.p_id=person.p_id;";
        String actual = query.select().innerJoin(PersonWithSimpleProperColumns.class)
                .on(query.getLimits().equals("id", PersonWithSimpleProperColumns.class,
                        "id", PersonWithSimpleProperColumns.class)).fetch();
        Assert.assertEquals(expected, actual);

    }

    @Test
    public void leftJoinTest() {
        QueryBuilderImpl query = new QueryBuilderImpl(PersonWithSimpleProperColumns.class);
        String expected = "SELECT person.p_id, person.name, person.age," +
                " person.bd FROM person LEFT  JOIN person " +
                "ON person.p_id=person.p_id;";
        String actual = query.select().leftJoin(PersonWithSimpleProperColumns.class)
                .on(query.getLimits().equals("id", PersonWithSimpleProperColumns.class, "id", PersonWithSimpleProperColumns.class))
                .fetch();


        Assert.assertEquals(expected, actual);
    }

    @Test
    public void rightJoinTest() {
        QueryBuilderImpl query = new QueryBuilderImpl(PersonWithSimpleProperColumns.class);
        String expected = "SELECT person.p_id, person.name, person.age," +
                " person.bd FROM person RIGHT  JOIN person " +
                "ON person.p_id=person.p_id;";
        String actual = query.select().rightJoin(PersonWithSimpleProperColumns.class)
                .on(query.getLimits().equals("id", PersonWithSimpleProperColumns.class, "id", PersonWithSimpleProperColumns.class)).fetch();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void fullOuterJoinTest() {
        QueryBuilderImpl query = new QueryBuilderImpl(PersonWithSimpleProperColumns.class);
        String expected = "SELECT person.p_id, person.name, person.age," +
                " person.bd FROM person FULL  OUTER  JOIN person " +
                "ON person.p_id=person.p_id;";
        String actual = query.select().fullOuterJoin(PersonWithSimpleProperColumns.class)
                .on(query.getLimits().equals("id", PersonWithSimpleProperColumns.class, "id", PersonWithSimpleProperColumns.class)).fetch();
        Assert.assertEquals(expected.trim(), actual.trim());
    }

}
