package com.triplelands.fileman.httplib;

import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.util.Log;

import com.triplelands.fileman.utils.StringHelpers;

public class InternetHttpConnection {
	private InternetConnectionListener listener;
	private URLConnection conn;
	private HttpURLConnection httpConn;
	private InputStream is;
	
	public InternetHttpConnection(InternetConnectionListener listener) {
		this.listener = listener;
	}

	public void setAndAccessURL(String urlString) {
			Log.i("tag", "akses mulai");
//			while(isConnecting){
				try {
					if(Thread.interrupted())
						listener.onCancelledConnection();
					
					int response = -1;

					URL url = new URL(urlString);
					conn = url.openConnection();

					if (!(conn instanceof HttpURLConnection))
						throw new IOException("Not an HTTP connection");

					HttpURLConnection httpConn = (HttpURLConnection) conn;
					httpConn.setAllowUserInteraction(false);
					httpConn.setInstanceFollowRedirects(true);
					httpConn.setRequestMethod("GET");
					httpConn.setConnectTimeout(60000);
					httpConn.connect();

					int length = httpConn.getContentLength();
					
					response = httpConn.getResponseCode();
					if (response == HttpURLConnection.HTTP_OK) {
						is = httpConn.getInputStream();
						
						if (listener != null) {
							Log.i("tag", "sukses");
							listener.onReceivedResponse(is, length);
						}
					}else{
						Log.e("tag", "not ok");
						listener.onConnectionResponseNotOk();
					}
					
					httpConn.disconnect();
					if(is != null){
						is.close();
						is = null;
					}
					
				} catch (SocketTimeoutException ex){
					if (listener != null) {
						Log.e("error", "connection timeout");
						listener.onConnectionTimeout();
					}
				} catch (InterruptedIOException ex){
					if (listener != null) {
						listener.onCancelledConnection();
					}
				} catch (Exception ex) {
					if (listener != null) {
						Log.e("error", "error connecting to the internet");
						listener.onConnectionError(ex);
					}
				}
//			}
	}
	
	public void postData(String url, String[] params) {
	    
	    HttpClient httpclient = new DefaultHttpClient();
	    HttpPost httppost = new HttpPost(url);

	    try {
	        
	        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	        for (int i = 0; i < params.length; i++) {
	        	System.out.println(params[i]);
	        	String[] arr = StringHelpers.explode('=', params[i]);
	        	nameValuePairs.add(new BasicNameValuePair(arr[0], arr[1]));
			}
	        
	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	        
	        HttpResponse response = httpclient.execute(httppost);
	        InputStream is = response.getEntity().getContent();
	        int length = (int)response.getEntity().getContentLength();
	        listener.onReceivedResponse(is, length);
	        
	    } catch (ClientProtocolException e) {
	    	listener.onConnectionError(e);
	    } catch (IOException e) {
	        listener.onConnectionError(e);
	    }
	} 
	
	public void cancel(Thread name) {
		System.out.println("canceling connection");
		name.interrupt();
        try {
			if(is != null){
				is.close();
				is = null;
			}
			if(httpConn != null){
				httpConn.disconnect();
				httpConn = null;
			}
			if(conn != null){
				conn = null;
			}
			listener.onCancelledConnection();
		} catch (Exception e) {
	        listener.onConnectionError(e);
		}
    }

}
