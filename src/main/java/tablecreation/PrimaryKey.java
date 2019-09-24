package tablecreation;

import java.util.ArrayList;
import java.util.List;

public class PrimaryKey {
    private List<Column> PKList;

    public PrimaryKey() {
        PKList = new ArrayList<>();
    }

    public PrimaryKey(List<Column> PKList) {
        this.PKList = PKList;
    }

    public List<Column> getPKList() {
        return PKList;
    }

    public void setPKList(List<Column> PKList) {
        this.PKList = PKList;
    }

    public void addPrimaryKey(Column primaryKey) {
        if (primaryKey != null)
            this.PKList.add(primaryKey);
    }
}
