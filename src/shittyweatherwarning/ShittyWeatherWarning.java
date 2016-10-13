/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shittyweatherwarning;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.regex.Pattern;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author arnarbarri
 */
public class ShittyWeatherWarning {

    /**
     * @param args the command line arguments
     */
    public static String html1 = "";
    public static String html2 = "";
    public static String[] a = new String[100];
    public static String[] b = new String[100];
    public static String[] c = new String[100];
    public static String[] d = new String[100];
    public static double[] Rdayafter = new double[24];
    public static double[] Ralldoubles = new double[200];
    public static double[] Wdayafter = new double[24];
    public static double[] Walldoubles = new double[200];
    public static String Rdayafterstring = "";
    public static String Wdayafterstring = "";
    
    public final static int at2015 = 12;
    
    public static final String username = "arnarsweatherwarning@gmail.com";
    public static final String password = "Shitty.Weather.Warning";
    public static final String targetname = "arnarb89@gmail.com";
    
    
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        GetWebsite();
        ChopText();
        WriteToFile();
        SendMail();
        //System.out.println(Rdayafterstring);
        
    }
    public static void GetWebsite() throws MalformedURLException, IOException{
        String urlToSearch = "http://www.vedur.is/vedur/spar/stadaspar/faxafloi/#group=11&station=1";
        BufferedReader bufferedReader = new BufferedReader( 
                                     new InputStreamReader( 
                                          new URL(urlToSearch)
                                              .openConnection()
                                              .getInputStream() ));

        StringBuilder sb = new StringBuilder();
        String line = null;
        while( ( line = bufferedReader.readLine() ) != null ) {
             sb.append( line ) ;
             sb.append( "\n");
        }
        bufferedReader.close();
        html1 = sb.toString();
        html2 = html1;
        //buffer.close();
    }
    
    
    public static void ChopText(){
        int counter1 = 0;
        
        String split1 = Pattern.quote("],'R':["); //Rigning Before
        String split2 = Pattern.quote("],'N':["); //Rigning After
        String split3 = Pattern.quote("],'F':["); //Vindur Before
        String split4 = Pattern.quote("],'TD':[");//Vindur After
        
        for(String line : html1.split(split1)){
            a[counter1++] = line;
            
        }
        counter1 = 0;
        for(String line : a[1].split(split2)){
            b[counter1++] = line;
        }
        html1 = b[0];
        
        counter1 = 0;
        for(String line : html1.split(",")){
            Ralldoubles[counter1] = Double.parseDouble(line);
            if(Ralldoubles[counter1]<0)Ralldoubles[counter1] = 0;
            counter1++;
        }
        for(int i = 0; i<24;i++){
            Rdayafter[i] = Ralldoubles[i+at2015];
        }
        
        ////////////
        
        counter1 = 0;
        for(String line : html2.split(split3)){
            a[counter1++] = line;
            
        }
        counter1 = 0;
        for(String line : a[1].split(split4)){
            b[counter1++] = line;
        }
        html2 = b[0];
        
        counter1 = 0;
        for(String line : html2.split(",")){
            Walldoubles[counter1] = Double.parseDouble(line);
            if(Walldoubles[counter1]<0)Walldoubles[counter1] = 0;
            counter1++;
        }
        for(int i = 0; i<24;i++){
            Wdayafter[i] = Walldoubles[i+at2015];
        }
        
        
        
    }
    
    
    
    
    public static void WriteToFile() throws FileNotFoundException, UnsupportedEncodingException{
        String x = System.getProperty("user.home") + "/Desktop";
        new File(x+"/Weather Test").mkdir();
        x = x + "\\Weather Test\\htmltest.txt";
        Path path = Paths.get(x);
        File y = new File (x);
        PrintWriter writer = new PrintWriter(x, "UTF-8");
        //writer.print(here);
        for(int i = 0;i<94;i++){
            writer.print(Ralldoubles[i]+" ");
            if(i==17||i==41||i==53||i==61||i==69||i==77||i==85||i==93) writer.print(System.getProperty("line.separator"));
        }
        writer.print(System.getProperty("line.separator"));
        writer.print(System.getProperty("line.separator"));
        Rdayafterstring = "";
        for(int i = 0;i<24;i++){
            writer.print(Rdayafter[i]+" ");
            String xy = Rdayafter[i]+" ";
            Rdayafterstring = Rdayafterstring+xy;
        }
        
        writer.print(System.getProperty("line.separator"));
        writer.print(System.getProperty("line.separator"));
        
        for(int i = 0;i<94;i++){
            writer.print(Walldoubles[i]+" ");
            if(i==17||i==41||i==53||i==61||i==69||i==77||i==85||i==93) writer.print(System.getProperty("line.separator"));
        }
        
        writer.print(System.getProperty("line.separator"));
        writer.print(System.getProperty("line.separator"));
        
        Wdayafterstring = "";
        for(int i = 0;i<24;i++){
            writer.print(Wdayafter[i]+" ");
            String xy = Wdayafter[i]+" ";
            Wdayafterstring = Wdayafterstring+xy;
        }
        
        
        writer.close();
        
    }
    
    
    
    public static void SendMail(){
        Properties props = new Properties();
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        Session session = Session.getInstance(props,
          new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
          });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO,
                InternetAddress.parse(targetname));
            message.setSubject("Testing Subject WeatherTest");
            message.setText("This is the day after string"
                + "\n\n"+Rdayafterstring
                + "\n\n"+Wdayafterstring);

            Transport.send(message);

            System.out.println("Done");
            System.out.println(Rdayafterstring);
            System.out.println(Wdayafterstring);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
    
    
}
