package annotations;

public class AssociatedClass {
    private String associatedTableName;
    private String joinColumn;
    private String inverseJoinColumns;

    public AssociatedClass(String associatedTableName, String joinColumn, String inverseJoinColumns) {
        this.associatedTableName = associatedTableName;
        this.joinColumn = joinColumn;
        this.inverseJoinColumns = inverseJoinColumns;
    }

    public String getAssociatedTableName() {
        return associatedTableName;
    }

    public String getJoinColumn() {
        return joinColumn;
    }

    public String getInverseJoinColumns() {
        return inverseJoinColumns;
    }

    @Override
    public String toString() {
        return "AssociatedClass{" +
                "associatedTableName='" + associatedTableName + '\'' +
                ", joinColumn='" + joinColumn + '\'' +
                ", inverseJoinColumns='" + inverseJoinColumns + '\'' +
                '}';
    }
}
