<%@page pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<t:page title="User">
	<jsp:attribute name="path">
		<li><a href="${contextPath}" style="color: #F1D372;"><i
				class="fa fa-industry mr-10"></i>Factory
		Optima</a></li>
<c:forEach var="pathComponent" items="${it.pathComponents}">
	<li><a href="${pathComponent.value}">${pathComponent.key}</a></li>
</c:forEach>
	</jsp:attribute>
	<jsp:attribute name="sidebar">
		<ul class="sidebar-menu" data-widget="tree">
	<li class="header">GROUP NAVIGATION</li>
	<t:li.sidebar active="true"
				href="${contextPath}/${it.path}" title="Home"
				icon="octicon octicon-home"></t:li.sidebar>
		</ul>
	</jsp:attribute>
	
	<jsp:body>
		<div class="box">
            <div class="box-header">
              <h3 class="box-title">Workspaces</h3>
            </div>
            <!-- /.box-header -->
            <div class="box-body no-padding">
              <table class="table">
                <tbody>
                <c:forEach var="workspace" items="${it.workspaces}">
                <tr>
                  <td><a href="${contextPath}/${workspace.pathWithNamespace}">${workspace.nameWithNamespace}</a></td>
                </tr>
              
                </c:forEach>
                
              </tbody></table>
            </div>
            <!-- /.box-body -->
          </div>
	</jsp:body>
</t:page>
