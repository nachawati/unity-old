<%@tag pageEncoding="utf-8" trimDirectiveWhitespaces="true"
	dynamic-attributes="attrs"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<ul class="sidebar-menu" data-widget="tree">
	<li class="header">NAVIGATION</li>
	<t:li.sidebar active="${fn:startsWith(attrs.active, 'home/')}"
		href="${pageContext.request.contextPath}" title="Home"
		icon="octicon octicon-home"></t:li.sidebar>
	<t:li.sidebar active="${fn:startsWith(attrs.active, 'workspaces/')}"
		href="${pageContext.request.contextPath}/workspaces" title="Workspaces" icon="octicon octicon-briefcase">
	</t:li.sidebar>	
	<t:li.sidebar active="${fn:startsWith(attrs.active, 'registry/')}"
		href="${pageContext.request.contextPath}/registry" title="Registry"
		icon="fa fa-cube"></t:li.sidebar>
	<t:li.sidebar active="${fn:startsWith(attrs.active, 'users/')}"
		href="${pageContext.request.contextPath}/users" title="Users"
		icon="octicon octicon-organization">
	</t:li.sidebar>
	<t:li.sidebar active="${fn:startsWith(attrs.active, 'settings/')}"
		href="${pageContext.request.contextPath}/settings" title="Settings" icon="octicon octicon-settings">
	</t:li.sidebar>
</ul>