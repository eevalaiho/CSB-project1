package sec.project.domain;

public class Account {

    public int id;
    public String name;
    public String iban;
    public Double balance;

    public Account() {
        super();
    }

    public Account(int id, String name, String iban, double balance) {
        this();
        this.id = id;
        this.name = name;
        this.iban = iban;
        this.balance = balance;
    }
}

