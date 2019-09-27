package tablecreation;

public enum SQLStatements {
    CREATE("CREATE TABLE "), CONSTRAINT(" CONSTRAINT "), WHERE(" WHERE "),INTO(" INTO "),
    UPDATE(" UPDATE "),DELETE(" DELETE "),INSERT(" INSERT "), SET(" SET "), VALUES(" VALUES "),
    NOT_NULL(" NOT NULL "), PK(" PRIMARY KEY "),FROM(" FROM "), DEFAULT(" DEFAULT "),
    FK(" FOREIGN KEY "), ALTER(" ALTER TABLE "), UNIQUE(" UNIQUE "),REFERENCE(" REFERENCES "),
    INDEX(" INDEX "), A_INCREMENT(" AUTO_INCREMENT "), ADD("ADD ");

    private String value;

    SQLStatements(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
