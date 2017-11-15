<%@tag pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@attribute name="href" fragment="false" required="true"%>
<%@attribute name="active" fragment="false" required="true"%>
<%@attribute name="icon" fragment="false" required="true"%>
<%@attribute name="title" fragment="false" required="true"%>
<jsp:doBody var="bodyText"></jsp:doBody>
<c:choose>
	<c:when test="${not empty bodyText}">
		<li class="treeview ${active ? 'active menu-open' : '' }"><a
			href="#"><i class="${icon} mr-3"></i><span>${title}</span><span
				class="pull-right-container"><i
					class="fa fa-angle-left pull-right"></i></span></a>
			<ul class="treeview-menu">${bodyText}
			</ul></li>
	</c:when>
	<c:otherwise>
		<li class="${active ? 'active menu-open' : '' }"><a
			href="${href}"><i class="${icon} mr-3"></i><span>${title}</span></a></li>
	</c:otherwise>
</c:choose>