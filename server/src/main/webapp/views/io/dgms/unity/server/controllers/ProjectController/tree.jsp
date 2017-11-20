<%@page pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<t:page title="${it.project.nameWithNamespace}">
	<jsp:attribute name="head">
	<style>
#repository-tab {
	overflow: auto;
	padding-bottom: 100px;
}

#ontology-tab {
	overflow: auto;
	padding-bottom: 100px;
}
</style>
</jsp:attribute>
	<jsp:attribute name="path">
		<t:project.path></t:project.path>
	</jsp:attribute>
	<jsp:attribute name="breadcrumbs">
		<li><a href="${pageContext.request.contextPath}/${it.path}/kb"><i
				class="octicon octicon-repo"></i></a></li>
		<c:forEach var="pathComponent" items="${it.pathComponents}">
			<li><a
				href="${pageContext.request.contextPath}/${it.project.pathWithNamespace}/${pathComponent.value}">${pathComponent.key}</a></li>
		</c:forEach>
	</jsp:attribute>
	<jsp:attribute name="menu">
		<t:project.repository.menu></t:project.repository.menu>
	</jsp:attribute>
	<jsp:attribute name="sidebar">
		<t:project.sidebar active="tree/"></t:project.sidebar>
	</jsp:attribute>
	<jsp:attribute name="sidenav">
		<div class="nav-tabs-custom">
     		<ul class="nav nav-tabs">
       			<li class="active"><a href="#repository-tab"
					data-toggle="tab">Repository</a></li>
				<li><a href="#ontology-tab" data-toggle="tab">Ontology</a></li>
     		</ul>
     		<div class="tab-content">
     			<div class="active tab-pane" id="repository-tab">
     				<div id="repository"></div>
     			</div>
     			<div class="tab-pane" id="ontology-tab">
     				<div id="ontology"></div>
     			</div>
     		</div>
   		</div>
	</jsp:attribute>
	<jsp:attribute name="scripts">
	<script
			src="${pageContext.request.contextPath}/assets/bower_components/ace-builds/src-noconflict/ace.js"
			type="text/javascript" charset="utf-8"></script>
			<script
			src="${pageContext.request.contextPath}/assets/bower_components/ace-builds/src-noconflict/ext-modelist.js"
			type="text/javascript" charset="utf-8"></script>
		<script>
			$('#repository')
					.jstree(
							{
								"plugins" : [ "wholerow", "json_data" ],
								"core" : {
									"multiple" : false,
									"data" : {
										"url" : function(node) {
											return '${pageContext.request.contextPath}/${it.project.pathWithNamespace}/tree.json?path='
													+ escape(node.id);
										},
										"data" : function(node) {
											return {
												"id" : node.id
											};
										}
									}
								}
							}).on('loaded.jstree', function() {
								$('#repository').jstree(true).load_node(${navComponents}, function () {
									var arr = ${navComponents};
									arr.forEach( function(i) { 
										$('#repository').jstree(true).open_node(i);
									});
									try {
										$('#repository').jstree(true).select_node(arr[arr.length - 1]);
									//$('#repository').jstree(true).get_node(i).state.selected = true;
									} catch (e){}
								});
							});
			/*.bind("click",
							function(e, data) {
								
							})*/
			$("#repository").on("click", ".jstree-anchor", function(e) {
				document.location.href = "${pageContext.request.contextPath}/${it.project.pathWithNamespace}/tree?path="
					+ $("#repository").jstree(true).get_node($(this)).id;
				
				});
			$('#ontology')
					.jstree(
							{
								"plugins" : [ "wholerow", "json_data" ],
								"core" : {
									"multiple" : false,
									"data" : {
										"url" : function(node) {
											return '${pageContext.request.contextPath}/${it.project.pathWithNamespace}/ontology.json?path='
													+ escape(node.id);
										},
										"data" : function(node) {
											return {
												"id" : node.id
											};
										}
									}
								}
							});
			
			var editor = ace.edit("editor");
			editor.setOptions({
				maxLines : Infinity,
				showPrintMargin : false
			});

			editor.setTheme("ace/theme/eclipse");
			var modelist = ace.require("ace/ext/modelist");
			var mode = modelist.getModeForPath("${it.request.getParameter("path")}").mode;
			editor.session.setMode(mode);
		</script>
	</jsp:attribute>
	<jsp:body>
	<c:choose>
		<c:when test="${file.directory}">
		<div class="box">
            <div class="box-header">
              <h3 class="box-title">${file.name}</h3>
            </div>
            <div class="box-body no-padding">
              <table class="table">
                <tbody>
                <c:forEach var="item"
									items="${file.getFiles(false).iterator()}">
                <tr>
                  <td><a
											href="${pageContext.request.contextPath}/${project.pathWithNamespace}/${item.path}">${item.name}</a></td>
                </tr>
              
                </c:forEach>
                
              </tbody>
				</table>
            </div>
          </div>
		
		</c:when>
		<c:otherwise>
			<div class="nav-tabs-custom">
            		<ul
						class="nav nav-tabs pull-right ui-sortable-handle bg-gray">
              			<li class="active"><a href="#content"
							data-toggle="tab">Content</a></li>
              			<li class="pull-left header"><i
							class="fa fa-file-text-o mr-10"></i>${it.request.getParameter("path")}</li>
            		</ul>
            		<div class="tab-content no-padding">
            			<div class="active tab-pane" id="content"
							style="position: relative; -webkit-tap-highlight-color: rgba(0, 0, 0, 0);">
            			            											<div class="container-fluid"
								style="padding: 0;">
            			            											<div id="editor">${content}</div>
            											</div>
         									</div>
              			
            		</div>
          		</div>	
		</c:otherwise>
	
	</c:choose>
		
	</jsp:body>
</t:page>
