<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<form action="${contextPath}/${it.path}/new" method="get">
	<input type="hidden" name="action" value="new"> <input
		type="hidden" id="new-location" name="location" value="">
	<div class="modal-content">
		<div class="modal-header bg-purple">
			<button type="button" class="close" data-dismiss="modal"
				aria-label="Close">
				<span aria-hidden="true">&times;</span>
			</button>
			<h4 class="modal-title" id="modalLabel">
				<i class="octicon octicon-plus mr-5"></i>New Artifact
			</h4>
		</div>
		<div class="modal-body">
			<div class="form-group">
				<label for="new-resource-name">Name</label>
				<div class=" input-group">
					<span class="input-group-addon"
						style="font-size: small; border-bottom-left-radius: 3px; border-top-left-radius: 3px;"
						id="new-location-addon"></span> <input type="text"
						class="form-control" id="new-name" name="name"
						placeholder="Enter resource name">
				</div>
			</div>
			<div class="form-group mb-0">
				<input type="hidden" id="new-type" name="type">
				<label for="">Artifact Type</label>
			</div>
			<div id="new-types"
				style="height: 300px !important; overflow-y: scroll; border: solid 1px rgb(210, 214, 222)"></div>

		</div>
		<div class="modal-footer">
			<button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
			<button type="submit" class="btn bg-purple">Construct</button>
		</div>
	</div>
	<script>
		$("#new-location").val($('#repository').jstree("get_selected"));
		$("#new-location-addon").text(
				"/" + $('#repository').jstree("get_selected") + "/");

		$('#new-types')
				.jstree(
						{
							"plugins" : [ "wholerow", "json_data" ],
							"core" : {
								"multiple" : false,
								"data" : {
									"url" : function(node) {
										if (node.id == '#') {
											return '${contextPath}/${it.path}/types.json?path='
											+ escape("http://dgms.io/ontologies/example#Artifact");
										} else {
											return '${contextPath}/${it.path}/types.json?path='
											+ escape(node.id);	
										}
										
									},
									"data" : function(node) {
										return {
											"id" : node.id
										};
									}
								}

							}
						}).on("select_node.jstree", function(evt, data) {
					$("#new-type").val(data.node.id);
				});
	</script>
</form>