package tablecreation;

import org.apache.log4j.Logger;
import org.junit.Test;
import tablecreation.classesintesting.PersonWithIndexes;
import tablecreation.classesintesting.PersonWithSeveralForeignKeys;

import static org.junit.Assert.*;

public class SQLTableQueryCreatorTest {
private static Logger logger = Logger.getLogger(SQLTableQueryCreatorTest.class);
@Test
public void createIndexQuery() throws Exception{
        TableConstructor tableConstructor = new TableConstructorImpl(PersonWithIndexes.class);
        Table table = tableConstructor.buildTable();
        logger.info(table.getIndexes());
        SQLTableQueryCreator creator = new SQLTableQueryCreator(table);
        logger.info(creator.createIndexQuery());
        }
        }