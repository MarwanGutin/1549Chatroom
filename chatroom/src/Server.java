import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

// Responsible for listening to clients who want to connect and spawn threads to handle them
public class Server {

    // responsible for listening to incoming connections/clients & creating socket obj to communicate with them
    private ServerSocket serverSocket;
    // constructor to set up server socket
    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }
    // responsible for keeping server running
    public void startServer() {

        // input/output error handling
        try {
            // server needs to run indefinitely
            while (!serverSocket.isClosed()) {
                // wait for client to connect, return socket obj to communicate with client
                Socket socket = serverSocket.accept();
                System.out.println("A new client has connected");
                // each clientHandler will be responsible for communicating with client
                ClientHandler clientHandler = new ClientHandler(socket);

                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        } catch (IOException e) {

        }

    }
    // shutdown server socket after error
    public void closeServerSocket() {
        // check if socket is null
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = new ServerSocket(1234);
        Server server = new Server(serverSocket);
        server.startServer();

    }

}
