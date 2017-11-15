<%@tag pageEncoding="utf-8" trimDirectiveWhitespaces="true"
	dynamic-attributes="attrs"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<t:li.menu modal="${contextPath}/${it.path}/console?action=execute" icon="octicon octicon-arrow-right" title="Run"></t:li.menu>
