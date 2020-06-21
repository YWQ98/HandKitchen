package xin.tapin.ywq138.utils;

import android.util.Log;

import java.util.Date;
import java.util.Properties;
import java.util.Random;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;


public class Email {

	public static boolean sendEmail(String emailaddress,String code){
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.qq.com");
		Session session = Session.getInstance(props, null);

		try {
			MimeMessage msg = new MimeMessage(session);
			msg.setFrom("1269412851@qq.com");
			msg.setRecipients(Message.RecipientType.TO,
					emailaddress);
			msg.setSubject("掌上厨房：验证码");
			msg.setSentDate(new Date());
			msg.setText(code);
			Transport.send(msg, "1269412851@qq.com", "qsyihdggqrzvigjb");
			return true;
		} catch (MessagingException mex) {
			Log.i("TAG", "sendEmail: " + mex);
			return false;
		}
	}
	public static String randomChar(){
		String code="";
        Random r = new Random();
        String s = "ABCDEFGHJKLMNPRSTUVWXYZ123456789";
        for (int i = 0; i < 6; i++) {
        	code+=s.charAt(r.nextInt(s.length()));
		}
        return code;
    }
	public static void main(String[] args) {
		String code=Email.randomChar();
		System.out.println(Email.sendEmail("470199898@qq.com", code));
	}
}
