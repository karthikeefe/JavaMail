package Mail.Mail;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.itextpdf.html2pdf.HtmlConverter;

public class SentMail {

	static Properties props;
	
	
	public static void main(String[] args) throws IOException {
		props();
		// Add recipient
		 String to = props.getProperty("to");
		 String cc = props.getProperty("cc");

		// Add sender
		 String from = props.getProperty("from");
		 final String username = props.getProperty("username");//your Gmail username 
		 final String password = props.getProperty("password");//your Gmail password

		String host = props.getProperty("host");

		Properties propts = new Properties();
		 propts.put("mail.smtp.auth", "true");
		 propts.put("mail.smtp.starttls.enable", "true"); 
		 propts.put("mail.smtp.host", host);
		 propts.put("mail.smtp.port", props.getProperty("port"));

		// Get the Session object
		 Session session = Session.getInstance(propts,
		 new javax.mail.Authenticator() {
		 protected PasswordAuthentication getPasswordAuthentication() {
		 return new PasswordAuthentication(username, password);
		 }
		 });

		try {
		 // Create a default MimeMessage object
		MimeMessage message = new MimeMessage(session);
		 
		 message.setFrom(new InternetAddress(from));
		 
		 message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(to));
		 message.setRecipients(Message.RecipientType.CC,InternetAddress.parse(cc));
		 
		 // Set Subject
		 message.setSubject("https://hclfirstcareers.com/fresher-engineering-campaign/?utm_source=EmployeeReferrer&utm_medium=Email&utm_campaign=PANIndia");
		String msg = "<!DOCTYPE html>\r\n" + 
				"<html>\r\n" + 
				"<head>\r\n" + 
				"<title>Page Title</title>\r\n" + 
				"</head>\r\n" + 
				"<body>\r\n" + 
				"\r\n" + 
				"<h1>This is a Heading</h1>\r\n" + 
				"<p>This is a paragraph.</p>\r\n" + 
				"\r\n" + 
				"</body>\r\n" + 
				"</html>";
		 
		// create a multipart message
		 Multipart multipart = new MimeMultipart("mixed");
		 
		/*//text msg part
		//Html Body Part
		 MimeBodyPart messageBodyPart = new MimeBodyPart();
		 messageBodyPart.setContent(msg, "text/html; charset=utf-8");
		 multipart.addBodyPart(messageBodyPart, 0);*/
		 
		// specify your file
				 MimeBodyPart attBodyPart = new MimeBodyPart();
				 DataSource source = new FileDataSource(props.getProperty("filepath"));
				 attBodyPart.setDataHandler(new DataHandler(source));
				 attBodyPart.setFileName(props.getProperty("filename"));
				// messageBodyPart.setContent(messages, "text/html");
				 multipart.addBodyPart(attBodyPart, 0); 
		 
		 
		//HTML parse
	        Document doc = Jsoup.parse(new File("./img/report.html"), "utf-8"); 
	        
	        Elements Tags = doc.getElementsByTag("html");
	        
	        String body = Tags.first().html();
	     
			String htmlText = "<html>"+ body +"</html>";
			
		String s = 	htmlText;

		//Html Body Part
		 MimeBodyPart messageBodyPart1 = new MimeBodyPart();
		 messageBodyPart1.setContent(s, "text/html; charset=utf-8");
		 multipart.addBodyPart(messageBodyPart1, 1);

		
		
		
		message.setContent(multipart);

		// Send message
		 Transport.send(message);

		System.out.println("Sent message successfully....");

		} catch (MessagingException e) {
		 throw new RuntimeException(e);
		 }
	}

	public static void props() {
		try {
			props = new Properties();
			FileInputStream file = new FileInputStream(System.getProperty("user.dir")+"\\mailconfig.properties");
			props.load(file);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
