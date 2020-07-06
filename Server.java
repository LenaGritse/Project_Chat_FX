package server;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;

public class Server {
    private List<ClientHandler> clients;
    private AuthService authService;
    private String nick;

    public AuthService getAuthService() {
        return authService;
    }

    public void adresMes(String fromNick, String msg) {
        String[] mesAdres = msg.split(" ");
        if (mesAdres.length < 3) return;

        String toNick = mesAdres[1];
        String mess = msg.substring(toNick.length() + 3);

        for (ClientHandler client : clients) {
            String clientNick = client.getNick();
            if (clientNick.equals(toNick) || clientNick.equals(fromNick))
                client.sendMsg("from: " + fromNick + " to: " + toNick + " " + mess);
        }
    }

    public Server() {
        clients = new Vector<>();
        authService = new SimpleAuthService();

        ServerSocket server = null;
        Socket socket;

        final int PORT = 8189;

        try {
            server = new ServerSocket(PORT);
            System.out.println("Сервер запущен!");

            while (true) {
                socket = server.accept();
                System.out.println("Клиент подключился");
                System.out.println("socket.getRemoteSocketAddress(): "+socket.getRemoteSocketAddress());
                System.out.println("socket.getLocalSocketAddress() "+socket.getLocalSocketAddress());
                new ClientHandler(this, socket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    void broadcastMsg(String nick, String msg) {
        if (msg.startsWith("/w")) {
            adresMes(nick, msg);
        } else
            for (ClientHandler client : clients) {
                    client.sendMsg(nick + ": " + msg);
            }
    }

        public void subscribe (ClientHandler clientHandler){
            clients.add(clientHandler);
        }

        public void unsubscribe (ClientHandler clientHandler){
            clients.remove(clientHandler);
        }

    }