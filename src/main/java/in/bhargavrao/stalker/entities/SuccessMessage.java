package in.bhargavrao.stalker.entities;

import java.util.ArrayList;
import java.util.List;

public class SuccessMessage extends  Message{
    List<Item> items;
    Integer quota;

    public SuccessMessage(){
        items = new ArrayList<Item>();
    }

    public SuccessMessage(List<Item> items, Integer quota) {
        this.items = items;
        this.quota = quota;
    }

    public Integer getQuota() {
        return quota;
    }

    public void setQuota(Integer quota) {
        this.quota = quota;
    }

    public void addItem(Item item){
        this.items.add(item);
    }

    public List<Item> getItems(){
        return items;
    }

    @Override
    public String toString() {
        return "SuccessMessage{" +
                "items=" + items +
                ", quota=" + quota +
                '}';
    }
}
