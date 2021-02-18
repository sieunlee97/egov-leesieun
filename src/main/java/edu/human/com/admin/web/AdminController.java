package edu.human.com.admin.web;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.human.com.member.service.EmployerInfoVO;
import edu.human.com.member.service.MemberService;

@Controller
public class AdminController {
	@Inject
	private MemberService memberService;
	
	@RequestMapping(value="/admin/member/insert_member.do", method=RequestMethod.POST)
	public String insert_member(EmployerInfoVO memberVO,RedirectAttributes rdat) throws Exception {
		//입력DB처리 호출
		memberService.insertMember(memberVO);
		rdat.addFlashAttribute("msg", "등록");
		return "redirect:/admin/member/view_member.do";
	}
	
	@RequestMapping(value="/admin/member/insert_member.do", method=RequestMethod.GET)
	public String insert_member(Model model) throws Exception {
		//입력폼 호출
		model.addAttribute("codeMap", memberService.selectCodeMap("COM999"));
		model.addAttribute("groupMap", memberService.selectGroupMap());
		return "admin/member/insert_member";
	}
	
	@RequestMapping(value="/admin/member/delete_member.do", method=RequestMethod.POST)
	public String delete_member(EmployerInfoVO memberVO, RedirectAttributes rdat) throws Exception {
		memberService.deleteMember(memberVO.getEMPLYR_ID());
		rdat.addFlashAttribute("msg", "삭제");
		return "redirect:/admin/member/list_member.do";
	}
	
	@RequestMapping(value="/admin/member/update_member.do", method=RequestMethod.POST)
	public String update_member(EmployerInfoVO memberVO, RedirectAttributes rdat) throws Exception {
		//회원 수정 페이지 DB처리
		memberService.updateMember(memberVO);
		rdat.addFlashAttribute("msg", "수정"); //아래 view_member.jsp로 변수 msg값을 전송한다.
		return "redirect:/admin/member/view_member.do?emplyr_id="+memberVO.getEMPLYR_ID();
	}
	
	@RequestMapping(value="/admin/member/view_member.do", method=RequestMethod.GET)
	public String view_member(Model model, @RequestParam("emplyr_id") String emplyr_id) throws Exception {
		//회원 상세보기[수정] 페이지 이동
		EmployerInfoVO memberVO = memberService.viewMember(emplyr_id);
		model.addAttribute("memberVO", memberVO);
		//공통코드 로그인활성/비활성 해시맵 오브젝트 생성(아래)
		//System.out.println("디버그 : "+ memberService.selectCodeMap("COM999"));
		//맵결과) 디버그 : {P={CODE=P, CODE_NM=활성}, S={CODE=S, CODE_NM=비활성}}
		model.addAttribute("codeMap", memberService.selectCodeMap("COM999"));
		//그룹이름 해시맵 오브젝트 생성(아래)
		model.addAttribute("groupMap", memberService.selectGroupMap());
		return "admin/member/view_member";
	}
	
	@RequestMapping(value="/admin/member/list_member.do", method=RequestMethod.GET)
	public String list_member(Model model) throws Exception {
		//회원관리 페이지 이동
		List<EmployerInfoVO> listMember = memberService.selectMember();
		model.addAttribute("listMember", listMember);
		return "admin/member/list_member";
	}
	
	@RequestMapping(value="/admin/home.do", method=RequestMethod.GET)
	public String home() throws Exception {
		return "admin/home";
	}
}
