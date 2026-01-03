import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException {
        Scanner scan = new Scanner(System.in);

        Socket socket = new Socket("localhost", 4242);
        PrintWriter pw = new PrintWriter(socket.getOutputStream());
        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        String toSend;

        new Thread (() -> {
            String message;

            try {
                while ((message = br.readLine()) != null) {
                    System.out.print("New message: ");
                    System.out.println(message);
                }
            } catch (IOException e) {
                System.out.println("Disconnected.");
            }
        }
        ).start();

        System.out.println("Enter a message:");

        while (true) {
            toSend = scan.nextLine();

            if (!toSend.isBlank()) {
                pw.println(toSend);
                pw.flush();
            }
        }
    }
}
