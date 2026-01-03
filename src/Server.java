import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Server {
    public static void main(String[] args) {
        try (ServerSocket ss = new ServerSocket(4242);) {
            System.out.println("Waiting for someone to join");
            while (true) {
                Socket socket = ss.accept();
            }
        } catch (UnknownHostException e) {
            System.out.println("Host not found.");
        } catch (IOException e) {
            System.out.println("I/O Error:");
            e.printStackTrace();
        }
    }
}