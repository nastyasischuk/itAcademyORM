package tablecreation;

public enum SQLTypes {
    VARCHAR(255), BIGINT,SMALLINT,TINYINT,FLOAT,DOUBLE,INTEGER,BIT,DATE,TIME,TIMESTAMP;

    private int length;

    SQLTypes() {
    }

    SQLTypes(int value) {
        this.length = value;
    }
    public void setVarcharLength(int length){
        this.length = length;
    }
    public int getLength(){
        return this.length;
    }

    //todo add length to varchar;
}
