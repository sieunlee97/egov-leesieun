# 전자정부표준프레임워크 커스터마이징 하기
**eGovFrame메뉴에서 Start > New Template Project 심플홈 템플릿 만들어서 커스터마이징 작업중**

### 20210324(수)
- <작업예정>
- [ ] 화면 권한 추가 : 관리자단 권한 관리 메뉴 추가 후 CRDU 작업 예정.
- [X] 글 수정/삭제는 본인이 작성한 글만 가능하도록 처리.


<수업>
- 글 수정/삭제 본인이 작성한 글만 가능하도록 처리.
- LoginVO.uniqId와 게시판 작성 아이디 frstRegisterId를 비교해서 수정/삭제 권한 제어
- 전자정부 : 회원 ID(userId)와 활동하는 ID(uniqId) 같지 않게 구성해서, 게시판의 등록자 ID != 회원ID / 게시판의 등록자 ID == 활동 ID
- > frstRegisterId(게시판 등록자 ID) == uniqId(활동 ID)


### 20210322(월)
- <수업>
- 순서1. pom.xml 수정 
- > 주의:egovframework.rte.fdl.security-3.10.0.jar버전으로 해야 하지만, 에러가 발생하여 3.9.0.jar버전으로 다운그레이션.
- 순서2. context-security.xml 생성
- 기술참조1-5단계 실행: https://github.com/miniplugin/egov
- > 기술참조: https://www.egovframe.go.kr/wiki/doku.php?id=egovframework:rte3:fdl:server_security:xmlschema_v3_8
- 이전 leesieun프로젝트는 xml에 화면(url) 권한에 대한 설정이 존재.
- egov 프로젝트는 xml에 화면(url) 권한에 대한 설정이 존재 X, authorrole 테이블에 위 화면 권한 설정 존재.
- > leesieun 프로젝트의 security-context.xml의 **인터셉터 url** 내용을** 쿼리로 대체**한다. **스프링 시큐리티 화면 권한을 DB로 제어**한다.
- *스프링 시큐리티 화면권한을 DB로 제어하기(입력순서중요): 관리자에서 추가/삭제/수정할 수 있는 기능 추가(아래)

```sql
CREATE TABLE IF NOT EXISTS `AUTHORROLE` (
  `AUTHORROLE_ID` decimal(20,0) NOT NULL,
  `ROLE_PTTRN` varchar(255) NOT NULL,
  `AUTHOR_CODE` varchar(255) NOT NULL,
  `AUTHORROLE_DC` VARCHAR(255) DEFAULT NULL,
  `SORT_ORDR` decimal(8,0) DEFAULT NULL,
  `USE_AT` char(1) NOT NULL,
  PRIMARY KEY (`AUTHORROLE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
```

```sql
INSERT INTO AUTHORROLE VALUES(1,'/.*.*.*','ROLE_ANONYMOUS','전체허용',1,'Y');
INSERT INTO AUTHORROLE VALUES(2,'/cop/com/.*.do.*','ROLE_ANONYMOUS','전체허용',2,'Y');
INSERT INTO AUTHORROLE VALUES(3,'/cop/bbs/*Master*.do','ROLE_USER','사용자만허용게시판',3,'Y');
INSERT INTO AUTHORROLE VALUES(4,'/uat/uia/.*.do.*','ROLE_USER','사용자만허용',4,'Y');
INSERT INTO AUTHORROLE VALUES(5,'/uss/umt/mber/.*.do.*','ROLE_USER','사용자만허용',5,'Y');
INSERT INTO AUTHORROLE VALUES(6,'/uat/uia/actionLogin.do','ROLE_ANONYMOUS','전체허용',6,'Y');
INSERT INTO AUTHORROLE VALUES(7,'/uat/uia/egovLoginUsr.do','ROLE_ANONYMOUS','전체허용',7,'Y');
INSERT INTO AUTHORROLE VALUES(8,'/tiles/login.do','ROLE_ANONYMOUS','전체허용',8,'Y');
INSERT INTO AUTHORROLE VALUES(9,'/login_action.do','ROLE_ANONYMOUS','전체허용',9,'Y');
INSERT INTO AUTHORROLE VALUES(10,'/cop/bbs/*Master*.do','ROLE_USER','사용자만허용',10,'Y');
INSERT INTO AUTHORROLE VALUES(11,'/admin/.*.*.*','ROLE_ADMIN','관리자만전체허용',11,'Y');
```

- 순서3. 그룹 정보 테이블에서 아래와 같이 변경 (lettnauthorgroupinfo 테이블)

'GROUP_00000000000000','ROLE_ADMIN'
'GROUP_00000000000001','ROLE_USER'
'GROUP_00000000000002','ROLE_ANONYMOUS'

- 순서4. context-security.xml에 아래 코드 추가

```xml
<!-- 아래 Spring Security를 적용하지 않는 경로 설정(Ant pattern 사용) -->
<security:http pattern="/css/**" security="none"/>
<security:http pattern="/common/**" security="none"/>
<security:http pattern="/images/**" security="none"/>
<security:http pattern="/js/**" security="none"/>
<security:http pattern="/resources/**" security="none"/>
<security:http pattern="\A/WEB-INF/jsp/.*\Z" security="none"/>

