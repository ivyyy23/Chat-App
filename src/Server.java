import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ConcurrentHashMap;

public class Server implements Runnable {
    private static ConcurrentHashMap<Socket, PrintWriter> sockets = new ConcurrentHashMap<>();
    private Socket socket;
    private static final Object lock = new Object();
    private PrintWriter pw;
    private static UserManager userManager;
    private String username;
    private String password;

    public Server(Socket socket) throws IOException {
        this.socket = socket;
        pw = new PrintWriter(socket.getOutputStream(), true);
        sockets.put(socket, pw);
        userManager = new UserManager();
    }

    @Override
    public void run() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            while (true) {

                String message = br.readLine();
                if (message == null) break;

                if (!message.isBlank()) {
                    if (!message.startsWith("__SYS__")) {
                        PrintWriter clientPW;
                        for (Socket c : sockets.keySet()) {
                            clientPW = sockets.get(c);
                            clientPW.println(message);
                            clientPW.flush();
                        }
                    } else {
                        userAcc(message);
                    }
                }
            }
        } catch (SocketException e) {
            if (username != null) {
                System.out.printf("%s left the chat.\n", username);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            pw.flush();
            pw.close();
            sockets.remove(socket);
        }
    }

    public static void main(String[] args) {
        try (ServerSocket ss = new ServerSocket(4242)) {
            while (true) {
                Socket socket = ss.accept();
                Server server = new Server(socket);
                Thread thread = new Thread(server);
                thread.start();
            }
        } catch (IOException e) {
            System.out.println("I/O Error:");
            e.printStackTrace();
        }
    }

    private void userAcc(String message) throws InterruptedException, IOException {

        String option = message.substring(7, 8);
        boolean success;
        if (option.equals("R")) { // option = register
            do {
                String user = message.substring(message.indexOf(":") + 1);
                username = user.substring(0, user.indexOf(":"));
                password = user.substring(user.indexOf(":") + 1);
                success = userManager.register(username, password);

                if (!success)
                    pw.println("__SYS__FAIL");
                else
                    pw.println("__SYS__SUCCESS");
            } while (!success);
        } else { // option = login
            do {
                String user = message.substring(message.indexOf(":") + 1);
                String username = user.substring(0, user.indexOf(":"));
                String password = user.substring(user.indexOf(":") + 1);
                success = userManager.login(username, password);

                if (!success)
                    pw.println("__SYS__FAIL");
                else
                    pw.println("__SYS__SUCCESS");
            } while (!success);
        }
    }
}