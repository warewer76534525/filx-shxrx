package com.triplelands.fileman.httplib;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.triplelands.fileman.utils.StringHelpers;

public class SongDownloaderTask extends AsyncTask<Void, String, Void> implements InternetConnectionListener{

	private InternetHttpConnection internetConnection;
	private String url;
	private Handler handler;
	
	public SongDownloaderTask(Context ctx, Handler handler, String url) {
		this.url = url;
		this.handler = handler;
		internetConnection = new InternetHttpConnection(this);
	}
	
	protected Void doInBackground(Void... params) {
		internetConnection.setAndAccessURL(url);
		return null;
	}

	@Override
	public void onReceivedResponse(InputStream is, int length) {
		String exploded[] = StringHelpers.explode('/', url);
		String name = exploded[exploded.length - 1];
		
		File songDir = new File(Environment.getExternalStorageDirectory() + "/.kidungjemaat/songfiles/");
		songDir.mkdirs();
		
		
		try {
			/*Save song*/
			File fileSong = new File(songDir, name + ".mid");
			FileOutputStream out = new FileOutputStream(fileSong);
			
			byte buf[] = new byte[1024];
	        int downloaded = 0;
	        
	        do {
	            int numread = is.read(buf);
	            if (numread <= 0)
	                break;
	            downloaded += numread;
	            out.write(buf, 0, numread);
	        } while (true);
	        
	        if(downloaded < length ){
	        	deleteFile(songDir, name);
	        } else {
	        	/* download selesai */
	        	if(fileSong.isFile()){
	        		System.out.println("complete download");
	        		Message msg = handler.obtainMessage();
	        		handler.sendMessage(msg);
	        	}
	        }
		} catch (Exception e) {
			sendErrorMessage();
		}
	}
	
	private void deleteFile(File dir, String filename){
		try{
			File file = new File(dir, filename);
	    	file.delete();
		}catch (Exception e) {
			Log.e("ERROR", "error deleting file");
		}
	}

	@Override
	public void onConnectionError(Exception ex) {
		sendErrorMessage();
	}

	@Override
	public void onConnectionResponseNotOk() {
		sendErrorMessage();
	}

	@Override
	public void onConnectionTimeout() {
		System.out.println("timeout kirim pesan");
		sendErrorMessage();
	}

	@Override
	public void onCancelledConnection() {
		sendErrorMessage();
	}
	
	private void sendErrorMessage(){
		Message msg = handler.obtainMessage();
		Bundle b = new Bundle();
		b.putString("error", "error");
		msg.setData(b);
		handler.sendMessage(msg);
	}

}
