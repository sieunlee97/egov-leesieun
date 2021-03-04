<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

	<!-- 메인콘텐츠영역 -->
	<div id="container">
		<!-- 메인상단위치표시영역 -->
		<div class="location_area customer">
			<div class="box_inner">
				<h2 class="tit_page">스프링 <span class="in">in</span> 자바</h2>
				<p class="location">고객센터 <span class="path">/</span> ${brdMstr.bbsNm}</p>
				<ul class="page_menu clear">
					<li><a href="<c:url value='/'/>tiles/board/list_board.do?bbsId=BBSMSTR_AAAAAAAAAAAA" class="<c:out value='${brdMstrVO.bbsId=="BBSMSTR_AAAAAAAAAAAA"? "on":""}'/>">공지사항</a></li>
					<li><a href="<c:url value='/'/>tiles/board/list_board.do?bbsId=BBSMSTR_BBBBBBBBBBBB" class="<c:out value='${brdMstrVO.bbsId=="BBSMSTR_BBBBBBBBBBBB"? "on":"" }'/>">갤러리</a></li>
				</ul>
			</div>
		</div>	
		<!-- //메인상단위치표시영역 -->

		<!-- 메인본문영역 -->
		<div class="bodytext_area box_inner">			
			<ul class="bbsview_list">
				<li class="bbs_title">${result.nttSj}</li>
				<li class="bbs_hit">작성자 : <span>${result.frstRegisterNm}</span></li>				
				<li class="bbs_hit">작성일 : <span>${result.frstRegisterPnttm}</span></li>
				<li class="bbs_date">조회수 : <span>${result.inqireCo}</span></li>
				<li class="bbs_content">
					<div class="editer_content">
					   ${result.nttCn}
                    </div>
				</li>
				<c:if test="${not empty result.atchFileId}">
				<li class="bbs_title">
					<strong> 첨부파일</strong>
	                <p>
	                <c:import url="/cmm/fms/selectFileInfs.do" charEncoding="utf-8">
	                    <c:param name="param_atchFileId" value="${result.atchFileId}" />
	                </c:import>
	                </p>
				</li>
				</c:if>
			</ul>
			<p class="btn_line txt_right">
				<a href="board_list.html" class="btn_bbs">목록</a>
			</p>
		</div>
		<!-- //메인본문영역 -->
	</div>
	<!-- //메인콘텐츠영역 -->
	