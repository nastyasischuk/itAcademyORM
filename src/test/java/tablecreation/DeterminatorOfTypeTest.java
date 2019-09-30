package tablecreation;

import org.junit.Test;
import tablecreation.classesintesting.PersonWithSimpleProperColumns;

import static org.junit.Assert.*;

public class DeterminatorOfTypeTest {

    @Test
    public void getSQLTypeTest() {
        int number =10;
        SQLTypes testedType = DeterminatorOfType.getSQLType(int.class);
        assertEquals(SQLTypes.INTEGER,testedType);
        assertNotEquals(SQLTypes.SMALLINT,testedType);
    }

   @Test
  public void getSQLTypeTestPrimitiveJavaType() {
      SQLTypes testedType = DeterminatorOfType.getSQLType(boolean.class);
      assertEquals(SQLTypes.BIT,testedType);
      assertNotEquals(SQLTypes.TINYINT,testedType);
  }

    @Test
    public void getSQLTypeTestNotExistingType(){
        SQLTypes testedType = DeterminatorOfType.getSQLType(PersonWithSimpleProperColumns.class);
        assertNull(testedType);
   }
}