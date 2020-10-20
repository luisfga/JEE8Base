package com.luisfga.business.helper;

import com.luisfga.business.entities.AppUser;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

@Stateless
public class MailHelper {
    
    @Resource
    private Session applicationMailSession;
    
    public void enviarEmailResetSenha(String contextPath, AppUser user, String windowToken) throws AddressException, MessagingException, UnsupportedEncodingException {

        Message message = new MimeMessage(applicationMailSession);
        message.setFrom(new InternetAddress(applicationMailSession.getProperty("mail.smtp.user"), contextPath.replace("/", ""))); // Remetente
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(user.getEmail()));
        message.setSubject("Redefinição de Senha");// Assunto

        //corpo da mensagem
        String msg = ""
                + "<style>"
                + ".button{background-color: #0099ff; color: white; padding: 5px 10px 5px 10px; "
                + "vertical-align: middle; text-align: center; text-decoration: none; border-radius: 20px; "
                + "font-size: 15px;}"
                + ".warning{color: red;}"
                + "</style>"
                + "<h2>Olá, "+user.getUserName()+".</h2>"
                + "<h4>Utilize o botão abaixo para acessar a página de redefinição de senha</h4>"
                + "<a class=\"button\" href=\"https://localhost:8443"+contextPath+"/passwordReset.xhtml"
                + "?encodedUserEmail="+Base64.getEncoder().encodeToString(user.getEmail().getBytes("UTF-8"))
                + "&windowToken="+windowToken+"\">Redefinir Senha</a><br/><br/>"
                + "*Se não foi você que solicitou a redefinição de senha. Desconsidere essa mensagem.<br/><br/>"
                + "*Este link só funcionará uma única vez. Se necessário, solicite novamente.<br/><br/>";

        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setContent(msg, "text/html; charset=UTF-8");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(mimeBodyPart);

        message.setContent(multipart);

        Transport.send(message);

    }
    
    public void enviarEmailConfirmacaoNovoUsuario(String contextPath, AppUser user) throws AddressException, MessagingException, UnsupportedEncodingException {

        Message message = new MimeMessage(applicationMailSession);

        message.setFrom(new InternetAddress(applicationMailSession.getProperty("mail.smtp.user"), contextPath.replace("/", ""))); // Remetente
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(user.getEmail()));
        message.setSubject("Confirmação de Nova Conta");// Assunto

        //corpo da mensagem
        String msg = ""
                + "<style>"
                + ".button{background-color: #0099ff; color: white; padding: 5px 10px 5px 10px; "
                + "vertical-align: middle; text-align: center; text-decoration: none; border-radius: 20px; "
                + "font-size: 15px;}"
                + "</style>"
                + "<h2>Olá, " + user.getUserName() + ".</h2>"
                + "<h4>Utilize o botão abaixo para confirmar sua conta recém criada</h4>"
                + "<a class=\"button\" href=\"https://localhost:8443"+contextPath+"/confirmRegistration.xhtml?encodedUserEmail="
                + Base64.getEncoder().encodeToString(user.getEmail().getBytes("UTF-8"))  + "\">Confirmar</a><br/><br/>";

        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setContent(msg, "text/html; charset=UTF-8");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(mimeBodyPart);

        message.setContent(multipart);

        Transport.send(message);

    }

}