<%@page pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<t:page title="${it.project.nameWithNamespace}">
	<jsp:attribute name="head">
	
	<style>
#editor {
	height: 100%;
}

#editor-container {
	bottom: 0;
	left: 230px;
	right: 0;
	top: 101px;
	position: fixed;
}

.sidebar-collapse #editor-container {
	left: 50px;
}

@media (max-width: 768px) {
	#editor-container {
		left: 0px;
	}
	#context_menu {
		margin-top: 200px;
	}
}
</style>
	
</jsp:attribute>
	<jsp:attribute name="path">
		<t:project.path></t:project.path>
	</jsp:attribute>
	<jsp:attribute name="menu">
		<t:project.console.menu></t:project.console.menu>
	</jsp:attribute>
	<jsp:attribute name="sidebar">
		<t:project.sidebar active="console/"></t:project.sidebar>
	</jsp:attribute>
	<jsp:attribute name="scripts">
		<script
			src="${pageContext.request.contextPath}/assets/bower_components/ace-builds/src-noconflict/ace.js"
			type="text/javascript" charset="utf-8"></script>
			<script
			src="${pageContext.request.contextPath}/assets/bower_components/ace-builds/src-noconflict/ext-modelist.js"
			type="text/javascript" charset="utf-8"></script>
			<script>
				var editor = ace.edit("editor");
				editor.setOptions({
					showPrintMargin : false,
					fontSize : "12pt"

				});

				editor.setTheme("ace/theme/eclipse");
				var modelist = ace.require("ace/ext/modelist");
				editor.session.setMode("ace/mode/jsoniq");
			</script>
			</jsp:attribute>
	<jsp:body>
		<div id="editor-container">
				<div id="editor"></div>
			
		</div>
          		
		
	</jsp:body>
</t:page>
