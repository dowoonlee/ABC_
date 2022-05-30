package com.astrobc.main.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.astrobc.util.userinfoBySummonerName;

@RestController
@RequestMapping("/api")
public class mainController {
	private static final String SUCCESS = "success";
	private static final String FAIL = "fail";
	
	@Autowired
	public userinfoBySummonerName  UIbySumName;
	
	@PostMapping("/userinfo")
	public ResponseEntity<String> userInfo(){
		UIbySumName.
	}

}
