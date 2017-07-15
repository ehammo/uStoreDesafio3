$(document).ready(function(){
    $('.uploadBtn').click(function() {
			var data = new FormData($('.uploadForm')[0]);
			var opts = {
				url: "http://localhost:8080/Desafio3-1/rest/upload",
				data: data,
				cache: false,
				contentType: false,
				processData: false,
				type: 'POST',
				success: function(data){
						alert(data);
				}
			};

		var uploadData = $.ajax(opts)
		uploadData.error(function(errorData) { alert("Algo deu errado"); });

    });
})