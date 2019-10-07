package tablecreation;

import java.util.Objects;

public class ManyToMany {
    private String OriginalTableName;
    private String PrimaryKeyOfOriginalTableName;
    private String LinkedTableName;
    private String PrimaryKeyOfLinkedTableName;
    private String AssociatedTableName;
    private String ForeignKeyToOriginalTableName;
    private String ForeignKeyToLinkedTableName;
    private String typeOfPKOriginal;
    private String typeOfPKLinked;

    public ManyToMany(String originalTableName, String primaryKeyOfOriginalTableName, String linkedTableName, String primaryKeyOfLinkedTableName, String associatedTableName, String foreignKeyToOriginalTableName, String foreignKeyToLinkedTableName, String typeOfPKOriginal, String typeOfPKLinked) {
        OriginalTableName = originalTableName;
        PrimaryKeyOfOriginalTableName = primaryKeyOfOriginalTableName;
        LinkedTableName = linkedTableName;
        PrimaryKeyOfLinkedTableName = primaryKeyOfLinkedTableName;
        AssociatedTableName = associatedTableName;
        ForeignKeyToOriginalTableName = foreignKeyToOriginalTableName;
        ForeignKeyToLinkedTableName = foreignKeyToLinkedTableName;
        this.typeOfPKOriginal = typeOfPKOriginal;
        this.typeOfPKLinked = typeOfPKLinked;
    }

    public ManyToMany() {
    }

    public String getOriginalTableName() {
        return OriginalTableName;
    }

    public void setOriginalTableName(String originalTableName) {
        OriginalTableName = originalTableName;
    }

    public String getPrimaryKeyOfOriginalTableName() {
        return PrimaryKeyOfOriginalTableName;
    }

    public void setPrimaryKeyOfOriginalTableName(String primaryKeyOfOriginalTableName) {
        PrimaryKeyOfOriginalTableName = primaryKeyOfOriginalTableName;
    }

    public String getLinkedTableName() {
        return LinkedTableName;
    }

    public void setLinkedTableName(String linkedTableName) {
        LinkedTableName = linkedTableName;
    }

    public String getPrimaryKeyOfLinkedTableName() {
        return PrimaryKeyOfLinkedTableName;
    }

    public void setPrimaryKeyOfLinkedTableName(String primaryKeyOfLinkedTableName) {
        PrimaryKeyOfLinkedTableName = primaryKeyOfLinkedTableName;
    }

    public String getAssociatedTableName() {
        return AssociatedTableName;
    }

    public void setAssociatedTableName(String associatedTableName) {
        AssociatedTableName = associatedTableName;
    }

    public String getForeignKeyToOriginalTableName() {
        return ForeignKeyToOriginalTableName;
    }

    public void setForeignKeyToOriginalTableName(String foreignKeyToOriginalTableName) {
        ForeignKeyToOriginalTableName = foreignKeyToOriginalTableName;
    }

    public String getForeignKeyToLinkedTableName() {
        return ForeignKeyToLinkedTableName;
    }

    public void setForeignKeyToLinkedTableName(String foreignKeyToLinkedTableName) {
        ForeignKeyToLinkedTableName = foreignKeyToLinkedTableName;
    }

    public String getTypeOfPKOriginal() {
        return typeOfPKOriginal;
    }

    public void setTypeOfPKOriginal(String typeOfPKOriginal) {
        this.typeOfPKOriginal = typeOfPKOriginal;
    }

    public String getTypeOfPKLinked() {
        return typeOfPKLinked;
    }

    public void setTypeOfPKLinked(String typeOfPKLinked) {
        this.typeOfPKLinked = typeOfPKLinked;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        ManyToMany that = (ManyToMany) object;
        return Objects.equals(OriginalTableName, that.OriginalTableName) &&
                Objects.equals(PrimaryKeyOfOriginalTableName, that.PrimaryKeyOfOriginalTableName) &&
                Objects.equals(LinkedTableName, that.LinkedTableName) &&
                Objects.equals(PrimaryKeyOfLinkedTableName, that.PrimaryKeyOfLinkedTableName) &&
                Objects.equals(AssociatedTableName, that.AssociatedTableName) &&
                Objects.equals(ForeignKeyToOriginalTableName, that.ForeignKeyToOriginalTableName) &&
                Objects.equals(ForeignKeyToLinkedTableName, that.ForeignKeyToLinkedTableName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(OriginalTableName, PrimaryKeyOfOriginalTableName, LinkedTableName, PrimaryKeyOfLinkedTableName, AssociatedTableName, ForeignKeyToOriginalTableName, ForeignKeyToLinkedTableName);
    }

    @Override
    public String toString() {
        return "ManyToMany{" +
                "OriginalTableName='" + OriginalTableName + '\'' +
                ", PrimaryKeyOfOriginalTableName='" + PrimaryKeyOfOriginalTableName + '\'' +
                ", LinkedTableName='" + LinkedTableName + '\'' +
                ", PrimaryKeyOfLinkedTableName='" + PrimaryKeyOfLinkedTableName + '\'' +
                ", AssociatedTableName='" + AssociatedTableName + '\'' +
                ", ForeignKeyToOriginalTableName='" + ForeignKeyToOriginalTableName + '\'' +
                ", ForeignKeyToLinkedTableName='" + ForeignKeyToLinkedTableName + '\'' +
                ", typeOfPKOriginal='" + typeOfPKOriginal + '\'' +
                ", typeOfPKLinked='" + typeOfPKLinked + '\'' +
                '}';
    }
}
