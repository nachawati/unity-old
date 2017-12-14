<%@page pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<t:page title="Studies · ${it.project.nameWithNamespace}">
	<jsp:attribute name="path">
		<t:project.path></t:project.path>
		<li><a
			href="${pageContext.request.contextPath}/${it.project.pathWithNamespace}/studies">Studies</a></li>
	</jsp:attribute>
	<jsp:attribute name="menu">
		<t:project.studies.menu></t:project.studies.menu>
	</jsp:attribute>
	<jsp:attribute name="sidebar">
		<t:project.sidebar active="studies/"></t:project.sidebar>
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
            <div class="box-body no-padding">
              <table class="table">
              	<thead>
              		<tr>
              			<th>ID</th>
              			<th>Study Name</th>
              			<th>Evaluations</th>
              			
              			<th>Last Activity</th>
              			<th>Status</th>
              		</tr>
              	</thead>
                <tbody>
                <c:forEach var="taskExecution"
							items="${it.project.taskExecutions.iterator()}">
                <tr>
                  <td>${taskExecution.id}</td>
									<td><a href="${pageContext.request.contextPath}/${it.project.pathWithNamespace}/studies/${taskExecution.id}">${taskExecution.task.name}</a></td>
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
