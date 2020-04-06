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
}
