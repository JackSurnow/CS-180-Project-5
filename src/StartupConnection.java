import java.net.Socket;
import java.util.Scanner;

public class StartupConnection {

  public static void main(String[] args) {
    
        Scanner scan = new Scanner(System.in);
        // Connection to server

        boolean connected = false;
        do {
            try {
                System.out.println("Please enter an IP.");
                String ip = scan.nextLine();
                System.out.println("Please enter a port number.");
                int port = scan.nextInt();
                Socket socket = new Socket(ip, port);
                connected = true;
            } catch (Exception e) {
                connected = false;
                System.out.println("Error: invalid port/IP");
                e.printStackTrace();
            }

            scan.nextLine();
        } while (!connected);
    
  }
}
