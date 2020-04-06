import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.time.Instant;
import java.util.HashMap;

public class PlayerManager {

    BufferedReader reader;
    PrintWriter writer;
    SqlDriver sqlDriver = new SqlDriver();

    public boolean login(String username, String password) {
        String query = "select * from public.\"players\" where username = '" + username.trim() + "' and password = '" + password.trim() + "'";
        boolean isAuth = false;
        try {
           JsonObject infoQueryPlayer = new JsonObject();
            infoQueryPlayer.addProperty("codice_auth", "long");
            JsonObject infoPlayer = sqlDriver.executeInfoQuery(query, infoQueryPlayer);

            if (infoPlayer.size() > 0 && !infoPlayer.isJsonNull()) {
                int codice_auth = infoPlayer.get("codice_auth").getAsInt();
                if (codice_auth == 1) {
                    isAuth = true;
                } else {
                    Instant instant = Instant.now();
                    long timeStampSeconds = instant.getEpochSecond();
                    writer.println("codice_auth");
                    writer.flush();
                    String codice_player = reader.readLine();
                    if (codice_auth == Integer.parseInt(codice_player) && (timeStampSeconds - codice_auth) < 600) {
                        isAuth = true;
                        query = "update public.\"players\" set codice_auth = 1 where nickname = '" + username.trim() + "'";
                        sqlDriver.executeBooleanQuery(query);
                    } else {

                        if ((timeStampSeconds - codice_auth) < 600) {
                            System.out.println("CODICE INSERITO NON VALIDO RIPROVARE");
                        } else {
                            System.out.println("TEMPO PER L'IMMISSIONE DEL CODICE TERMINATO, RIESEGUIRE LA REGISTRAZIONE");
                            query = "delete from public.\"players\" where nickname = '" + username.trim() + "'";
                            sqlDriver.executeBooleanQuery(query);
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return isAuth;
    }

    public JsonObject getInfoPlayer(String nickname) {
        String query = "select * from public.\"players\" where nickname = '" + nickname + "'";
        JsonObject infoPlayer = null;
        try {
            System.out.println(query);
            JsonObject infoQueryPlayer = new JsonObject();
            infoQueryPlayer.addProperty("nickname", "String");
            infoQueryPlayer.addProperty("nome", "String");
            infoQueryPlayer.addProperty("cognome", "String");
            infoQueryPlayer.addProperty("email", "String");
            infoQueryPlayer.addProperty("id_number", "int");

            infoPlayer = sqlDriver.executeInfoQuery(query, infoQueryPlayer);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return infoPlayer;
    }

    public boolean hasPlayer(String nickname, String email) {
        String query = "select * from public.\"players\" where nickname = '" + nickname + "' or email = '" + email + "'";
        boolean hasAuth = false;
        try {
            System.out.println(query);
            JsonObject infoQueryPlayer = new JsonObject();
            infoQueryPlayer.addProperty("nickname", "String");
            JsonObject infoPlayer = sqlDriver.executeInfoQuery(query, infoQueryPlayer);
            if (infoPlayer.size() > 0 && !infoPlayer.isJsonNull()) {
                hasAuth = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hasAuth;
    }

    public boolean registrazione(JsonObject playerInfo, String password_email, String username_email) {
        boolean hasAuth = false;
        if (!hasPlayer(playerInfo.get("nickname").getAsString(), playerInfo.get("email").getAsString())) {
            String query = "insert into public.\"players\" (cognome, email, nickname, nome, password, codice_auth) values ('" + playerInfo.get("cognome").getAsString() + "','" + playerInfo.get("email").getAsString()
                    + "','" + playerInfo.get("nickname").getAsString() + "','" + playerInfo.get("nome").getAsString() + "','" + playerInfo.get("password").getAsString() + "','" + playerInfo.get("codice_auth").getAsLong() + "')";
            try {
                sqlDriver.executeBooleanQuery(query);
                EmailManager emailManager = new EmailManager();
                emailManager.createMail(playerInfo, password_email, username_email);
                hasAuth = true;
            } catch (Exception e) {
                hasAuth = false;
                e.printStackTrace();
            }
        } else {
            System.out.println("IL NICKNAME O L'EMAIL SONO GIA' STATO UTILIZZATO");
        }
        return hasAuth;
    }

    public PlayerManager(BufferedReader reader, PrintWriter writer) {
        this.reader = reader;
        this.writer = writer;
    }
}
