<%@tag pageEncoding="utf-8" trimDirectiveWhitespaces="true"
	dynamic-attributes="attrs"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<form action="#" method="get" class="sidebar-form">
	<div>
		<select class="form-control select2" style="width: 100%;">
			<optgroup label="Branches">
				<c:forEach var="branch" items="${it.project.repository.branches.iterator()}">
					<option>${branch.name}</option>
				</c:forEach>
			</optgroup>
			<optgroup label="Tags">
				<c:forEach var="tag" items="${it.project.repository.tags.iterator()}">
					<option>${tag.name}</option>
				</c:forEach>
			</optgroup>
			<optgroup label="Commits">
				<option selected="selected">${it.ref}</option>
			</optgroup>
		</select>
	</div>
</form>
<ul class="sidebar-menu" data-widget="tree">
	<li class="header">PROJECT NAVIGATION</li>
	<t:li.sidebar active="${fn:startsWith(attrs.active, 'home/')}"
		href="${pageContext.request.contextPath}/${it.project.pathWithNamespace}" title="Home"
		icon="octicon octicon-home"></t:li.sidebar>
	<t:li.sidebar active="${fn:startsWith(attrs.active, 'console/')}"
		href="${pageContext.request.contextPath}/${it.project.pathWithNamespace}/console" title="Console"
		icon="octicon octicon-terminal"></t:li.sidebar>
		<!-- 
	<t:li.sidebar active="${fn:startsWith(attrs.active, 'studies/')}"
		href="${pageContext.request.contextPath}/${it.project.pathWithNamespace}/studies"
		title="Studies" icon="octicon octicon-beaker">
	</t:li.sidebar>
	 -->
	<t:li.sidebar active="${fn:startsWith(attrs.active, 'tree/')}"
		href="${pageContext.request.contextPath}/${it.project.pathWithNamespace}/tree"
		title="Repository" icon="octicon octicon-repo">
	</t:li.sidebar>
	<t:li.sidebar active="${fn:startsWith(attrs.active, 'settings/')}"
		href="${pageContext.request.contextPath}/${it.project.pathWithNamespace}/settings"
		title="Settings" icon="octicon octicon-settings">
	</t:li.sidebar>
</ul>