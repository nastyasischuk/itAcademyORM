package CRUD;

import CRUD.buildingObject.ObjectBuilder;
import org.junit.Test;
import java.util.Date;
import static org.junit.Assert.*;

public class ObjectBuilderTest {
    @Test
    public void constructResultSetMethodName() {
        Class classToTest = Date.class;
        ObjectBuilder objectBuilder = new ObjectBuilder();
        String resultName = objectBuilder.constructResultSetMethodName(classToTest);
        assertNull(resultName);
    }

}