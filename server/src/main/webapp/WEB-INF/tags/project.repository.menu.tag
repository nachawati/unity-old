<%@tag pageEncoding="utf-8" trimDirectiveWhitespaces="true"
	dynamic-attributes="attrs"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<t:li.menu modal="?action=upload" icon="octicon octicon-cloud-upload"></t:li.menu>
<t:li.menu cls="divider" modal="${pageContext.request.contextPath}/${it.path}/tree?action=new" icon="octicon octicon-plus" title="New">
</t:li.menu>
<t:li.menu modal="${pageContext.request.contextPath}/${it.path}/tree?action=new.artifact" icon="octicon octicon-file-code"
	title="Artifact"></t:li.menu>
<t:li.menu icon="octicon octicon-pulse" title="Analytics">
	<t:li.menu modal="${pageContext.request.contextPath}/${it.path}/tree?action=new.analytics.compute" icon=""
		title="Compute"></t:li.menu>
	<t:li.menu modal="${pageContext.request.contextPath}/${it.path}/tree?action=new.analytics.predict" icon=""
		title="Predict"></t:li.menu>
	<h6 class="dropdown-header m-0" style="color: #999;">Advanced
		Analytics</h6>
	<t:li.menu modal="${pageContext.request.contextPath}/${it.path}/tree?action=new.analytics.learn" icon="" title="Learn"></t:li.menu>
	<t:li.menu modal="${pageContext.request.contextPath}/${it.path}/tree?action=new.analytics.optimize.deterministic" icon=""
		title="Optimize (Deterministic)"></t:li.menu>
	<t:li.menu modal="${pageContext.request.contextPath}/${it.path}/tree?action=new.analytics.optimize.stochastic.1stage"
		icon="" title="Optimize (1-Stage Stochastic)"></t:li.menu>
	<h6 class="dropdown-header m-0" style="color: #999;">Extended
		Analytics</h6>
	<t:li.menu modal="${pageContext.request.contextPath}/${it.path}/tree?action=new.analytics.pareto" icon=""
		title="Compute Pareto Frontier"></t:li.menu>
</t:li.menu>

<t:li.menu cls="divider" modal="${pageContext.request.contextPath}/${it.path}/tree?action=save&path=${pageContext.request.getParameter('path')}" icon="fa fa-save"
	title="Save"></t:li.menu>
<t:li.menu modal="?action=properties"
	icon="octicon octicon-info" title="Properties"></t:li.menu>
<t:li.menu icon="octicon octicon-tools" title="Manage">
	<t:li.menu modal="?action=history" title="History"></t:li.menu>
	<li class="divider"></li>
	<t:li.menu modal="?action=copy" title="Copy"></t:li.menu>
	<t:li.menu modal="?action=move" title="Move"></t:li.menu>
	<t:li.menu modal="?action=rename" title="Rename"></t:li.menu>
	<li class="divider"></li>
	<t:li.menu modal="?action=delete" title="Delete"></t:li.menu>
</t:li.menu>
<t:li.menu cls="divider" modal="${pageContext.request.contextPath}/${it.path}/tree?action=run&path=${pageContext.request.getParameter('path')}"
	icon="octicon octicon-zap" title="Run"></t:li.menu>