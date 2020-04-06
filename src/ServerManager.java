import com.google.gson.JsonObject;

import java.util.HashMap;

public class ServerManager {

    SqlDriver sqlDriver = new SqlDriver();

    public boolean isAuthServer(String username, String password) {
        String query = "select * from public.\"credenziali\" where username = '" + username.trim() + "' and password = '" + password.trim() + "'";
        boolean isAuth = false;
        try {
            System.out.println(query);
            JsonObject queryCredenziali = new JsonObject();
            queryCredenziali.addProperty("username", "string");
            JsonObject infoCredenziali = sqlDriver.executeInfoQuery(query, queryCredenziali);

            if (infoCredenziali.size() > 0 && !infoCredenziali.isJsonNull()) {
                isAuth = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isAuth;
    }

    public boolean hasServerAuth() {
        String query = "select * from public.\"credenziali\"";
        boolean hasAuth = false;
        try {
            System.out.println(query);
           JsonObject queryCredenziali = new JsonObject();
            queryCredenziali.addProperty("username", "string");
            JsonObject infoCredenziali = sqlDriver.executeInfoQuery(query, queryCredenziali);

            if (infoCredenziali.size() > 0 && !infoCredenziali.isJsonNull()) {
                hasAuth = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hasAuth;
    }

    public boolean insertNewAuth(ServerAuthCredential serverAuthCredential) {
        String query = "insert into public.\"credenziali\" values ('" + serverAuthCredential.getUsername().trim() + "','" + serverAuthCredential.getPassword() + "')";
        boolean hasAuth = false;
        try {
            hasAuth = sqlDriver.executeBooleanQuery(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hasAuth;
    }
}
