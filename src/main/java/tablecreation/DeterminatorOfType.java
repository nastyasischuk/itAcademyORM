package tablecreation;

import java.util.HashMap;
import java.util.Map;
import java.sql.*;
public class DeterminatorOfType {
   private static final Map<Class<?>, SQLTypes> map= new HashMap<>();

    public static SQLTypes getSQLType(Class<?> c){
        return map.get(c);
    }


    static {
        map.put(Integer.class,SQLTypes.INTEGER);
        map.put(int.class,SQLTypes.INTEGER);
        map.put(Boolean.class,SQLTypes.BIT);
        map.put(boolean.class,SQLTypes.BIT);
        map.put(float.class,SQLTypes.FLOAT);
        map.put(Float.class,SQLTypes.FLOAT);
        map.put(double.class,SQLTypes.DOUBLE);
        map.put(Double.class,SQLTypes.DOUBLE);
        map.put(byte.class,SQLTypes.TINYINT);
        map.put(Byte.class,SQLTypes.TINYINT);
        map.put(long.class,SQLTypes.BIGINT);
        map.put(Long.class,SQLTypes.BIGINT);
        map.put(short.class,SQLTypes.SMALLINT);
        map.put(Short.class,SQLTypes.SMALLINT);
        map.put(String.class,SQLTypes.VARCHAR);

        map.put(Date.class,SQLTypes.DATE);
        map.put(Time.class,SQLTypes.TIME);
        map.put(Timestamp.class,SQLTypes.TIMESTAMP);

    }
}
