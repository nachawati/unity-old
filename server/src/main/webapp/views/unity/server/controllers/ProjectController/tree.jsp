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
		<c:forEach var="pathComponent" items="${pathComponents}">
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
			<script
			src="${pageContext.request.contextPath}/assets/bower_components/json-editor/dist/jsoneditor.min.js"></script>
			
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
										var ii = 1;
										while (arr.length - ii >= 0 && !$('#repository').jstree(true).get_node(arr[arr.length - ii]))
											ii++;
										
										$('#repository').jstree(true).select_node(arr[arr.length - ii]);
									//$('#repository').jstree(true).get_node(i).state.selected = true;
									} catch (e){alert(e)}
								});
							});
			/*.bind("click",
							function(e, data) {
								
							})*/
			$("#repository").on("click", ".jstree-anchor", function(e) {
				document.location.href = "${pageContext.request.contextPath}/${it.project.pathWithNamespace}/tree/"
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
			
			try {
				var editor = ace.edit("editor");
				editor.setOptions({
					maxLines : Infinity,
					showPrintMargin : false
				});
	
				editor.setTheme("ace/theme/eclipse");
				var modelist = ace.require("ace/ext/modelist");
				var mode = modelist.getModeForPath("${file.name}").mode;
				editor.session.setMode(mode);
			} catch (e) {}
			
			try {
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
				
				var form = new JSONEditor(document.getElementById("form-view"),
						{
							ajax: true,
							schema: JSON.parse("${schema}")
						});
				form.setValue(${content});
				form.on('change',function() {
					$("#new-content").val(JSON.stringify(form.getValue(), null, 4));
				});
			} catch (e) {
			}
			
		</script>
	</jsp:attribute>
	<jsp:body>
	<c:choose>
		<c:when test="${file.directory}">
		<div class="box">
            <div class="box-header">
              <h3 class="box-title">
							<i class="fa fa-folder mr-15"></i>${file.name}</h3>
            </div>
            <div class="box-body no-padding">
            	<table class="table">
                	<tbody>
                		<c:forEach var="item"
									items="${file.getFiles(false).iterator()}">
	                		<tr>
	                			<td>
		                			<c:choose>
		                				<c:when test="${item.directory}">
		                					<i class="fa fa-folder ml-5 mr-10"></i>
		                				</c:when>
		                				<c:otherwise>
		                					<i class="fa fa-file-o ml-5 mr-10"></i>
		                				</c:otherwise>
	            			        </c:choose>
	            			        <c:choose>
	            			        	<c:when test="${not empty prefix}">
	            			        		<a
														href="${pageContext.request.contextPath}/${it.project.pathWithNamespace}/tree/${prefix}/${item.path}">${item.name}</a>
	            			        	</c:when>
	            			        	<c:otherwise>
	            			        		<a
														href="${pageContext.request.contextPath}/${it.project.pathWithNamespace}/tree/${it.project.pathWithNamespace}/${item.path}">${item.name}</a>
	            			        	</c:otherwise>
	            			        </c:choose>
            			        </td>
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
							class="fa fa-file-o mr-15"></i>${file.name}</li>
            		</ul>
            		<div class="tab-content no-padding">
            			<div class="active tab-pane" id="content"
							style="position: relative; -webkit-tap-highlight-color: rgba(0, 0, 0, 0);">
        			    		<c:choose>
        			    			<c:when test="${not empty schema}">
        			    				<div class="container-fluid">
        			    				<input type="hidden" id="new-content" name="content"
											value="">
        			    					<div id="form-view"></div>
        			    				</div>
        			    			</c:when>
        			    			<c:otherwise>
        			    			        			    	<div class="container-fluid" style="padding: 0;">
        			    				<div id="editor">${content}</div>
        			    					</div>
        			    			</c:otherwise>
        			    		</c:choose>
       			    	
       			    	</div>
            		</div>
          		</div>	
		</c:otherwise>
	
	</c:choose>
		
	</jsp:body>
</t:page>
