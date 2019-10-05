package tablecreation;

import exceptions.NoPrimaryKeyException;
import exceptions.SeveralPrimaryKeysException;

public interface TableConstructor {
    Table buildTable() throws NoPrimaryKeyException, SeveralPrimaryKeysException;
}
