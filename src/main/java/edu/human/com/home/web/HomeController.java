package edu.human.com.home.web;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.impl.SimpleLog;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springmodules.validation.commons.DefaultBeanValidator;

import edu.human.com.board.service.BoardService;
import edu.human.com.member.service.EmployerInfoVO;
import edu.human.com.member.service.MemberService;
import edu.human.com.util.CommonUtil;
import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.LoginVO;
import egovframework.com.cmm.service.EgovFileMngService;
import egovframework.com.cmm.service.EgovFileMngUtil;
import egovframework.com.cmm.service.FileVO;
import egovframework.com.cmm.util.EgovUserDetailsHelper;
import egovframework.let.cop.bbs.service.Board;
import egovframework.let.cop.bbs.service.BoardMaster;
import egovframework.let.cop.bbs.service.BoardMasterVO;
import egovframework.let.cop.bbs.service.BoardVO;
import egovframework.let.cop.bbs.service.EgovBBSAttributeManageService;
import egovframework.let.cop.bbs.service.EgovBBSManageService;
import egovframework.let.utl.sim.service.EgovFileScrty;
import egovframework.rte.fdl.property.EgovPropertyService;
import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

@Controller
public class HomeController {
	
	private Logger logger = Logger.getLogger(SimpleLog.class);
	
	@Autowired
	private EgovBBSAttributeManageService bbsAttrbService;
	@Autowired
	private EgovPropertyService propertyService;
	@Autowired
	private EgovBBSManageService bbsMngService;
	@Autowired
	private EgovMessageSource egovMessageSource;
	@Autowired
	private DefaultBeanValidator beanValidator;
	@Autowired
	private EgovFileMngUtil fileUtil;
	@Autowired
	private EgovFileMngService fileMngService; 

	
	@Inject
	private CommonUtil commUtil;
	@Inject
	private BoardService boardService;
	@Inject
	private MemberService memberService;
	@RequestMapping("tiles/member/mypage_delete.do")
	public String mypage_delete(HttpServletRequest request,EmployerInfoVO memberVO, RedirectAttributes rdat) throws Exception {
		//회원 수정 페이지 DB처리
		if(memberVO.getPASSWORD() != null && memberVO.getPASSWORD() != "") {
			String formPassword = memberVO.getPASSWORD();
			String encPassword = EgovFileScrty.encryptPassword(formPassword, memberVO.getEMPLYR_ID());
			memberVO.setPASSWORD(encPassword);
		}
		memberVO.setEMPLYR_STTUS_CODE("S"); //회원 비활성화로 변경
		memberService.updateMember(memberVO);
		rdat.addFlashAttribute("msg", "회원 탈퇴");
		//세션 날리기
		request.getSession().invalidate(); //현재 URL의 모둔 LoginVO 세션값 날림
		return "redirect:/tiles/home.do";
	}
	@RequestMapping("/tiles/member/mypage.do")
	public String mypage (EmployerInfoVO memberVO, RedirectAttributes rdat) throws Exception {
		//회원 수정 페이지 DB처리
		if(memberVO.getPASSWORD() != null && memberVO.getPASSWORD() != "") {
			String formPassword = memberVO.getPASSWORD();
			String encPassword = EgovFileScrty.encryptPassword(formPassword, memberVO.getEMPLYR_ID());
			memberVO.setPASSWORD(encPassword);
		}
		memberService.updateMember(memberVO);
		rdat.addFlashAttribute("msg", "수정");
		return "redirect:/tiles/member/mypage_form.do";
	}
	@RequestMapping("/tiles/member/mypage_form.do")
	public String mypage_form(HttpServletRequest request, Model model) throws Exception {
		//마이페이지 이동
		LoginVO sessionLoginVO = (LoginVO) request.getSession().getAttribute("LoginVO");
		EmployerInfoVO memberVO = memberService.viewMember(sessionLoginVO.getId());
		System.out.println("디버그 세션 아이디 출력 : "+ sessionLoginVO.getId());
		model.addAttribute("memberVO", memberVO);
		//공통코드 로그인활성/비활성 해시맵 오브젝트 생성(아래)
		//System.out.println("디버그 : "+ memberService.selectCodeMap("COM999"));
		//맵결과) 디버그 : {P={CODE=P, CODE_NM=활성}, S={CODE=S, CODE_NM=비활성}}
		model.addAttribute("codeMap", memberService.selectCodeMap("COM999"));
		//그룹이름 해시맵 오브젝트 생성(아래)
		model.addAttribute("groupMap", memberService.selectGroupMap());
		return "member/mypage.tiles";
	}
	
