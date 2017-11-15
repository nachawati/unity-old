<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<form action="${contextPath}/${it.path}/copy" method="post">
	<input type="hidden" name="action" value="copy"> <input
		type="hidden" id="copy-target" name="copy-target" value="">
	<div class="modal-content">
		<div class="modal-header bg-purple">
			<button type="button" class="close" data-dismiss="modal"
				aria-label="Close">
				<span aria-hidden="true">&times;</span>
			</button>
			<h4 class="modal-title" id="modalLabel">
				<i class="octicon octicon-plus mr-5"></i>Copy Artifact
			</h4>
		</div>
		<div class="modal-body">
			<div class="form-group">
				<label for="location">Source</label><input type="text"
					class="form-control" id="copy-source" name="copy-source"
					placeholder="Enter source" readonly>
			</div>
			<div class="row">
				<div class="col-md-12">
					<div class="form-group mb-0">
						<input type="hidden" id="artifactType" name="artifactType">
						<label for="">Target</label>
					</div>
					<div id="copy-target-tree"
						style="max-height: 300px !important; overflow-y: scroll; border: solid 1px rgb(210, 214, 222)"></div>
				</div>
			</div>
		</div>
		<div class="modal-footer">
			<button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
			<button type="submit" class="btn bg-purple">Copy</button>
		</div>
	</div>
	<script>
		$("#copy-source").val($('#repository').jstree("get_selected"));

		$('#copy-target-tree')
				.jstree(
						{
							"plugins" : [ "wholerow", "json_data" ],
							"core" : {
								"multiple" : false,
								"data" : {
									"url" : function(node) {
										return '${contextPath}/${it.workspace.pathWithNamespace}/tree.json?path='
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
							$("#copy-target")
									.val(
											data.instance
													.get_node(data.selected[0]).id);
						});
	</script>
</form>