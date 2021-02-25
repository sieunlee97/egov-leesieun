<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../include/header.jsp" %>

<!-- 대시보드 본문 Content Wrapper. Contains page content -->
  <div class="content-wrapper">
    <!-- 본문헤더 Content Header (Page header) -->
    <div class="content-header">
      <div class="container-fluid">
        <div class="row mb-2">
          <div class="col-sm-6">
            <h1 class="m-0">${bdMstr.bbsNm} 게시물수정</h1>
          </div><!-- /.col -->
          <div class="col-sm-6">
            <ol class="breadcrumb float-sm-right">
              <li class="breadcrumb-item"><a href="#">Home</a></li>
              <li class="breadcrumb-item active">${bdMstr.bbsNm} 게시물수정</li>
            </ol>
          </div><!-- /.col -->
        </div><!-- /.row -->
      </div><!-- /.container-fluid -->
    </div>
    <!-- /.content-header -->

    <!-- 본문내용 Main content -->
    <section class="content">
      <div class="container-fluid">
        
        <div class="row"><!-- 부트스트랩의 디자인 클래스 row -->
          <div class="col-12"><!-- 그리드시스템중 12가로칼럼 width:100% -->
          
          <!-- form start -->
          <form name="update_form" action="<c:url value='/'/>admin/board/update_board.do" method="post" encType="multipart/form-data">
          <div class="card card-primary">
              <div class="card-header">
                <h3 class="card-title">UPDATE Board</h3>
              </div>
              <!-- /.card-header -->
              
                <div class="card-body">
                  <div class="form-group">
                    <label for="nttSj">title</label>
                    <input value="${result.nttSj}" type="text" class="form-control" name="nttSj" id="nttSj" placeholder="제목 입력해주세요." required>
                  <!-- form에서 input같은 입력태그에는 name 속성이 반드시 필요.name 속성값 = DB 필드 속성명
                  DB에 입력할 때 값을 전송하게 되는데 전송값을 저장하는 이름이 name이 되고,위에서는 user_id이다.-->
                  </div>
                  <div class="form-group">
                  <!-- label for과 textarea id는 같게 설정 -->
                    <label for="nttCn">Content</label>
					<textarea row="5" name="nttCn" id="nttCn" class="form-control">${result.nttCn}</textarea>
                  </div>
                  <div class="form-group">
                  	<label for="frstRegisterNm">writer</label>
                  	<input value="${result.frstRegisterNm}" type="text" class="form-control" name="frstRegisterNm" id="frstRegisterNm" placeholder="작성자을 입력해주세요." required>
                  	<!-- 필수 입력값은 html5에서 지원하는 유효성 검사 중 required 속성 사용해서 null값을 체크한다.(유효성검사) -->
                  </div>
                  <div class="form-group">
                  	<label for="customFile">attach</label>
                  	<div class="custom-file">
	                  	<c:if test="${not empty result.atchFileId}">
			                <hr>
			                <strong><i class="far fa-save mr-1"></i> 첨부파일</strong>
			                <p class="text-muted">
			                <c:import url="/cmm/fms/selectFileInfs.do" charEncoding="utf-8">
			                    <c:param name="param_atchFileId" value="${result.atchFileId}" />
			                </c:import>
			                </p>
		                </c:if>
                      <input type="file" name="file" class="custom-file-input" id="customFile">
                      <label class="custom-file-label" for="customFile">파일을 선택해주세요</label>
                  </div>
                </div>
                <!-- /.card-body -->
             </div> <!-- /.card card-primary -->
          
          <!-- 버튼영역 시작 -->
            <div class="card-body">
            	<button id="btn_list" type="button" class="btn btn-primary float-right mr-1">목록</button>
              	<button id="btn_view" type="button" class="btn btn-info float-right mr-1">이전</button>              	
              	<button type="submit" class="btn btn-warning float-right mr-1 text-white">수정</button>
              	<!-- a태그는 링크이동은 되지만, post값을 전송하지 못한다. 그래서 button태그를 사용. -->
              </div>
          <!-- 버튼영역 끝 -->
         	<input type="hidden" name="pageIndex" value="<c:out value='${searchVO.pageIndex}'/>"/>
			<input type="hidden" name="returnUrl" value="<c:url value='/cop/bbs/forUpdateBoardArticle.do'/>"/>
			<input type="hidden" name="bbsId" value="<c:out value='${result.bbsId}'/>" />
			<input type="hidden" name="nttId" value="<c:out value='${result.nttId}'/>" />
			<input type="hidden" name="bbsAttrbCode" value="<c:out value='${bdMstr.bbsAttrbCode}'/>" />
			<input type="hidden" name="bbsTyCode" value="<c:out value='${bdMstr.bbsTyCode}'/>" />
			<input type="hidden" name="replyPosblAt" value="<c:out value='${bdMstr.replyPosblAt}'/>" />
         	<input type="hidden" name="fileAtchPosblAt" value="<c:out value='${bdMstr.fileAtchPosblAt}'/>" />
			<input type="hidden" name="posblAtchFileNumber" value="<c:out value='${bdMstr.posblAtchFileNumber}'/>" />
			<input type="hidden" name="posblAtchFileSize" value="<c:out value='${bdMstr.posblAtchFileSize}'/>" />
			<input type="hidden" name="tmplatId" value="<c:out value='${bdMstr.tmplatId}'/>" />
			<input type="hidden" name="ntcrNm" value="dummy">   <!-- validator 처리를 위해 지정 -->
			<input type="hidden" name="password" value="dummy"> <!-- validator 처리를 위해 지정 -->
			<input name="ntceBgnde" type="hidden" value="10000101">
			<input name="ntceEndde" type="hidden" value="99991231">
			<input type="hidden" name="atchFileId" value="<c:out value='${result.atchFileId}'/>" >
			<input type="hidden" name="fileSn" value="0"> <!-- 파일순서 -->
          </form>
          <!-- 폼내부에 버튼이 있어야지만, submit버튼이 작동한다. -->
          
          </div>
        </div>
        
      </div><!-- /.container-fluid -->
    </section>
    <!-- /.content -->
  </div>
  <!-- /.content-wrapper -->

