package com.example.demo.cotroller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.lang.model.element.Element;
import javax.lang.model.util.Elements;

import org.jsoup.Jsoup;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.JusikProfile;

@RestController
public class JusikProfileController {

	private Map<String, JusikProfile> jusikMap;
	
	@PostConstruct
	public void init() {

	
	
	}
	
	@GetMapping("/jusikname/{name}")
	public JusikProfile getJusikProfile(@PathVariable("name") String name) {
		return jusikMap.get(name);
	}
	

	@Service
	public class JscoupComponent {
		
		private String URL_38_1 = "http://www.38.co.kr/html/fund/?o=k";
		private String URL_38_2 = "http://www.38.co.kr/html/fund/index.htm?o=k&page=2";
		

		@PostConstruct //앱 실행시 바로 시작될 수 있도록 함  
		public void getKoreaCovidDatas() throws IOException {

	        org.jsoup.nodes.Document doc = Jsoup.connect(URL_38_1).get();
	        org.jsoup.nodes.Document doc2 = Jsoup.connect(URL_38_2).get();
	        org.jsoup.select.Elements contents = doc.select("table[summary=\"공모주 청약일정\"]");
	        
	        org.jsoup.select.Elements contents_name = contents.select("tbody tr td a font");
	        
	        
	        org.jsoup.nodes.Element contents_forurl = contents.select("tbody tr td a").first();//url로 접속해야 하는 
	        
	        System.out.println(contents_forurl);
	        String contents_ChungYakDay = contents.select("tbody tr td").text();
	        
	        
	        String a = contents_name.text();
	        
	        String[] array = a.split(" "); //주식 이름 array 
	        
	        String[] no_names = contents_ChungYakDay.split(" "); //이름 제외한것들 array 
	        
	        
	        
	        List<String> names = new ArrayList<String>(); //주식이름 
	        List<String> ChungYakDay = new ArrayList<String>();//청약일 
	        List<String> SetPrice = new ArrayList<String>();//확정공모가 
	        List<String> HopePrice = new ArrayList<String>();//희망공모가 
	        List<String> Companys = new ArrayList<String>();//증권사 
	        
	        for(int i = 0 ; i < array.length ; i++) {
	        	if(! array[i].equals("(유가)")) //유가라고 붙어있는것remove
	        		names.add(array[i]);
	        	
	        }

	        for(int i = 0 ; i < no_names.length ;i ++) {
	        	if(i % 7 == 1)
	        		ChungYakDay.add(no_names[i]);
	        	if(i % 7 == 2)
	        		SetPrice.add(no_names[i]);
	        	if(i % 7 == 3)
	        		HopePrice.add(no_names[i]);
	        	if(i % 7 == 5)
	        		Companys.add(no_names[i]);
	        	
	        }
	        
	        
			jusikMap = new HashMap<String, JusikProfile>();
			for(int i = 0 ;i < ChungYakDay.size(); i++)
				jusikMap.put(names.get(i), new JusikProfile(names.get(i), ChungYakDay.get(i), "아직몰라","아직몰라", HopePrice.get(i),Companys.get(i), SetPrice.get(i)));
			
	  }
		
	}
}
