import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.sql.*;
import java.util.Set;

public class SqlDriver {

    private final String url = "jdbc:postgresql://localhost/LaboratorioB";
    private final String user = "postgres";
    private final String password = "qwerty";

    public Connection connect() {

        Connection conn = null;
        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to the PostgreSQL server successfully.");
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }

        return conn;
    }

    public boolean disconnect(Connection conn) {
        boolean response = false;
        try {
            conn.close();
            response = true;
            System.out.println("Disconnected to the PostgreSQL server successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return response;
    }

    public JsonObject executeInfoQuery(String query, JsonObject infoDataReturn) {
        Connection con = connect();
        Statement stmt = null;
        JsonObject response = new JsonObject();
        try {
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            System.out.println(query);
            while (rs.next()) {
               response = createInfoItem(rs, infoDataReturn);
            }
        } catch (Exception e) {
            disconnect(con);
            e.printStackTrace();
        }
        return response;
    }

    public JsonArray executeMultiInfoQuery(String query, JsonObject infoDataReturn) {
        Connection con = connect();
        Statement stmt = null;
        JsonArray response = new JsonArray();
        try {
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            System.out.println(query);
            while (rs.next()) {
                response.add(createInfoItem(rs, infoDataReturn));
            }
        } catch (Exception e) {
            disconnect(con);
            e.printStackTrace();
        }
        return response;
    }

    public JsonObject createInfoItem(ResultSet rs, JsonObject infoDataReturn) {
        JsonObject element = new JsonObject();
        Set<String> keys = infoDataReturn.keySet();
        try {
            for (String item_key : keys) {
                String item_value = infoDataReturn.get(item_key).getAsString();
                if (item_value.equalsIgnoreCase("String")) {
                    element.addProperty(item_key, rs.getString(item_key));
                }
                if (item_value.equalsIgnoreCase("long")) {
                    element.addProperty(item_key, rs.getLong(item_key));
                }
                if (item_value.equalsIgnoreCase("int")) {
                    element.addProperty(item_key, rs.getInt(item_key));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return element;
    }


    public boolean executeBooleanQuery(String query) {
        Connection con = connect();
        Statement stmt = null;
        boolean response = false;
        try {
            stmt = con.createStatement();
            stmt.execute(query);
            response = true;
            disconnect(con);
        } catch (Exception e) {
            response = false;
            disconnect(con);
            e.printStackTrace();
        }
        return response;
    }
}
