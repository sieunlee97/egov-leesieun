package edu.human.com.util;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.human.com.member.service.EmployerInfoVO;
import edu.human.com.member.service.MemberService;
import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.LoginVO;
import egovframework.let.uat.uia.service.EgovLoginService;

@Controller
public class CommonUtil {

	@Inject
	private MemberService memberService;
	
	@Autowired
	private EgovLoginService loginService;
	
	@Autowired
	private EgovMessageSource egovMessageSource;
	
	/**
	 * 기존 로그인 처리는 egov 그대로 사용.
	 * 단, 로그인 처리 이후 이동할 페이지를 OLD에서 NEW로 변경.
	 */
	@RequestMapping(value = "/login_action.do") //변경1
	public String actionLogin(@ModelAttribute("loginVO") LoginVO loginVO, HttpServletRequest request, ModelMap model) throws Exception {

		// 1. 일반 로그인 처리
		LoginVO resultVO = loginService.actionLogin(loginVO);

		boolean loginPolicyYn = true;

		if (resultVO != null && resultVO.getId() != null && !resultVO.getId().equals("") && loginPolicyYn) {
			//로그인 성공 시
			request.getSession().setAttribute("LoginVO", resultVO);
			return "forward:/tiles/home.do"; //변경2
		} else {
			//로그인 실패시 
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.login"));
			return "login.tiles"; // 변경3
		}

	}
	
	
	@RequestMapping(value="/idcheck.do", method=RequestMethod.GET)
	@ResponseBody //반환값으로 페이지를 명시하지 않고 반환값이 텍스트라고 명시
	public String idcheck(@RequestParam("emplyr_id") String emplyr_id) throws Exception {
		String result = "0"; //기본값으로 중복 ID가 존재하지 않는다는 표시 0으로 초기화	
		EmployerInfoVO memberVO = memberService.viewMember(emplyr_id);
		if(memberVO != null) {result = "1";} // 중복ID가 존재할 때 표시 1
		
		return result; //1.jsp이 페이지로 이동한다는 뜻이 아니라, text값으로 반환한다.
	}
	
	
	/**
     * XSS 방지 처리.
     *
     * @param data
     * @return
     */
    public String unscript(String data) {
        if (data == null || data.trim().equals("")) {
            return "";
        }

        String ret = data;

        ret = ret.replaceAll("<(S|s)(C|c)(R|r)(I|i)(P|p)(T|t)", "&lt;script");
        ret = ret.replaceAll("</(S|s)(C|c)(R|r)(I|i)(P|p)(T|t)", "&lt;/script");

        ret = ret.replaceAll("<(O|o)(B|b)(J|j)(E|e)(C|c)(T|t)", "&lt;object");
        ret = ret.replaceAll("</(O|o)(B|b)(J|j)(E|e)(C|c)(T|t)", "&lt;/object");

        ret = ret.replaceAll("<(A|a)(P|p)(P|p)(L|l)(E|e)(T|t)", "&lt;applet");
        ret = ret.replaceAll("</(A|a)(P|p)(P|p)(L|l)(E|e)(T|t)", "&lt;/applet");

        ret = ret.replaceAll("<(E|e)(M|m)(B|b)(E|e)(D|d)", "&lt;embed");
        ret = ret.replaceAll("</(E|e)(M|m)(B|b)(E|e)(D|d)", "&lt;embed");

        ret = ret.replaceAll("<(F|f)(O|o)(R|r)(M|m)", "&lt;form");
        ret = ret.replaceAll("</(F|f)(O|o)(R|r)(M|m)", "&lt;form");

        return ret;
    }

}
