package com.petcare.missedPet;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.petcare.disease.GcmDTO;
import com.petcare.disease.LateDiseaseDTO;


@RestController
public class MissedController {

	@RequestMapping(path = "/getCity", method = RequestMethod.GET)
	public List<CityDTO> getCity() throws IOException{

		Document doc = Jsoup.connect("http://openapi.animal.go.kr/openapi/service/rest/abandonmentPublicSrvc/sido?ServiceKey=qK1psrABUse%2B6Tww%2FOK5CxjWAGvy4TvUqb45X%2BPfcy5rPmaANYIhQbIXxobp1H7TYAeFDE%2BYSQsbndxWweL9zA%3D%3D").get();


			Elements cityList=doc.select("item");

			
			ArrayList<CityDTO> cityDtoList = new ArrayList<CityDTO>();
			CityDTO generateCity;
			for(Element city: cityList){
				generateCity = new CityDTO();
				
				generateCity.setOrgCd(Integer.parseInt(city.select("orgCd").text()));
				generateCity.setOrgdownNm(city.select("orgdownNm").text());
				
				cityDtoList.add(generateCity);
			}


		
		return cityDtoList;
	}
	
	@RequestMapping(path = "/getDistrict", method = RequestMethod.GET)
	public List<CityDTO> getDistrict(@RequestParam(value="uprCd") int uprCd) throws IOException{

		System.out.println(uprCd);
		Document doc = Jsoup.connect("http://openapi.animal.go.kr/openapi/service/rest/abandonmentPublicSrvc/sigungu?ServiceKey=qK1psrABUse%2B6Tww%2FOK5CxjWAGvy4TvUqb45X%2BPfcy5rPmaANYIhQbIXxobp1H7TYAeFDE%2BYSQsbndxWweL9zA%3D%3D&upr_cd=" + uprCd).get();


			Elements cityList=doc.select("item");

			
			ArrayList<CityDTO> districtDtoList = new ArrayList<CityDTO>();
			CityDTO generateDistrict;
			for(Element city: cityList){
				generateDistrict = new CityDTO();
				
				generateDistrict.setOrgCd(Integer.parseInt(city.select("orgCd").text()));
				generateDistrict.setOrgdownNm(city.select("orgdownNm").text());
				
				districtDtoList.add(generateDistrict);
			}

		
		return districtDtoList;
	}
	
	
	@RequestMapping(path = "/getMissedPetList", method = RequestMethod.GET)
	public List<CityDTO> getMissedPetList(@RequestParam(value="uprCd") int uprCd, @RequestParam(value="orgCd") int orgCd, @RequestParam(value="state") int state, @RequestParam(value="pageNo") int pageNo) throws IOException{

		System.out.println(uprCd);
		Document doc = null;
		if(uprCd==0){
		 doc = Jsoup.connect("http://openapi.animal.go.kr/openapi/service/rest/abandonmentPublicSrvc/abandonmentPublic?ServiceKey=qK1psrABUse%2B6Tww%2FOK5CxjWAGvy4TvUqb45X%2BPfcy5rPmaANYIhQbIXxobp1H7TYAeFDE%2BYSQsbndxWweL9zA%3D%3D").get();
		}
		else if(orgCd==0) {
			 doc = Jsoup.connect("http://openapi.animal.go.kr/openapi/service/rest/abandonmentPublicSrvc/abandonmentPublic?ServiceKey=qK1psrABUse%2B6Tww%2FOK5CxjWAGvy4TvUqb45X%2BPfcy5rPmaANYIhQbIXxobp1H7TYAeFDE%2BYSQsbndxWweL9zA%3D%3D&upr_cd=" + uprCd +"&pageNo="+pageNo).get();
		}
		else if(state==0) {
			 doc = Jsoup.connect("http://openapi.animal.go.kr/openapi/service/rest/abandonmentPublicSrvc/abandonmentPublic?ServiceKey=qK1psrABUse%2B6Tww%2FOK5CxjWAGvy4TvUqb45X%2BPfcy5rPmaANYIhQbIXxobp1H7TYAeFDE%2BYSQsbndxWweL9zA%3D%3D&upr_cd=" + uprCd +"&org_cd="+orgCd +"&pageNo="+pageNo).get();
		}
		else {
			 doc = Jsoup.connect("http://openapi.animal.go.kr/openapi/service/rest/abandonmentPublicSrvc/abandonmentPublic?ServiceKey=qK1psrABUse%2B6Tww%2FOK5CxjWAGvy4TvUqb45X%2BPfcy5rPmaANYIhQbIXxobp1H7TYAeFDE%2BYSQsbndxWweL9zA%3D%3D&upr_cd=" + uprCd+"&org_cd="+orgCd+"&state="+state +"&pageNo="+pageNo).get();
		}

			Elements cityList=doc.select("item");

			
			ArrayList<CityDTO> districtDtoList = new ArrayList<CityDTO>();
			CityDTO generateDistrict;
			for(Element city: cityList){
				generateDistrict = new CityDTO();
				
				generateDistrict.setOrgCd(Integer.parseInt(city.select("orgCd").text()));
				generateDistrict.setOrgdownNm(city.select("orgdownNm").text());
				
				districtDtoList.add(generateDistrict);
			}

		
		return districtDtoList;
	}
	


}