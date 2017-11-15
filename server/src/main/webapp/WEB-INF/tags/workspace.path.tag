<%@tag pageEncoding="utf-8" trimDirectiveWhitespaces="true"
	dynamic-attributes="attrs"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<li><a href="${contextPath}" style="color: #F1D372;"><i class="fa fa-industry mr-10"></i>Factory
		Optima</a></li>
<c:forEach var="pathComponent" items="${it.pathComponents}">
	<li><a href="${pathComponent.value}">${pathComponent.key}</a></li>
</c:forEach>