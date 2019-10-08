package fluentquery.Dslmpl;

import fluentquery.Dslnterfaces.*;

public class TableImpl implements Table {
    private String name;

    TableImpl(String tableName) {
        this.name = tableName;
    }

    @Override
    public String getName() {
        return name;
    }
}
