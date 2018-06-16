package com.petcare.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.google.android.gcm.server.Message;
import com.petcare.disease.DiseaseMapper;
import com.petcare.disease.GCMService;
import com.petcare.disease.GcmDTO;
import com.petcare.disease.LateDiseaseDTO;

@Component
public class GreetingBatchBean {
	@Autowired
	DiseaseMapper diseaseMapper;

	@Autowired
	GCMService gcmService;


	@Scheduled(fixedRate = 3600000)
	public void cronJob() throws IOException {

		System.out.println("Run Scheduled method!!");

		Calendar cal = Calendar.getInstance( );  // 현재 날짜/시간 등의 각종 정보 얻기
		java.text.DateFormat format = new java.text.SimpleDateFormat("yyyyMMdd");

		for(int dateCount=-6; dateCount<1; dateCount++){

			String day=format.format(cal.getTime());
			System.out.println("day > "+day);

			Document doc = Jsoup.connect("http://data.mafra.go.kr:7080/openapi/9c9027504b1265d5556462aff936f8ca978d2d6158584f902756e335fdc2091c/xml/Grid_20151204000000000316_1/1/5?OCCRRNC_DE="+day).get();

			Elements count=doc.select("totalCnt");
			int total=Integer.parseInt(count.text());

			if(total>0){
				int num1=0;
				int num2=0;
				int num3=0;
				int num4=0;
				int num5=0;
				int num6=0;
				LateDiseaseDTO lateDisease=null;
				ArrayList<LateDiseaseDTO> diseaseList = new ArrayList<LateDiseaseDTO>(6);

				Elements name=doc.select("LKNTS_NM");

				for(Element n: name){

					String lknts_nm=n.text();

					lateDisease=new LateDiseaseDTO();
					lateDisease.setLknts_nm(lknts_nm);;
					diseaseList.add(lateDisease);


				}


				Elements dgnss=doc.select("DGNSS_ENGN_NM");



				for(Element d: dgnss){

					String dgnss_engs_nm=d.text();

					LateDiseaseDTO late=diseaseList.get(num1);
					late.setDgnss_engs_nm(dgnss_engs_nm);
					diseaseList.set(num1, late);
					num1++;

				}

				//occrrnc_lvstckcnt


				Elements occrrnc=doc.select("OCCRRNC_LVSTCKCNT");



				for(Element o: occrrnc){

					String occrrnc_lvstckcnt=o.text();

					LateDiseaseDTO late=diseaseList.get(num2);
					late.setOccrrnc_lvstckcnt(occrrnc_lvstckcnt);
					diseaseList.set(num2, late);
					num2++;

				}

				//lvstckspc_nm
				Elements lvstckspc=doc.select("LVSTCKSPC_NM");



				for(Element l: lvstckspc){

					String lvstckspc_nm=l.text();

					LateDiseaseDTO late=diseaseList.get(num3);
					late.setLvstckspc_nm(lvstckspc_nm);
					diseaseList.set(num3, late);
					num3++;

				}

				//occrrnc_de
				Elements occrrnc_d=doc.select("OCCRRNC_DE");



				for(Element o: occrrnc_d){

					String occrrnc_de=o.text();

					LateDiseaseDTO late=diseaseList.get(num4);
					late.setOccrrnc_de(occrrnc_de);
					diseaseList.set(num4, late);
					num4++;

				}

				//farm_locplc
				Elements farm=doc.select("FARM_LOCPLC");

				for(Element f:farm){

					String farm_locplc=f.text();
					LateDiseaseDTO late=diseaseList.get(num5);
					late.setFarm_locplc(farm_locplc);
					diseaseList.set(num5, late);
					num5++;

				}

				//ictsd_occrrnc_no 질병 고유 번호
				Elements ictsd=doc.select("ICTSD_OCCRRNC_NO");

				for(Element i:ictsd){

					String ictsd_occrrnc_no=i.text();
					LateDiseaseDTO late=diseaseList.get(num6);
					late.setIctsd_occrrnc_no(ictsd_occrrnc_no);
					diseaseList.set(num6, late);
					num6++;

				}

				for(LateDiseaseDTO dto : diseaseList ){
					System.out.println(" 6 "+dto.getIctsd_occrrnc_no());
						int ictsd_occrrnc_no=Integer.parseInt(dto.getIctsd_occrrnc_no());
					int result =diseaseMapper.diseaseCheck(ictsd_occrrnc_no);
					if(result==0){
						diseaseMapper.lateDiseaseInsert(dto);
						//result 0이므로 데이터베이스에 없는 질병정보 -> 푸쉬실행
						String body = dto.getOccrrnc_de()+ " | " + dto.getFarm_locplc() + "에서 " + dto.getLknts_nm() + " 발생!";
						GcmDTO[] gcmList = diseaseMapper.selectRegistration();
						sendAllGcm(gcmList, body, String.valueOf(dto.getId()));
						
						//없는 정보이므로 디비에 넣기 
						
						return;
					}
				}

			}else{
				System.out.println(day+" 올라온 정보 없음");
			}
			
			cal.add(cal.DATE, -1);
		}

	}


	public void sendAllGcm(GcmDTO[] gcmList, String body, String dId) throws IOException {
		

		Message.Builder builder = new Message.Builder();
		builder.addData("title", "나만의 수의사");
		builder.addData("body", body);
		builder.addData("dId", dId);
		Message message = builder.build();

		String result = "";

		System.out.println(body);

		for(GcmDTO gcmDto : gcmList) {
			result = gcmService.send(gcmDto.getRegistrationId(), message);
			System.out.println(result);
		}
	}
}