<egov-security:config id="securityConfig"
		loginUrl="/tiles/login.do"
		logoutSuccessUrl="/tiles/home.do"
		loginFailureUrl="/tiles/login.do?msg_security=1"
		accessDeniedUrl="/tiles/home.do?msg_security=2"
 
		dataSource="egov.dataSource"
		jdbcUsersByUsernameQuery="SELECT a.EMPLYR_ID AS USER_ID, a.PASSWORD, 1 AS ENABLED, a.USER_NM, 'USR' AS USER_SE, a.EMAIL_ADRES AS USER_EMAIL, '-' AS ORGNZT_ID, a.ESNTL_ID, '-' AS ORGNZT_NM 
								FROM LETTNEMPLYRINFO a, LETTNAUTHORGROUPINFO b WHERE a.GROUP_ID = b.GROUP_ID AND a.EMPLYR_ID = ?"
		jdbcAuthoritiesByUsernameQuery="SELECT a.EMPLYR_ID USER_ID, b.GROUP_NM AUTHORITY 
										FROM LETTNEMPLYRINFO a, LETTNAUTHORGROUPINFO b WHERE a.GROUP_ID = b.GROUP_ID AND a.EMPLYR_ID = ?"
		jdbcMapClass="edu.human.com.authorrole.EgovSessionMapping"
 
		requestMatcherType="regex"
		hash="plaintext"
		hashBase64="false"
 
		concurrentMaxSessons="2"
		concurrentExpiredUrl="/tiles/home.do"
 
		defaultTargetUrl="/tiles/home.do"
 
		sniff="false"
		xframeOptions="SAMEORIGIN" 
		xssProtection="false" 
		csrf="false"
/>
<egov-security:initializer id="initializer" supportPointcut="false" supportMethod="false" />
egov-security:secured-object-config id="securedObjectConfig"
	roleHierarchyString="
			ROLE_ADMIN > ROLE_USER
			ROLE_USER > ROLE_ANONYMOUS"
	sqlRolesAndUrl="
			SELECT ROLE_PTTRN url, AUTHOR_CODE authority 
             FROM AUTHORROLE 
             WHERE USE_AT='Y' ORDER BY SORT_ORDR DESC"
