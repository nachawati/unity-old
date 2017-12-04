<%@tag pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@attribute name="head" fragment="true" required="false"%>
<%@attribute name="path" fragment="true" required="false"%>
<%@attribute name="sidebar" fragment="true" required="false"%>
<%@attribute name="menu" fragment="true" required="false"%>
<%@attribute name="breadcrumbs" fragment="true" required="false"%>
<%@attribute name="properties" fragment="true" required="false"%>
<%@attribute name="scripts" fragment="true" required="false"%>
<%@attribute name="sidenav" fragment="true" required="false"%>
<%@attribute name="title" fragment="false" required="true"%>
<%@attribute name="search_action" fragment="false" required="false"%>
<%@attribute name="search_label" fragment="false" required="false"%>
<c:set var="search_action"
	value="${(empty search_action) ? '/search' : search_action}" />
<jsp:invoke fragment="sidebar" var="sidebarText"></jsp:invoke>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>${title}&#32;·&#32;Unity&#32;Server</title>
<meta
	content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no"
	name="viewport">
<link rel="icon" type="image/png"
	href="${pageContext.request.contextPath}/assets/favicon.png">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/bower_components/bootstrap/dist/css/bootstrap.min.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/bower_components/jstree/dist/themes/default/style.min.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/bower_components/font-awesome/css/font-awesome.min.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/bower_components/Ionicons/css/ionicons.min.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/bower_components/admin-lte/dist/css/AdminLTE.min.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/bower_components/admin-lte/dist/css/skins/skin-purple.min.css">
<link rel="stylesheet"
	href="https://fonts.googleapis.com/css?family=Source+Sans+Pro:300,400,600,700,300italic,400italic,600italic">
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/octicons/4.4.0/font/octicons.css">
<!--[if lt IE 9]>
  <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
  <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
