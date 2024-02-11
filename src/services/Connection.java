package services;

public class Connection {
    String host;

    String username;

    String password;

    String port;

    String database;

    public Connection() {
        this.host = "127.0.0.1";
        this.username = "local";
        this.password = "local";
        this.port = "3306";
        this.database = "portfolio";
        this.connect();
    }

    private void connect() {

    }

}
