<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<div class="modal-content">
	<div class="modal-header bg-purple">
		<button type="button" class="close" data-dismiss="modal"
			aria-label="Close">
			<span aria-hidden="true">&times;</span>
		</button>
		<h4 class="modal-title" id="modalLabel">
			<i class="octicon octicon-plus mr-5"></i>Import JSON
		</h4>
	</div>
	<div class="modal-body">
		<div class="row">
			<div class="col-md-12">
				<div class="form-group mb-0">Resource</div>
				<div id="import-tree"
					style="max-height: 300px !important; overflow-y: scroll; border: solid 1px rgb(210, 214, 222)"></div>
			</div>
		</div>
	</div>
	<div class="modal-footer">
		<button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
		<button type="button" class="btn bg-purple" id="import-button">Import</button>
	</div>
</div>
<script>
		$("#import-button").click(function() {
			$.get("${pageContext.request.contextPath}/${it.project.pathWithNamespace}/file?path=" + $('#import-tree').jstree("get_selected"), function(data){
				try {
					var f = form.getEditor("${it.request.getParameter("path")}");
				f.setValue(JSON.parse(data));
				} catch (e) {}
				 $('#modal').modal('toggle');
			});
		});
	
		$('#import-tree')
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
						});
	</script>
</form>