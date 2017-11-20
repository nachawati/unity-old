<%@page pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<t:page title="${it.project.nameWithNamespace}">
	<jsp:attribute name="head">
	<style>
#repository-tab {
	overflow: auto;
	padding-bottom: 100px;
}

#ontology-tab {
	overflow: auto;
	padding-bottom: 100px;
}
</style>
</jsp:attribute>
	<jsp:attribute name="path">
		<t:project.path></t:project.path>
	</jsp:attribute>
	<jsp:attribute name="breadcrumbs">
		<li><a href="${pageContext.request.contextPath}/${it.path}/kb"><i
				class="octicon octicon-repo"></i></a></li>
		<c:forEach var="pathComponent" items="${it.pathComponents}">
			<li><a
				href="${pageContext.request.contextPath}/${it.project.pathWithNamespace}/${pathComponent.value}">${pathComponent.key}</a></li>
		</c:forEach>
	</jsp:attribute>

	<jsp:attribute name="sidebar">
		<t:project.sidebar active="kernel/"></t:project.sidebar>
	</jsp:attribute>

	<jsp:attribute name="scripts">
	<script
			src="${pageContext.request.contextPath}/assets/bower_components/ace-builds/src-noconflict/ace.js"
			type="text/javascript" charset="utf-8"></script>
			<script
			src="${pageContext.request.contextPath}/assets/bower_components/ace-builds/src-noconflict/ext-modelist.js"
			type="text/javascript" charset="utf-8"></script>
		
	</jsp:attribute>
	<jsp:body>
	
			<div class="box">
            <div class="box-header">
              <h3 class="box-title">Task Executions</h3>
            </div>
            <!-- /.box-header -->
            <div class="box-body no-padding">
              <table class="table">
                <tbody>
                <c:forEach var="taskExecution"
							items="${it.project.taskExecutions.iterator()}">
                <tr>
                  <td>${taskExecution.id}</td>
									<td>${taskExecution.task.name}</td>
									<td>${taskExecution.dateInitiated}</td>
									<td>${taskExecution.dateTerminated}</td>
									<td>${taskExecution.status}</td>
                </tr>
                
              
                </c:forEach>
                
              </tbody>
				</table>
            </div>
            <!-- /.box-body -->
          </div>
		
	</jsp:body>
</t:page>
