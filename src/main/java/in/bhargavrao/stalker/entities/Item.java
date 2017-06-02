package in.bhargavrao.stalker.entities;


public class Item {

    private String username;
    private String link;


    public Item(String username, String link) {
        this.username = username;
        this.link = link;
    }

    public Item() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public String toString() {
        return "Item{" +
                "username='" + username + '\'' +
                ", link='" + link + '\'' +
                '}';
    }
}