	@RequestMapping("/tiles/join.do")
	public String join(EmployerInfoVO memberVO,RedirectAttributes rdat) throws Exception {
		//입력DB처리 호출
		//1. egov암호화툴로 암호화
		String formPassword = memberVO.getPASSWORD(); //jsp입력폼에서 전송된 암호값GET
		String encPassword = EgovFileScrty.encryptPassword(formPassword, memberVO.getEMPLYR_ID());
		memberVO.setPASSWORD(encPassword); //egov암호화툴로 암호화된 값 SET
		//2. ESNTL_ID 고유 ID 생성
		memberVO.setESNTL_ID("USRCNFRM_"+ memberVO.getEMPLYR_ID()); //고유ID값 SET
		memberService.insertMember(memberVO);
		rdat.addFlashAttribute("msg", "회원가입");
		return "redirect:/tiles/home.do";
	}
	
	@RequestMapping("/tiles/join_form.do")
	public String join_form() throws Exception {
		return "join.tiles";
	}
	
	//갤러리 최근 게시물 미리보기 처리
	@RequestMapping("/tiles/board/previewImage.do")
	public void previewImage(HttpServletRequest request, HttpServletResponse response, @RequestParam("atchFileId") String atchFileId) throws Exception {
		FileVO fileVO = new FileVO();
		fileVO.setAtchFileId(atchFileId);
		for(int cnt=0; cnt<3; cnt++) {
			fileVO.setFileSn(Integer.toString(cnt));
			fileVO = fileMngService.selectFileInf(fileVO);
			if(fileVO != null) {
				break;
			}
		}
		
		File file = null;
		//첨부파일 확장자가 이미지가 아닐 때, 액박이미지 대신 대체 이미지 지정(아래)
		String[] imgCheck = {"jpg", "jpeg", "gif", "png", "bmp"};
		boolean boolCheck = Arrays.asList(imgCheck).contains(fileVO.getFileExtsn().toLowerCase());
		if(boolCheck == false) { //첨부파일이 이미지가 아니라면,
			System.out.println("디버그 :"+ fileVO.getFileExtsn().toLowerCase());
			//위에서 구한 첨부파일 저장위치, 저장파일명을 가지고 화면에 출력-스트림(아래)
			String path = request.getSession().getServletContext().getRealPath("/resources/home/img/no_image.png");
			System.out.println("디버그 path2: " + path);
			file = new File(path);
		} else {
			file = new File(fileVO.getFileStreCours(), fileVO.getStreFileNm());
		}
		
		//스트리밍에 필요한 클래스 변수 생성(아래 3가지)
		FileInputStream fis = new FileInputStream(file); //저장된 파일을 스트림클래스를 이용해서 읽어들임
		BufferedInputStream bis = new BufferedInputStream(fis); //중간저장소 : 내용 날아가지 않도록. fis인풋스트림(필수값)을 받아서 버퍼에 저장
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		int imgByte; // while 반복문의 반복조건에 사용될 변수
		while((imgByte=bis.read()) != -1) {
			baos.write(imgByte);
		}
//===================여기까지가 baos객체(출력버퍼)에 이미지 내용을 임시저장한 상태(위)===========================
		
		String type=""; //type변수 초기화
		if( fileVO.getFileExtsn() != null && !"".equals(fileVO.getFileExtsn()) ){
			// 첨부파일 확장이름이 존재할 때, 이미지 파일을 체크한다.(아래)
			if("jpg".equals(fileVO.getFileExtsn().toLowerCase())) {
				type="image/jpeg"; //jpg != jpeg 이름이 달라서 조건 필요
			} else {
				type="imge/"+fileVO.getFileExtsn().toLowerCase();
			}
		}
		//브라우저에서 출력하는 response 응답코드(아래)
		response.setHeader("Content-Type", type);
		response.setContentLength(baos.size());
		baos.writeTo(response.getOutputStream()); //실제 출력 전송
		response.getOutputStream().flush(); //실제 화면에 출력
		response.getOutputStream().close(); //응답객체 종료 ( 메모리 날리기)

	}
	
