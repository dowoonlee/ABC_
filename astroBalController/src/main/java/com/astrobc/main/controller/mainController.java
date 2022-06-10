package com.astrobc.main.controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.astrobc.main.model.dto.LeagueEntry;
import com.astrobc.main.model.dto.Summoner;
import com.astrobc.util.riotAPIUtil;

@RestController
@RequestMapping("/api")
public class mainController {
	private static final String SUCCESS = "success";
	private static final String FAIL = "fail";

	@Autowired
	public riotAPIUtil UIbySumName;
	
	@Autowired
	private ResourceLoader resourceLoader;

	@PostMapping("/summoner")
	public ResponseEntity<Summoner> getSummonerData(String region, String summonerName) throws IOException {
		
		Resource resource = resourceLoader.getResource("classpath:");
		String filePath = resource.getURI().getPath() + "/static/key.dat";
		BufferedReader reader = new BufferedReader(new FileReader(filePath));
		String key = reader.readLine();
		
		HashMap<String, Object> result = riotAPIUtil.getSummoner(summonerName, key);
		int statusCode = (int) result.get("statusCode");
		if (statusCode == 403) {
			Summoner summoner = new Summoner();
			return new ResponseEntity<Summoner>(summoner, HttpStatus.FORBIDDEN);
		} else {
			Summoner summoner = (Summoner) result.get("summoner");
			return new ResponseEntity<Summoner>(summoner, HttpStatus.OK);
		}

	}
	@PostMapping("/leagueEntry")
	public ResponseEntity<LeagueEntry> getLeagueEntryData(String region, String summonerName) throws IOException {
		
		Resource resource = resourceLoader.getResource("classpath:");
		String filePath = resource.getURI().getPath() + "/static/key.dat";
		BufferedReader reader = new BufferedReader(new FileReader(filePath));
		String key = reader.readLine();
		
		HashMap<String, Object> result0 = riotAPIUtil.getSummoner(summonerName, key);
		int statusCode = (int) result0.get("statusCode");
		if (statusCode == 403) {
			LeagueEntry leagueEntry = new LeagueEntry();
			return new ResponseEntity<LeagueEntry>(leagueEntry, HttpStatus.FORBIDDEN);
		} else {
			Summoner summoner = (Summoner) result0.get("summoner");
			HashMap<String, Object> result = riotAPIUtil.getLeagueEntry(summoner.getId(), key);
			statusCode = (int) result.get("statusCode");
			if (statusCode == 403) {
				LeagueEntry leagueEntry = new LeagueEntry();
				return new ResponseEntity<LeagueEntry>(leagueEntry, HttpStatus.FORBIDDEN);
			} else {
				LeagueEntry leagueEntry = (LeagueEntry) result.get("leagueEntry");
				return new ResponseEntity<LeagueEntry>(leagueEntry, HttpStatus.OK);
			}
		}

	}

}
