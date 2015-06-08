import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

@SuppressWarnings("deprecation")
public class ApacheHTTPClientExample {

	public static void main(String[] args) throws Exception {

		SSLContext sslcontext = buildSSLContext();
		// ## SHOULD FIRE OVER-PERMISSIVE HOSTNAME VERIFIER
		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
				sslcontext,
				new String[] { "TLSv1" },
				null,
				SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

		CloseableHttpClient httpclient = HttpClients.custom()
				.setSSLSocketFactory(sslsf)
				.build();
		try {

			HttpGet httpget = new HttpGet("https://www.google.com");

			System.out.println("executing request" + httpget.getRequestLine());

			CloseableHttpResponse response = httpclient.execute(httpget);
			try {
				HttpEntity entity = response.getEntity();

				System.out.println("----------------------------------------");
				System.out.println(response.getStatusLine());
				if (entity != null) {
					System.out.println("Response content length: " + entity.getContentLength());
				}
				for (Header header : response.getAllHeaders()) {
					System.out.println(header);
				}
				EntityUtils.consume(entity);
			} finally {
				response.close();
			}
		} finally {
			httpclient.close();
		}
	}

	private static SSLContext buildSSLContext()
			throws NoSuchAlgorithmException, KeyManagementException,
			KeyStoreException {
		SSLContext sslcontext = SSLContexts.custom()
				.setSecureRandom(new SecureRandom())
				.loadTrustMaterial(null, new TrustStrategy() {
					// ## SHOULD FIRE OVER-PERMISSIVE TRUST MANAGER
					public boolean isTrusted(X509Certificate[] chain, String authType)
							throws CertificateException {
						return true;
					}
				})
				.build();
		return sslcontext;
	}
}