<%@ include file="../include/footer.jsp" %>
<!-- 첨부파일 부트스트랩 디자인 JS -->
<script src="<c:url value='/'/>resources/plugins/bs-custom-file-input/bs-custom-file-input.min.js"></script>
<!-- 첨부파일 선택한 내용 출력 실행 -->
<script>
$(document).ready(function () {
  bsCustomFileInput.init();
});
</script>

<script>
$(document).ready(function(){
	var update_form = $("form[name='update_form']");
	$("#btn_list").on("click", function(){
		//alert("목록");
		update_form.attr("action", "<c:url value='/admin/board/list_board.do' />");
		update_form.submit();
	});
	$("#btn_view").on("click", function(){
		//alert("이전");
		update_form.attr("action", "<c:url value='/admin/board/view_board.do' />");
		update_form.submit();
	});
});
</script>
<script>
$(document).ready(function(){
	$("#nttCn").summernote({
		height:150,
		lang:"ko-KR",
		placeholder:'글 내용을 입력해 주세요',
		toolbar: [
				    ['fontname', ['fontname']],
				    ['fontsize', ['fontsize']],
				    ['style', ['bold', 'italic', 'underline','strikethrough', 'clear']],
				    ['color', ['forecolor','color']],
				    ['table', ['table']],
				    ['para', ['ul', 'ol', 'paragraph']],
				    ['height', ['height']],
				    ['insert',['link','video']],//'picture',
				    ['view', ['fullscreen', 'help']]
				],
		fontNames: ['Arial', 'Arial Black', 'Comic Sans MS', 'Courier New','맑은 고딕','궁서','굴림체','굴림','돋음체','바탕체'],
		fontSizes: ['8','9','10','11','12','14','16','18','20','22','24','28','30','36','50','72']
	});
});//textarea 중 content아이디영역을 섬머노트에디터로 변경처리 함수실행
</script>
