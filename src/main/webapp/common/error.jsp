<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" >
<meta http-equiv="content-language" content="ko">
<title>ERROR</title>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td width="100%" height="100%" align="center" valign="middle" style="padding-top:150px;"><table border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td><span style="font-family:Tahoma; font-weight:bold; color:#000000; line-height:150%; width:440px; height:70px;">
       	 오류발생 알림화면(허용되지 않는 요청을 하셨습니다)
	    <!-- 
		   행자부 시큐어코딩 가이드에 따라서 에러코드를 노출하면 않되기 때문에 일반안내문자로 표기합니다.
		   이전 스프링프로젝트에서 사용한 @ControllerAdvice 컨트롤러에서 발생되는 에러를 가로채는 기능을 사용X
		   사전처리: 이전에 주석처리한 web.xml 의 에러페이지 주석해제 후 error.jsp에 아래내용 추가
		   사전확인: 이전에 주석처리한 egov-com-servlet.xml 내용만 확인
		   기술참조:https://mystarlight.tistory.com/123
		-->
		<!-- 주의: 개발할때는 아래내용이 필요하지만, 배포할때는 주석 처리해서 보이지 않게 합니다. -->
		<br>에러code : ${requestScope['javax.servlet.error.status_code']}
		<br>exception type : ${requestScope['javax.servlet.error.exception_type']}
		<br>message : ${requestScope['javax.servlet.error.message']}
		<br>exception : ${requestScope['javax.servlet.error.exception']}
		<br>request uri : <a href="${requestScope['javax.servlet.error.request_uri']}">${requestScope['javax.servlet.error.request_uri']}</a>
		<br>servlet name : ${requestScope['javax.servlet.error.servlet_name']}
		<c:set var="exception" value="${requestScope['javax.servlet.error.exception']}"/>
		<br>에러추적trace : 
		 <ul>
		  <c:forEach items="${exception.getStackTrace()}" var="stack">
		<li>${stack.toString()}</li>
		</c:forEach>
        </span></td>
      </tr>
    </table></td>
  </tr>
</table>
</body>
</html>