import com.google.gson.JsonObject;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.*;
import java.util.Properties;

public class EmailManager {

    public void createMail(JsonObject infoMail, String password_user, String username_user) {

        try {
            String password = password_user;
            String username = username_user;
            String subject = "CODICE DI CONFERMA REGISTRAZIONE";
            String to = "marazziludovico@gmail.com";
            String body = "Ciao " + infoMail.get("nome").getAsString() + ",<br/> la tua registrazione è quasi completata, esegui la login e inserisci questo codice: <br/> <p style=\"color=red;\">" + infoMail.get("codice_auth").getAsString() + "</p>";

            final JTextField uf = new JTextField(username);
            final JPasswordField pf = new JPasswordField(password);
            final JTextField tf = new JTextField(to);
            final JTextField sf = new JTextField(subject);
            final JTextArea bf = new JTextArea(null, body, 10, 20);

            Object[] message = {
                    "Username / From:", uf,
                    "Password:", pf,
                    "To:", tf,
                    "Subject:", sf,
                    "Body:", bf
            };

            // serve per la grafica

            /* int option = JOptionPane.showOptionDialog(null, message, "Send email",
                    JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new String[]{"Send", "Cancel"}, "Send");
            System.out.println("ARRIVO FINO A QUI 1");
            if (option == JOptionPane.YES_OPTION) {

            } */
            password = new String(pf.getPassword());
            username = uf.getText();
            to = tf.getText();
            subject = sf.getText();
            body = bf.getText();
            sendEmail(username, password, to, subject, body);

        } catch (Exception e) {
            System.err.println("SMTP SEND FAILED:");
            System.err.println(e.getMessage());
            e.printStackTrace();

        }
    }

    public static void sendEmail(String usr, String pwd, String to, String subject, String body) {
        try {
            String password = pwd;
            String username = usr;

            String host = "smtp.office365.com";
            String from = username;

            Properties props = System.getProperties();
            props.put("mail.smtp.host", host);
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.port", 587);

            Session session = Session.getInstance(props);

            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(from));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));
            msg.setSubject(subject);
            msg.setContent(body, "text/html; charset=utf-8");

            Transport.send(msg, username, password);
            System.out.println("\nMail was sent successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
