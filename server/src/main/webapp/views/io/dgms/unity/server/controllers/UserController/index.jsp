<%@page pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<t:page title="User">
	<jsp:attribute name="path">
		<li><a href="${pageContext.request.contextPath}"
			style="color: #D3F172;"><i class="fa fa-globe mr-10"></i>Explore</a></li>
<c:forEach var="pathComponent" items="${it.pathComponents}">
	<li><a href="${pathComponent.value}">${pathComponent.key}</a></li>
</c:forEach>
	</jsp:attribute>
	<jsp:attribute name="sidebar">
		<ul class="sidebar-menu" data-widget="tree">
	<li class="header">USER NAVIGATION</li>
	<t:li.sidebar active="true"
				href="${pageContext.request.contextPath}/${it.path}" title="Home"
				icon="octicon octicon-home"></t:li.sidebar>
		</ul>
	</jsp:attribute>
	<jsp:body>
		<div class="box">
            <div class="box-header">
              <h3 class="box-title">Projects</h3>
            </div>
            <div class="box-body no-padding">
              <table class="table">
                <tbody>
                <c:forEach var="project"
							items="${it.user.projects.iterator()}">
                <tr>
                  <td><a
									href="${pageContext.request.contextPath}/${project.pathWithNamespace}">${project.nameWithNamespace}</a></td>
                </tr>
              
                </c:forEach>
                
              </tbody>
				</table>
            </div>
          </div>
	</jsp:body>
</t:page>
