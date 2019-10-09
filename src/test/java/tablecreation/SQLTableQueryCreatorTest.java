package tablecreation;

import org.apache.log4j.Logger;
import org.junit.Test;
import tablecreation.classesintesting.PersonWithIndexes;
import tablecreation.classesintesting.PersonWithSeveralForeignKeys;

import static org.junit.Assert.*;

public class SQLTableQueryCreatorTest {
@Test
public void createIndexQuery() throws Exception{
        TableConstructor tableConstructor = new TableConstructorImpl(PersonWithIndexes.class);
        Table table = tableConstructor.buildTable();
        assertEquals(1,table.getIndexes().size());
        SQLTableQueryCreator creator = new SQLTableQueryCreator(table);
        assertEquals("CREATE  INDEX name_index ON (name);",creator.createIndexQuery().get(0));
        }
        }