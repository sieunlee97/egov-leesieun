package edu.human.com.util;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.human.com.member.service.EmployerInfoVO;
import edu.human.com.member.service.MemberService;

@Controller
public class CommonUtil {

	@Inject
	private MemberService memberService;
	
	@RequestMapping(value="/idcheck.do", method=RequestMethod.GET)
	@ResponseBody //반환값으로 페이지를 명시하지 않고 반환값이 텍스트라고 명시
	public String idcheck(@RequestParam("emplyr_id") String emplyr_id) throws Exception {
		String result = "0"; //기본값으로 중복 ID가 존재하지 않는다는 표시 0으로 초기화	
		EmployerInfoVO memberVO = memberService.viewMember(emplyr_id);
		if(memberVO != null) {result = "1";} // 중복ID가 존재할 때 표시 1
		
		return result; //1.jsp이 페이지로 이동한다는 뜻이 아니라, text값으로 반환한다.
	}
}
