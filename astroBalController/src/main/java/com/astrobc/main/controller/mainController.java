package com.astrobc.main.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.astrobc.main.model.dto.Summoner;
import com.astrobc.util.userinfoBySummonerName;

@RestController
@RequestMapping("/api")
public class mainController {
	private static final String SUCCESS = "success";
	private static final String FAIL = "fail";

	@Autowired
	public userinfoBySummonerName UIbySumName;
	

	@PostMapping("/userinfo")
	public ResponseEntity<Summoner> userInfo(String region, String summonerName) {
		System.out.println(summonerName);
		System.out.println(region);
		HashMap<String, Object> result = UIbySumName.getUserInfo(summonerName);
		int statusCode = (int) result.get("statusCode");
		if (statusCode == 403) {
			Summoner summoner = new Summoner();
			return new ResponseEntity<Summoner>(summoner, HttpStatus.FORBIDDEN);
		} else {
			Summoner summoner = (Summoner) result.get("summoner");
			return new ResponseEntity<Summoner>(summoner, HttpStatus.OK);
		}

	}

}
