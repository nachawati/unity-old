<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<div class="modal-content">
	<div class="modal-header bg-purple">
		<button type="button" class="close" data-dismiss="modal"
			aria-label="Close">
			<span aria-hidden="true">&times;</span>
		</button>
		<h4 class="modal-title" id="modalLabel">
			<i class="octicon octicon-zap mr-5"></i>Execute
		</h4>
	</div>
	<div class="modal-body">
		<div class="row">
			<div class="col-md-12">
				<div id="results">
					<div style="padding: 100px; text-align: center;">
						<i class="fa fa-spinner fa-spin fa-5x fa-fw"></i>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="modal-footer">
		<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
	</div>
</div>
<script>
	$.ajax({
		url : "${pageContext.request.contextPath}/${it.path}/console",
		method : "POST",
		data : editor.getValue(),
		success : function(data) {
			$("#results").text(data);
		},
		error : function(data) {
			$("#results").text(data);
		}
	});
</script>