/>
```

- 순서5. edu.human.com.authorrole 패키지에 EgovSessionMapping 클래스 생성
- > context-security.xml의 쿼리 결과를 변수로 담을 공간 생성, 세션에 사용될 값 저장
- > 사용자 정보 테이블을 쿼리할 떄, EgovUsersByUsernameMapping에 매핑한 후 세션 변수 발생
- 순서6. web.xml 필터 체인 부분 확인 (필수)
- 순서7. CommonUtil.java 클래스에 Spring Security 코딩 추가
- 순서8. EgovUserDetailsHelper.getAuthorities() 메서드 복사 후 CommonUtil 클래스에 생성 -> 인증된 사용자의 권한 정보를 가져온다
- 아래 내용을 입력해서 관리자만 공지사항 입력 가능하게 처리.
- INSERT INTO AUTHORROLE VALUES(12,'/tiles/board/insert_board_form.*BBSMSTR_AAAAAAAAAAAA','ROLE_ADMIN','관리자만전체허용',12,'Y');
-스프링 시큐리티 화면권한 AUTHORROLE테이블을 관리자에서 지정할 수 있게 추가(R만): 기술참조 1)~4)작업
- 스프링 시큐리티 화면권한 AUTHORROLE테이블을 관리자에서 지정할 수 수 있게 추가(CRUD모두)


### 20210315(월)
- <수업>
- 회원탈퇴 기능 -> HomeControlelr클래스의 mypage_delete() 메서드
- 탈퇴 = 회원 상태코드를 비활성화로 변경 후 세션 날림

```java
memberVO.setEMPLYR_STTUS_CODE("S"); 
memberService.updateMember(memberVO);
//세션 날리기
request.getSession().invalidate(); //현재 URL의 모둔 LoginVO 세션값 날림
```

- **권한체크**
- CommonUtil 클래스에 관리자 권한 체크 **getAuthorities()** 메서드 추가

```java

//로그인 인증 + 권한 체크 1개의 메소드로 처리(아래)
public Boolean getAuthorities() throws Exception {
	Boolean authority = Boolean.FALSE;
	//인증 체크 (LoginVO가 null이면 = 로그인 상태가 아니면)
	if (EgovObjectUtil.isNull((LoginVO) RequestContextHolder.getRequestAttributes().getAttribute("LoginVO", RequestAttributes.SCOPE_SESSION))) {
		return authority;
	}
	//권한 체크 (관리자인지, 일반사용지안지 판단)
	LoginVO sessionLoginVO = (LoginVO) RequestContextHolder.getRequestAttributes().getAttribute("LoginVO", RequestAttributes.SCOPE_SESSION);
	EmployerInfoVO memberVO = memberService.viewMember(sessionLoginVO.getId());
	if("GROUP_00000000000000".equals(memberVO.getGROUP_ID())) {
		authority = Boolean.TRUE; //관리자
	}
	return authority;
}
```
- CommonUtil클래스의 로그인 처리에 권한 체크 소스 추가. 로그인 성공 후, 관리자 그룹 세션 처리.

```java
//권한 체크 (관리자인지, 일반사용지안지 판단)
LoginVO sessionLoginVO = (LoginVO) RequestContextHolder.getRequestAttributes().getAttribute("LoginVO", RequestAttributes.SCOPE_SESSION);
EmployerInfoVO memberVO = memberService.viewMember(sessionLoginVO.getId());
if("GROUP_00000000000000".equals(memberVO.getGROUP_ID())) {
	request.getSession().setAttribute("ROLE_ADMIN", memberVO.getGROUP_ID());
}
```
- AdminController클래스 권한 체크 부분 소스 변경

```java
// 사용자권한 처리 new
if(!commUtil.getAuthorities()) {
	model.addAttribute("msg", "관리자그룹만 접근 가능합니다.\\n사용자 홈페이지로 이동");
	return "home.tiles";
}
```

### 20210312(금)
- <수업>
- 회원가입 화면 구현.회원가입 기능 추가 완료.
- 마이페이지 화면 구현.
- 마이페이지 수정 & 회원탈퇴기능 구현중.

### 20210309(화)
- <작업예정>
- [X] 사용자단(관리자단) 페이징 처리 확인 예정.
- [X] AVD에 헬로우월드 앱 1개 생성해서 배포 예정.
- 애플리케이션 설계 과목 진도.
- 안드로이드 스튜디오 4.1.2 최신버전 학습 -PPT슬라이드 교재.

----------------------------------------------------------------

<수업>

- 페이징 처리 jsFunction 없어서 오류 발생 (jsFunction="fn_egov_select_noticeList" 필요)
- list_borad.jsp에 스크립트 추가.

```jsp
<script>
function fn_egov_select_noticeList(pageNo) {
    document.search_form.pageIndex.value = pageNo;
    document.search_form.submit();  
}
</script>
```

- <안드로이드 스튜디오>
- 안드로이드앱은 액티비티(xml 화면) - 프로그램(java파일) 한 쌍으로 구성.
- 웹에서 사용하는 크기 단위 : px(pixel)로 통일. 확대시 깨짐
- 앱에서 사용하는 크기 단위 : sp(scale-independent pixel). 확장가능 픽셀, 문자전용크기
- 한글 깨지는 문제 발생! 갤럭시AVD 한글은 현재 롤리팝 5.1부터 지원.
- > System Image : 기존에 다운로드했던 JellyBean 삭제, Lollipop 다운로드

### 20210308(월)
- <작업예정>
- [X] 안드로이드 스튜디오 AVD 애뮬 1개 추가 
- 헬로앱 1개 생성. 애뮬레이터로 실행 예정.
- egov 프로젝트 메인페이지 출력물(갤러리, 공지사항) 시큐어코딩 처리 예정. X
- [X] 갤러리 게시판 이미지 미리보기 기능 추가. 
- 안드로이드 PPT 수업 진행 예정.

----------------------------------------------------------------

<수업>
- 기존 egov의 save로즉에 포함되어있기 때문에 시큐어코딩 처리는 필요 X
- 갤러리 게시판 이미지 미리보기 (jpg, jpeg, bmp, png, gif)
- 첨부파일이 이미지가 아닐 때 no_image.png 출력

```java
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
		
