package transaction;

public interface Transactions {
    void begin();

    void commit();

    void rollback();
}
