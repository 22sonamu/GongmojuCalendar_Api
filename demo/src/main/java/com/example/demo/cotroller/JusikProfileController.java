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
        List<String> names = new ArrayList<String>(); //주식이름 
        List<String> ChungYakDay = new ArrayList<String>();//청약일 
        List<String> SetPrice = new ArrayList<String>();//확정공모가 
        List<String> HopePrice = new ArrayList<String>();//희망공모가 
        List<String> Companys = new ArrayList<String>();//증권사
        List<String> RefundDay = new ArrayList<String>();//환불일.
        List<String> SangJangDay = new ArrayList<String>();//상장일.
        List<String> DetailUrl = new ArrayList<String>();//세부정보 url
		
		private String URL_38_1 = "http://www.38.co.kr/html/fund/?o=k";
		private String URL_38_2 = "http://www.38.co.kr/html/fund/index.htm?o=k&page=2";
		

		@PostConstruct //앱 실행시 바로 시작될 수 있도록 함  
		public void parse() throws IOException {

			getKoreaCovidDatas(URL_38_1);
			getKoreaCovidDatas(URL_38_2);
			jusikMap = new HashMap<String, JusikProfile>();
			for(int i = 0 ;i < ChungYakDay.size(); i++)
				jusikMap.put(names.get(i), new JusikProfile(names.get(i), ChungYakDay.get(i), RefundDay.get(i),SangJangDay.get(i), HopePrice.get(i),Companys.get(i), SetPrice.get(i), DetailUrl.get(i)));
		}
		
		
		
		public void getKoreaCovidDatas(String URL) throws IOException {

	       
	        org.jsoup.nodes.Document doc = Jsoup.connect(URL).get();
	        
	        org.jsoup.select.Elements contents = doc.select("table[summary=\"공모주 청약일정\"]");
	        
	        org.jsoup.select.Elements contents_name = contents.select("tbody tr td a font");
	        
	        
	        
	        
	        
	        String contents_ChungYakDay = contents.select("tbody tr td").text();
	        
	        
	        
	        String contents_forurl = contents.select("tbody tr td a").toString();//toString과 text의 차이점.
	        
	        String[] h = contents_forurl.split("no=");
	        //이름타고 다른 url로 들어가야 얻을 수 있는 정보(환불일, 상장일)
	        for(int i = 1 ; i < h.length ;i ++) { //url parsing
	        	if(i % 2 == 0) {
	        		
	        		String url_number = h[i].substring(0, 4);
	        		String full_url = "http://www.38.co.kr/html/fund/?o=v&no=".concat(url_number).concat("&l=&page=1");
	        		DetailUrl.add(full_url);
	        		org.jsoup.nodes.Document plusdoc = Jsoup.connect(full_url).get();
	        		org.jsoup.select.Elements plus_contents = plusdoc.select("table[summary=\"공모청약일정\"]").select("tbody tr td");
	        		String plus_profile = plus_contents.text(); //주요일정 , 공모사항 ,IR일정이 담겨있음.
	        		//System.out.println(plus_profile) ----> 참고. 
	        		String[] for_refundday = plus_profile.split("환불일");
	        		////////////환불일 배열에 추가 /////////
	        		RefundDay.add(for_refundday[1].substring(1,11)); 
	        		
	        		String[] for_sangjangday = plus_profile.split("상장일");
	        		
	        		
	        		if(!for_sangjangday[1].substring(0,2).equals(" 2"))//상장일이 아직 정해지지 않았을 경우.(2021~ 이렇게 시작 안하고빈칸.)
	        			SangJangDay.add("-");
	        		else// 상장일이 정해진 경우.(2021 ~ 이렇게 시작할때)
	        			/////////////상장일 배열에 추가////////////////
	        			SangJangDay.add(for_sangjangday[1].substring(0,11)); 
	        		
	        		
	        		
	        		
	        	}
	        	
	        	
	        	
	        }
	        
	        
	        
	        
	        String a = contents_name.text();
	        
	        String[] array = a.split(" "); //주식 이름 array 
	        
	        String[] no_names = contents_ChungYakDay.split(" "); //이름 제외한것들 array 
	        
	        
	        
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
	        
	        
			
	  }

		
	}
}
