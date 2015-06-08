import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;


@SuppressWarnings("deprecation")
public class ApacheHTTPClientFluentExample {

	public static void main(String args[]) throws ClientProtocolException, IOException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
		ApacheHTTPClientFluentExample example = new ApacheHTTPClientFluentExample();
		example.test1();
		example.test2();
		example.test3();
		example.test4();
	}

	public void test1() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, ClientProtocolException, IOException{
		// ## SHOULD FIRE OVER-PERMISSIVE HOSTNAME VERIFIER
		CloseableHttpClient httpClient = HttpClients.custom().
				setHostnameVerifier(new AllowAllHostnameVerifier()).
				setSslcontext(new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
					// ## SHOULD FIRE OVER-PERMISSIVE TRUST MANAGER
					@Override
					public boolean isTrusted(X509Certificate[] arg0, String arg1) throws java.security.cert.CertificateException {
						return true;
					}
				}).build()).build();
		HttpGet httpget = new HttpGet("https://www.google.com/");
		CloseableHttpResponse response = httpClient.execute(httpget);
		try {
			System.out.println(response.toString());
		} finally {
			response.close();
		}
	}

	public void test2() throws ClientProtocolException, IOException{
		// ## SHOULD FIRE OVER-PERMISSIVE HOSTNAME VERIFIER
		CloseableHttpClient httpClient = HttpClients.custom().setHostnameVerifier(AllowAllHostnameVerifier.INSTANCE).build();
		HttpGet httpget = new HttpGet("https://www.google.com/");
		CloseableHttpResponse response = httpClient.execute(httpget);
		try {
			System.out.println(response.toString());
		} finally {
			response.close();
		}
	}

	public void test3() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, ClientProtocolException, IOException{
		// ## SHOULD FIRE OVER-PERMISSIVE HOSTNAME VERIFIER
		CloseableHttpClient httpClient = HttpClients.custom().setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE).build();
		HttpGet httpget = new HttpGet("https://www.google.com/");
		CloseableHttpResponse response = httpClient.execute(httpget);
		try {
			System.out.println(response.toString());
		} finally {
			response.close();
		}
	}

	public void test4() throws NoSuchAlgorithmException, ClientProtocolException, IOException, KeyManagementException{
		Executor.unregisterScheme("https");
		SSLContext sslContext = SSLContext.getInstance("SSL");
		sslContext.init(null, null, null);
		// ## SHOULD FIRE OVER-PERMISSIVE HOSTNAME VERIFIER
		SSLSocketFactory sslSocketFactory = new SSLSocketFactory(sslContext, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
		Executor.registerScheme(new Scheme("https", 443, sslSocketFactory));

		String responseAsString = Request.Get("https://www.google.com")
				.execute().returnContent().asString();
		System.out.println(responseAsString);
	}
}
