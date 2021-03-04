<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<style>
.select {
	margin-top:15px;
	padding: 5px;
	height: 35px;
	line-height: 35px;
	border: none;
	border-bottom: 1px solid #ccc;
}
</style>
	<!-- 메인콘텐츠영역 -->
	<div id="container">
		<!-- 메인상단위치표시영역 -->
		<div class="location_area customer">
			<div class="box_inner">
				<h2 class="tit_page">스프링 <span class="in">in</span> 자바</h2>
				<p class="location">고객센터 <span class="path">/</span>${brdMstrVO.bbsNm}</p>
				<ul class="page_menu clear">
					<li><a href="<c:url value='/'/>tiles/board/list_board.do?bbsId=BBSMSTR_AAAAAAAAAAAA" class="<c:out value='${brdMstrVO.bbsId=="BBSMSTR_AAAAAAAAAAAA"? "on":""}'/>">공지사항</a></li>
					<li><a href="<c:url value='/'/>tiles/board/list_board.do?bbsId=BBSMSTR_BBBBBBBBBBBB" class="<c:out value='${brdMstrVO.bbsId=="BBSMSTR_BBBBBBBBBBBB"? "on":"" }'/>">갤러리</a></li>
				</ul>
			</div>
		</div>	
		<!-- //메인상단위치표시영역 -->

		<!-- 메인본문영역 -->
		<div class="bodytext_area box_inner">
			<!-- 검색폼영역 -->
			<form id="search_form" name="search_form" method="get" action="<c:url value='/'/>tiles/board/list_board.do" class="minisrch_form">
				<fieldset>
					<legend>검색</legend>
					<select name="searchCnd" class="select">
						<option value="0" <c:out value='${searchVO.searchCnd=="0"? "selected":""}'/> >제목</option>
						<option value="1" <c:if test="${searchVO.searchCnd=='1'}">selected</c:if>>내용</option>
						<option value="2" <c:if test="${searchVO.searchCnd=='2'}">selected</c:if>>작성자</option>
					</select>
					<input value="${searchVO.searchWrd}" name="searchWrd" type="text" class="tbox" title="검색어를 입력해주세요" placeholder="검색어를 입력해주세요">
					<button class="btn_srch">검색</button>
				</fieldset>
				<input type="hidden" name="bbsId" value="<c:out value='${boardVO.bbsId}'/>" />
				<input type="hidden" name="nttId"  value="0" />
				<input type="hidden" name="bbsTyCode" value="<c:out value='${brdMstrVO.bbsTyCode}'/>" />
				<input type="hidden" name="bbsAttrbCode" value="<c:out value='${brdMstrVO.bbsAttrbCode}'/>" />
				<input type="hidden" name="authFlag" value="<c:out value='${brdMstrVO.authFlag}'/>" />
				<input name="pageIndex" type="hidden" value="<c:out value='${searchVO.pageIndex}'/>"/> 
			</form>
			<!-- //검색폼영역 -->
			
			<!-- 게시물리스트영역 -->
			<table class="bbsListTbl" summary="번호,제목,조회수,작성일 등을 제공하는 표">
				<caption class="hdd">공지사항  목록</caption>
				<thead>
					<tr>
						<th scope="col">번호</th>
						<th scope="col">제목</th>
						<th scope="col">조회수</th>
						<th scope="col">작성일</th>
					</tr>
				</thead>
				<tbody>
				<c:forEach items="${resultList}" var="boardVO" varStatus="cnt">
					<tr>
						<td>${paginationInfo.totalRecordCount+1-((searchVO.pageIndex-1)*searchVO.pageSize+cnt.count)}</td>
						<td class="tit_notice"><a href="#">
							${boardVO.nttSj}
						</a></td>
						<td>${boardVO.inqireCo}</td>
						<td>${boardVO.frstRegisterPnttm}</td>
					</tr>
				</c:forEach>
				</tbody>
			</table>
			<!-- //게시물리스트영역 -->
			
			<!-- 페이징처리영역 -->
			<div class="pagination">
				<a href="javascript:;" class="firstpage  pbtn"><img src="<c:url value='/' />resources/home/img/btn_firstpage.png" alt="첫 페이지로 이동"></a>
				<a href="javascript:;" class="prevpage  pbtn"><img src="<c:url value='/' />resources/home/img/btn_prevpage.png" alt="이전 페이지로 이동"></a>
				<a href="javascript:;"><span class="pagenum currentpage">1</span></a>
				<a href="javascript:;"><span class="pagenum">2</span></a>
				<a href="javascript:;"><span class="pagenum">3</span></a>
				<a href="javascript:;"><span class="pagenum">4</span></a>
				<a href="javascript:;"><span class="pagenum">5</span></a>
				<a href="javascript:;" class="nextpage  pbtn"><img src="<c:url value='/' />resources/home/img/btn_nextpage.png" alt="다음 페이지로 이동"></a>
				<a href="javascript:;" class="lastpage  pbtn"><img src="<c:url value='/' />resources/home/img/btn_lastpage.png" alt="마지막 페이지로 이동"></a>
			</div>
			<!-- //페이징처리영역 -->
			<p class="btn_line">
				<a href="board_write.html" class="btn_baseColor">등록</a>
			</p>
		</div>
		<!-- //메인본문영역 -->
	</div>
	<!-- //메인콘텐츠영역 -->