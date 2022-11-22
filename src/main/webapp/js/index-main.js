import "./index-openEmail.js";

window.addEventListener("load", () => getEntities());


async function  getEntities() {
	document.querySelector("#result").innerHTML = 
	`<div class="spinner-border" role="status">
	  <span class="sr-only">Loading...</span>
	</div>`;	
	const response = await fetch (`/postbox/service/postboxEntities`);
	let files; 
	let content;
	try {
		files = await response.json();
		    content = `<div class="row">`;
	const options = { day: "2-digit", month: "2-digit", year: "numeric" };
	
	files.forEach((item) => {
		
		const date = new Date(item["insertation_dt"]);
	    const d = date.toLocaleDateString("de-DE", options);

		content+= `
		<div class="col-12">
		<a href='#' id='${item["filename"]}'
		   onclick=`;
		
		content+= ' openEmail(this.id)';
		
		content+= ` class="list-group-item list-group-item-action flex-column align-items-start">
		    <div class="d-flex w-100 justify-content-between">
		      <h5 class="mb-1">${item["subject"]}</h5>
		      <small>${d}</small>
		    </div>
		    <p class="mb-1" data-bs-toggle="modal" data-bs-target="#emailModal">${item["filename"]}</p>
		    
		  </a>
		  </div>
		  `;
		content+="</div>"
      });
      


	}
	catch {
		content = `Nix da`;
	}
		  
     document.querySelector("#result").innerHTML = '';
     document.querySelector("#result").innerHTML = content;


}




