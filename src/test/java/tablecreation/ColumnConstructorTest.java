package tablecreation;

import exceptions.NoPrimaryKeyException;
import exceptions.WrongSQLType;
import org.junit.Test;
import tablecreation.classesintesting.PersonMissedForeignKey;
import tablecreation.classesintesting.PersonWithConstraints;
import tablecreation.classesintesting.PersonWithSeveralForeignKeys;
import tablecreation.classesintesting.PersonWithSimpleProperColumns;

import static org.junit.Assert.*;

public class ColumnConstructorTest {
    ColumnConstructor columnConstructor;

    @Test
    public void buildColumnTestAssigneNameToColumn() throws Exception {
        columnConstructor = new ColumnConstructor(PersonWithSimpleProperColumns.class.getDeclaredField("id"));
        assertEquals("p_id", columnConstructor.buildColumn().getName());
        assertNotEquals("id", columnConstructor.buildColumn().getName());
    }

    @Test
    public void buildColumnTestWithoutNameAssignment() throws Exception {
        columnConstructor = new ColumnConstructor(PersonWithSimpleProperColumns.class.getDeclaredField("age"));
        assertEquals("age", columnConstructor.buildColumn().getName());
    }


    @Test(expected = WrongSQLType.class)
    public void buildColumnTestRefernceWithoutForeignKey() throws WrongSQLType, NoPrimaryKeyException, NoSuchFieldException {
        columnConstructor = new ColumnConstructor(PersonMissedForeignKey.class.getDeclaredField("per"));
    }

    @Test
    public void buildColumnTestUniqueConstraint() throws Exception {
        columnConstructor = new ColumnConstructor(PersonWithConstraints.class.getDeclaredField("name"));
        assertTrue(columnConstructor.buildColumn().isUnique());
    }

    @Test
    public void buildColumnTestAutoIncrementConstraint() throws Exception {
        columnConstructor = new ColumnConstructor(PersonWithConstraints.class.getDeclaredField("id"));

        assertTrue(columnConstructor.buildColumn().isAutoincrement());
        assertFalse(columnConstructor.buildColumn().isUnique());
    }

    @Test
    public void buildColumnTestDefaultConstraint() throws Exception {
        columnConstructor = new ColumnConstructor(PersonWithConstraints.class.getDeclaredField("age"));
        assertEquals("18", columnConstructor.buildColumn().getDefaultValue());
        assertNotEquals("", columnConstructor.buildColumn().getDefaultValue());
    }

    @Test
    public void buildColumnTestForeignKey() throws Exception {
        columnConstructor = new ColumnConstructor(PersonWithSeveralForeignKeys.class.getDeclaredField("person1"));
        assertTrue(columnConstructor.buildColumn().isForeignKey());
    }

    @Test
    public void buildColumnTestClassNullablePrimaryKey() throws Exception {

        columnConstructor = new ColumnConstructor(PersonWithSeveralForeignKeys.class.getDeclaredField("id"));
        Column column = columnConstructor.buildColumn();
        assertFalse(column.isNullable());
    }

    @Test
    public void buildColumnTestClassWithDate() throws Exception {
        columnConstructor = new ColumnConstructor(PersonWithSimpleProperColumns.class.getDeclaredField("bd"));
        Column column = columnConstructor.buildColumn();
        assertEquals(SQLTypes.DATE, column.getType());
    }

    @Test
    public void buildColumnTestClassWith() throws Exception {
        columnConstructor = new ColumnConstructor(PersonWithSimpleProperColumns.class.getDeclaredField("bd"));
        Column column = columnConstructor.buildColumn();
        assertEquals(SQLTypes.DATE, column.getType());
    }

}