package CRUD;

import CRUD.requests.DSLImpl.StartClass;
import org.junit.Test;

import static CRUD.requests.DSLImpl.StartClass.*;
import static org.junit.Assert.assertEquals;

public class DSLQueryTest {

    @Test
    public void queryCreationTest() {
        String actualQuery = StartClass.select(count(column("col54"))).from(table("Products")).where(skip()
                .exists(select(avg(column("d")), column("col")).from(table("Orders"))
                        .where(column("product_id").equal("id")))).groupBy(column("d")).having(column("d").equal(1)).end();

        String actual2 = StartClass.select(column("col"), column("col2")).from(table("table"))
                .where(column("column").greaterOrEqual(12).and(column("column2").lessOrEqual(15))
                        .orNot(column("column3").isNotNull()).or(column("column").between(12, 56))).end();

        String expectedQuery  = "select  count(col54)  from Products where  exists  (select  avg(d) , col from Orders " +
            "where product_id = id)  group by d  having d = 1;";

        String expected2 = "select col, col2 from table where column >= 12 and column2 <= 15 or not column3 is not null  or column between 12 and 56;";

        assertEquals(actualQuery, expectedQuery);
        assertEquals(actual2, expected2);
    }
}
