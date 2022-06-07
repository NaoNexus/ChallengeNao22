import java.io.PrintWriter;
import java.net.Socket;

public class client {
    public static void main(String[] args) throws Exception {

        String[] ips = {"192.168.0.104", 
                        "192.168.0.105"};

        for (int i = 0; i<2; i++) {

            Socket s = new Socket(ips[i], 5050);
            PrintWriter writer = new PrintWriter(s.getOutputStream(), true);
            writer.print("go");
            writer.close();
            s.close();
        }
    }
}