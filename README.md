# 전자정부표준프레임워크 커스터마이징 하기

#### 20210215(월) 
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

#### 20210210(수) 

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