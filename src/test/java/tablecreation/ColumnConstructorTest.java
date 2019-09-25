package tablecreation;

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
    public void buildColumnTestAssigneNameToColumn() {
        try {

            columnConstructor = new ColumnConstructor(PersonWithSimpleProperColumns.class.getDeclaredField("id"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals("p_id", columnConstructor.buildColumn().getName());
        assertNotEquals("id", columnConstructor.buildColumn().getName());
    }


    @Test(expected = WrongSQLType.class)
    public void buildColumnTestRefernceWithoutForeignKey() throws WrongSQLType {
        try {
            columnConstructor = new ColumnConstructor(PersonMissedForeignKey.class.getDeclaredField("per"));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void buildColumnTestUniqueConstraint() {
        try {
            columnConstructor = new ColumnConstructor(PersonWithConstraints.class.getDeclaredField("name"));
        } catch (NoSuchFieldException | WrongSQLType e) {
            e.printStackTrace();
        }
        assertTrue(columnConstructor.buildColumn().isUnique());
    }

    @Test
    public void buildColumnTestAutoIncrementConstraint() {
        try {
            columnConstructor = new ColumnConstructor(PersonWithConstraints.class.getDeclaredField("id"));
        } catch (NoSuchFieldException | WrongSQLType e) {
            e.printStackTrace();
        }
        assertTrue(columnConstructor.buildColumn().isAutoincrement());
        assertFalse(columnConstructor.buildColumn().isUnique());
    }

    @Test
    public void buildColumnTestDefaultConstraint() {
        try {
            columnConstructor = new ColumnConstructor(PersonWithConstraints.class.getDeclaredField("age"));
        } catch (NoSuchFieldException | WrongSQLType e) {
            e.printStackTrace();
        }
        assertEquals("18", columnConstructor.buildColumn().getDefaultValue());
        assertNotEquals("", columnConstructor.buildColumn().getDefaultValue());
    }

    @Test
    public void buildColumnTestForeignKey() {
        try {
            columnConstructor = new ColumnConstructor(PersonWithSeveralForeignKeys.class.getDeclaredField("person1"));
        } catch (NoSuchFieldException | WrongSQLType e) {
            e.printStackTrace();
        }
        assertTrue(columnConstructor.buildColumn().isForeignKey());
    }
    @Test
    public void buildColumnTestClassNullablePrimaryKey(){
        try {
            columnConstructor = new ColumnConstructor(PersonWithSeveralForeignKeys.class.getDeclaredField("id"));
        } catch (NoSuchFieldException | WrongSQLType e) {
            e.printStackTrace();
        }
        Column column = columnConstructor.buildColumn();
        assertFalse(column.isNullable());
    }


}