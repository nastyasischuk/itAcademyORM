package transaction;

public interface Transactions {
    public abstract void begin();
    public abstract void commit();
    public abstract void rollback();
}
