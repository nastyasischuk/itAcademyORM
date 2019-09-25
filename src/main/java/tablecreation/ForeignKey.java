package tablecreation;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ForeignKey that = (ForeignKey) o;
        return Objects.equals(tableName, that.tableName) &&
                Objects.equals(constructionName, that.constructionName) &&
                Objects.equals(foreignKeyName, that.foreignKeyName) &&
                Objects.equals(referenceTableName, that.referenceTableName) &&
                Objects.equals(referencePKName, that.referencePKName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tableName, constructionName, foreignKeyName, referenceTableName, referencePKName);
    }
}