```
- 콘텐츠.replaceAll("정규식패턴", ""); 해석 : 콘텐츠 내에 정규식 패턴에 필터링 되는 문자열을 "" 삭제시키라는 명령

### 20210305(금)
- <작업예정>
- egov프로젝트 에러표시 타일즈 템플릿 렌더링(화면출력)때문에 에러 원인 안보인다. (해결책_아래)
- [X] web.xml에서 error-page 설정을 주석처리 예정.
- [X] egov 게시판 CRUD 중 CUD(타일즈적용) -> 홈컨트롤러 기존 관리자단 egov서비스이용해서 작업 예정.
- 안드로이드 스튜디오4.x 설치 후 기본학습 -> 스프링프로젝트와 통신에 사용.
- 사용자단 디자인으로 메인작업(게시판내용 미리보기출력)예정.

----------------------------------------------------------------

<수업>
- 개발할때는 프론트가 되었던, 백엔드, C, 자바, 스프링이든 디버그환경을 신경써야 한다.
- 프로그래밍은 디버그-문제해결하는 부분이 주작업.

----------------------------------------------------------------

- <안드로이드 스튜디오>
- 안드로이드앱 처음 Blank앱(공백앱)을 생성시 Hello world 출력(안드로이드폰에 출력)
- > C언어처음 : printf("hello c!");
- > 자바언어처음 : System.out.println("hello world!");
- > 스프링처음 : 스프링MVC프로젝트 최초생성 Hello월드 시간출력(한글시간깨짐)
- 인텔리J(안드로드이ST,그레이들 빌드, build.gradle) = 이클립스(egov-IDE,메이븐 빌드,pom.xml)
- 앱프로젝트생성시 선택(자바.java, 코틀린.kt): 자바선택
- SDK(소프트웨어 개발 키트-외부라이브러리): 메이븐의 레포지토리폴더와 유사
- 안드로드이스튜디오 빌드->PC와USB로 연결된 스마트폰으로 실행결과확인 대신.
- 앱 결과물을 출력할 PC애뮬레이터 설치 후 애뮬을 이용해서 실행결과확인.
- 안드로이등가상기기: AVD(안드로이드 버추어 디바이스=애뮬레이터)
- > 실제 핸드폰 없이, 앱구동 가능.


### 20210304(목)
- <작업예정>
- NEW egov 게시판 CRUD 디자인부터 jsp변경(타일즈적용) -> HomeController : 기존 egov서비스 이용해서  화면 CRUD 작업예정
- 사용자단 디자인으로 메인작업(게시판내용 미리보기 출력) 예정.
- 안드로이드 스튜디오4.x 설치 후 기본학습 -> 스프링프로젝트와 통신에 사용.
- 신교재 2권 받으시면,목차 중에서 우리프로젝트진행상황과 매칭되는 부분 체크예정

----------------------------------------------------------------

<수업>
- **페이징 처리** : list_board.jsp 실행 -> ui:pagination의 type="paging"타입 호출 -> context-common.xml의 paginationManager(빈) 실행 -> rendererType이 paging인 pageRenderer(빈) 실행
- jsp 의 EL(Express Language)표기법 사용해서 변수 출력
- 주로 사용하는 jsp 템플릿 종류 : 타일즈, 벨로서티, 타임리프


### 20210303(수)
- (과제) 프로젝트 제안서 설명.
- v10.0.0 이론 설명.

### 20210302(화)
- 이전 스프링 프로젝트에서 사용한 @ControllerAdvice, 컨트롤러에서 발생되는 에러를 가로채는 기능을 사용하지 않고 아래 내용을 사용한다.
- 행정자치부 시큐어 코딩 가이드 : 에러코드를 노출하면 안되기 때문에 일반 안내문자로 표기한다. 따라서 사용자화면에는 에러코드를 노출하지 않는다.
- 하지만 우리는 개발할 때의 에러 상황을 알기 위해서, 개발할 때는 시큐어코딩 따르지 않는다.
- > /common/error.jsp 수정


### 20210226(금)
- <수업> 
- 첨부파일 저장한 이후 수정할 때 에러 발생하는 것 처리. AdminController의 update_board 메소드 수정.

```java
BoardVO bdvo = new BoardVO();
bdvo = bbsMngService.selectBoardArticle(boardVO);

