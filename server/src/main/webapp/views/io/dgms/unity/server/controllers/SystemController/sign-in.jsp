<%@page pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>Sign in &middot; Unity DGMS</title>
<meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/bower_components/bootstrap/dist/css/bootstrap.min.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/bower_components/font-awesome/css/font-awesome.min.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/bower_components/Ionicons/css/ionicons.min.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/bower_components/admin-lte/dist/css/AdminLTE.min.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/bower_components/admin-lte/plugins/iCheck/square/blue.css">
<!--[if lt IE 9]>
  <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
  <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
  <![endif]-->
<link rel="stylesheet"
	href="https://fonts.googleapis.com/css?family=Source+Sans+Pro:300,400,600,700,300italic,400italic,600italic">
<link rel="icon" type="image/png" href="${contextPath}/assets/favicon.png">
</head>
<body class="hold-transition login-page">
	<div class="login-box">
		<div class="login-logo" style="font-size: 40px;">
			<b>Unity</b> DGMS
		</div>
		<div class="login-box-body">
			<p class="login-box-msg text-bold">Please sign in to continue</p>
			<form action="${pageContext.request.contextPath}/users/sign-in" method="post">
				<div class="form-group has-feedback">
					<input type="text" class="form-control" placeholder="Username or email" name="username"><span
						class="glyphicon glyphicon-envelope form-control-feedback"></span>
				</div>
				<div class="form-group has-feedback">
					<input type="password" class="form-control" placeholder="Password" name="password"><span
						class="glyphicon glyphicon-lock form-control-feedback"></span>
				</div>
				<div class="row">
					<div class="col-xs-8">
						<del>Register a new account</del>
					</div>
					<div class="col-xs-4">
						<button type="submit" class="btn bg-purple btn-block btn-flat">Sign In</button>
					</div>
				</div>
			</form>
		</div>
		<div style="margin-top: 1em; text-align: right; font-size: small" class="text-muted">
			<b>Version</b> 0.1.0-SNAPSHOT
		</div>
	</div>
	<script src="${pageContext.request.contextPath}/assets/bower_components/jquery/dist/jquery.min.js"></script>
	<script src="${pageContext.request.contextPath}/assets/bower_components/bootstrap/dist/js/bootstrap.min.js"></script>
	<script src="${pageContext.request.contextPath}/assets/bower_components/admin-lte/plugins/iCheck/icheck.min.js"></script>
	<script>
		$(function() {
			$('input').iCheck({
				checkboxClass : 'icheckbox_square-blue',
				radioClass : 'iradio_square-blue',
				increaseArea : '20%'
			});
		});
	</script>
</body>
</html>
