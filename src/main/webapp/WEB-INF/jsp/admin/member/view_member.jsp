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
            <h1 class="m-0">회원수정</h1>
          </div><!-- /.col -->
          <div class="col-sm-6">
            <ol class="breadcrumb float-sm-right">
              <li class="breadcrumb-item"><a href="#">Home</a></li>
              <li class="breadcrumb-item active">회원수정</li>
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
          <form name="write_form" action="<c:url value='/admin/member/update_member.do' />" method="post">
          <div class="card card-primary">
              <div class="card-header">
                <h3 class="card-title">UPDATE Member</h3>
              </div>
              <!-- /.card-header -->
              
                <div class="card-body">
                  <div class="form-group">
                    <label for="EMPLYR_ID">EMPLYR_ID</label>
                    <input value="${memberVO.EMPLYR_ID}" type="text" class="form-control" name="EMPLYR_ID" id="EMPLYR_ID" placeholder="ID를 입력해주세요." readonly required>
                  <!-- form에서 input같은 입력태그에는 name 속성이 반드시 필요.name 속성값 = DB 필드 속성명
                  DB에 입력할 때 값을 전송하게 되는데 전송값을 저장하는 이름이 name이 되고,위에서는 user_id이다.-->
                  </div>
                  <div class="form-group">
                    <label for="PASSWORD">PASSWORD</label>
                    <input type="password" class="form-control" name="PASSWORD" id="PASSWORD" placeholder="암호를 입력해주세요." required>
                  </div>
                   <div class="form-group">
                    <label for="PASSWORD_HINT">PASSWORD_HINT</label>
                    <input value="${memberVO.PASSWORD_HINT}" type="text" class="form-control" name="PASSWORD_HINT" id="PASSWORD_HINT" placeholder="암호 힌트를 입력해주세요." required>
                  </div>
                  <div class="form-group">
                    <label for="PASSWORD_CNSR">PASSWORD_CNSR</label>
                    <input value="${memberVO.PASSWORD_CNSR}" type="text" class="form-control" name="PASSWORD_CNSR" id="PASSWORD_CNSR" placeholder="답변을 입력해주세요." required>
                  </div>
                  <div class="form-group">
                  	<label for="USER_NM">USER_NM</label>
                  	<input value="${memberVO.USER_NM}" type="text" class="form-control" name="USER_NM" id="USER_NM" placeholder="이름을 입력해주세요." required>
                  	<!-- 필수 입력값은 html5에서 지원하는 유효성 검사 중 required 속성 사용해서 null값을 체크한다.(유효성검사) -->
                  </div>
                  <div class="form-group">
                  	<label for="SEXDSTN_CODE">SEXDSTN_CODE</label>
                  	<select class="form-control" name="SEXDSTN_CODE" id="SEXDSTN_CODE">
                  		<option value="M" <c:out value="${(memberVO.SEXDSTN_CODE=='M')? 'selected':''}" /> >남자</option>
                  		<option value="F" <c:out value="${(memberVO.SEXDSTN_CODE=='F')? 'selected':''}" /> >여자</option>
               		</select>
                  </div>
                  <div class="form-group">
                  	<label for="EMAIL_ADRES">EMAIL_ADRES</label>
                  	<input value="${memberVO.EMAIL_ADRES}" type="email" class="form-control" name="EMAIL_ADRES" id="EMAIL_ADRES" placeholder="이메일주소를 입력해주세요." required>
                  </div>
                  <div class="form-group">
                  	<label for="HOUSE_ADRES">HOUSE_ADRES</label>
                  	<input value="${memberVO.HOUSE_ADRES}" type="email" class="form-control" name="HOUSE_ADRES" id="HOUSE_ADRES" placeholder="주소를 입력해주세요." required>
                  </div>
                  <div class="form-group">
                  	<label for="ORGNZT_ID">ORGNZT_ID</label>
                  	<input value="${memberVO.ORGNZT_ID}" type="text" class="form-control" name="ORGNZT_ID" id="ORGNZT_ID" placeholder="소속기관명을 입력해주세요." required>
                  </div>
                  <div class="form-group">
                  	<label for="enabled">EMPLYR_STTUS_CODE</label>
                  	<select class="form-control" name="enabled" id="enabled">
                  		<option value="P" <c:out value="${(memberVO.EMPLYR_STTUS_CODE=='P')? 'selected':'' }" /> >활성</option>
                  		<option value="S" <c:out value="${(memberVO.EMPLYR_STTUS_CODE=='S')? 'selected':'' }" /> >비활성</option>
               		</select>
                  </div>
                  <div class="form-group">
                  	<label for="GROUP_ID">GROUP_ID</label>
                  	<select class="form-control" name="GROUP_ID" id="GROUP_ID">
                  		<option value="ROLL_ADMIN">ROLL_ADMIN</option>
                  		<option value="ROLL_USER" selected>ROLL_USER</option>
               		</select>
                  </div>
                </div>
                <!-- /.card-body -->
             </div> <!-- /.card card-primary -->
          
          <!-- 버튼영역 시작 -->
            <div class="card-body">
              	<a href="member_list.html" class="btn btn-primary float-right mr-1">LIST ALL</a>              	
              	<button type="submit" class="btn btn-warning float-right mr-1 text-white">SUBMIT</button>
              	<!-- a태그는 링크이동은 되지만, post값을 전송하지 못한다. 그래서 button태그를 사용. -->
              </div>
          <!-- 버튼영역 끝 -->
          
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