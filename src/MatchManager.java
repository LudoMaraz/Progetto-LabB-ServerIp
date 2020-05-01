import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.PrintWriter;

public class MatchManager {
    BufferedReader reader;
    PrintWriter writer;
    SqlDriver sqlDriver = new SqlDriver();

    public MatchManager(BufferedReader reader, PrintWriter writer) {
        this.reader = reader;
        this.writer = writer;
    }

    public boolean createMatch(JsonObject infoPartita) {
        String query = "insert into public.\"games\" (id_partita, num_giocatori, nome, num_giocatori_iscritti, is_open_game) values ('" + infoPartita.get("id_partita").getAsString() + "','" + infoPartita.get("num_player").getAsInt() + "','" +
                infoPartita.get("nome").getAsString() + "','1" + "','true')";
        boolean response = false;
        try {
            response = sqlDriver.executeBooleanQuery(query);
            if (response == true) {
                query = "create table public.\"" + infoPartita.get("id_partita").getAsString() + "\" (nickname varchar primary key, punteggio_totale int, punteggio_parziale int, num_manche int)";
                response = sqlDriver.executeBooleanQuery(query);
                if (response == true) {
                    query = "insert into public.\"" + infoPartita.get("id_partita").getAsString() + "\"(nickname, punteggio_totale, punteggio_parziale, num_manche) values ('" + infoPartita.get("player1").getAsString() + "', 0, 0, 1)";
                    response = sqlDriver.executeBooleanQuery(query);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public JsonArray visualizzaListaMatch() {
        String query = "select * from public.\"games\"";
        JsonObject infoDataReturn = new JsonObject();
        JsonArray response = new JsonArray();
        try {
            infoDataReturn.addProperty("id_partita", "String");
            infoDataReturn.addProperty("num_giocatori", "int");
            infoDataReturn.addProperty("num_giocatori_iscritti", "int");
            infoDataReturn.addProperty("nome", "String");
            response = sqlDriver.executeMultiInfoQuery(query, infoDataReturn);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public boolean partecipaMatch(JsonObject infoPartita) {
        String query = "select * from public.\"games\" where id_partita ='" + infoPartita.get("id_partita").getAsString() + "' AND is_open_game = true";
        JsonObject infoDataReturn = new JsonObject();
        boolean response = false;
        String idPlayer = infoPartita.get("id_player").getAsString();
        try {
            infoDataReturn.addProperty("id_partita", "String");
            infoDataReturn.addProperty("num_giocatori", "int");
            infoDataReturn.addProperty("num_giocatori_iscritti", "int");

            infoPartita = sqlDriver.executeInfoQuery(query, infoDataReturn);
        } catch (Exception e) {
            e.printStackTrace();
        }
        int num_giocatori_iscritti = infoPartita.get("num_giocatori_iscritti").getAsInt();

        if (infoPartita.get("num_giocatori").getAsInt() > num_giocatori_iscritti) {
            query = "update public.\"games\" set num_giocatori_iscritti = " + (num_giocatori_iscritti + 1) + "where id_partita ='" + infoPartita.get("id_partita").getAsString() + "'";
            try {
                response = sqlDriver.executeBooleanQuery(query);
                if (response == true) {
                    query = "insert into public.\"" + infoPartita.get("id_partita").getAsString() + "\"(nickname, punteggio_totale, punteggio_parziale, num_manche) values ('" + idPlayer + "', 0, 0, 1)";
                    response = sqlDriver.executeBooleanQuery(query);
                }
            } catch (Exception e) {
                writer.println("Si è verificato un errore nella registrazione alla partita");
                writer.flush();
                e.printStackTrace();
                return false;
            }

        } else {
            writer.println("Le iscrizioni alla partita sono chiuse");
            writer.flush();
        }
        return response;
    }

    public boolean leaveMatch(JsonObject infoPartita){

        JsonObject infoDataReturn = new JsonObject();
        boolean response = false;
        try{
            if (infoPartita.get("is_open_game").getAsBoolean() == true ){
                String query ="delete from public.\"" + infoPartita.get("id_partita").getAsString()+"\" where nickname = '" +infoPartita.get("id_giocatore").getAsString() +"'";
                infoDataReturn.addProperty("id_partita", "String");
                response = sqlDriver.executeBooleanQuery(query);
                int num_giocatori_iscritti = infoPartita.get("num_giocatori_iscritti").getAsInt();
                if (response == true){
                    query = "update public.\"games\" set num_giocatori_iscritti = " + (num_giocatori_iscritti - 1) + "where id_partita ='" + infoPartita.get("id_partita").getAsString() + "'";
                    response = sqlDriver.executeBooleanQuery(query);
                }
                else {
                    writer.println("Si è verificato un errore nell'abbandono della partita");
                    writer.flush();
                }
        } else {
                String query ="drop table public.\""+ infoPartita.get("id_partita").getAsString()+"\"";
                response = sqlDriver.executeBooleanQuery(query);
                if (response == true){
                    query = "delete from public.\"games\" where id_partita =  '" + infoPartita.get("id_partita").getAsString() + "'";
                    response = sqlDriver.executeBooleanQuery(query);
                }
            }
        } catch (Exception e){
            writer.println("Si è verificato un errore nell'abbandono della partita");
            writer.flush();
            e.printStackTrace();
        }

        return response;

    }
}