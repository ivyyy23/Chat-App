import java.io.*;
import java.util.HashMap;

public class UserManager {
    private static HashMap<String, String> users;
    private static File userFile = new File("data/users.bin");

    public UserManager() throws IOException {
        loadUsers();
    }

    private void loadUsers() {
        if (!userFile.exists()) {
            users = new HashMap<>();
        } else {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(userFile))) {
                users = (HashMap<String, String>) ois.readObject();
            } catch (Exception e) {
                users = new HashMap<>();
            }
        }
    }

    private synchronized void saveToFile() throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(userFile))) {
            oos.writeObject(users);
            oos.flush();
        }
    }

    // synchronized so that no 2 threads can register at the same time with the same username
    public synchronized boolean findUsername(String username) {
        return users.containsKey(username);
    }

    public boolean register(String username, String password) throws IOException {
        if (findUsername(username)) {
            return false;  // if username is found, failure
        } else {
            users.put(username, password); // if username isn't found, save credentials to hashmap
            saveToFile();
            return true;
        }
    }

    public boolean login(String username, String password) throws IOException {
        if (!findUsername(username)) {
            return false;
        } else {
            return users.get(username).equals(password);
        }
    }
}