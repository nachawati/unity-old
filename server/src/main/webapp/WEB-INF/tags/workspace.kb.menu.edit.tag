<%@tag pageEncoding="utf-8" trimDirectiveWhitespaces="true" dynamic-attributes="attrs"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<t:li.menu cls="bg-danger" href="${contextPath}/${it.path}/${path}" icon="octicon octicon-arrow-left" title="Cancel"></t:li.menu>
<t:li.menu cls="divider" href="" icon="fa fa-floppy-o" title="Save Changes"></t:li.menu>