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
        String query = "insert into public.\"games\" (id_partita, num_giocatori, nome, player1) values ('" + infoPartita.get("id_partita").getAsString() + "','" + infoPartita.get("num_player").getAsInt() + "','" +
                infoPartita.get("nome").getAsString() + "','" + infoPartita.get("player1").getAsString() + "')";
        boolean response = false;
        try {
            response = sqlDriver.executeBooleanQuery(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public JsonObject visualizzaListaMatch(){
        String query = "select * from public.\"games\"";
        JsonObject infoDataReturn = new JsonObject();
        JsonObject response = new JsonObject();
        try{
            infoDataReturn.addProperty("id_partita", "String");
            infoDataReturn.addProperty("num_player", "int");
            response = sqlDriver.executeInfoQuery(query, infoDataReturn);
        } catch(Exception e){
            e.printStackTrace();
        }
        return response;
    }

    public boolean partecipaMatch(JsonObject infoPartita){
        String query = "select " + infoPartita.get("id_partita").getAsString() + "from public.\"games\" where " + infoPartita.get("num_giocatori_iscritti").getAsInt() + "< " + infoPartita.get("num_giocatori").getAsInt();
        JsonObject infoDataReturn = new JsonObject();
        JsonObject response = new JsonObject();
        try{
            infoDataReturn.addProperty("id_partita", "String");
            response = sqlDriver.executeInfoQuery(query, infoDataReturn);
        } catch(Exception e){
            e.printStackTrace();
        }
        return response;

        if(infoPartita.get("isOpenGame").getAsBoolean() == true) {
            String query2 = "update " + infoPartita.get("num_giocatori_iscritti").getAsInt() + "from \"games\"";
            boolean response2 = false;
            try {
                response2 = sqlDriver.executeBooleanQuery(query2);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response2;

        } else {
            writer.println("Le iscrizioni alla partita sono chiuse");
            writer.flush();
        }

    }
}