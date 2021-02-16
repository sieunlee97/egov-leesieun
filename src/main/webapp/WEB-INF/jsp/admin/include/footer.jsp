<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 관리자단 푸터 시작 -->
  <footer class="main-footer">
    <strong>Copyright &copy; 2014-2020 <a href="https://adminlte.io">AdminLTE.io</a>.</strong>
    All rights reserved.
    <div class="float-right d-none d-sm-inline-block">
      <b>Version</b> 3.1.0-rc
    </div>
  </footer>
 

  <!-- Control Sidebar, 로그아웃 영역 -->
  <aside class="control-sidebar control-sidebar-dark">
    <!-- Control sidebar content goes here -->
    <div class="p-3 control-sidebar-content text-center">
	    <h5>로그 아웃</h5><hr class="mb-2"/>
	    <a href="<c:url value='/uat/uia/actionLogout.do' />" class="btn btn-lg btn-primary">로그아웃</a>
    </div>
  </aside>
  <!-- /.control-sidebar -->
</div>
<!-- ./wrapper -->

<!-- jQuery -->
<script src="<c:url value='/' />resources/plugins/jquery/jquery.min.js"></script>
<!-- jQuery UI 1.11.4 -->
<script src="<c:url value='/' />resources/plugins/jquery-ui/jquery-ui.min.js"></script>
<!-- Resolve conflict in jQuery UI tooltip with Bootstrap tooltip -->
<script>
  $.widget.bridge('uibutton', $.ui.button)
</script>
<!-- Bootstrap 4 -->
<script src="<c:url value='/' />resources/plugins/bootstrap/js/bootstrap.bundle.min.js"></script>
<!-- ChartJS -->
<script src="<c:url value='/' />resources/plugins/chart.js/Chart.min.js"></script>
<!-- Sparkline -->
<script src="<c:url value='/' />resources/plugins/sparklines/sparkline.js"></script>
<!-- JQVMap -->
<script src="<c:url value='/' />resources/plugins/jqvmap/jquery.vmap.min.js"></script>
<script src="<c:url value='/' />resources/plugins/jqvmap/maps/jquery.vmap.usa.js"></script>
<!-- jQuery Knob Chart -->
<script src="<c:url value='/' />resources/plugins/jquery-knob/jquery.knob.min.js"></script>
<!-- daterangepicker -->
<script src="<c:url value='/' />resources/plugins/moment/moment.min.js"></script>
<script src="<c:url value='/' />resources/plugins/daterangepicker/daterangepicker.js"></script>
<!-- Tempusdominus Bootstrap 4 -->
<script src="<c:url value='/' />resources/plugins/tempusdominus-bootstrap-4/js/tempusdominus-bootstrap-4.min.js"></script>
<!-- Summernote -->
<script src="<c:url value='/' />resources/plugins/summernote/summernote-bs4.min.js"></script>
<!-- overlayScrollbars -->
<script src="<c:url value='/' />resources/plugins/overlayScrollbars/js/jquery.overlayScrollbars.min.js"></script>
<!-- AdminLTE App -->
<script src="<c:url value='/' />resources/dist/js/adminlte.js"></script>
<!-- AdminLTE for demo purposes -->
<script src="<c:url value='/' />resources/demo.js"></script> <!-- 원래 경로 dist/js/demo.js -->
<!-- AdminLTE dashboard demo (This is only for demo purposes) -->
<script src="<c:url value='/' />resources/dist/js/pages/dashboard.js"></script>
</body>
</html>
 <!-- 관리자단 푸터 끝 -->