	@RequestMapping("/tiles/board/update_board.do")	
	public String update_board(final MultipartHttpServletRequest multiRequest, @ModelAttribute("searchVO") BoardVO boardVO,
		    @ModelAttribute("bdMstr") BoardMaster bdMstr, @ModelAttribute("board") Board board, BindingResult bindingResult, ModelMap model,
		    SessionStatus status, RedirectAttributes rdat) throws Exception {

	    	// 사용자권한 처리
	    	if(!EgovUserDetailsHelper.isAuthenticated()) {
	    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.login"));
	        	return "login.tiles";
	    	}

		LoginVO user = (LoginVO)EgovUserDetailsHelper.getAuthenticatedUser();
		Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();

		String atchFileId = boardVO.getAtchFileId();

		beanValidator.validate(board, bindingResult);
		if (bindingResult.hasErrors()) {

		    boardVO.setFrstRegisterId(user.getUniqId());

		    BoardMaster master = new BoardMaster();
		    BoardMasterVO bmvo = new BoardMasterVO();
		    BoardVO bdvo = new BoardVO();

		    master.setBbsId(boardVO.getBbsId());
		    master.setUniqId(user.getUniqId());

		    bmvo = bbsAttrbService.selectBBSMasterInf(master);
		    bdvo = bbsMngService.selectBoardArticle(boardVO);

		    model.addAttribute("result", bdvo);
		    model.addAttribute("bdMstr", bmvo);

		    return "board/update_board.tiles";
		}

		if (isAuthenticated) {
		    final Map<String, MultipartFile> files = multiRequest.getFileMap();
		    if (!files.isEmpty()) {
				if ("".equals(atchFileId)) {  // 기존 첨부파일이 존재하지 않으면 신규등록
					System.out.println("디버그1"+atchFileId);
				    List<FileVO> result = fileUtil.parseFileInf(files, "BBS_", 0, atchFileId, "");
				    atchFileId = fileMngService.insertFileInfs(result);
				    board.setAtchFileId(atchFileId);
				} else { // 기존 첨부파일이 존재하면
					System.out.println("디버그) 파일ID : "+atchFileId);
				    FileVO fvo = new FileVO();
				    fvo.setAtchFileId(atchFileId);
				    int cnt = fileMngService.getMaxFileSN(fvo);
				    List<FileVO> _result = fileUtil.parseFileInf(files, "BBS_", cnt, atchFileId, "");
				    fileMngService.updateFileInfs(_result);
				}
		    }

		    board.setLastUpdusrId(user.getUniqId());

		    board.setNtcrNm("");	// dummy 오류 수정 (익명이 아닌 경우 validator 처리를 위해 dummy로 지정됨)
		    board.setPassword("");	// dummy 오류 수정 (익명이 아닌 경우 validator 처리를 위해 dummy로 지정됨)
		    //게시물 업데이트 레코드 처리(아래)
		    bbsMngService.updateBoardArticle(board);
		}
			BoardVO bdvo = new BoardVO();
		    bdvo = bbsMngService.selectBoardArticle(boardVO);
		    rdat.addFlashAttribute("msg", "수정");
		    
		    return "redirect:/tiles/board/view_board.do?bbsId="+bdvo.getBbsId()
			+"&nttId="+bdvo.getNttId()+"&bbsTyCode="+bdvo.getBbsTyCode()
			+"&bbsAttrbCode="+bdvo.getBbsAttrbCode()+"&authFlag=Y"
			+"&pageIndex="+bdvo.getPageIndex();	

		//return "redirect:/admin/board/list_board.do?bbsId="+board.getBbsId();
	}
	
