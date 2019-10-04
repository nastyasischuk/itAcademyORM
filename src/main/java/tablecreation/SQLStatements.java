package tablecreation;

public enum SQLStatements {

    CREATE_TABLE("CREATE TABLE "),
    CREATE("CREATE "),
    WHERE(" WHERE "),
    INTO(" INTO "),
    SET(" SET "),
    VALUES(" VALUES "),
    FROM(" FROM "),
    CONSTRAINT(" CONSTRAINT "),
    NOT_NULL(" NOT NULL "),
    PK(" PRIMARY KEY "),
    ON(" ON "),
    FK(" FOREIGN KEY "),
    ALTER_TABLE(" ALTER TABLE "),
    UNIQUE(" UNIQUE "),
    DEFAULT(" DEFAULT "),
    CHECK(" CHECK "),
    INDEX(" INDEX "),
    A_INCREMENT(" AUTO_INCREMENT "),
    ADD(" ADD "),
    REFERENCE(" REFERENCES "),
    UPDATE(" UPDATE "),
    DELETE(" DELETE "),
    INSERT(" INSERT "),
    SELECT(" SELECT "),
    MIN(" MIN "),
    MAX(" MAX "),
    IN(" IN "),
    NOT(" NOT "),
    BETWEEN(" BETWEEN "),
    AND(" AND "),
    OR(" OR "),
    ORDER_BY(" ORDER BY "),
    GROUP_BY(" GROUP BY "),
    ASC(" ASC "),
    DESC(" DESC "),
    IS(" IS "),
    NULL(" NULL "),
    HAVING(" HAVING "),
    COUNT(" COUNT "),
    AVG(" AVG "),
    SUM(" SUM "),
    EXISTS(" EXISTS "),
    ANY(" ANY "),
    ALL(" ALL "),
    ON_DELETE_CASCADE(" ON DELETE CASCADE ");//TODO Create few more requests

    private String value;

    SQLStatements(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}