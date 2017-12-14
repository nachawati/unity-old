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
		<script src="${pageContext.request.contextPath}/assets/bower_components/json-editor/dist/jsoneditor.min.js"></script>
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
							})
					.bind(
							"changed.jstree",
							function(e, data) {
								var node = $(e.target).closest("li");
								document.location.href = "${pageContext.request.contextPath}/${it.project.pathWithNamespace}/tree/"
										+ node[0].id;
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
							})
					.bind(
							"changed.jstree",
							function(e, data) {
								var node = $(e.target).closest("li");
								document.location.href = "${pageContext.request.contextPath}/${it.project.pathWithNamespace}/tree/"
										+ node[0].id;
							});
			
			JSONEditor.defaults.theme = 'bootstrap3';
			JSONEditor.defaults.iconlib = 'fontawesome4';
			
			JSONEditor.defaults.editors.object = JSONEditor.defaults.editors.object.extend({
				  build: function() {
				  	this._super();
				  	
				  	 this.importjson_controls = this.theme.getHeaderButtonHolder();
				     this.title.appendChild(this.importjson_controls);
				  	
				  	this.importjson_button = this.getButton('Import','import','Import');
				  	$(this.importjson_button).attr("data-toggle","modal");
				  	$(this.importjson_button).attr("data-target","#modal");
				  	$(this.importjson_button).attr("href","${pageContext.request.contextPath}/${it.project.pathWithNamespace}/tree?action=import&path=" + encodeURI(this.path));
				      this.importjson_controls.appendChild(this.importjson_button);
				  	
				  }
				});
			
			try {
				var form = new JSONEditor(document.getElementById("form-view"),
						{
							ajax: true,
							schema: JSON.parse("${schema}")
						});
				form.on('change',function() {
					$("#new-content").val(JSON.stringify(form.getValue(), null, 4));
				});
			} catch (e) {
			}
			/*
			.on('loaded.jstree', function() {
				$('#artifacts').jstree(true).load_node(${navComponents}, function () {
					${navComponents}.forEach(function(i) { 
						$('#artifacts').jstree(true).open_node(i);
						//$('#kbnav').jstree(true).get_node(i).state.selected = true;
					});
				});
			})*/;
		</script>
	</jsp:attribute>
	<jsp:body>
	
	<form method="post" action="${pageContext.request.contextPath}/${it.path}/new">
		<input type="hidden" name="name" value="${it.request.getParameter("name")}">
		<input type="hidden" name="location" value="${it.request.getParameter("location")}">
		<input type="hidden" name="type" value="${it.request.getParameter("type")}">
		<input type="hidden" id="new-content" name="content" value="">
	<div class="nav-tabs-custom">
		<ul class="nav nav-tabs pull-right ui-sortable-handle bg-gray">
			<li class="active"><a href="#content" data-toggle="tab">Content</a></li>
			<li class="pull-left header"><i class="fa fa-file-text-o mr-10"></i>${it.request.getParameter("name")}</li>
		</ul>
		<div class="tab-content no-padding">
			<div class="active tab-pane" id="content" style="position: relative; -webkit-tap-highlight-color: rgba(0, 0, 0, 0);">
				<div class="container-fluid">
					<div id="form-view"></div>
					<input type="submit" value="Save"/>
				</div>
			</div>
		</div>
	</div>
	</form>
    	
	</jsp:body>
</t:page>
