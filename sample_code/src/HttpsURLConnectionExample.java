import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class HttpsURLConnectionExample {
	public static void main(String[] args) throws Exception {
		TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}
			public void checkClientTrusted(X509Certificate[] certs, String authType) {
			}
			// ## SHOULD FIRE OVER-PERMISSIVE TRUST MANAGER
			public void checkServerTrusted(X509Certificate[] certs, String authType){
			}
		}
		};

		SSLContext sc = SSLContext.getInstance("SSL");
		sc.init(null, trustAllCerts, new java.security.SecureRandom());

		// ## SHOULD FIRE OFTEN-MISUSED CUSTOM SSLSOCKETFACTORY
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

		// ## SHOULD FIRE OVER-PERMISSIVE HOSTNAME VERIFIER
		HostnameVerifier allHostsValid = new HostnameVerifier() {
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		};

		URL url = new URL("https://www.google.com/");
		HttpsURLConnection con = (HttpsURLConnection) url.openConnection();

		// ## SHOULD FIRE OFTEN-MISUSED CUSTOM HOSTNAMEVERIFIER
		HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

		Reader reader = new InputStreamReader(con.getInputStream());
		while (true) {
			int ch = reader.read();
			if (ch==-1) {
				break;
			}
			System.out.print((char)ch);
		}
	}
}