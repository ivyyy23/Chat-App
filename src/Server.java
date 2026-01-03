import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class Server implements Runnable {
    static HashMap<Socket, PrintWriter> sockets = new HashMap<>();
    Socket socket;
    static Object lock = new Object();
    PrintWriter pw;

    public Server(Socket socket) throws IOException {
        this.socket = socket;
        pw = new PrintWriter(socket.getOutputStream());
        synchronized (lock) {
            sockets.put(socket, pw);
        }
    }

    @Override
    public void run() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            while (true) {
                String message = br.readLine();
                if (!message.isBlank()) {

                    PrintWriter clientPW;
                    for (Socket c : sockets.keySet()) {
                        if (!c.equals(socket)) {
                            clientPW = sockets.get(c);
                            clientPW.println(message);
                            clientPW.flush();
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("I/O Error:");
            e.printStackTrace();
        } finally {
            pw.flush();
            pw.close();
            synchronized (lock) {
                sockets.remove(socket);
            }
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
}