import java.io.*;
import java.net.*;
import java.util.*;

public class Server {

    private static Set<ClientHandler> clients =
            Collections.synchronizedSet(new HashSet<>());

    public static void main(String[] args) {
        System.out.println("Server started on port 1234...");

        try (ServerSocket serverSocket = new ServerSocket(1234)) {
            while (true) {
                Socket socket = serverSocket.accept();
                new ClientHandler(socket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    static void broadcast(String message, ClientHandler sender) {
        
        System.out.println(message);

        synchronized (clients) {
            for (ClientHandler client : clients) {
                if (client != sender) {
                    client.sendMessage(message);
                }
            }
        }
    }

    static void addClient(ClientHandler client) {
        clients.add(client);
    }

    static void removeClient(ClientHandler client) {
        clients.remove(client);
    }
}

class ClientHandler extends Thread {

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private String name;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            
            out.println("Enter your name:");
            name = in.readLine();

            Server.addClient(this);
            Server.broadcast(">>> " + name + " joined the chat!", this);

            String message;
            while ((message = in.readLine()) != null) {
                if (message.equalsIgnoreCase("/exit")) break;

                
                Server.broadcast(name + ": " + message, this);
            }

        } catch (IOException e) {
            System.out.println(name + " disconnected unexpectedly.");
        } finally {
            try { socket.close(); } catch (IOException e) {}
            Server.removeClient(this);
            Server.broadcast("<<< " + name + " left the chat.", this);
        }
    }

    void sendMessage(String message) {
        out.println(message);
    }
}
