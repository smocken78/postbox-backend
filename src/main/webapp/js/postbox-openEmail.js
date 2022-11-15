async function openEmail (file) {


		document.querySelector("#msg").innerHTML = 
		`<div class="spinner-border" role="status">
		  <span class="sr-only">Loading...</span>
		</div>`;
			const response = await fetch ('/postbox/service/postboxEmail?filename='+file);
			const json = await response.json();
			
			let text;
			let html;
			let dwnld = '<div class="row">';
			
			json.forEach((item) => {
				
				if (item["content-type"].match("text/plain")) {
					text = item["content"]
				}
				else if (item["content-type"].match("text/html")) {
					html = item["content"]
				}
				else {
					var filenameRegex = /name[^;=\n]*=((['"]).*?\2|[^;\n]*)/;
					var splitted = item["content-type"].split(";");
					var matches = filenameRegex.exec(item["content-type"]);
            		if (matches != null && matches[1]) {
            			filename = matches[1].replace(/['"]/g, '');
          			
            			dwnld += `<div class="col-4">
            			<a download="${filename}" type="button" class="btn btn-dark btn-lg" href="data:${splitted[0].trim()};base64,${item["content"]}"><i class="bi bi-filetype-xlsx"></i> Download</a>
            			</div>`

					}
				}
			 });
			 
			 dwnld += '</div>';
			 
		     
	        document.querySelector("#msg").innerHTML = dwnld;
		  
};

