package com.petcare.disease;



import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


@Mapper
public interface DiseaseMapper {

	DiseaseDTO diseaseDetail(int id);
	
	DiseaseDTO[] diseaseList(String category);
	
	int countFromDisease(String category);
	
	void registrationIdInsert(GcmDTO gcmDto);
	
	String getRegistrationId(int id);
	
	GcmDTO selectRegistrationId(GcmDTO gcmDto);
	
	GcmDTO[] selectRegistration();

	int lateDiseaseInsert(LateDiseaseDTO lateDisease);
	
    int diseaseCheck(int ictsd_occrrnc_no);
    
    LateDiseaseDTO[] getLateDiseaseList(int page);
    
    LateDiseaseDTO lateDiseaseDetail(int id);
    
    int countFromlateDisease();
    
    String[] autocomplete(@Param("type") int type, @Param("text") String text, @Param("category") String category);
    
    DiseaseDTO[] searchDiseaseList(@Param("type") int type, @Param("text") String text, @Param("category") String category, @Param("page") int page);
    
    int searchDiseaseListCount(@Param("type") int type, @Param("text") String text, @Param("category") String category);
    
    
}
