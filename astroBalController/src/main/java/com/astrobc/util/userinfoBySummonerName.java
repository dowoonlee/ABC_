package com.astrobc.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;


@Component
public class userinfoBySummonerName {

	private static void getUserInfo() {
		String userId = "hide on bush".replace(" ", "%20");
		String api_key = "RGAPI-d1c4d29b-7d57-47d2-8b17-e5cd6ad4647f";
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
		String id = (String) jsonObject.get("id");
		String accountId = (String) jsonObject.get("accountId");
		String puuid = (String) jsonObject.get("puuid");
		String name = (String) jsonObject.get("name");
		String revisionDate = (String) jsonObject.get("revisionDate");
		int summonerLevel = (int) jsonObject.get("summonerLevel");
		
		System.out.println(id);
		System.out.println(accountId);
		System.out.println(puuid);
		System.out.println(name);
		System.out.println(revisionDate);
		System.out.println(summonerLevel);
		
		

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