	@RequestMapping("/tiles/board/update_board_form.do")
	public String update_board_form (@ModelAttribute("searchVO") BoardVO boardVO, @ModelAttribute("board") BoardVO vo, ModelMap model)
		    throws Exception {

		// 로그인 체크 (비로그인시, 로그인 페이지로 이동)
		if(!boardVO.getBbsId().equals("BBSMSTR_BBBBBBBBBBBB") && !EgovUserDetailsHelper.isAuthenticated()) {
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.login"));
	    	return "login.tiles";
		}

		LoginVO user = (LoginVO)EgovUserDetailsHelper.getAuthenticatedUser();
		Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();

		boardVO.setFrstRegisterId(user.getUniqId());

		BoardMaster master = new BoardMaster();
		BoardMasterVO bmvo = new BoardMasterVO();
		BoardVO bdvo = new BoardVO();

		vo.setBbsId(boardVO.getBbsId());

		master.setBbsId(boardVO.getBbsId());
		master.setUniqId(user.getUniqId());

		if (isAuthenticated) {
		    bmvo = bbsAttrbService.selectBBSMasterInf(master);
		    bdvo = bbsMngService.selectBoardArticle(boardVO);
		    logger.debug("디버그 : 인증받은 사용자 정보를 저장한 객체 - "+ user.getUniqId());
		    logger.debug("디버그 : 게시판의 등록자의 아이디 - "+ bdvo.getFrstRegisterId());
		}
			

		model.addAttribute("result", bdvo); //게시물 정보가 담긴 오브젝트(게시물제목, 내용, 첨부파일id,...)
		model.addAttribute("bdMstr", bmvo); //게시판 정보가 담긴 오브젝트(게시판명, 게시판id,...)

		//----------------------------
		// 기본 BBS template 지정 게시판ID별로 필요한 디자인 css파일 변경시켜준다.
		//----------------------------
		if (bmvo.getTmplatCours() == null || bmvo.getTmplatCours().equals("")) {
		    bmvo.setTmplatCours("/css/egovframework/cop/bbs/egovBaseTemplate.css");
		}
		model.addAttribute("brdMstrVO", bmvo); //위에서 정의된 bdMstr 모델과 같음. 두명 이상이 만들어서
		
		//게시물 작성자와 로그인한 사용자 활동ID 비교-------------------------------------------------------
		if(user.getUniqId() != bdvo.getFrstRegisterId()) {
			model.addAttribute("msg", "본인이 작성한 글만 수정이 가능합니다.\\n이전 페이지로 이동");
			return "board/view_board.tiles";
		}
		////-----------------------------
		return "board/update_board.tiles";
	}
	
