package eu.kraenz.moshstore.users;

public class User {
    public long id;
    public String email;
    public String name;
    public String password; // don't do this. i'm just following the tutorial

    public User(long id, String email, String name, String password) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.password = password;
    }
}
