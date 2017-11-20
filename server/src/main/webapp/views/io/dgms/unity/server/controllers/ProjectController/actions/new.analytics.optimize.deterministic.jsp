<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<form action="${pageContext.request.contextPath}/${it.path}/new" method="get">
	<input type="hidden" name="action" value="new"> <input
		type="hidden" id="new-location" name="location" value=""> <input
		type="hidden" id="new-type" name="type"
		value="http://dgms.io/ontologies/example#Deterministic">

	<div class="modal-content">
		<div class="modal-header bg-purple">
			<button type="button" class="close" data-dismiss="modal"
				aria-label="Close">
				<span aria-hidden="true">&times;</span>
			</button>
			<h4 class="modal-title" id="modalLabel">
				<i class="octicon octicon-plus mr-5"></i>New Deterministic
				Optimization Input
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
	</script>
</form>