	@RequestMapping("/tiles/board/delete_board.do")
	public String delete_board(FileVO fileVO, BoardVO boardVO, RedirectAttributes rdat) throws Exception {
		if(boardVO.getAtchFileId() != null && !"".equals(boardVO.getAtchFileId())) {
			System.out.println("디버그) 첨부파일 ID : "+boardVO.getAtchFileId());
			//fileVO.setAtchFileId(boardVO.getAtchFileId());
			//fileMngService.deleteAllFileInf(fileVO); //USE_AT='N', 삭제X
			//물리파일 지우려면 2가지 값 필수 : file_stre_cours, stre_file_nm
			
			//실제 폴더에서 파일도 지우기(아래:1개만 삭제하는 로직 -> 여러개 삭제하는 로직으로 변경)
			if(fileVO.getAtchFileId() != null && fileVO.getAtchFileId() !="") {
				List<FileVO> fileList = fileMngService.selectFileInfs(fileVO);
				for(FileVO delfileVO : fileList) {
					File target = new File(delfileVO.getFileStreCours(), delfileVO.getStreFileNm());
					if(target.exists()) {
						target.delete();//폴더에서 기존첨부파일 지우기
						System.out.println("디버그 : 첨부파일 삭제OK");
					}
				}	
			}
			//첨부파일 레코드 삭제(아래)
			boardService.delete_attach(boardVO.getAtchFileId());
		}
		//게시물 삭제(아래)
		boardService.delete_board((int)boardVO.getNttId());
		
		rdat.addFlashAttribute("msg", "삭제");
		return "redirect:/tiles/board/list_board.do?bbsId="+boardVO.getBbsId();
	}
	
	@RequestMapping("/tiles/board/insert_board.do")
	public String insert_board(final MultipartHttpServletRequest multiRequest, @ModelAttribute("searchVO") BoardVO boardVO,
		    @ModelAttribute("bdMstr") BoardMaster bdMstr, @ModelAttribute("board") Board board, BindingResult bindingResult, SessionStatus status,
		    ModelMap model, RedirectAttributes rdat) throws Exception {
		// 사용자권한 처리
		if(!EgovUserDetailsHelper.isAuthenticated()) {
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.login"));
	    	return "login.tiles";
		}

		LoginVO user = (LoginVO)EgovUserDetailsHelper.getAuthenticatedUser();
		Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();

		beanValidator.validate(board, bindingResult);
		if (bindingResult.hasErrors()) { //전송값이 문자인데 필드값은 날짜일 때 바인딩 에러 발생. 이때 if문 실행
			System.out.println("디버그"+board.toString());
		    BoardMasterVO master = new BoardMasterVO();
		    BoardMasterVO vo = new BoardMasterVO();

		    vo.setBbsId(boardVO.getBbsId());
		    vo.setUniqId(user.getUniqId());

		    master = bbsAttrbService.selectBBSMasterInf(vo);

		    model.addAttribute("bdMstr", master);

		    //----------------------------
		    // 기본 BBS template 지정
		    //----------------------------
		    if (master.getTmplatCours() == null || master.getTmplatCours().equals("")) {
			master.setTmplatCours("/css/egovframework/cop/bbs/egovBaseTemplate.css");
		    }

		    model.addAttribute("brdMstrVO", master);
		    ////-----------------------------

		    return "board/insert_board.tiles";
		}

		if (isAuthenticated) {
		    List<FileVO> result = null;
		    String atchFileId = "";

		    final Map<String, MultipartFile> files = multiRequest.getFileMap();
		    if (!files.isEmpty()) {
			result = fileUtil.parseFileInf(files, "BBS_", 0, "", "");
			atchFileId = fileMngService.insertFileInfs(result);
		    }
		    board.setAtchFileId(atchFileId);
		    board.setFrstRegisterId(user.getUniqId());
		    board.setBbsId(board.getBbsId());

		    board.setNtcrNm("");	// dummy 오류 수정 (익명이 아닌 경우 validator 처리를 위해 dummy로 지정됨)
		    board.setPassword("");	// dummy 오류 수정 (익명이 아닌 경우 validator 처리를 위해 dummy로 지정됨)
		    //board.setNttCn(unscript(board.getNttCn()));	// XSS 방지

		    bbsMngService.insertBoardArticle(board);
		}
		
		rdat.addFlashAttribute("msg", "등록");
		return "redirect:/tiles/board/list_board.do?bbsId="+board.getBbsId();
	}
	
