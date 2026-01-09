import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Client {
    private Socket socket;
    private PrintWriter pw;
    private BufferedReader br;
    private BlockingQueue<String> systemQueue;
    private String username;
    private String password;
    private MessageListener listener;

    public Client() throws IOException {
        socket = new Socket("localhost", 4242);
        pw = new PrintWriter(socket.getOutputStream(), true);
        br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        systemQueue = new LinkedBlockingQueue<>();

        startListening();
    }

    // GUI calls this method
    public void setMessageListener(MessageListener listener) {
        this.listener = listener;  // Store the GUI (or whoever)
    }

    public boolean register(String u, String p) throws InterruptedException {
        String sysMessage;
        pw.println(String.format("__SYS__R:%s:%s\n", u, p));
        sysMessage = systemQueue.take();

        if (sysMessage.equals("__SYS__FAIL")) {
            return false;
        }

        this.username = u;
        this.password = p;
        return true;
    }

    public boolean login(String u, String p) throws InterruptedException {
        String sysMessage;
        pw.println(String.format("__SYS__L:%s:%s\n", u, p));
        sysMessage = systemQueue.take();

        if (sysMessage.equals("__SYS__FAIL")) {
            return false;
        }

        this.username = u;
        this.password = p;
        return true;
    }

    // background thread - reads input stream for messages, login / registration credentials
    public void startListening() {
        new Thread (() -> {
            String message;

            try {
                while ((message = br.readLine()) != null) {
                    if (!message.startsWith("__SYS__")) {
                        // sends the message to the listener, aka, the GUI
                        if (listener != null) {
                            listener.onMessageReceived(message);
                        }
                    } else {
                        systemQueue.put(message);
                    }
                }
            } catch (IOException e) {
                System.out.println("Disconnected.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        ).start();
    }

    public void sendMessage(String message) {
        pw.println(String.format("%s: %s", username, message));
    }
}
