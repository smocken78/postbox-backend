async function openEmail (file) {


		document.querySelector("#msg").innerHTML = 
		`<div class="spinner-border" role="status">
		  <span class="sr-only">Loading...</span>
		</div>`;
			const response = await fetch ('/postbox/service/postboxEmail?filename='+file);
			const json = await response.json();
			
			let text;
			let html;
			let dwnld;
			
			json.forEach((item) => {
				
				if (item["content-type"].match("text/plain")) {
					text = item["content"]
				}
				else if (item["content-type"].match("text/html")) {
					html = item["content"]
				}
				else if (item["content-type"].match("application/octet-stream")) {
					
				}
				
				
			 });
		     
	        document.querySelector("#msg").innerHTML = text;
		  
};

