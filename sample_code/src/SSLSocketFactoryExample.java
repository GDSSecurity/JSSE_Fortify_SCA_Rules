import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.net.UnknownHostException;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class SSLSocketFactoryExample {
    
    public static void main(String args[]) throws UnknownHostException, IOException{
        
        URL url = new URL("https://www.google.com");
        SSLSocketFactory factory = 	(SSLSocketFactory)SSLSocketFactory.getDefault();
        SSLSocket socket = 	(SSLSocket)factory.createSocket(url.getHost(), 443);
        
        // ## SHOULD FIRE MISSING HOSTNAME VERIFICATION
        
        PrintWriter out = new PrintWriter( new OutputStreamWriter ( socket.getOutputStream() ) );
        out.println("GET " + url.toString() + " HTTP/1.1");
        out.println();
        out.flush();
        
        BufferedReader in = new BufferedReader(
                                               new InputStreamReader(
                                                                     socket.getInputStream()));
        
        String line;
        
        while ((line = in.readLine()) != null) {
            System.out.println(line);
        }
        out.close();
        in.close();
    }
}