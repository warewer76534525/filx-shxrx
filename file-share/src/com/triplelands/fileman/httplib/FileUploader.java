package com.triplelands.fileman.httplib;

import java.io.File;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

public class FileUploader {
	private static final String uploadUrl = "http://192.168.1.5/test/upload.php";
	
	public String upload(String userid, String category, String filePath) {
		String responseBody = null;
		try {
			MultipartEntity entity = new MultipartEntity(
					HttpMultipartMode.BROWSER_COMPATIBLE);
			File file = new File(filePath);
			
			FileBody encFile = new FileBody(file);
			entity.addPart("userfile", encFile);
			entity.addPart("userid", new StringBody(userid));
			entity.addPart("category", new StringBody(category));

			HttpPost request = new HttpPost(uploadUrl);
			
			request.setEntity(entity);

			HttpClient client = new DefaultHttpClient();
			ResponseHandler<String> responseHandler = new BasicResponseHandler();

			responseBody = client.execute(request, responseHandler);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return responseBody;
	}
}