return "redirect:/admin/board/view_board.do?bbsId="+bdvo.getBbsId()
+"&nttId="+bdvo.getNttId()+"&bbsTyCode="+bdvo.getBbsTyCode()
+"&bbsAttrbCode="+bdvo.getBbsAttrbCode()+"&authFlag=Y"
+"&pageIndex="+bdvo.getPageIndex();	
```

- 수정한 이후 리스트페이지 이동 -> 뷰페이지 이동으로 수정.
- 기존 egov는 첨부파일을 여러번 입력 가능하기 때문에, 우리의 삭제 로직 변경.  AdminController의 delete_board 메소드 수정.
- > 원래 로직 (아래)

```java
//실제 폴더에서 파일도 지우기(아래)
if(fileVO.getAtchFileId() != null && fileVO.getAtchFileId() !="") { 
	FileVO delfileVO = fileMngService.selectFileInf(fileVO);
	File target = new File(delfileVO.getFileStreCours(), delfileVO.getStreFileNm());
	if(target.exists()) {
		target.delete();//폴더에서 기존첨부파일 지우기
		System.out.println("디버그 : 첨부파일 삭제OK");
	}
}
```
- > 수정 로직(아래)

```java
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
```
- 글 작성에 관련된 insert_board.jsp 생성 + 컨트롤러 추가. CRUD 중 C 완료.
- 관리자단 마무리. 사용자단 디자인으로 메인 + 게시판 CRUD
- 사용자단 resources/home 폴더 생성 -> 우리 디자인 적용.
- 기존 egov디자인 main 폴더 -> 우리 home 디자인의 메인 home폴더 변경해서 적용.
- edu.human.come.home.web 패키지에 HomeController 생성.
- index.jsp 파일을 수정해서 home폴더가 주 디자인으로 되도록 변경.
- 로그아웃은 페이지 없이 처리. 따라서 HomeController만 매핑을 추가해서 처리.

----------------------------------------------------------------

<템플릿 사용>
- tiles, velocity, thymeleaf 3가지 jsp 템플릿 종류 중 **tiles 템플릿 사용**하여 home 디자인 사용
- IT 강의 저장소 V.9.0.0 하단 자료 참고.

- **pom.xml에 의존성 추가**

```xml
<!-- Tiles -->
<dependency>
	<groupId>org.apache.tiles</groupId>
    <artifactId>tiles-extras</artifactId>
    <version>3.0.8</version>
