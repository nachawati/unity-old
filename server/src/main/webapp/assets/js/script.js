$("#modal").on("show.bs.modal", function(e) {
	var link = $(e.relatedTarget);
	$(this).find(".modal-dialog").empty();
	$(this).find(".modal-dialog").load(link.attr("href"));
});
