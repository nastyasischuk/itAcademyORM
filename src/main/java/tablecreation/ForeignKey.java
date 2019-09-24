package tablecreation;

public class ForeignKey {
    private String tableName;
    private String constructionName;
    private String foreignKeyName;
    private String referenceTableName;
    private String referencePKName;

    public ForeignKey() {
    }

    public ForeignKey(String tableName, String constructionName, String foreignKeyName, String referenceTableName, String referencePKName) {
        this.tableName = tableName;
        this.constructionName = constructionName;
        this.foreignKeyName = foreignKeyName;
        this.referenceTableName = referenceTableName;
        this.referencePKName = referencePKName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getConstructionName() {
        return constructionName;
    }

    public void setConstructionName(String constructionName) {
        this.constructionName = constructionName;
    }

    public String getForeignKeyName() {
        return foreignKeyName;
    }

    public void setForeignKeyName(String foreignKeyName) {
        this.foreignKeyName = foreignKeyName;
    }

    public String getReferenceTableName() {
        return referenceTableName;
    }

    public void setReferenceTableName(String referenceTableName) {
        this.referenceTableName = referenceTableName;
    }

    public String getReferencePKName() {
        return referencePKName;
    }

    public void setReferencePKName(String referencePKName) {
        this.referencePKName = referencePKName;
    }
}
