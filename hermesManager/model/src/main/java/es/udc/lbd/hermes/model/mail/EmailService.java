package es.udc.lbd.hermes.model.mail;
import java.util.Calendar;
import java.util.List;

import javax.activation.DataSource;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import es.udc.lbd.hermes.model.mail.mail.SimpleMail;


/**
 * Contén as funcionalidades de envío de correo
 * 
 * @author Guillermo (02/03/2009)
 * 
 */
@Service
public class EmailService {

	@Autowired
	private JavaMailSenderImpl mailSender;

	@Autowired
	@Qualifier("messageSource")
	public MessageSource messageSource;

	
//	private static final Logger LOG = LoggerFactory.getLogger(EmailService.class);
	
	/** DEBUG poner a true para desactivar temporalmente el envio de mails */
	private static final boolean ENVIO_DESACTIVADO = false;


	/**
	 * Envía un correo electrónico. Como replyTo coloca o email que ben indicado
	 * no parámetro de configuración en BD CFG_BD_NUC_EMAIL_CONTACTO ou null se non existe
	 */
	public boolean enviarCorreo(String correoElectronico, String asunto,
			String texto, String textoHTML){

		try {
//			obterReplyTo() ==> Obter o establecer en un parametro del fichero de configuracion
			
			enviarCorreoUtil(correoElectronico, asunto, texto, textoHTML, null, null);

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			//throw new RuntimeException(e);
			return false;
			
		}
	}
	
	public boolean enviarCorreo(String correoElectronico, String asunto,
			String texto, String textoHTML, String[] replyTo){

		try {

			enviarCorreoUtil(correoElectronico, asunto, texto, textoHTML, replyTo, null);

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			//throw new RuntimeException(e);
			return false;
			
		}
	}
	
	
    /** Para implementar medidas de seguridad relativas a cambio de contraseñas **/
	
	
	public boolean enviaCorreo(SimpleMail mail) {

//		if (LOG.isInfoEnabled()) {
//			LOG.info("enviaCorreo: " + mail.toString());
//		}
		
		String receiver = mail.getCorreoElectronico();
		String subject = mail.getAsunto();
		String message = mail.getTexto();
		String messageHtml = mail.getTextoHTML();
		String[] replyTo = mail.getReplyTo();

		try {
			enviarCorreoUtil(receiver, subject, message, messageHtml, replyTo, null);
			return true;
		} catch (Exception e) {
//			LOG.error("enviarCorreo", e);
			return false;
		}
	}
	
    
   
    /******************************************/
    
	
	/**
	 * Método utilizado para enviar correos individuais e tamén para o
	 * envío de correos masivos (SendMassiveThread)
	 * @param correoElectronico
	 * @param asunto
	 * @param texto
	 * @param textoHTML
	 * @throws MessagingException 
	 * @throws Exception
	 */
     public void enviarCorreoUtil(String correoElectronico, String asunto,
			String texto, String textoHTML, String [] replyTo, List<byte []> files) throws MessagingException {
 		
	    	if (ENVIO_DESACTIVADO) {
	 			return;
	 		}
		
			MimeMessage mensaxe = mailSender.createMimeMessage();
		
			mensaxe.setHeader("X-Mailer", "JavaMailer");
			mensaxe.setSentDate(Calendar.getInstance().getTime());
			mensaxe.setFrom();
			if(replyTo !=null && replyTo.length > 0){
				
				InternetAddress [] internetAdrAddresses = new InternetAddress[replyTo.length];
				for(int i=0; i<replyTo.length; i++){
					internetAdrAddresses [i] = new InternetAddress(replyTo[i]);
				}
				mensaxe.setReplyTo(internetAdrAddresses);
				
			}
			mensaxe.setSender(new InternetAddress(mailSender.getUsername()));
			mensaxe.setRecipients(RecipientType.TO, correoElectronico);
			mensaxe.setSubject(asunto, "UTF-8");
	
			// 1. create and fill the text/plain message part
			MimeBodyPart plaintext = new MimeBodyPart();
			plaintext.setText(texto, "UTF-8");
	
			// 2. create and fill the text/html message part
			MimeBodyPart htmltext = new MimeBodyPart();
			htmltext.setContent(textoHTML, "text/html; charset=\"UTF-8\"");
	
			// 3. create Multipart and add parts to it
			Multipart mp = new MimeMultipart("alternative"); // result ->
															 // multipart
															 // /alternative
			mp.addBodyPart(plaintext);
			mp.addBodyPart(htmltext);
			// add the Multipart to the message
			mensaxe.setContent(mp);
			// Helper
 	 		MimeMessageHelper helper = new MimeMessageHelper(mensaxe, true, "UTF-8");
 	 		
 			helper.setText(textoHTML, true);
			if(files!=null){
				for (byte[] a: files){
		 			DataSource dataSource = new  ByteArrayDataSource(a,"application/pdf");
		 			helper.addAttachment("Factura.pdf", dataSource);
				
				}
			}
//					
//			
//
			mailSender.send(mensaxe);
	}
     
}
