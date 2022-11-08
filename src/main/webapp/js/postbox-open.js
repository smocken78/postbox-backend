async function openEmail (file) {


		document.querySelector("#msg").innerHTML = 
		`<div class="spinner-border" role="status">
		  <span class="sr-only">Loading...</span>
		</div>`;
			const response = await fetch ('/cst/service/postboxFile?filename='+file);
			const body = await response.text();
		     
	        document.querySelector("#msg").innerHTML = body;
		  
};

