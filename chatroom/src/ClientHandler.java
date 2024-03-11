import java.io.*;
import java.lang.reflect.Array;
import java.net.Socket;
import java.util.ArrayList;

// Runnable allows instances to be executed by separate threads
public class ClientHandler implements Runnable{

    // static array list of every clientHandler instance, looped through to broadcast messages
    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    // passed from server class
    private Socket socket;
    //used to read data (messages sent from client)
    private BufferedReader bufferedReader;
    // used to send data (messages) to other clients
    private BufferedWriter bufferedWriter;
    // client username
    private String clientUsername;

    public ClientHandler(Socket socket) {
        try {
            this.socket = socket;
            // stream = byte stream, writer = character stream
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // set the username
            this.clientUsername = bufferedReader.readLine();
            // add this clientHandler to the list
            clientHandlers.add(this);
            // display that new user has connected
            broadcastMessage("SERVER: " + clientUsername + " has entered the chat!");
        } catch (IOException e) {
            // shutdown everything
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    @Override
    public void run() {
        // separate thread waiting for messages, and another for working w the program
        // used to hold message received from client
        String messageFromClient;

        // listening for messages from the client
        while (socket.isConnected()) {
            try {
                messageFromClient = bufferedReader.readLine();
                broadcastMessage(messageFromClient);
            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
                break;
            }
        }
    }

    public void broadcastMessage(String messageToSend) {
        // loop through clientHandler list
        for (ClientHandler clientHandler: clientHandlers) {
            try {
                if (!clientHandler.clientUsername.equals(clientUsername)) {
                    clientHandler.bufferedWriter.write(messageToSend);
                    clientHandler.bufferedWriter.newLine();
                    // send message even if stream isn't full
                    clientHandler.bufferedWriter.flush();
                }
            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
            }
        }
    }

    public void removeClientHandler() {
        clientHandlers.remove(this);
        broadcastMessage("SERVER: " + clientUsername + " has left the chat!");
    }

    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        removeClientHandler();
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

}
