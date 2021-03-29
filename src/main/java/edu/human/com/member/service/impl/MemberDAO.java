package edu.human.com.member.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import edu.human.com.common.EgovComAbstractMapper;
import edu.human.com.member.service.EmployerInfoVO;
import edu.human.com.util.PageVO;

/**
 * egov에서 DAO는 sqlSession템플릿을 바로 접근하지 않고, 
 * EgovAbstractMapper클래스를 상속받아서 DAO 구현 메소드를 사용.
 * @author 이시은
 *
 */

@Repository
public class MemberDAO extends EgovComAbstractMapper {
	
	public List<EmployerInfoVO> selectMember(PageVO pageVO) throws Exception {
		return selectList("memberMapper.selectMember", pageVO);
	}
	
	public EmployerInfoVO viewMember(String emplyr_id) throws Exception {
		return selectOne("memberMapper.viewMember", emplyr_id);
	}
	public int deleteMember(String emplyr_id) throws Exception {
		// affected된 row값이 반환된다. 
		return delete("memberMapper.deleteMember", emplyr_id);
	}
	public void insertMember(EmployerInfoVO employerInfoVO) throws Exception {
		insert("memberMapper.insertMember", employerInfoVO);
	}
	public void updateMember(EmployerInfoVO employerInfoVO) throws Exception {
		update("memberMapper.updateMember", employerInfoVO);
	}
	public Map<Object, Object> selectCodeMap(String code_id) throws Exception {
		return selectMap("memberMapper.selectCodeMap", code_id, "code");
	}
	public Map<Object, Object> selectGroupMap() throws Exception {
		//memberMapper 쿼리 호출
		return selectMap("memberMapper.selectGroupMap", "group_id");
	}
}
