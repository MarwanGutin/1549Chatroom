import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username;

    public Client(Socket socket, String username) {
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.username = username;
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public void sendMessage() {
        try {
            // send username to server to declare new client has joined
            bufferedWriter.write(username);
            bufferedWriter.newLine();
            bufferedWriter.flush();

            // get user input
            Scanner scanner = new Scanner(System.in);
            while (socket.isConnected()) {
                // get next message
                String messageToSend = scanner.nextLine();
                // send the message
                bufferedWriter.write(username + ": " + messageToSend);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public void listenForMessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String msgeFromGroupChat;

                while (socket.isConnected()) {
                    try {
                        msgeFromGroupChat = bufferedReader.readLine();
                        System.out.println(msgeFromGroupChat);
                    } catch (IOException e) {
                        closeEverything(socket, bufferedReader, bufferedWriter);
                    }
                }
            }
        }).start();
    }

    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your username: ");
        String username = scanner.nextLine();
        System.out.println("Enter the server's IP address: ");
        String IP = scanner.nextLine();
        System.out.println("Enter the servers port: ");
        int port = Integer.parseInt(scanner.nextLine());
        //Socket socket = new Socket("localhost", 1234);
        Socket socket = new Socket(IP, port);
        Client client = new Client(socket, username);
        System.out.println("IP: " + socket.getLocalAddress());
        System.out.println("Port: " + socket.getPort());
        client.listenForMessage();
        client.sendMessage();
    }

}
