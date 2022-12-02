package umn.ac.id.lanpu;

public class User {
    public String id, name, email;
    public int balance;

    public User(String name, String email, int balance){
//      Create user id based on time';
        this.name = name;
        this.email = email;
        this.balance = balance;
    }

    public User(String id, String name, String email, int balance) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.balance = balance;
    }

}
