package com.astrobc.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.stereotype.Component;



@Component
public class userinfoBySummonerName {

	public static HashMap<String, String> getUserInfo() {
		String userId = "hide on bush".replace(" ", "%20");
		String api_key = "RGAPI-85a702cb-e0cb-4c8c-ad91-d2000eaa2ed4";
		String requestURL = "https://kr.api.riotgames.com/lol/summoner/v4/summoners/by-name/" + userId;
		Map<String, String> requestHeaders = new HashMap<>();
		requestHeaders.put("User-Agent",
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/102.0.5005.62 Safari/537.36");
		requestHeaders.put("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7,ja;q=0.6,es;q=0.5");
		requestHeaders.put("Accept-Charset", "application/x-www-form-urlencoded; charset=UTF-8");
		requestHeaders.put("Origin", "https://developer.riotgames.com");
		requestHeaders.put("X-Riot-Token", api_key);
		String output = get(requestURL, requestHeaders);
		JSONObject jsonObject = new JSONObject(output);
		
		Iterator<String> itr = jsonObject.keys();
		HashMap<String, String> result = new HashMap<String, String>();
		while (itr.hasNext()) {
			String key = itr.next().toString();
			result.put(key, jsonObject.get(key).toString());
		}
		return result;
		

	}

	private static String get(String apiUrl, Map<String, String> requestHeaders) {
		HttpURLConnection con = connect(apiUrl);
		try {
			con.setRequestMethod("GET");
			for (Map.Entry<String, String> header : requestHeaders.entrySet()) {
				con.setRequestProperty(header.getKey(), header.getValue());
			}

			int responseCode = con.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				InputStream is = con.getInputStream();
				return readBody(is);
			} else {
				return error(con.getErrorStream());
			}
		} catch (IOException e) {
			throw new RuntimeException("API 요청과 응답 실패", e);
		} finally {
			con.disconnect();
		}
	}

	private static HttpURLConnection connect(String apiUrl) {
		try {
			URL url = new URL(apiUrl);
			return (HttpURLConnection) url.openConnection();
		} catch (MalformedURLException e) {
			throw new RuntimeException("API URL이 잘못되었습니다. :" + apiUrl, e);
		} catch (IOException e) {
			throw new RuntimeException("연결이 실패했습니다. :" + apiUrl, e);
		}
	}

	private static String error(InputStream body) {
		InputStreamReader streamReader = new InputStreamReader(body);
		try (BufferedReader lineReader = new BufferedReader(streamReader)) {
			StringBuilder responseBody = new StringBuilder();

			String line;
			while ((line = lineReader.readLine()) != null) {
				responseBody.append(line);
			}

			return responseBody.toString();
		} catch (IOException e) {
			throw new RuntimeException("API 응답을 읽는데 실패했습니다.", e);
		}

	}

	private static String readBody(InputStream body) {
		InputStreamReader streamReader = new InputStreamReader(body);

		try (BufferedReader lineReader = new BufferedReader(streamReader)) {
			StringBuilder responseBody = new StringBuilder();

			String line;
			while ((line = lineReader.readLine()) != null) {
				System.out.println(line);
				responseBody.append(line);
			}

			return responseBody.toString();
		} catch (IOException e) {
			throw new RuntimeException("API 응답을 읽는데 실패했습니다.", e);
		}
	}

}
