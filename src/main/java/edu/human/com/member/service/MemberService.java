package edu.human.com.member.service;

import java.util.List;
import java.util.Map;

public interface MemberService {
	public List<EmployerInfoVO> selectMember() throws Exception;
	public EmployerInfoVO viewMember(String emplyr_id) throws Exception;
	public int deleteMember(String emplyr_id) throws Exception;
	public void insertMember(EmployerInfoVO employerInfoVO) throws Exception;
	public void updateMember(EmployerInfoVO employerInfoVO) throws Exception;
	//공통코드 맵타입 반환받기(아래)
	public Map<Object, Object> selectCodeMap(String code_id) throws Exception;
}
