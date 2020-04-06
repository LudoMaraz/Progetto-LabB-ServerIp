import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.*;
import java.net.Socket;

public class ServerThread extends Thread {
    private Socket socket;

    public ServerThread(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        Gson gson = new Gson();
        try {
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            OutputStream output = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);
            String text = "";
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            System.out.println("Enter Username email istituzionale:");
            String username_mail = br.readLine();
            System.out.print("Enter password email istituzionale:");
            String password_mail = br.readLine();
            do {
                text = reader.readLine();
                System.out.println(text);
                if(text.equalsIgnoreCase("login")) {
                    System.out.println("Ingresso in metodo login");
                    PlayerManager playerManager = new PlayerManager(reader, writer);
                    JsonObject credenziali = gson.fromJson(reader.readLine(), JsonObject.class);
                    writer.println(playerManager.login(credenziali.get("username").getAsString(), credenziali.get("password").getAsString()) ? "autorizzato" : "no");
                    writer.flush();
                    writer.println(playerManager.getInfoPlayer(credenziali.get("username").getAsString()));
                    writer.flush();
                } else if (text.equalsIgnoreCase("registrazione")) {
                    PlayerManager playerManager = new PlayerManager(reader, writer);
                    System.out.println("Ingresso in metodo registrazione");
                    JsonObject playerInfo = gson.fromJson(reader.readLine(), JsonObject.class);
                    System.out.println(playerInfo);
                    writer.println(playerManager.registrazione(playerInfo, password_mail, username_mail) ? "registrato" : "no");
                    writer.flush();
                }
                if (text.equalsIgnoreCase("create_match")) {
                    JsonObject infoMatch = gson.fromJson(reader.readLine(), JsonObject.class);
                    MatchManager matchManager = new MatchManager(reader, writer);
                    writer.println(matchManager.createMatch(infoMatch) ? "ok_match_create" : "no");
                    writer.flush();
                }
            } while (!text.equals("bye"));

            socket.close();
        } catch (Exception ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}