</dependency>
```

- src/main/resources/egovframework/spring/com에 **context-tiles.xml** 설정파일 추가

```xml
<!DOCTYPE tiles-definitions PUBLIC "-//Apache Software Foundation//DTD Tiles Configuration 3.0//EN"
 "http://tiles.apache.org/dtds/tiles-config_3_0.dtd">
<tiles-definitions>
    <definition name="tiles_layout"  templateExpression="/WEB-INF/jsp/tiles/layouts/layout.jsp">
        <put-attribute name="header" expression="/WEB-INF/jsp/tiles/layouts/header.jsp" />
        <put-attribute name="content" expression="" />
        <put-attribute name="footer" expression="/WEB-INF/jsp/tiles/layouts/footer.jsp" />
    </definition>
    <definition name="*.tiles" extends="tiles_layout">
        <put-attribute name="content" expression="/WEB-INF/jsp/tiles/{1}.jsp" />
    </definition>
    <definition name="*/*.tiles" extends="tiles_layout">
        <put-attribute name="content" expression="/WEB-INF/jsp/tiles/{1}/{2}.jsp" />
    </definition>
    <definition name="*/*/*.tiles" extends="tiles_layout">
        <put-attribute name="content" expression="/WEB-INF/jsp/tiles/{1}/{2}/{3}.jsp" />
    </definition>
</tiles-definitions>
```
- src/main/webapp/WEB-INF/jsp에 tiles 폴더 생성.
- src/main/webapp/WEB-INF/config/egovframework/springmvc/egov-com-servlet.xml에 빈 추가 + 로그인체크 예외항목에 추가

```xml
<!-- 타일즈 jsp템플릿 리졸버 해석기 설정추가(아래) -->
<!-- 화면처리용 JSP 파일명의  prefix, suffix 처리에 대한 타일즈 설정추가 -->
<bean id="tilesViewResolver" class="org.springframework.web.servlet.view.UrlBasedViewResolver"
p:order="0" p:viewClass="org.springframework.web.servlet.view.tiles3.TilesView" />
<bean id="tilesConfigurer" class="org.springframework.web.servlet.view.tiles3.TilesConfigurer" >
	<property name="definitions">
		<value>classpath*:egovframework/spring/com/context-tiles.xml</value>
	</property>
