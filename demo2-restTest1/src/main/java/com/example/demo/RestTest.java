package com.example.demo;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class RestTest {
	
	@PostConstruct
	public void test() {
	    List<HttpMessageConverter<?>> converters = new ArrayList<HttpMessageConverter<?>>();
	    converters.add(new FormHttpMessageConverter());
	    converters.add(new StringHttpMessageConverter());
	 
	    RestTemplate restTemplate = new RestTemplate();
	    restTemplate.setMessageConverters(converters);
	 
	    // parameter 세팅
	    MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
	    map.add("str", "thisistest");
	 
	    // REST API 호출
	    String result = restTemplate.postForObject("http://localhost:8080/jusiknam/노을", map, String.class);
	    System.out.println("------------------ TEST 결과 ------------------");
	    System.out.println(result);

		
	}


}
