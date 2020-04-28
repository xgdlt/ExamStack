<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%-- <%@taglib uri="spring.tld" prefix="spring"%> --%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String servletPath = (String)request.getAttribute("javax.servlet.forward.servlet_path");
String[] list = servletPath.split("\\/");
request.setAttribute("role",list[1]);
request.setAttribute("topMenuId",list[2]);
request.setAttribute("leftMenuId",list[3]);
%>

<!DOCTYPE html>
<html>
  	<head>
   		<base href="<%=basePath%>">
   	 	<meta charset="utf-8"><meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
		<title>试题管理</title>
		<meta name="keywords" content="">
		<link rel="shortcut icon" href="<%=basePath%>resources/images/favicon.ico" />
		<link href="resources/bootstrap/css/bootstrap-huan.css" rel="stylesheet">
		<link href="resources/font-awesome/css/font-awesome.min.css" rel="stylesheet">
		<link href="resources/css/style.css" rel="stylesheet">
		<link href="resources/css/layui.css" rel="stylesheet">
		<link href="resources/css/question-add.css" rel="stylesheet">
		<link href="resources/chart/morris.css" rel="stylesheet">
	</head>
	<body>
			<!-- Javascript files -->
    		<!-- jQuery -->
    		<script type="text/javascript" src="resources/js/jquery/jquery-1.9.0.min.js"></script>
    		<script type="text/javascript" src="resources/js/jquery/fileupload/vendor/jquery.ui.widget.js"></script>
            <script type="text/javascript" src="resources/js/jquery/fileupload/jquery.iframe-transport.js"></script>
            <script type="text/javascript" src="resources/js/jquery/fileupload/jquery.fileupload.js"></script>
    		<script type="text/javascript" src="resources/js/all.js"></script>
    		<script type="text/javascript" src="resources/js/layer/layui.js"></script>
    		<script type="text/javascript" src="resources/js/uploadify/jquery.uploadify3.1Fixed.js"></script>
    		<script type="text/javascript" src="resources/js/question-import.js"></script>

    		<!-- Bootstrap JS -->
    		<script type="text/javascript" src="resources/bootstrap/js/bootstrap.min.js"></script>
		<header>
			<span style="display:none;" id="rule-role-val"><%=list[1]%></span>
			<div class="container">
				<div class="row">
					<div class="col-xs-5">
						<div class="logo">
							<h1><a href="#">网站管理系统</a></h1>
							<div class="hmeta">
								专注互联网在线考试解决方案
							</div>
						</div>
					</div>
					<div class="col-xs-7" id="login-info">
						<c:choose>
							<c:when test="${not empty sessionScope.SPRING_SECURITY_CONTEXT.authentication.principal.username}">
								<div id="login-info-user">
									
									<a href="user-detail/${sessionScope.SPRING_SECURITY_CONTEXT.authentication.principal.username}" id="system-info-account" target="_blank">${sessionScope.SPRING_SECURITY_CONTEXT.authentication.principal.username}</a>
									<span>|</span>
									<a href="j_spring_security_logout"><i class="fa fa-sign-out"></i> 退出</a>
								</div>
							</c:when>
							<c:otherwise>
								<a class="btn btn-primary" href="user-register">用户注册</a>
								<a class="btn btn-success" href="user-login-page">登录</a>
							</c:otherwise>
						</c:choose>
					</div>
				</div>
			</div>
		</header>
		<!-- Navigation bar starts -->

		<div class="navbar bs-docs-nav" role="banner">
			<div class="container">
				<nav class="collapse navbar-collapse bs-navbar-collapse" role="navigation">
					<c:import url="/common-page/top-menu?topMenuId=${topMenuId}&leftMenuId=${leftMenuId}" charEncoding="UTF-8" />
				</nav>
			</div>
		</div>

		<!-- Navigation bar ends -->

		<!-- Slider starts -->

		<div>
			<!-- Slider (Flex Slider) -->

			<div class="container" style="min-height:500px;">

				<div class="row">
					<div class="col-xs-2" id="left-menu">
						<c:import url="/common-page/left-menu?topMenuId=${topMenuId}&leftMenuId=${leftMenuId}" charEncoding="UTF-8" />

					</div>
					<div class="col-xs-10" id="right-content">
						<div class="page-header">
							<h1><i class="fa fa-cloud-upload"></i> 导入试题 </h1>
						</div>
						<div class="page-content" style="padding-top:20px;">
							<form id="from-question-import" action="secure/question-import">
							<div class="form-line upload-question-group">
								<span class="form-label">选择题库：</span>
								<select class="df-input-narrow">
									<option value="0">-- 请选择 --</option>
										<c:forEach items="${fieldList }" var="item">
											<option value="${item.fieldId }">${item.fieldName }</option>
										</c:forEach>
								</select>
								<span class="form-message"></span>
							</div>

							<div class="form-line template-download">
								<span class="form-label">下载模板：</span>
								<a href="resources/template/question.xlsx" style="color:rgb(22,22,22);text-decoration: underline;">点击下载</a>
							</div>
							<!--
							<div class="form-line control-group">
								<span class="form-label"><span class="warning-label">*</span>上传文件：</span>
								<div class="controls file-form-line">
									<div>
										<div id="div-file-list"></div>

										<div id="fileQueue"></div>
										<div id="uploadify"></div>
									</div>
									<span class="help-inline form-message"></span>
								</div>
							</div>

							<div class="form-line control-group">
                                <span class="form-label"><span class="warning-label">*</span>上传文件：</span>
                                <div class="controls file-form-line">
                                    <input id="fileupload" type="file" name="fileupload" data-url="<%=basePath%>secure/upload-file" value=""/>
                                </div>
                            </div>
                            -->

                            <div class="form-line control-group">
                                <span class="form-label"><span class="warning-label">*</span>上传文件：</span>
                                <div class="controls file-form-line">
                                    <input type="hidden" id="upload_file" name="uploadFile" value="">
                                    <button type="button" class="layui-btn" id="test1">
                                        <i class="fa fa-cloud"></i>上传文件
                                    </button>
                                </div>
                            </div>


							<div class="form-line">
								<input value="提交" type="submit" class="df-submit btn btn-info">
							</div>
						</form>
						</div>
					</div>
				</div>
			</div>
		</div>
		<footer>
			<div class="container">
				<div class="row">
					<div class="col-md-12">
						<div class="copy">
							<p>
								<a href="." target="_blank">主页</a> |  <a href="http://172.20.20.172:6006/shadow/" target="_blank">论坛</a>
                            </p>
						</div>
					</div>
				</div>

			</div>

		</footer>

		<!-- Slider Ends -->
<!--
<script type="text/javascript">

    layui.use('upload', function () {
        var upload = layui.upload;
        //执行实例
        var uploadInst = upload.render({
            elem: '#test1', //绑定元素
            url: '<%=basePath%>secure/upload-file', //上传接口
            size: 10000000,
            accept: 'file',
            done: function (r) {
                $("#upload_file").val(r);
                layer.msg(r);
                app.getData();
            },
            error: function (r) {
                layer.msg(r);
            }
        });
    });

</script>
-->
	</body>
</html>