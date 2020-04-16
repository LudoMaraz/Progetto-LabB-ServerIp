import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerIp {

    public static void main(String[] args) throws IOException {
        if (args.length < 1) return;

        int port = Integer.parseInt(args[0]);
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        if (isAuthToConnectServer(br)) {

            try (ServerSocket serverSocket = new ServerSocket(port)) {

                System.out.println("Server is listening on port " + port);
                System.out.println("inserisci email dominio istituzionale:");
                String username_mail = br.readLine();
                System.out.println("inserisci password dominio istituzionale:");
                String password_mail = br.readLine();

                while (true) {
                    Socket socket = serverSocket.accept();
                    System.out.println("New client connected");

                    new ServerThread(socket, username_mail, password_mail).start();
                }

            } catch (IOException ex) {
                System.out.println("Server exception: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

    private static boolean isAuthToConnectServer(BufferedReader br) {
        boolean isAuth = false;
        try {

            System.out.println("Enter Username:");
            String username_client = br.readLine();
            System.out.print("Enter password:");
            String password_client = br.readLine();



            ServerManager serverManager = new ServerManager();
            if (serverManager.isAuthServer(username_client, password_client)) {
                System.out.println("Perfetto");
                isAuth = true;
            } else if (!serverManager.hasServerAuth()) {
                System.out.println("Procedo con salvataggio credenziali");
                ServerAuthCredential serverAuthCredential = new ServerAuthCredential();
                serverAuthCredential.setPassword(password_client);
                serverAuthCredential.setUsername(username_client);
                serverManager.insertNewAuth(serverAuthCredential);
                isAuth = true;
            } else {
                System.out.println("Credenziali errate");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Errore inserimento credenziali");
            return false;
        }

        return isAuth;
    }
}

