package net.pinger.quests.storage.credentials;

public class StorageCredentials {
    private final String host;
    private final String username;
    private final String password;

    public StorageCredentials(String host, String username, String password) {
        this.host = host;
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public String getHost() {
        return this.host;
    }

}
