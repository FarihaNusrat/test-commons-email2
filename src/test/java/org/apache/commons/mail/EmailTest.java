package org.apache.commons.mail;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.subethamail.wiser.Wiser;
import org.subethamail.wiser.WiserMessage;

public class EmailTest {
	Email stubobj;
	Wiser wiser;

	public static final String[] testEmails = { "test1@abc.com", "test2@def.com", "test3@ghi.com", "test4@jkl.com" };

	@Before
	public void setup() {
		stubobj = new EmailConcrete();
		wiser=new Wiser();
		wiser.setPort(2500);
		wiser.start();
		
	}



	@Test
	public void testaddCc() {
		try {
			stubobj.addCc("test@abc.com");
		} catch (EmailException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		List<InternetAddress> ccList = stubobj.getCcAddresses();
		String strCc = null;
		if (ccList.size() > 0) {
			strCc = ccList.get(0).getAddress();
		}

		assertEquals("test@abc.com",strCc);
	}

	@Test
	public void testaddBcc() {

		try {
			stubobj.addBcc(testEmails);
		} catch (EmailException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String Email = null;
		List<InternetAddress> bccList = stubobj.getBccAddresses();
		if (bccList.size() > 0) {
			for (int i = 0; i < bccList.size(); i++) {
				Email = bccList.get(i).getAddress();
				assertEquals(testEmails[i],Email);
			}
		}
	}

	@Test(expected = EmailException.class)
	public void testaddBccwithNull() throws EmailException {
		String[] testemail = null;
		
			stubobj.addBcc(testemail);
	
	}

	@Test
	public void testaddHeader() {
		String name = "abc";
		String value = "5";
		stubobj.addHeader(name, value);

		String getKey = null;

		for (String key : stubobj.headers.keySet()) {
			getKey = key;

		}

//		String[] arr=(String[]) 
		assertEquals(value, stubobj.headers.get(name));
		assertEquals(name,getKey);

	}

	

	@Test(expected = IllegalArgumentException.class)
	public void testaddHeaderwithEmptyName() throws IllegalArgumentException {
		String name = null;
		String value = "5";

		stubobj.addHeader(name, value);
		
	}

	@Test(expected = IllegalArgumentException.class)
	public void testaddHeaderwithEmptyValue() throws IllegalArgumentException{
		String name = "abc";
		String value = null;
		
		stubobj.addHeader(name, value);
		
	}

	@Test
	public void testaddReplyTo() {
		String email = "test@abc.com";
		String name = "abc";
		try {
			stubobj.addReplyTo(email, name);
		} catch (EmailException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		List<InternetAddress> replytoList = stubobj.getReplyToAddresses();

		String replytoAddress = null;
		String replytoName = null;
		if (replytoList.size() > 0) {
			replytoAddress = replytoList.get(0).getAddress();
			replytoName = replytoList.get(0).getPersonal();
		}
		assertEquals(email,replytoAddress);
		assertEquals(name,replytoName);
	}

	@Test 
	public void testsetFrom()
	{
		String email="test@abc.com";
		try {
			stubobj.setFrom(email);
		} catch (EmailException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		InternetAddress fromAddress = stubobj.getFromAddress();
		String fromaddress=null;
		fromaddress=fromAddress.getAddress();
		assertEquals(email,fromaddress);
	}
	
	@Test(expected=EmailException.class)
	public void testsetFromwithNull() throws EmailException
	{
		String email="123";
		
		stubobj.setFrom(email);
		
	}
	
	@Test
	public void testgetSentDate()
	{
		LocalDate localDate = LocalDate.of(2024, 5, 10);
		Date date1 = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
		stubobj.sentDate=date1;
		//Date date=new Date();
		Date sentdate=stubobj.getSentDate();
		assertEquals(date1,sentdate);
	}
	
	@Test
	public void testgetSentDatewithNull()
	{
		Date sentdate=null;
		if(stubobj.sentDate==null)
		{
			sentdate=stubobj.getSentDate();
		}
		Date date=new Date();
		assertEquals(date,sentdate);
	}
	
	@Test
	public void testgetHostName()
	{
		String hostname="ABC";
		stubobj.setHostName(hostname);
		assertEquals(hostname,stubobj.getHostName());
		
		
		
	}
	
	@Test
	public void testgetHostNamewithSession()
	{
		Properties prop=new Properties();
		prop.setProperty("mail.smtp.host", "abc");
		
		Authenticator authenticator=new DefaultAuthenticator("testuser", "pwd");
		Session obj=Session.getInstance(prop, authenticator);
		
		
		stubobj.setMailSession(obj);
		String actual=stubobj.getHostName();
		
		assertEquals("abc",actual);
		
	}
	
	@Test
	public void testgetHostNamewithNull()
	{
		String hostname=null;
		stubobj.setHostName(hostname);
		assertEquals(hostname,stubobj.getHostName());
		
		
		
	}
	
	@Test
	public void testgetSocketConnectionTimeout()
	{
		int timeout=12345;
		stubobj.setSocketConnectionTimeout(timeout);
		assertEquals(timeout, stubobj.getSocketConnectionTimeout());
	}
	
	@Test
	public void testUpdateContentTypewithNull()
	{
		stubobj.updateContentType(null);
		assertEquals(null, stubobj.contentType);
	}
	
	@Test
	public void testUpdateContentTypewithCharset()
	{
		String content1="text/html; charset=";
		stubobj.updateContentType(content1);
		assertEquals(content1, stubobj.contentType);
		
		String content2="text/html; charset= abc";
		stubobj.updateContentType(content2);
		assertEquals(content2, stubobj.contentType);
	}
	
	@Test
	public void testUpdateContentTypewithUSASCII()
	{
		String content="text/html";
		String charset="US-ASCII";
		stubobj.setCharset(charset);
		stubobj.updateContentType(content);
		String ctype="text/html; charset=US-ASCII";
		
	
		assertEquals(ctype, stubobj.contentType);
	}
	
	@Test(expected=EmailException.class)
	public void testGetMailSessionwithNullSessionNullHost() throws EmailException
	{
		stubobj.getMailSession();
		
	}
	
	@Test
	public void testGetMailSessionwithHostname() throws EmailException
	{
		stubobj.setHostName("ABC");
		Session session=stubobj.getMailSession();
		Properties properties=session.getProperties();
		String host=properties.getProperty("mail.smtp.host");
		assertEquals("ABC",host);
		String value=properties.getProperty("mail.smtp.starttls.enable");
		assertEquals("false",value);
	}
	
	@Test
	public void testGetMailSessionwithAuthenticator() throws EmailException
	{
		Properties prop=new Properties();
		prop.setProperty("mail.smtp.host", "abc");
		
		Authenticator authenticator=new DefaultAuthenticator("testuser", "pwd");
		stubobj.setAuthenticator(authenticator);
		stubobj.setHostName("ABC");
		Session session=stubobj.getMailSession();
		Properties properties=session.getProperties();
		String value=properties.getProperty("mail.smtp.auth");
		assertEquals("true",value);
		
		
	}
	
	@Test
	public void testGetMailSessionwithSSLOnConnect() throws EmailException
	{
		Properties prop=new Properties();
		prop.setProperty("mail.smtp.host", "abc");
		
		Authenticator authenticator=new DefaultAuthenticator("testuser", "pwd");
		stubobj.setAuthenticator(authenticator);
		stubobj.setHostName("ABC");
		stubobj.setSSLOnConnect(true);
		Session session=stubobj.getMailSession();
		Properties properties=session.getProperties();
		String value=properties.getProperty("mail.smtp.socketFactory.fallback");
		assertEquals("false",value);
	}
	
	@Test
	public void testGetMailSessionwithSSLOnConnectSSLCheckServerIdentity() throws EmailException
	{
		Properties prop=new Properties();
		prop.setProperty("mail.smtp.host", "abc");
		
		Authenticator authenticator=new DefaultAuthenticator("testuser", "pwd");
		stubobj.setAuthenticator(authenticator);
		stubobj.setHostName("ABC");
		stubobj.setSSLOnConnect(true);
		stubobj.setSSLCheckServerIdentity(true);
		Session session=stubobj.getMailSession();
		Properties properties=session.getProperties();
		String value=properties.getProperty("mail.smtp.ssl.checkserveridentity");
		assertEquals("true",value);
		
	}
	
	@Test
	public void testGetMailSessionwithBounceAddress() throws EmailException
	{
		stubobj.setHostName("ABC");
		stubobj.setBounceAddress("bounceaddress");
		Session session=stubobj.getMailSession();
		Properties properties=session.getProperties();
		String address=properties.getProperty("mail.smtp.from");
		assertEquals("bounceaddress",address);
		
	}
	
	//Null message
	@Test
	public void testBuildMimeMessagewithFromAddressToAddress() throws EmailException, IOException, MessagingException
	{
		Properties prop=new Properties();
		prop.setProperty("mail.smtp.host", "abc");
		
		Authenticator authenticator=new DefaultAuthenticator("testuser", "pwd");
		Session session=Session.getInstance(prop, authenticator);
		stubobj.setMailSession(session);
		stubobj.setFrom("test@abc.com");
		stubobj.addTo("test@def.com");
		stubobj.buildMimeMessage();
		MimeMessage mimeMessage=stubobj.getMimeMessage();
		String text=mimeMessage.getContent().toString();
		assertEquals("",text);
		
		
	}
	
	//Null message
	@Test
	public void testBuildMimeMessagewithSubjectCharset() throws EmailException, MessagingException
	{
		Properties prop=new Properties();
		prop.setProperty("mail.smtp.host", "abc");
		Authenticator authenticator=new DefaultAuthenticator("testuser", "pwd");
		Session session= Session.getInstance(prop, authenticator);
		stubobj.setMailSession(session);
		stubobj.setSubject("A Subject");
		stubobj.setCharset("US-ASCII");
		stubobj.setFrom("test@abc.com");
		stubobj.addCc("test@def.com");
		stubobj.addBcc("test@hjb.com");
		stubobj.buildMimeMessage();
		
		MimeMessage mimeMessage=stubobj.getMimeMessage();
		String subject=mimeMessage.getSubject();
		assertEquals("A Subject",subject);
		
		Address[] CcAddress=mimeMessage.getRecipients(Message.RecipientType.CC);
		String cc=null;
		for(int i=0;i<CcAddress.length;i++)
		{
			cc= CcAddress[i].toString();
		}
	
		assertEquals("test@def.com",cc);
		
	}
	
	//Null message Null charset
@Test
	public void testBuildMimeMessagewithSubject() throws EmailException, MessagingException
	{
		Properties prop=new Properties();
		prop.setProperty("mail.smtp.host", "abc");
		Authenticator authenticator=new DefaultAuthenticator("testuser", "pwd");
		Session session= Session.getInstance(prop, authenticator);
		stubobj.setMailSession(session);
		stubobj.setSubject("A Subject");
		stubobj.setFrom("test@abc.com");
		stubobj.addBcc("test@hjb.com");
		stubobj.buildMimeMessage();
		MimeMessage mimeMessage=stubobj.getMimeMessage();
		String subject=mimeMessage.getSubject();
		assertEquals("A Subject",subject);
		
		Address[] BccAddress=mimeMessage.getRecipients(Message.RecipientType.BCC);
		String bcc=null;
		for(int i=0;i<BccAddress.length;i++)
		{
			bcc= BccAddress[i].toString();
		}
	
		assertEquals("test@hjb.com",bcc);
	}

	//Null Message, Null Subject, Null Charset
	@Test
	public void testBuildMimeMessagewithContent() throws EmailException, IOException, MessagingException
	{
		Properties prop=new Properties();
		prop.setProperty("mail.smtp.host", "abc");
		
		Authenticator authenticator=new DefaultAuthenticator("testuser", "pwd");
		Session session=Session.getInstance(prop, authenticator);
		stubobj.setMailSession(session);
		stubobj.setFrom("test@abc.com");
		stubobj.addTo("test@def.com");
		stubobj.setContent("test content", "text/plain");

		stubobj.buildMimeMessage();
		
		MimeMessage mimeMessage=stubobj.getMimeMessage();
		String text=mimeMessage.getContent().toString();
		assertEquals("test content",text);
		
		
	}
	
	@Test
	public void testBuildMimeMessagewithContentElse() throws EmailException, IOException, MessagingException
	{
		Properties prop=new Properties();
		prop.setProperty("mail.smtp.host", "abc");
		
		Authenticator authenticator=new DefaultAuthenticator("testuser", "pwd");
		Session session=Session.getInstance(prop, authenticator);
		stubobj.setMailSession(session);
		stubobj.setFrom("test@abc.com");
		stubobj.addTo("test@def.com");
		stubobj.setContent("content", "text");
		stubobj.addHeader("A header", "12345");

		stubobj.buildMimeMessage();
		
		MimeMessage mimeMessage=stubobj.getMimeMessage();
		String text=mimeMessage.getContent().toString();
		assertEquals("content",text);
		String[] header=mimeMessage.getHeader("A Header");
		assertEquals("12345",header[0]);
	
	}
	

	/*@Test
	public void testBuildMimeMessagewithEmailBody() throws EmailException, IOException, MessagingException
	{
		Properties prop=new Properties();
		prop.setProperty("mail.smtp.host", "abc");
		
		Authenticator authenticator=new DefaultAuthenticator("testuser", "pwd");
		Session session=Session.getInstance(prop, authenticator);
		stubobj.setMailSession(session);
		stubobj.setFrom("test@abc.com");
		stubobj.addTo("test@def.com");
		MimeMultipart mimeMultiPart= new MimeMultipart("Test MimeMultiPart");
		stubobj.setContent(mimeMultiPart);
		stubobj.buildMimeMessage();
		MimeMessage mimeMessage=stubobj.getMimeMessage();
		//String text=mimeMessage.getDataHandler().getDataSource().getContentType();
		//String text=mimeMessage.getDataHandler().getDataSource().get
		assertEquals("Test MimeMultiPart",text);
		
	}*/
	
	@Test(expected=EmailException.class)
	public void testBuildMimeMessagewithNullFromAddress() throws EmailException
	{
		Properties prop=new Properties();
		prop.setProperty("mail.smtp.host", "abc");
		
		Authenticator authenticator=new DefaultAuthenticator("testuser", "pwd");
		Session session=Session.getInstance(prop, authenticator);
		stubobj.setMailSession(session);
		//stubobj.setFrom("test@abc.com");
		stubobj.addTo("test@def.com");
		stubobj.buildMimeMessage();
	}
	
	@Test(expected=EmailException.class)
	public void testBuildMimeMessagewithNullToCcBccAddress() throws EmailException
	{
		Properties prop=new Properties();
		prop.setProperty("mail.smtp.host", "abc");
		
		Authenticator authenticator=new DefaultAuthenticator("testuser", "pwd");
		Session session=Session.getInstance(prop, authenticator);
		stubobj.setMailSession(session);
		stubobj.setFrom("test@abc.com");
		//stubobj.addTo("test@def.com");
		stubobj.buildMimeMessage();
	}
	
	@Test
	public void testBuildMimeMessagewithReplyToAddress() throws EmailException, MessagingException
	{
		Properties prop=new Properties();
		prop.setProperty("mail.smtp.host", "abc");
		
		Authenticator authenticator=new DefaultAuthenticator("testuser", "pwd");
		Session session=Session.getInstance(prop, authenticator);
		stubobj.setMailSession(session);
		stubobj.setFrom("test@abc.com");
		stubobj.addTo("test@def.com");
		stubobj.addReplyTo("test@reply.com");
		stubobj.buildMimeMessage();
		MimeMessage mimeMessage=stubobj.getMimeMessage();
		Address[] ReplyToAddress=mimeMessage.getReplyTo();
		String ReplyTo=null;
		for(int i=0;i<ReplyToAddress.length;i++)
		{
			ReplyTo= ReplyToAddress[i].toString();
		}
	
		assertEquals("test@reply.com",ReplyTo);
	}
	
	
	@Test(expected=IllegalStateException.class)
	public void testBuildMimeMessagewithMessage() throws EmailException, IOException, MessagingException
	{
		Properties prop=new Properties();
		prop.setProperty("mail.smtp.host", "abc");
		
		Authenticator authenticator=new DefaultAuthenticator("testuser", "pwd");
		Session session=Session.getInstance(prop, authenticator);
		stubobj.setMailSession(session);
		stubobj.setFrom("test@abc.com");
		stubobj.addTo("test@def.com");
		stubobj.buildMimeMessage();
		stubobj.buildMimeMessage();
		
		
	}
	@Test
	public void testSend() throws EmailException, MessagingException
	{
		Properties prop=new Properties();
		//prop.setProperty("mail.smtp.host", "abc");
		prop.setProperty("mail.smtp.host", "localhost");
		prop.setProperty("mail.smtp.port", "2500");
		
		Authenticator authenticator=new DefaultAuthenticator("testuser", "pwd");
		Session session=Session.getInstance(prop, authenticator);
		stubobj.setMailSession(session);
		stubobj.setFrom("test@abc.com");
		stubobj.addTo("test@def.com");
		stubobj.setSubject("New Subject");

		stubobj.send();
		WiserMessage emailMessage = wiser.getMessages().get(0);
		MimeMessage mimeMessage=emailMessage.getMimeMessage();
		String subject= mimeMessage.getSubject();
		assertEquals("New Subject",subject);

		
		
	}
	@After
	public void teardown() {
		wiser.stop();
	}
}