</bean>
```
- 로그인체크 예외항목에 추가
 
```xml
<mvc:exclude-mapping path="/tiles/*.do"/>
```
- /tiles/home.do URL 호출 -> home.tiles file호출  tiles설정이 가로챈다.(**layout.jsp파일에서** 가로챔)
- /WEB-INF/jsp/tiles/{1}.jsp 바인딩 -> content이름에 담겨서
- /WEB-INF/jsp/tiles/layouts/layout.jsp 파일의 content이름에 나오게 된다.
-  톰캣 실행 시, 가장 먼저 실행되는 설정 파일은 **web.xml**
 - web.xml에서 welcome-file-first 로 시작 파일 지정. 현재는 index.jsp
 - index.jsp에서 document.location.href로 /tiles/home.do로 이동하게끔 되어있음.
 - 따라서, sht_webapp/ 경로 실행시 => sht_webapp/tiles/home.do 경로 실행.


### 20210225(목)
- egov(Mysql) 마무리
- 기존 : 게시판 삭제 시 기존 USE_AT='N' 처리해서 실제 삭제X
- 변경 : 게시판 삭제 시 레코드 삭제 처리로, 첨부파일 같이 삭제


### 20210222(월)
- <작업예정>
- egov 관리자단 게시판 부분 CRUD 작업 마무리 예정.
- 사용자단 메인페이지 UI변경 예정. ( tiles템플릿을 사용할 예정 )
- 위 JSP UI템플릿은  include 기능을 확장한 라이브러리.

----------------------------------------------------------------

<수업>
- 기존 전자정부프로젝트에서의 삭제 => 진짜 삭제가 아니고, 삭제 필드(플래그필드  USE_AT)에 삭제표시 Y->N 한다.
- egov 프로젝트에서의 삭제 => 진짜 삭제
- 바인딩 = 매칭 = 매핑


### 20210219(금)
- <작업예정>
- 수업전 확인: JSP뷰단 <- PageVO변수 <- 컨트롤러(매개변수) <- 쿼리실행
- 매퍼쿼리 <-> PageVO변수 <-> 컨트롤러(매개변수) -> 서비스를 호출 -> DAO호출 -> 쿼리실행
- egov프로젝트 관리자관리 검색 및 페이징 처리 추가 마무리.
- egov게시물관리 CRUD 처리(관리자관리처럼 마이바티스이용 안하고) ibatis(기존클래스이용)적용.

----------------------------------------------------------------

<수업>

- 전자정부표준프레임워크기반 심플홈템플릿 스프링웹프로젝트를 커스터마이징하는 중
- 정부에서 발주해서 삼성SDS + LG CNS + SK C&C 컨소시엄으로 만들었습니다.(2011년 배포 ~ 지금까지)
- 1.egov 게시물관리 CRUD처리(관리자관리처럼 마이바티스 사용하지 않고, ibatis(기존클래스이용)적용)
- 2.egov 게시물관리는 컨트롤러와 JSP단만 처리(AdminLTE)
- 위 두가지를 정리하면, 
- 관리자(회원)관리는 컨트롤러+JSP+서비스+MyBatis를 우리가 만든 것 사용.
- 게시물 관리는 컨트롤러+JSP는 우리가 만들고 서비스+iBatis는 전자정부가 만든 것 사용.
- egov 프로젝트에서 세션이 발생하는 부분
- > egovframework.let.uat.uia.web패키지 안 EgovLoginController.java
- admin/board/list_board.jsp <-> EgovNoticeList.jsp
- AdminController.java <-> EgovBBSManageController.java
- egov페이징 UI설정 : context-common.xml -> ImagePaginationRenderer.java


### 20210218(목)
- <작업예정>
- C언어 포인터실습결과 확인
- egov프로젝트 관리자관리 CRUD 중 Create, Delete 마무리.
- egov프로젝트 관리자관리 검색 및 페이징 처리 추가.

----------------------------------------------------------------

<수업>
- [X] egov프로젝트 관리자관리 CRUD 완료.
- > 참고 : egovframework.let.uat.uia.service.impl패키지 내 EgovLoginServiceImpl.java(로그인처리부분)
- [X] C언어 포인터실습결과 확인
- [X] 관리자 관리 검색 처리 완료.
- if(a==b) TRUE, if(a===b) FALSE, 이것처럼 ==값만비교, ===값과 자료형 까지 비교

### 20210217(수)
- <작업예정>
- egov프로젝트 관리자관리 CRUD 마무리.
- egov프로젝트 관리자관리 검색 및 페이징 처리 추가.
- egov게시물관리 CRUD 처리(관리자관리처럼 마이바티스 이용 안 하고) ibatis(기존 클래스 이용)적용.
- egov게시물관리는 컨트롤러와 JSP단만 처리합니다.(AdminLTE)
- 사용자단 메인페이지 UI 변경예정(타일즈템플릿-벨로서티템플릿 jsp UI템플릿을 사용) tiles템플릿을 사용예정.
- 남는 시간에 C언어 계속진행. 

----------------------------------------------------------------

수업
- egov프로젝트 관리자관리 CRUD 중 Read, Update 완료.
- C언어 포인터 실습

### 20210216(화)
- <작업에정>
- C언어 기초: 5장확인 후 6장 시작예정.
- 키보드로 입력받은 1개문자를 아스키코드로 변환하는 C프로그램을 작성할 예정.
----------------------------------------------------------------

<수업>
- 이클립스 빌드 속도 때문에 window-> preferences -> validation -> build탭의 체크 모두 해제
- [X] web 패키지(컨트롤러) 작업 + AdminLTE로 뷰 jsp단 작업. (마이바티스기반 - 새로 만들기)
- jsp단의 root(최상위)경로를 html태그의 /에서 <c:url value='/' />로 변경
- /admin/home.do URL액션을 주는 설정(web.xml) 확인.
- /admin/**/*.do URL액션에 로그인체크권한설정 추가(egov-com-servlet.xml)
- 결과적으로는 사용자(기존)과 관리자단 분리.
- 관리자단 home.jsp > header, footer로 분리
- 코드 인스펙션(소스분석): egov프로젝트의 진입점(webapp/index.jsp) 확인
- egov프로젝트의 jsp(동적페이지)폴더에 html(정적페이지)를 jsp로 변환해서 배치
- 쿼리에 MySQL 함수(function)사용해서 code_nm, group_nm 출력되도록 함
 

### 20210215(월) 
- <작업예정>
- 관리자관리 기능 추가 시, edu.~ 패키지를 생성해서 관리자 관리 기능 만드는 중
- > egov프로젝트 생성 시 심플홈템플릿 프로젝트는 관리자관리 기능 없음.
- member쿼리 CRUD추가, DAO CRUD추가, Service CRUD추가, JUnit CRUD테스트

----------------------------------------------------------------

<수업>
- VO-매퍼쿼리-DAO-Service-JUnit 구조로 select까지 진행했음.
- [X] JUnit CRUD 마무리
- egov 패키지 구조1 : 서비스 패키지(VO포함, 인터페이스)
- egov 패키지 구조2 : 서비스임플리먼트 패키지(서비스클래스, DAO클래스)
- JUnit CRUD까지 한 후, 컨트롤러 패키지작업 + AdminLTE로 뷰 jsp단 작업
- 게시판 관리 부분도 작업 : 아이바티스 기반(기존egov것)으로 작업(컨트롤러+jsp뷰단만 작업)
- row(레코드)데이터, raw(가공전 원시)데이터 - 빅데이터 과목에서.
- [X] C언어 기초 : 3장 입출력함수와 연산자

----------------------------------------------------------------

- <이론>
- 아스키코드? 사람이 이해하는 문자와 컴퓨터가 이해하는 문자가 다르기 때문에 만들어진 문자.
- 참조 : https://ko.wikipedia.org/wiki/ASCII
- ex1. a(사람) = 97(컴퓨터), b(사람) = 98(컴퓨터)
- ex2. 0(사람) = 48(컴퓨터), 1(온도/습도) = 49(컴퓨터,아스키코드)

### 20210210(수) 

- <작업예정>
- 전자정부표준프레임워크 커스터마이징 깃 확인.
- AdminLTE 템플릿을 egov 프로젝트에 붙여넣기
- AdminLTE 관리자 관리 기능 추가

----------------------------------------------------------------

<수업>
- 사전지식 : egov 프로젝트 기본은 iBatis-> myBatis로 변경해서 커스터마이징
- 목표 : 전자정부프레임워크를 커스터마이징하는 과정을 거쳐서, 내부 구조를 확인한다.
- [X] 1. 마이바티스 사용가능하게 설정(pom.xml 내용 추가)
- > myBatis용 context명세파일 생성.(context-mapper.xml) : 마이바티스에 사용되는 각종 경로 명시(mapper-config.xml과 기본 매퍼쿼리파일 member_mysql.xml 생성)
- > edu.home.com 패키지 생성, 클래스빈자동등록추가(egov-com-servlet.xml), VO+매퍼쿼리추가(스프링에서 클래스를 사용하려면 빈으로 등록해야함)
- [X] 2. 스프링의 sqlSession 템플릿을 DAO에서 @Inject로 직접 사용하지 않고, 전자정부전용 EgovAbstractMapper클래스를 상속(필수) -> EgovComAbstractMapper.java(Mybatis용)
- [X] 3. egov 패키지명명 규칙 : 패키지는 기능별로 구분하고, 서비스와 DAO가 같은 패키지에 존재함 그래스 서비스에 DAO 생성하게 됨.
- > 서비스 클래스 생성 후 Impl 폴더에 DAO 클래스 생성 
- [X] 4. JUnit테스트
- > pom.xml에 외부라이브러리 추가

-----------------------------------------------------------------
