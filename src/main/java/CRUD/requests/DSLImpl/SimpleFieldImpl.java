package CRUD.requests.DSLImpl;

import CRUD.requests.DSLInterfaces.*;

public class SimpleFieldImpl implements SimpleField {
    private String name;

    SimpleFieldImpl(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
