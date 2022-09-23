package pl.rengreen.taskmanager.serviceImplementation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import pl.rengreen.taskmanager.model.Task;
import pl.rengreen.taskmanager.model.User;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.util.Date;
import java.util.Properties;

@Service
@Data
@AllArgsConstructor
public class MailService {




    public void sendEmail(Task task, User user) throws MessagingException {
    Properties mailProperties = new Properties();
        mailProperties.put("mail.smtp.auth", true);
        mailProperties.put("mail.smtp.starttls.enable", "true");
        mailProperties.put("mail.smtp.host", "smtp.gmail.com");
        mailProperties.put("mail.smtp.port", "587");
        String emailID = "my task manager";

        Authenticator auth = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("developernoreplies@gmail.com", "lltwmmnlqqzrqqvs");
            }
        };
        Session session = Session.getDefaultInstance(mailProperties, auth);

        createEmail(user, task, session);

    }




    public void createEmail(User user, Task task, Session session) throws MessagingException {
        String mailSubject = task.getName();
        String emailBody = "Hello " + user.getName() + ", a new task has been added to your list" + "\n"
                + "Due date:" + task.getDate() + "\n"
                + "Details:" + task.getDescription();
        MimeMessage mimeMessage = new MimeMessage(session);
        mimeMessage.addHeader("Content-type", "text/HTML; charset=UTF-8");
        mimeMessage.addHeader("format", "flowed");
        mimeMessage.addHeader("Content-Transfer-Encoding", "8bit");

        mimeMessage.setFrom(new InternetAddress("developernoreplies@gmail.com"));
        mimeMessage.setReplyTo(InternetAddress.parse("developernoreplies@gmail.com"));
        mimeMessage.setSubject(mailSubject, "UTF-8");
        mimeMessage.setText(emailBody, "UTF-8");
        mimeMessage.setSentDate(new Date());
        mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmail(), true));

        // Create the message body part
        BodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setText(emailBody);

        // Create a multipart message for attachment
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);

        // Second part is image attachment
        messageBodyPart = new MimeBodyPart();
        String filename = "/Users/mac/Downloads/activity-manager/src/main/resources/static/images/email.png";
        DataSource source = new FileDataSource(new File(filename));
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName(filename);

        //Trick is to add the content-id header here
        messageBodyPart.setHeader("Content-ID", "image_id");
        multipart.addBodyPart(messageBodyPart);

        //third part for displaying image in the email body
        messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent("<img src='cid:image_id'>", "text/html");
        multipart.addBodyPart(messageBodyPart);

        //Set the multipart message to the email message
        mimeMessage.setContent(multipart);

        Transport.send(mimeMessage);

    }




    //    public void createEmail(String recipients, Task task, Session session) throws MessagingException {
//        String mailSubject = task.getName();
//        String emailBody = task.getDescription();
//        MimeMessage mimeMessage = new MimeMessage(session);
//        mimeMessage.addHeader("Content-type", "text/HTML; charset=UTF-8");
//        mimeMessage.addHeader("format", "flowed");
//        mimeMessage.addHeader("Content-Transfer-Encoding", "8bit");
//
//        mimeMessage.setFrom(new InternetAddress("developernoreplies@gmail.com"));
//        mimeMessage.setReplyTo(InternetAddress.parse("developernoreplies@gmail.com"));
//        mimeMessage.setSubject(mailSubject, "UTF-8");
//        mimeMessage.setText(emailBody, "UTF-8");
//        mimeMessage.setSentDate(new Date());
//        mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(recipients, true));
//        Transport.send(mimeMessage);
//
//    }
}
