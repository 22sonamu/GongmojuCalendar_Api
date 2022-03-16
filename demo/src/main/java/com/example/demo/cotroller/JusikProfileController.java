package com.example.demo.cotroller;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.lang.model.element.Element;
import javax.lang.model.util.Elements;
import javax.swing.text.Document;

import org.apache.commons.logging.Log;
import org.jsoup.Jsoup;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

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

	@GetMapping("/jusikname/all")
	public List<JusikProfile> getJusikProfileList(){
		return new ArrayList<JusikProfile>(jusikMap.values());
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
		
		

		@PostConstruct //TODO 앱 실행시 바로 시작될 수 있도록 함  
		public void parse() throws IOException {

			getDatas(URL_38_1);//첫번째 페이지 크롤링 
			getDatas(URL_38_2);//두번째 페이지 크롤링 
			jusikMap = new HashMap<String, JusikProfile>();
			for(int i = 0 ;i < ChungYakDay.size(); i++)
				jusikMap.put(names.get(i), new JusikProfile(names.get(i), ChungYakDay.get(i), RefundDay.get(i),SangJangDay.get(i), HopePrice.get(i),Companys.get(i), SetPrice.get(i), DetailUrl.get(i)));
				//크롤링을 통해 얻은 배열들을 jusikMap 객체에등록한다.

			
		    
		}
		

		public void getDatas(String URL) throws IOException {//TODO 웹 크롤링
		

	       
			org.jsoup.nodes.Document doc = Jsoup.connect(URL).get();
	        
	        org.jsoup.select.Elements contents = doc.select("table[summary=\"공모주 청약일정\"]");    
	        org.jsoup.select.Elements contents_name = contents.select("tbody tr td a font");
	        
	        
	        
	        String contents_ChungYakDay = contents.select("tbody tr td").text();
	        	        
	        String contents_forurl = contents.select("tbody tr td a").toString();
	        
	        String[] h = contents_forurl.split("no=");
	        //이름타고 다른 url로 들어가야 얻을 수 있는 정보(환불일, 상장일)
	        for(int i = 1 ; i < h.length ;i ++) {//주식의 수 만큼
	        	if(i % 2 == 0) {
	        		
	        		String url_number = h[i].substring(0, 4);
	        		String full_url = "http://www.38.co.kr/html/fund/?o=v&no=".concat(url_number).concat("&l=&page=1");
	        		//상세 정보 페이지의 full url 만들기
	        		DetailUrl.add(full_url);
	        		org.jsoup.nodes.Document plusdoc = Jsoup.connect(full_url).get();//상세 정보 페이지로 접근
	        		org.jsoup.select.Elements plus_contents = plusdoc.select("table[summary=\"공모청약일정\"]").select("tbody tr td");
	        		//상세 정보 페이지 중 공모주 청약 일정이 있는 html 태그까지 접근 
	        		String plus_profile = plus_contents.text(); //주요일(환불일, 상장일) , 공모사항 ,IR일정이 담겨있음.
	        		//System.out.println(plus_profile) ----> 참고. 
	        		String[] for_refundday = plus_profile.split("환불일");//주식의 환불일을 구하기 위함 
	        		
	        		RefundDay.add(for_refundday[1].substring(1,11)); //환불일은 기간으로 구성되어 있어서 글자수가 일정함 -> substring을 이용하여 원하는 정보 추출후 배열에 저장  
	        		
	        		String[] for_sangjangday = plus_profile.split("상장일");//주식의 상장일을 구하기 위함 
	        		
	        		
	        		if(!for_sangjangday[1].substring(0,2).equals(" 2"))//상장일이 아직 정해지지 않았을 경우.(2021~ 이렇게 시작 안하고빈칸.)
	        			SangJangDay.add("-");
	        		else//상장일이 정해진경우 
	        			/////////////상장일 배열에 추가////////////////
	        			SangJangDay.add(for_sangjangday[1].substring(0,11)); 
	        		//환불일처럼 기간은 글자수가 정해져 있으므로 substring을 통해 원하는 정보 추출 
	        		
	        		
	        		
	        		
	        	}
	        	
	        	
	        	
	        }
	        
	        
	        
	        
	        String a = contents_name.text();
	        
	        String[] array = a.split(" "); //주식 이름 array 
	        
	        String[] no_names = contents_ChungYakDay.split(" "); //이름 제외한것들 array 
	        
	        
	        
	        for(int i = 0 ; i < array.length ; i++) {
	        	if(! array[i].equals("(유가)")) //주식 이름 중 유가라고 붙어있는것remove
	        		names.add(array[i]);
	        	
	        }
	        //no_names에서는 이름을 제외한  정보들이
	        //청약일, 확정공모가 , 희망공모가 , 증권사 순서대로 들어있다.
	        //for문에서 나머지를 이용하여 원하는 정보를 추출한다.
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
