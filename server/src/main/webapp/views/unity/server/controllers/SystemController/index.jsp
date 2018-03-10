<%@page pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<t:page title="Explore">
	<jsp:attribute name="path">
		<li><a href="${pageContext.request.contextPath}"
			style="color: #D3F172;"><i class="fa fa-globe mr-10"></i>Explore</a></li>
<c:forEach var="pathComponent" items="${it.pathComponents}">
	<li><a href="${pathComponent.value}">${pathComponent.key}</a></li>
</c:forEach>
	</jsp:attribute>
	<jsp:attribute name="sidebar">
		<ul class="sidebar-menu" data-widget="tree">
	<t:li.sidebar active="true"
				href="${pageContext.request.contextPath}/${it.path}" title="Home"
				icon="octicon octicon-home"></t:li.sidebar>
		</ul>
	</jsp:attribute>
	<jsp:attribute name="hero">
		<div class="bg-purple-active text-center pt-50 pb-50" style="background-image: url(${pageContext.request.contextPath}/assets/img/dark-leather.png);  box-shadow: 0px 10px 50px 0px rgba(0,0,0,0.5) !important; ">
  			<h1 class="mt-0">Welcome to Unity Server</h1>
  			<h2 class="mt-0" style="color: #ccc; font-size: 1.8em;">Unity is a system for decision-guided analytics management</h2>
  			
  			<div class="container pt-25">
  			<div class="row features">
  				<div class="col-sm-4 pt-25">
  					<div>
  						<img
								src="${pageContext.request.contextPath}/assets/img/database-px-png.png" />
  					</div>
  					<h2>Datasets</h2>
					<p class="info"></p>
  				</div>
  				<div class="col-sm-4 pt-25"
						style="border-top: solid 1px rgb(74, 71, 133);">
  				<div>
  						<img
								src="${pageContext.request.contextPath}/assets/img/f-component_256-128.png" />
  					</div>
  					<h2>Models</h2>
					<p class="info"></p>
  				</div>
  				<div class="col-sm-4 pt-25">
  				<div>
  						<img
								src="${pageContext.request.contextPath}/assets/img/Infinity_endless_eternity_maths_mathematical_shows_loop-128.png" />
  					</div>
  					<h2>Algorithms</h2>
					<p class="info"></p>
  				</div>
  			</div>
  			
  			</div>
		</div>
	</jsp:attribute>

	<jsp:body>
	
	<div class="container">
	<div class="alert bg-purple"
				style="background-color: #dff0f7 !important; border-color: #cae6f1 !important; color: #006589 !important;">
                <h4>
					<i class="icon fa fa-check"></i> Alert!</h4>
                Success alert preview. This alert is dismissable.
              </div>
	<div class="nav-tabs-custom" style="background-color: transparent">
            <!-- Tabs within a box -->
            <ul class="nav nav-tabs" style="font-size: 1.1em;">
              <li class="active"><a href="#revenue-chart"
						data-toggle="tab"><b>Public</b></a></li>
              <li><a href="#sales-chart" data-toggle="tab"><b>Your Work</b></a></li>
            </ul>
            <div class="tab-content no-padding">
              <div class="chart tab-pane active" id="revenue-chart">

<nav class="navbar navbar-static-top hidden-xs  mb-0" id="context_menu">
					<div class="navbar-header pull-right">
						<button type="button" class="navbar-toggle collapsed"
									data-toggle="collapse" data-target="#navbar-collapse-sub">
							<i class="fa fa-ellipsis-h"></i>
						</button>
					</div>
					<div class="collapse navbar-collapse pull-right"
								id="navbar-collapse-sub">
						<ul class="nav navbar-nav">
								<li class=""><a href="?action=upload" data-remote="false"
										data-toggle="modal" data-target="#modal"><i
											class="octicon octicon-cloud-upload"></i></a></li>
							<li class="divider"><a
										href="/unity-server/NIST/Manufacturing/tree?action=new"
										data-remote="false" data-toggle="modal" data-target="#modal"><i
											class="octicon octicon-plus mr-5"></i><span>New</span></a></li>
							</ul>
					</div>
				</nav>
				<table class="table ">
                <tbody>
<c:forEach var="i" begin="0" end="10" step="1">

							
                <tr>
                  <td class="text-center"
											style="width: 60px; vertical-align: middle">
                  	<div>
											<i class="fa fa-caret-up fa-2x" style="color: #999"></i>
										</div>
                  	<div class="text-muted"
												style="font-size: 1.5em; position: relative; top: -.5em;">45</div>
                  </td>
                  <td class="text-center"
											style="width: 60px; vertical-align: middle">
                  	<img
											src="${pageContext.request.contextPath}/assets/img/Hdd_hard_disk_data_data_storage_storage_ide-128.png"
											style="width: 100%" />
                  </td>
                  <td style=" vertical-align: middle">
                  	<div class="text-bold" style="font-size: 1.2em">Update software</div>
                  	<div class="text-muted">Update software</div>
									</td>
				<td style="width: 200px; vertical-align: middle">                    <div
												class="progress progress-xs">
                      <div class="progress-bar progress-bar-danger"
													style="width: 55%"></div>
                    </div>

									
									</td>            
									
									
									<td style="width: 250px; vertical-align: middle"><div
												class="btn-group">
                      <button type="button" class="btn btn-lg btn-default"><i class="fa fa-table text-muted"></i></button>
                      <button type="button" class="btn btn-lg btn-default">jq</button>
                      <button type="button" class="btn btn-lg btn-default"><i class="fa fa-terminal text-muted"></i></button>
                      <button type="button" class="btn btn-lg btn-default"><i class="fa fa-book text-muted"></i></button>
                    </div></td>
                </tr>
</c:forEach>
          
              </tbody>
					</table>
				</div>
              <div class="chart tab-pane" id="sales-chart">abc</div>
            </div>
          </div>
          </div>	
	</jsp:body>
</t:page>
