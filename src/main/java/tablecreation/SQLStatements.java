
package tablecreation;

public enum SQLStatements {
    CREATE_TABLE("CREATE TABLE "),CREATE("CREATE "), CONSTRAINT(" CONSTRAINT "),
    NOT_NULL(" NOT NULL "), PK(" PRIMARY KEY "), ON(" ON "),  FK(" FOREIGN KEY "),
    ALTER_TABLE(" ALTER TABLE "), UNIQUE(" UNIQUE "), DEFAULT(" DEFAULT "),CHECK(" CHECK "),
    INDEX(" INDEX "), A_INCREMENT(" AUTO_INCREMENT "), ADD(" ADD "), REFERENCE(" REFERENCES ");

    private String value;

    SQLStatements(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}