	//게시물 등록 폼 화면 호출 POST
	@RequestMapping("/tiles/board/insert_board_form.do")
	public String insert_board_form(@ModelAttribute("searchVO") BoardVO boardVO, ModelMap model) throws Exception {
		// 사용자권한 처리
		if(!EgovUserDetailsHelper.isAuthenticated()) {
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.login"));
	    	return "login.tiles";
		}

	    LoginVO user = (LoginVO)EgovUserDetailsHelper.getAuthenticatedUser();
		Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();

		BoardMasterVO bdMstr = new BoardMasterVO();

		if (isAuthenticated) {

		    BoardMasterVO vo = new BoardMasterVO();
		    vo.setBbsId(boardVO.getBbsId());
		    vo.setUniqId(user.getUniqId());

		    bdMstr = bbsAttrbService.selectBBSMasterInf(vo);
		    model.addAttribute("bdMstr", bdMstr);
		}

		//----------------------------
		// 기본 BBS template 지정
		//----------------------------
		if (bdMstr.getTmplatCours() == null || bdMstr.getTmplatCours().equals("")) {
		    bdMstr.setTmplatCours("/css/egovframework/cop/bbs/egovBaseTemplate.css");
		}

		model.addAttribute("brdMstrVO", bdMstr);
		////-----------------------------
		return "board/insert_board.tiles";
	}
	
	
	@RequestMapping("/tiles/board/view_board.do")
	public String view_board(@ModelAttribute("searchVO") BoardVO boardVO, ModelMap model) throws Exception {
		LoginVO user = new LoginVO();
	    if(EgovUserDetailsHelper.isAuthenticated()){
	    	user = (LoginVO)EgovUserDetailsHelper.getAuthenticatedUser();
		}

		// 조회수 증가 여부 지정
		boardVO.setPlusCount(true);

		//---------------------------------
		// 2009.06.29 : 2단계 기능 추가
		//---------------------------------
		if (!boardVO.getSubPageIndex().equals("")) {
		    boardVO.setPlusCount(false);
		}
		////-------------------------------

		boardVO.setLastUpdusrId(user.getUniqId());
		BoardVO vo = bbsMngService.selectBoardArticle(boardVO);
		//시큐어코딩 시작(게시물제목/내용에서 자바스크립트 코드 제거)
		String subject = commUtil.unscript(vo.getNttSj()); //게시물제목
		String content = commUtil.unscript(vo.getNttCn()); //게시물내용
		vo.setNttSj(subject);
		vo.setNttCn(content);
	
		model.addAttribute("result", vo);

		model.addAttribute("sessionUniqId", user.getUniqId());

		//----------------------------
		// template 처리 (기본 BBS template 지정  포함)
		//----------------------------
		BoardMasterVO master = new BoardMasterVO();

		master.setBbsId(boardVO.getBbsId());
		master.setUniqId(user.getUniqId());

		BoardMasterVO masterVo = bbsAttrbService.selectBBSMasterInf(master);

		if (masterVo.getTmplatCours() == null || masterVo.getTmplatCours().equals("")) {
		    masterVo.setTmplatCours("/css/egovframework/cop/bbs/egovBaseTemplate.css");
		}

		model.addAttribute("brdMstrVO", masterVo);
		
		return "board/view_board.tiles"; //.tiles로 리턴받으면, 루트가 tiles폴더가 루트가 되고, view_board.jsp가 호출
	}
	
