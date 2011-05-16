package com.triplelands.fileman.service;

import java.lang.reflect.Type;
import java.util.List;

import com.google.myjson.reflect.TypeToken;
import com.triplelands.fileman.httplib.HttpClientUtils;
import com.triplelands.fileman.model.OnlineFile;

public class FileManagerService {
	private static final String SERVER_URL = "http://serverside.url/service.php";
	public List<OnlineFile> getMyFiles(String userId) {
		Type listType = new TypeToken<List<OnlineFile>>(){}.getType();
		return HttpClientUtils.getList2(SERVER_URL, listType);
	}
	
	public List<OnlineFile> searchOnlineFiles(String searchPattern,  String category) {
		Type listType = new TypeToken<List<OnlineFile>>(){}.getType();
		return HttpClientUtils.getList2(SERVER_URL, listType);
	}
}
