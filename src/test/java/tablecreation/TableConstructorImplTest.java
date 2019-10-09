package tablecreation;

import exceptions.NoPrimaryKeyException;
import exceptions.SeveralPrimaryKeysException;
import exceptions.WrongSQLType;
import org.junit.Test;
import tablecreation.classesintesting.*;

import static org.junit.Assert.*;

public class TableConstructorImplTest {
    TableConstructor tableConstructor;

    @Test
    public void buildTableTestNameOfTableFromAnnotation() throws Exception {
        tableConstructor = new TableConstructorImpl(PersonWithSimpleProperColumns.class);
        Table table = tableConstructor.buildTable();
        assertEquals("person", table.getTableName());
    }

    @Test
    public void buildTableTestNameWithoutTableFromAnnotation() throws Exception {
        tableConstructor = new TableConstructorImpl(PersonWithSeveralIndexes.class);
        Table table = tableConstructor.buildTable();
        assertEquals("PersonWithSeveralIndexes", table.getTableName());
    }

    @Test(expected = NoPrimaryKeyException.class)
    public void buildTableTestTableWithoutPrimaryKey() throws Exception {
        tableConstructor = new TableConstructorImpl(PersonWithoutPrimaryKey.class);
        tableConstructor.buildTable();
    }

    @Test
    public void buildTableTestTableWithSeveralForeignKeys() throws Exception {
        tableConstructor = new TableConstructorImpl(PersonWithSeveralForeignKeys.class);
        Table table = tableConstructor.buildTable();

        assertEquals(2, table.getForeignKeys().size());
        assertEquals("per_constr", table.getForeignKeys().get(0).getForeignKeyName());
        assertEquals("person2", table.getForeignKeys().get(1).getForeignKeyName());
    }

    @Test
    public void buildTableTestClassWithIndex() throws Exception {
        tableConstructor = new TableConstructorImpl(PersonWithIndexes.class);
        Table table = tableConstructor.buildTable();
        assertEquals(1, table.getIndexes().size());
        assertEquals("name_index", table.getIndexes().get(0).getName());
        assertFalse(table.getIndexes().get(0).isUnique());
    }

    @Test
    public void buildTableTestClassWithOneIndexesCombinedFromSeveralIdentical() throws Exception {
        tableConstructor = new TableConstructorImpl(PersonWithSeveralIndexes.class);
        Table table = tableConstructor.buildTable();
        assertEquals(1, table.getIndexes().size());
        assertEquals("name_index", table.getIndexes().get(0).getName());
    }

    @Test(expected = SeveralPrimaryKeysException.class)
    public void buildTableTestClassWithSeveralPrimaryKeys() throws Exception {
        tableConstructor = new TableConstructorImpl(PersonWithSeveralPrimaryKeys.class);
        tableConstructor.buildTable();
    }

    @Test
    public void buildTableTestCheckConstraint() throws Exception {
        tableConstructor = new TableConstructorImpl(PersonWithConstraints.class);
        Table table = tableConstructor.buildTable();
        assertNotNull(table.getCheckConstraint());
        assertEquals("age>18",table.getCheckConstraint());
    }


}