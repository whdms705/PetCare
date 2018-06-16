package com.petcare.disease;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class DiseaseController {

	@Autowired
	DiseaseMapper diseaseMapper;

	@Autowired
	GCMService gcmService;

	@RequestMapping(path = "/uidInsert", method = RequestMethod.POST)
	public String registerId(GcmDTO gcmDto) {

		if(diseaseMapper.selectRegistrationId(gcmDto) == null) {
			diseaseMapper.registrationIdInsert(gcmDto);
		}

		return "success";
	}

	@RequestMapping(path = "/disease/{d_id}", method = RequestMethod.GET)
	public DiseaseDTO showDetail(@RequestParam(value="list[]") String[] list,@PathVariable(value = "d_id")int id){

		DiseaseDTO diseaseDetail=diseaseMapper.diseaseDetail(id);
		String symptom=diseaseDetail.getD_symptom();
		
		for(int i=0;i<list.length;i++){
			symptom=symptom.replace(list[i], "<span class='highlight'>"+list[i]+"</span>");
		}

		
		diseaseDetail.setD_symptom(symptom);
		


		return diseaseDetail;
	}

	@RequestMapping(path = "/lateDisease", method = RequestMethod.GET)
	public Map<String,Object> getLateDiseaseList(@RequestParam(value = "page") int page) {
		Map<String,Object> map = new HashMap<String,Object>();

		int pageCount = (int) (((diseaseMapper.countFromlateDisease() - 1) / 10) + 1);
		map.put("pageCount", pageCount);

		int start = (page-1) * 10;

		LateDiseaseDTO[] lateDiseaseList = diseaseMapper.getLateDiseaseList(start);


		map.put("lateDiseaseList", lateDiseaseList);


		return map;
	}

	@RequestMapping(path = "/lateDisease/{id}", method = RequestMethod.GET)
	public LateDiseaseDTO showLateDiseaseDetail(@PathVariable(value = "id")int id){

		System.out.println(id);

		LateDiseaseDTO lateDiseaseDetail=diseaseMapper.lateDiseaseDetail(id);


		return lateDiseaseDetail;
	}


	@RequestMapping(path = "/disease", method = RequestMethod.GET)
	public Map<String,Object> diseaseList(@RequestParam(value="list[]") String[] list, @RequestParam(value="category") String category, @RequestParam(value="page") int page) {

		for(String temp : list) {
			System.out.print(temp + " ");
		}
		System.out.println();
		System.out.println(category);

		Map<String,Object> map = new HashMap<String,Object>();


		DiseaseDTO[] dtoList = diseaseMapper.diseaseList(category);		

		List<DiseaseDTO> diseaseList = new ArrayList<DiseaseDTO>();
		List<DiseaseDTO> diseaseList2 = new ArrayList<DiseaseDTO>();

		int startIndex = (page-1) * 10;

		//page-1 * 10 < 異쒕젰�븷 �씤�뜳�뒪�궗�씠  <=  (page * pagesize)-1
		int recordCount=0;

		for(DiseaseDTO dto : dtoList) {
			for(String symptom : list) {
				if(dto.getD_symptom().matches(".*"+symptom+".*")) {
					dto.setCount(dto.getCount()+1);
				}	
			}
			if(dto.getCount() > 0){
				diseaseList.add(dto);
				recordCount++;
			}
		}

		int pageCount = (int) (((recordCount - 1) / 10) + 1);
		map.put("pageCount", pageCount);


		Collections.sort(diseaseList, new CountDescCompare());


		for(int i=0; i<10 && recordCount > startIndex+i; i++) {

			diseaseList2.add(diseaseList.get(startIndex+i));

		}

		map.put("diseaseList", diseaseList2);

		return map;
	}
	public static class CountDescCompare implements Comparator<DiseaseDTO> { // Count濡� �궡由쇱감�닚 �젙�젹 
		/**
		 * �궡由쇱감�닚(DESC)
		 */
		public int compare(DiseaseDTO arg0, DiseaseDTO arg1) {
			// TODO Auto-generated method stub
			return arg0.getCount() > arg1.getCount() ? -1 : arg0.getCount() < arg1.getCount() ? 1:0;
		}


	}
	


	//autocomplete 遺�遺�
	@RequestMapping(path = "/autocomplete", method = RequestMethod.GET)
	public String[] getAutocomplete(@RequestParam(value="type") int type, @RequestParam(value="text") String text, @RequestParam(value="category") String category) {
		System.out.println("type >> "+type+" text "+text+" category >>"+category);
		String[] list = diseaseMapper.autocomplete(type, text, category);
		System.out.println(list.toString());
		return list;
	}
	
	
	@RequestMapping(path = "/searchList", method = RequestMethod.GET)
	public Map<String, Object> getSearchList(@RequestParam(value="type") int type, @RequestParam(value="text") String text, @RequestParam(value="category") String category, @RequestParam(value="page") int page) {
		Map<String,Object> map = new HashMap<String,Object>();
		
		int pageCount = (int) (((diseaseMapper.searchDiseaseListCount(type, text, category) - 1) / 10) + 1);
		map.put("pageCount", pageCount);

		int startPage = (page-1) * 10;

		DiseaseDTO[] diseaseList = diseaseMapper.searchDiseaseList(type, text, category, startPage);


		map.put("diseaseList", diseaseList);
		

		return map;
	}


}