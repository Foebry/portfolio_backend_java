package entities;

import java.util.ArrayList;

import annotations.Column;
import annotations.Serialize;

public class Paginated<T extends Serializable> extends Serializable {

    @Column(type = "number")
    @Serialize()
    protected int page;

    @Column(type = "number")
    @Serialize()
    protected int pageSize;

    @Column(type = "number")
    @Serialize()
    protected int total;

    @Column(type = "ArrayList", subType = "Serializable")
    @Serialize()
    protected ArrayList<T> items;

    public Paginated(int page, int pageSize, int total, ArrayList<T> items) {
        this.page = page;
        this.pageSize = pageSize;
        this.total = total;
        this.items = items;
    }

    public int getpage() {
        return this.page;
    }

    public int pageSize() {
        return this.pageSize;
    }

    public int total() {
        return this.total;
    }

    public ArrayList<T> getItems() {
        return this.items;
    }

}