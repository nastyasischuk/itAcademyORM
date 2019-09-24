package tablecreation;

public enum SQLStatements {
    CREATETABLE("CREATETABLE TABLE "), CONSTRAINT(" CONSTRAINT "),
    NOT_NULL(" NOT NULL "), PK(" PRIMARY KEY "),
    FK(" FOREIGN KEY "), ALTER(" ALTER TABLE "), UNIQUE(" UNIQUE "), DEFAULT(" DEFAULT "),
    INDEX(" INDEX "), A_INCREMENT(" AUTO_INCREMENT "), ADD("ADD "), REFERENCE(" REFERENCES ");

    String value;

    SQLStatements(String value){
        this.value = value;
    }
}
