# 전자정부표준프레임워크 커스터마이징 하기
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

- 기존 전자정부프로젝트에서의 삭제 => 진짜 


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
