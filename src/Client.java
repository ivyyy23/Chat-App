import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        try (
                Socket socket = new Socket("localhost", 4242);
                PrintWriter pw = new PrintWriter(socket.getOutputStream());
                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        ) {

            String message;

            do {
                System.out.println("Enter your message:");
                message = scan.nextLine();
                pw.println(message);
            } while (!message.isEmpty());

            pw.flush();

        } catch (UnknownHostException e) {
            System.out.println("Host not found.");
        } catch (IOException e) {
            System.out.println("I/O Error:");
            e.printStackTrace();
        }
    }
}
