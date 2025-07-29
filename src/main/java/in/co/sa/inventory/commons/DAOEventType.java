package in.co.sa.inventory.commons;

public enum DAOEventType {

    ADD,
    DELETE,
    UPDATE;

    boolean isAdd() {
        return this == ADD;
    }

    boolean isDelete() {
        return this == DELETE;
    }
}