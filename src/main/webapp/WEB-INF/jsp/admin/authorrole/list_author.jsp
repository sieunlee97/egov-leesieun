<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../include/header.jsp" %>

<!-- Content Wrapper. Contains page content, 대시보드 본문 -->
  <div class="content-wrapper">
    <!-- Content Header (Page header), 본문 헤더 -->
    <div class="content-header">
      <div class="container-fluid">
        <div class="row mb-2">
          <div class="col-sm-6">
            <h1 class="m-0">화면 권한 리스트 </h1>
          </div><!-- /.col -->
          <div class="col-sm-6">
            <ol class="breadcrumb float-sm-right">
              <li class="breadcrumb-item"><a href="#">Home</a></li>
              <li class="breadcrumb-item active">화면 권한 리스트</li>
            </ol>
          </div><!-- /.col -->
        </div><!-- /.row -->
        <div>
        	<span style="font-size:12px; font-weight:bold; color:red;">주의) 추가/수정/삭제 시 서버를 재시작해야 권한 적용됩니다.</span>
        </div>
      </div><!-- /.container-fluid -->
    </div>
    <!-- /.content-header -->

    <!-- Main content, 본문 내용 -->
    <section class="content">
      <div class="container-fluid">
       
       <div class="row"> <!-- 부트스트랩의 디자인클래스 row -->
          <div class="col-12"> <!-- 부트스트랩의 그리드시스템 중 12 가로칼럼 (width:100%와 같은 뜻) -->
            <div class="card"> <!-- 부트스트랩의 카드클래스 : 네모난 디자인 -->
              <div class="card-header">
                <h3 class="card-title">권한 검색</h3>
                <div class="card-tools">
                  
                  <form name="search_form" action="<c:url value='/'/>admin/authorrole/list_author.do" method="get">
                  <div class="input-group input-group-sm">
                  <!-- 부트스트랩 템플릿만으로는 디자인처리가 안되는 경우가 있기 때문에 종종 인라인스타일 사용 -->
                    <div>
                       <select name="search_type" class="form-control">
                            <option value="all" selected>-전체-</option>
                            <option value="author_code" data-select2-id="8">권한코드</option>
                            <option value="role_pttrn" data-select2-id="24">화면경로</option>
                       </select>
                    </div>
                    <div>
                    	<input type="text" name="search_keyword" class="form-control float-right" placeholder="Search">
					</div>
                    <div class="input-group-append">
                      <button type="submit" class="btn btn-default">
                        <i class="fas fa-search"></i>
                      </button>
                    </div>
                  </div>
                  </form>
                  
                </div>
              </div>
              <!-- /.card-header -->
              <div class="card-body table-responsive p-0">
                <table class="table table-hover text-nowrap">
                  <thead>
                    <tr>
                      <!-- 테이블 헤드 타이틀태그 th -->
                      <th>ROLE_PTTRN</th>
                      <th>AUTHOR_CODE</th>
                      <th>AUTHORROLE_DC</th>
                      <th>SORT_ORDR</th>
                      <th>USE_AT</th>
                    </tr>
                  </thead>
                  <tbody>
                  <c:if test="${empty authorRoleList}">
                  <tr><td class="text-center" colspan="5">조회된 값이 없습니다.</td></tr>           
                  </c:if>
                  <c:forEach items="${authorRoleList}" var="vo">
                    <tr>
                      <td><a href="<c:url value='/admin/authorrole/view_author.do?authorrole_id=${vo.AUTHORROLE_ID}&page=${pageVO.page}&search_type${pageVO.search_type}=&search_keyword=${pageVO.search_keyword}' />">
                      ${vo.ROLE_PTTRN}</a></td> <!-- table data 태그 -->
                      <!-- 위의 링크a 값은 리스트가 늘어날수록 동적으로 user_id값이 변하게 된다. 개발자가 jsp처리 -->
                      <td>${vo.AUTHOR_CODE}</td>
                      <td>${vo.AUTHORROLE_DC}</td>
                      <td>${vo.SORT_ORDR}</td>
                      <td>${vo.USE_AT}</td>    
                    </tr>
                    </c:forEach>
                  </tbody>
                </table>
              </div>
              <!-- /.card-body -->
              
            </div>
            <!-- /.card -->
            
            <!-- 버튼영역 시작 -->
            <div class="card-body">
            	<a href="<c:url value='/admin/authorrole/insert_author_form.do' />" class="btn btn-primary float-right">등록</a>
            	<!-- 부트스트랩 디자인 버튼클래스를 이용해서 a태그를 버튼모양 만들기 -->
            	<!-- btn클래스명이 버튼모양을 변경, btn-primary클래스명의 버튼색상 변경 -->
            </div>
            <!-- 버튼영역 끝 -->
            
           <!-- 페이징처리 시작 -->
            <div class="pagination justify-content-center">
          	 <ul class="pagination">
          	 <c:if test="${pageVO.prev}"><!-- if문 true 일 때 아래 실행 -->
              	<li class="paginate_button page-item previous" id="example1_previous">
              	<a href="<c:url value='/'/>admin/authorrole/list_author.do?page=${pageVO.startPage-1}&search_type=${pageVO.search_type}&search_keyword=${pageVO.search_keyword}" 
              	aria-controls="example1" data-dt-idx="0" tabindex="0" class="page-link">&laquo;</a></li>
             </c:if>
              <!-- previous (위) -->
              
             <!-- jstl for문이고, 향상된 for문이아닌 고전for문으로 시작값, 종료값 var변수idx는 인덱스값이 저장되어 있습니다. -->
            	 <c:forEach begin="${pageVO.startPage}" end="${pageVO.endPage}" var="idx">
            	 	<li class='paginate_button page-item <c:out value="${idx==pageVO.page?'active':''}" />'>
            	 	<a href="<c:url value='/' />admin/authorrole/list_author.do?page=${idx}&search_type=${pageVO.search_type}&search_keyword=${pageVO.search_keyword}" aria-controls="example2" data-dt-idx="1" tabindex="0" class="page-link">${idx}</a></li>
            	 </c:forEach>
                  
              <!-- next (아래)  --> 
             <c:if test="${pageVO.next}"> <!-- if문 true 일 때 아래 실행 -->
              	<li class="paginate_button page-item next" id="example1_next">
              	<a href="<c:url value='/'/>admin/authorrole/list_author.do?page=${pageVO.endPage+1}&search_type=${pageVO.search_type}&search_keyword=${pageVO.search_keyword}" 
              	aria-controls="example1" data-dt-idx="7" tabindex="0" class="page-link">&raquo;</a></li>
             </c:if>
             </ul>
            </div>
            <!-- 페이징처리 끝 -->
              
          </div>
        </div>
        
      </div><!-- /.container-fluid -->
    </section>
    <!-- /.content -->
  </div>
  <!-- /.content-wrapper -->

<%@ include file="../include/footer.jsp" %>