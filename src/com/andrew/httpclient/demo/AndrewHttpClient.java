package com.andrew.httpclient.demo;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpTrace;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

public class AndrewHttpClient {

	private RequestConfig requestConfig = RequestConfig.custom()
			.setConnectionRequestTimeout(20000)
			.setConnectTimeout(10000)
			.setSocketTimeout(10000)
			.build();
	
	private CloseableHttpClient httpClient = HttpClients.createDefault();
	
	public void requestWithHeader(String url, String headers, String body, String method){

		CloseableHttpResponse response = null;
		HttpRequestBase request = genRequest(url, body, method);
		addHeader(request, headers);
		try {
			response = httpClient.execute(request);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (null != response){
			System.out.println("*********************  begin show response ************************");
			System.out.println("code:" + response.getStatusLine().getStatusCode());
			System.out.println("phase:" + response.getStatusLine().getReasonPhrase());
			System.out.println("protocol:" + response.getStatusLine().getProtocolVersion());
			System.out.println("url:" + request.getURI());
			
			HttpEntity entity = response.getEntity();
			
			System.out.println("********************  response headers  **********************");
			Header[] responseHeader = response.getAllHeaders();
			if (null != responseHeader)
				for(Header h : responseHeader){
					System.out.println("\tkey:" + h.getName());
					System.out.println("\tvalue:" + h.getValue());
				}
			else{
				System.out.println("\tresponse headers is null");
			}
			System.out.println("********************  response headers  **********************");
			System.out.println("********************  response body  **********************");
			try {
				String defaultEncoding = "UTF-8";
				Header encoding = entity.getContentEncoding();
				if (null != encoding)
					defaultEncoding = encoding.getValue();
				String content = EntityUtils.toString(entity, defaultEncoding);
				System.out.println("\tresponse content:" + content);
			} catch (ParseException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			System.out.println("********************  response body  **********************");
			
			System.out.println("*********************  end show response ************************");
		}
	}
	
	private HttpRequestBase genRequest(String url, String body, String method){
		HttpRequestBase httpRequestBase = null;
		HttpEntityEnclosingRequestBase httpEntity = null;
		switch(method.toLowerCase()){
		case "get":
			httpRequestBase = new HttpGet(url);
			break;
		case "post":
			httpEntity = genMethodBody(new HttpPost(url), body);
			break;
		case "put":
			httpEntity = genMethodBody(new HttpPut(url), body);
			break;
		case "delete":
			httpRequestBase = new HttpDelete(url);
			break;
		case "options":
			httpRequestBase = new HttpOptions(url);
			break;
		case "head":
			httpRequestBase = new HttpHead(url);
			break;
		case "trace":
			httpRequestBase = new HttpTrace(url);
			break;
		case "patch":
			httpEntity = genMethodBody(new HttpPatch(url), body);
			break;
		default:
			break;
		}
		
		if (null != httpEntity) return httpEntity;
		if (null != httpRequestBase){
			httpRequestBase.setConfig(requestConfig);
			return httpRequestBase;
		}
		return null;
	}
	
	private HttpEntityEnclosingRequestBase genMethodBody(HttpEntityEnclosingRequestBase request, String body){
		request.setConfig(requestConfig);
		
		if (null != body){
			StringEntity requestEntity = null;
			try{
				requestEntity = new StringEntity(body, "UTF-8");
//				InputStream iStream = requestEntity.getContent();
//				String bodySending = IOUtils.toString(iStream, "UTF-8");
//				System.out.println("body:" + bodySending);
			} catch (UnsupportedCharsetException e){
				requestEntity = null;
			}
			
			if (null != requestEntity){
				request.setEntity(requestEntity);
			}
		}
		return request;
	}
	
	private void addHeader(HttpRequestBase request, String headers){
		if (StringUtils.isEmpty(headers))
			return;
		
		JSONObject header = new JSONObject(headers);
		Map<String, Object> headerMap = header.toMap();
		for(Entry<String, Object>entry : headerMap.entrySet()){
			request.addHeader(entry.getKey(), entry.getValue().toString());
			System.out.println("requset set header:");
			System.out.println("\tkey:" + entry.getKey());
			System.out.println("\tvalue:" + entry.getValue());
		}
		
		Header[] Hs = request.getAllHeaders();
		if (null != headers){
			System.out.println("requeset header setted:");
			for(Header h : Hs){
				System.out.println("\tkey:" + h.getName());
				System.out.println("\tvalue:" + h.getValue());
			}
		}
	}

}
