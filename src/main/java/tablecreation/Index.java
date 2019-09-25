package tablecreation;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Index {
    private String name;
    private List<Column> columnsInIndex;
    private boolean unique;

    public Index(String name,boolean unique){
        this.name = name;
        columnsInIndex = new ArrayList<>();
    }

 public void addColumns(Column column){
        columnsInIndex.add(column);
 }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Index index = (Index) o;
        return name.equals(index.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public String getName() {
        return name;
    }

    public boolean isUnique() {
        return unique;
    }
}
