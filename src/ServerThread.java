import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sun.org.apache.xerces.internal.impl.xpath.regex.Match;

import java.io.*;
import java.net.Socket;

public class ServerThread extends Thread {
    private Socket socket;
    private String username_mail;
    private String password_mail;

    public ServerThread(Socket socket, String username_insubria, String password_insubria) {
        this.socket = socket;
        this.username_mail = username_insubria;
        this.password_mail = password_insubria;
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
                if(text.equalsIgnoreCase("visualizza_lista_match")){
                    MatchManager matchManager = new MatchManager(reader, writer);
                    writer.println(matchManager.visualizzaListaMatch());
                    writer.flush();
                }
                if(text.equalsIgnoreCase("partecipa_match")){
                    JsonObject infoMatch = gson.fromJson(reader.readLine(), JsonObject.class);
                    MatchManager matchManager = new MatchManager(reader, writer);
                    writer.println(matchManager.partecipaMatch(infoMatch) ? "ok_partecipazione_accettata" : "no");
                    writer.flush();
                }
                if(text.equalsIgnoreCase("leave_match")){
                    JsonObject infoMatch = gson.fromJson(reader.readLine(), JsonObject.class);
                    MatchManager matchManager = new MatchManager(reader, writer);
                    writer.println(matchManager.leaveMatch(infoMatch) ? "ok_match_left" : "no");
                    writer.flush();
                }
                if(text.equalsIgnoreCase("modify_data")){
                    PlayerManager playerManager = new PlayerManager(reader,writer);
                    JsonObject playerInfo = gson.fromJson(reader.readLine(), JsonObject.class);
                    System.out.println(playerInfo);
                    writer.println(playerManager.modifyProfile(playerInfo) ? "ok_dati_modificati" : "no");
                    writer.flush();
                }
                if(text.equalsIgnoreCase("reset_psw")){
                    PlayerManager playerManager = new PlayerManager(reader,writer);
                    System.out.println("Ingresso nel metodo reset psw");
                    JsonObject playerInfo = gson.fromJson(reader.readLine(), JsonObject.class);
                    System.out.println(playerInfo);
                    System.out.println("modify psw method");
                    writer.println(playerManager.resetPsw(playerInfo) ? "ok_reset_psw_effettuato" : "no");
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