<![endif]-->
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/spacing.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/style.css">
<jsp:invoke fragment="head" />
</head>
<body
	class="hold-transition skin-purple ${empty sidebarText ? 'layout-top-nav' : 'sidebar-mini'}">
	<div class="wrapper">
		<header class="main-header">

			<c:if test="${not empty sidebarText}">
				<a href="${pageContext.request.contextPath}" class="logo"><span
					class="logo-mini"><img
						src="${pageContext.request.contextPath}/assets/img/logo.png"
						class="img-square" alt=""></span><span class="logo-lg"><b>Unity</b>
						Server</span></a>
			</c:if>
			<nav class="navbar navbar-static-top">
				<c:if test="${not empty sidebarText}">
					<a href="#" class="sidebar-toggle" data-toggle="push-menu"
						role="button"><span class="sr-only">Toggle navigation</span></a>
				</c:if>
				<ul class="nav navbar-nav breadcrumb pull-left">
					<jsp:invoke fragment="path"></jsp:invoke>
				</ul>
				<div class="navbar-header pull-right">
					<button type="button" class="navbar-toggle collapsed"
						data-toggle="collapse" data-target="#navbar-collapse">
						<i class="fa fa-bars"></i>
					</button>
				</div>
				
				<div class="navbar-custom-menu pull-right">
					<ul class="nav navbar-nav">
						<li class="dropdown user user-menu"><a href="#"
							class="dropdown-toggle" data-toggle="dropdown"><img
								src="${user.imageUrl}" alt="" class="img-circle mr-3"><span
								class="caret"></span></a>
							<ul class="dropdown-menu">
								<li class="user-header"><img src="${user.imageUrl}" alt=""
									class="img-circle">
									<p>${user.name}<small>${user.email}</small>
									</p></li>
								<li class="user-footer">
									<div class="pull-right">
										<a href="${pageContext.request.contextPath}/users/sign-out"
											class="btn btn-default btn-flat">Sign out</a>
									</div>
								</li>
							</ul></li>
					</ul>
				</div>
				<div class="navbar-custom-menu pull-right">
					<ul class="nav navbar-nav">
						<li><a><b>Unity</b> Server (v0.1.0-SNAPSHOT)</a></li>
					</ul>
				</div>
				<div class="collapse navbar-collapse pull-right"
					id="navbar-collapse">
					<form class="navbar-form pull-left-not-xs hidden-md hidden-sm"
						role="search" action="${pageContext.request.contextPath}${search_action}" method="get">
						<div class="form-group input-group">
							<c:if test="${not empty search_label}">
								<span class="input-group-addon"
									style="background-color: rgba(255, 255, 255, 0.0); color: #fff; font-size: small; border-color: rgba(255, 255, 255, 0.2); border-bottom-left-radius: 3px; border-top-left-radius: 3px;">${search_label}</span>
							</c:if>
							<input type="text" class="form-control" id="navbar-search-input"
								name="q" placeholder="Search" style=""><span
								class="input-group-btn">
								<button type="submit" class="btn bg-purple">
									<i class="fa fa-search"></i>
								</button>
							</span>
						</div>
					</form>
					<!-- 
					<ul class="nav navbar-nav">
						<li class="dropdown"><a href="#" class="dropdown-toggle" data-toggle="dropdown"><i
								class="octicon octicon-plus"></i><span class="visible-xs-inline mr-5 ml-3">Construct</span><span class="caret"></span>
						</a>
							<ul class="dropdown-menu dropdown-menu-right" role="menu">
								<li><a data-toggle="modal" href="#exampleModal">New Collection</a></li>
								<li><a data-toggle="modal" href="#new-repository">New Repository</a></li>
								<li><a data-toggle="modal" href="#new-group">New Group</a></li>
							</ul></li>
					</ul>
					 -->
				</div>
				
			</nav>
			<jsp:invoke fragment="menu" var="menuText"></jsp:invoke>
			<c:if test="${not empty menuText}">
				<nav class="navbar navbar-static-top hidden-xs" id="context_menu">
					<div class="navbar-header pull-right">
						<button type="button" class="navbar-toggle collapsed"
							data-toggle="collapse" data-target="#navbar-collapse-sub">
							<i class="fa fa-ellipsis-h"></i>
						</button>
					</div>
					<div class="collapse navbar-collapse pull-left"
						id="navbar-collapse-sub">
						<ul class="nav navbar-nav">${menuText}
						</ul>
					</div>
				</nav>
			</c:if>
		</header>

		<c:if test="${not empty sidebarText}">
			<aside class="main-sidebar">
				<section class="sidebar">${sidebarText}</section>
			</aside>
		</c:if>
		<div class="content-wrapper">
			<div id="canvas"></div>
			<section class="content">
				<jsp:invoke fragment="breadcrumbs" var="breadcrumbsText"></jsp:invoke>
				<c:if test="${not empty breadcrumbsText}">
					<div class="container-fluid">
						<ol class="breadcrumb mb-15">${breadcrumbsText}
						</ol>
					</div>
				</c:if>
				<div class="container-fluid" >
					<div class="row">
						<jsp:invoke fragment="sidenav" var="sidenavText"></jsp:invoke>
						<c:choose>
							<c:when test="${not empty sidenavText}">
								<div class="col-md-4">${sidenavText}</div>
								<div class="col-md-8">
									<jsp:doBody></jsp:doBody>
								</div>
							</c:when>
							<c:otherwise>
								<div class="col-md-12">
									<jsp:doBody></jsp:doBody>
								</div>
							</c:otherwise>
						</c:choose>
					</div>
				</div>
			</section>
		</div>
	</div>
	<div class="modal fade" id="modal" tabindex="-1" role="dialog"
		aria-labelledby="modalLabel" aria-hidden="true">
		<div class="modal-dialog modal-lg"></div>
	</div>
	<script
		src="${pageContext.request.contextPath}/assets/bower_components/jquery/dist/jquery.min.js"></script>
	<script
		src="${pageContext.request.contextPath}/assets/bower_components/bootstrap/dist/js/bootstrap.min.js"></script>
	<script
		src="${pageContext.request.contextPath}/assets/bower_components/jquery-slimscroll/jquery.slimscroll.min.js"></script>
	<script
		src="${pageContext.request.contextPath}/assets/bower_components/jstree/dist/jstree.min.js"></script>
	<script
		src="${pageContext.request.contextPath}/assets/bower_components/fastclick/lib/fastclick.js"></script>
	<script
		src="${pageContext.request.contextPath}/assets/bower_components/admin-lte/dist/js/adminlte.min.js"></script>
	<script src="${pageContext.request.contextPath}/assets/js/script.js"></script>
	<jsp:invoke fragment="scripts" />
</body>
</html>