	@RequestMapping("/tiles/board/list_board.do")
	public String list_board(@ModelAttribute("searchVO") BoardVO boardVO, ModelMap model) throws Exception {
		LoginVO user = (LoginVO)EgovUserDetailsHelper.getAuthenticatedUser();

		boardVO.setBbsId(boardVO.getBbsId());
		boardVO.setBbsNm(boardVO.getBbsNm());

		BoardMasterVO vo = new BoardMasterVO();
		System.out.println("디버그: 게시판아이디는 "+boardVO.getBbsId());
		vo.setBbsId(boardVO.getBbsId());
		vo.setUniqId(user.getUniqId());

		BoardMasterVO master = bbsAttrbService.selectBBSMasterInf(vo);

		//-------------------------------
		// 방명록이면 방명록 URL로 forward
		//-------------------------------
		if (master.getBbsTyCode().equals("BBST04")) {
		    return "forward:/cop/bbs/selectGuestList.do";
		}
		////-----------------------------

		boardVO.setPageUnit(propertyService.getInt("pageUnit"));
		boardVO.setPageSize(propertyService.getInt("pageSize"));

		PaginationInfo paginationInfo = new PaginationInfo();

		paginationInfo.setCurrentPageNo(boardVO.getPageIndex());
		paginationInfo.setRecordCountPerPage(boardVO.getPageUnit());
		paginationInfo.setPageSize(boardVO.getPageSize());

		boardVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
		boardVO.setLastIndex(paginationInfo.getLastRecordIndex());
		boardVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());

		Map<String, Object> map = bbsMngService.selectBoardArticles(boardVO, vo.getBbsAttrbCode());
		int totCnt = Integer.parseInt((String)map.get("resultCnt"));

		paginationInfo.setTotalRecordCount(totCnt);

		//-------------------------------
		// 기본 BBS template 지정
		//-------------------------------
		if (master.getTmplatCours() == null || master.getTmplatCours().equals("")) {
		    master.setTmplatCours("/css/egovframework/cop/bbs/egovBaseTemplate.css");
		}
		////-----------------------------

		model.addAttribute("resultList", map.get("resultList"));
		model.addAttribute("resultCnt", map.get("resultCnt"));
		model.addAttribute("boardVO", boardVO);
		model.addAttribute("brdMstrVO", master);
		model.addAttribute("paginationInfo", paginationInfo);
		
		return "board/list_board.tiles";
	}
	
	@RequestMapping("/tiles/login.do")
	public String login() throws Exception {
		return "login.tiles";
	}
	
	@RequestMapping("/logout.do")
	public String logout(HttpServletRequest request) throws Exception {
		//RequestContextHolder.getRequestAttributes().removeAttribute("LoginVO", RequestAttributes.SCOPE_SESSION);
		request.getSession().invalidate(); //현재 URL의 모둔 세션값(ROLE_ADMIN포함) 날림
		return "redirect:/";
	}
	
	//method.RequestMethoid=GET[POST] 없이 사용하면, 둘다 허용되는 매핑이됨
	@RequestMapping("/tiles/home.do")
	public String home(ModelMap model) throws Exception {
		//메인페이지에 최근 게시물 출력하는 서비스 호출 전 Get/Set
		BoardVO boardVO = new BoardVO();
		Map<String, Object> boardMap = null;
		boardVO.setPageUnit(3); //한페이지당 출력할 개수 5개
		boardVO.setPageSize(10); //리스트하단 표시할 페이징 개수
		boardVO.setBbsId("BBSMSTR_BBBBBBBBBBBB"); //갤러리 3개
		
		PaginationInfo paginationInfo = new PaginationInfo();

		paginationInfo.setCurrentPageNo(boardVO.getPageIndex());
		paginationInfo.setRecordCountPerPage(boardVO.getPageUnit());
		paginationInfo.setPageSize(boardVO.getPageSize());

		boardVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
		boardVO.setLastIndex(paginationInfo.getLastRecordIndex());
		boardVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
		
		boardMap = bbsMngService.selectBoardArticles(boardVO, "BBSA02");
		model.addAttribute("galleryList", boardMap.get("resultList"));
		
		boardVO.setPageUnit(5);
		boardVO.setBbsId("BBSMSTR_AAAAAAAAAAAA");
		boardMap = bbsMngService.selectBoardArticles(boardVO, "BBSA02");
		model.addAttribute("noticeList", boardMap.get("resultList"));
		return "home.tiles";
	}
}
