package com.yunhetong.sdk.tool;

import com.android.volley.VolleyLog;

import org.apache.http.conn.ssl.SSLSocketFactory;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class SSLTrustAllSocketFactory extends SSLSocketFactory {

	private static final String TAG = "SSLTrustAllSocketFactory";
	private SSLContext mCtx;

	public class SSLTrustAllManager implements X509TrustManager {

		@Override
		public void checkClientTrusted(X509Certificate[] arg0, String arg1)
				throws CertificateException {
		}

		@Override
		public void checkServerTrusted(X509Certificate[] arg0, String arg1)
				throws CertificateException {
		}

		@Override
		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}

	}

	public SSLTrustAllSocketFactory(KeyStore truststore) throws Throwable {
		super(truststore);
		try {
			mCtx = SSLContext.getInstance("TLS");
			mCtx.init(null, new TrustManager[] { new SSLTrustAllManager() },
					null);
			setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
		} catch (Exception ex) {
		}
	}

	@Override
	public Socket createSocket(Socket socket, String host, int port,
			boolean autoClose) throws IOException, UnknownHostException {
		return mCtx.getSocketFactory().createSocket(socket, host, port,
				autoClose);
	}

	@Override
	public Socket createSocket() throws IOException {
		return mCtx.getSocketFactory().createSocket();
	}

	public static SSLSocketFactory getSocketFactory() {
		if(!VolleyLog.DEBUG){
			return SSLSocketFactory.getSocketFactory();
		}
		try {
			KeyStore trustStore = KeyStore.getInstance(KeyStore
					.getDefaultType());
			trustStore.load(null, null);
			SSLSocketFactory factory = new SSLTrustAllSocketFactory(trustStore);
			return factory;
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}

	public static javax.net.ssl.SSLSocketFactory getAndroidSocketFactory() {
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public X509Certificate[] getAcceptedIssuers() {
				return new X509Certificate[] {};
			}

			public void checkClientTrusted(X509Certificate[] chain,
					String authType) throws CertificateException {
			}

			public void checkServerTrusted(X509Certificate[] chain,
					String authType) throws CertificateException {
			}
		} };
		try {
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			return sc.getSocketFactory();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}