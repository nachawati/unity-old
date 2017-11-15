<%@tag pageEncoding="utf-8" trimDirectiveWhitespaces="true"
	dynamic-attributes="attrs"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<form action="#" method="get" class="sidebar-form">
	<div>
		<select class="form-control select2" style="width: 100%;">
			<optgroup label="Branches">
				<c:forEach var="branch" items="${it.workspace.repository.branches}">
					<option>${branch.name}</option>
				</c:forEach>
			</optgroup>
			<optgroup label="Tags">
				<c:forEach var="tag" items="${it.workspace.repository.tags}">
					<option>${tag.name}</option>
				</c:forEach>
			</optgroup>
			<optgroup label="Commits">
				<option selected="selected">${it.reference}</option>
			</optgroup>
		</select>
	</div>
</form>
<ul class="sidebar-menu" data-widget="tree">
	<li class="header">WORKSPACE NAVIGATION</li>
	<t:li.sidebar active="${fn:startsWith(attrs.active, 'home/')}"
		href="${contextPath}/${it.workspace.pathWithNamespace}" title="Home"
		icon="octicon octicon-home"></t:li.sidebar>
	<t:li.sidebar active="${fn:startsWith(attrs.active, 'console/')}"
		href="${contextPath}/${it.workspace.pathWithNamespace}/console" title="Console"
		icon="octicon octicon-terminal"></t:li.sidebar>
	<t:li.sidebar active="${fn:startsWith(attrs.active, 'tree/')}"
		href="${contextPath}/${it.workspace.pathWithNamespace}/tree"
		title="Repository" icon="octicon octicon-repo">
	</t:li.sidebar>
	<t:li.sidebar active="${fn:startsWith(attrs.active, 'kernel/')}"
		href="${contextPath}/${it.workspace.pathWithNamespace}/kernel"
		title="Kernel" icon="octicon octicon-gear">
	</t:li.sidebar>
	<t:li.sidebar active="${fn:startsWith(attrs.active, 'members/')}"
		href="${contextPath}/${it.workspace.pathWithNamespace}/members"
		title="Members" icon="octicon octicon-organization">
	</t:li.sidebar>
	<t:li.sidebar active="${fn:startsWith(attrs.active, 'settings/')}"
		href="${contextPath}/${it.workspace.pathWithNamespace}/settings"
		title="Settings" icon="octicon octicon-settings">
	</t:li.sidebar>
</ul>