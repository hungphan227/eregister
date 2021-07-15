import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Test {

    public static void main(String[] args) throws Exception {
        int n = 112;
        if ((n & 1 ) == 0) {
            System.out.println("even");
        }
    }

    public static void checkTry() {
        try {
            return;
        } finally {
            System.out.println("final");
        }
    }

    public static void startServer() {
        try {
            ServerSocket serverSocket = new ServerSocket(1234);
            Socket socket = serverSocket.accept();
//            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            DataInputStream input = new DataInputStream(socket.getInputStream());
            String s = input.readUTF();
            System.out.println("Server receive: " + s);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void startClient() {
        try {
            Socket socket = new Socket("localhost", 1234);
//            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
//            String s = "abc";
//            dataOutputStream.writeUTF(s);
            System.out.println("Client ends");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

class Parent {
    public static String getName() {
        return "parent";
    }
}

class Child extends Parent {
    public static String getName() {
        return "child";
    }
}
