package com.andrew.httpclient.demo;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.json.JSONObject;

public class Entry {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		CloseableHttpResponse response;
		AndrewHttpClient client = new AndrewHttpClient();
		
		File http = new File("resource/http.json");
        String content;
		try {
			String bodyStr = FileUtils.readFileToString(new File("resources/body.json"), "UTF-8");
			String headerStr = FileUtils.readFileToString(new File("resource/header.json"), "UTF-8");
			
			content = FileUtils.readFileToString(http, "UTF-8");
			JSONObject httpObj  = StringUtils.isEmpty(content) ? null : new JSONObject(content);
			
			String url;
			String method;
			if (null == httpObj || StringUtils.isEmpty(url = httpObj.optString("url", "")) 
					|| StringUtils.isEmpty(method = httpObj.optString("method", ""))){
				System.out.println("No http Configuration,need url and method.");
				return;
			}
			
			client.requestWithHeader(url, headerStr, bodyStr, method);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
}
