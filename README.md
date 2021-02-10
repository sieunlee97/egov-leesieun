#### 20210210(수) 

- <작업예정>
- 전자정부표준프레임워크 커스터마이징 깃 확인.
- AdminLTE 템플릿을 egov 프로젝트에 붙여넣기
- AdminLTE 관리자 관리 기능 추가

----------------------------------------------------------------

<수업>
- 사전지식 : egov 프로젝트 기본은 iBatis-> myBatis로 변경해서 커스터마이징
- 목표 : 전자정부프레임워크를 커스터마이징하는 과정을 거쳐서, 내부 구조를 확인한다.
- 1. 마이바티스 사용가능하게 설정(pom.xml 내용 추가)
- > myBatis용 context명세파일 생성.(context-mapper.xml) : 마이바티스에 사용되는 각종 경로 명시(mapper-config.xml과 기본 매퍼쿼리파일 member_mysql.xml 생성)
- > edu.home.com 패키지 생성, 클래스빈자동등록추가(egov-com-servlet.xml), VO+매퍼쿼리추가(스프링에서 클래스를 사용하려면 빈으로 등록해야함)
- 2. 스프링의 sqlSession 템플릿을 DAO에서 @Inject로 직접 사용하지 않고, 전자정부전용 EgovAbstractMapper클래스를 상속(필수) -> EgovComAbstractMapper.java(Mybatis용)
- 3.

-----------------------------------------------------------------