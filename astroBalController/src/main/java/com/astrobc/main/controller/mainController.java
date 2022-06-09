package com.astrobc.main.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
	public ResponseEntity<HashMap<String, String>> userInfo(){
		HashMap<String, String> userInfo = UIbySumName.getUserInfo();
		if (userInfo.get("status")!=null) {
			return new ResponseEntity<HashMap<String, String>>(userInfo, HttpStatus.FORBIDDEN);
		}else {
			return new ResponseEntity<HashMap<String, String>>(userInfo, HttpStatus.OK);
		}
		
	}

}
