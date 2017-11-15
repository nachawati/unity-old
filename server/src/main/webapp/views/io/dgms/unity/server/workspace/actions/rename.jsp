<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<form action="${contextPath}/${it.path}" method="post">
	<input type="hidden" name="action" value="copy"> <input
		type="hidden" id="copy-target" name="copy-target" value="">
	<div class="modal-content">
		<div class="modal-header bg-purple">
			<button type="button" class="close" data-dismiss="modal"
				aria-label="Close">
				<span aria-hidden="true">&times;</span>
			</button>
			<h4 class="modal-title" id="modalLabel">Rename Resource</h4>
		</div>
		<div class="modal-body">
			<div class="form-group">
				<label for="location">Path</label><input type="text"
					class="form-control" id="rename-path" name="path" readonly>
			</div>
			<div class="form-group">
				<label for="location">New Name</label><input type="text"
					class="form-control" id="rename-name" name="name">
			</div>
		</div>
		<div class="modal-footer">
			<button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
			<button type="submit" class="btn bg-purple">Rename</button>
		</div>
	</div>
	<script>
		$("#rename-path").val($('#repository').jstree("get_selected"));
	</script>
</form>