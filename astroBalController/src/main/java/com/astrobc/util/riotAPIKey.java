package com.astrobc.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;


public class riotAPIKey {

	@Autowired
	ResourceLoader resourceLoader;

	public String ApiKey() throws IOException {
		Resource resource = resourceLoader.getResource("classpath:");
		String filePath = resource.getURI().getPath() + "/static/key.dat";
		System.out.println(filePath);
		BufferedReader reader = new BufferedReader(new FileReader(filePath));

		return reader.readLine();
	}

}
