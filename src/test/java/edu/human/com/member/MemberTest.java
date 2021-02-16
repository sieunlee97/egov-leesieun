package edu.human.com.member;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import edu.human.com.member.service.EmployerInfoVO;
import edu.human.com.member.service.MemberService;
import egovframework.let.utl.sim.service.EgovFileScrty;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations ={
		"file:src/main/webapp/WEB-INF/config/egovframework/springmvc/egov-com-servlet.xml",
		"file:src/main/resources/egovframework/spring/com/*.xml"}
)
@WebAppConfiguration
public class MemberTest {
	@Inject
	private DataSource dataSource;
	
	@Inject
	private MemberService memberService;
	
	@Test
	public void updateMember() throws Exception {
		EmployerInfoVO memberVO = new EmployerInfoVO();
		memberVO.setEMPLYR_ID("user_1");
		memberVO.setORGNZT_ID("ORGNZT_0000000000000");
		memberVO.setUSER_NM("사용자_1");
		//암호값이 공백이면, 쿼리에서 제외된다.
		String secPassword = "";
		memberVO.setPASSWORD(secPassword);
		memberVO.setPASSWORD_HINT("사는 동네는?");
		memberVO.setPASSWORD_CNSR("신방동");
		memberVO.setEMAIL_ADRES(memberVO.getEMPLYR_ID()+"@abc.com");
		memberVO.setSEXDSTN_CODE("F");
		memberVO.setHOUSE_ADRES("집주소");
		memberVO.setGROUP_ID("GROUP_00000000000000");
		memberVO.setEMPLYR_STTUS_CODE("P"); //회원상태코드 P-활성, S-비활성
		//memberVO.setESNTL_ID("USRCNFRM_00000000000");
		memberService.updateMember(memberVO);
	}
	
	@Test
	public void deleteMember() throws Exception {
		int result = memberService.deleteMember("user_2");
		if(result > 0) {
			System.out.println("삭제되었습니다.");
		} else {
			System.out.println("삭제된 값이 없습니다.");
		}
		
	}
	
	@Test
	public void insertMember() throws Exception {
		EmployerInfoVO memberVO = new EmployerInfoVO(); //고전방식 객체 생성
		//memberVO에 set으로 값을 입력한 후, DB에 insert
		//emplyr_id는 기본키이기 때문에, 중복허용하지 않게 처리(아래)
		List<EmployerInfoVO> memberList = memberService.selectMember();
		memberVO.setEMPLYR_ID("user_" + memberList.size());
		memberVO.setORGNZT_ID("ORGNZT_0000000000000"); //외래키이기 때문에 
		memberVO.setUSER_NM("사용자_"+memberList.size());
		//암호화작업 (아래) 스프링시큐리티X, egov전용 시큐리티 암호화("입력한문자","입력한ID")
		String secPassword = EgovFileScrty.encryptPassword("1234", memberVO.getEMPLYR_ID());
		memberVO.setPASSWORD(secPassword);
		memberVO.setPASSWORD_HINT("사는 동네는?");
		memberVO.setPASSWORD_CNSR("쌍용동");
		memberVO.setEMAIL_ADRES("abc@abc.com");
		memberVO.setSEXDSTN_CODE("F");
		memberVO.setHOUSE_ADRES("집주소");
		memberVO.setGROUP_ID("GROUP_00000000000000");
		memberVO.setEMPLYR_STTUS_CODE("P"); //회원상태코드 P-활성, S-비활성
		memberVO.setESNTL_ID("USRCNFRM_"+memberList.size()); //고유ID이기 때문에 중복되면 안됨.
		memberService.insertMember(memberVO);
	}
	
	@Test
	public void viewMember() throws Exception {
		EmployerInfoVO memberVO = memberService.viewMember("admin");
		System.out.println("admin회원의 상세정보 : "+ memberVO.toString());
	}
	
	@Test
	public void selectMember() throws Exception {
		List<EmployerInfoVO> memberList = memberService.selectMember();
		for(EmployerInfoVO member:memberList) {
			System.out.println("현재 등록된 회원 : "+ member.toString());
		}
	}
	
	@Test
	public void dbConnect_test() throws SQLException {
		Connection connect = dataSource.getConnection();
		System.out.println("데이터베이스 커넥션 결과 : "+ connect);
	}
	
	@Test
	public void junit_test() {
		System.out.println("JUnit 실행 확인");
	}
}
