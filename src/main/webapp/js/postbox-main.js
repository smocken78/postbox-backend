import "./postbox-open.js";

window.addEventListener("load", () => getEntities());


async function  getEntities() {
	document.querySelector("#result").innerHTML = 
	`<div class="spinner-border" role="status">
	  <span class="sr-only">Loading...</span>
	</div>`;	
	const response = await fetch (`/cst/service/postboxEntities`);
	const files = await response.json();

    let content = `<div class="row">
  			<div class="col-4">`;
	const options = { day: "2-digit", month: "2-digit", year: "numeric" };
	
	files.forEach((item) => {
		
		const date = new Date(item["insertation_dt"]);
	    const d = date.toLocaleDateString("de-DE", options);

	    
		content+= `
		
		<a href='#' id='${item["filename"]}'
		   onclick=`;
		
		content+= " openEmail(this.id) ";
		
		content+= ` class="list-group-item list-group-item-action flex-column align-items-start">
		    <div class="d-flex w-100 justify-content-between">
		      <h5 class="mb-1">${item["title"]}</h5>
		      <small>${d}</small>
		    </div>
		    <p class="mb-1">${item["filename"]}</p>
		    
		  </a>
		  `;
	
      });
      
      content+= `</div>
      <div class="col-8">
		   <div id="msg"></div>
		  </div>
	   </div>`
		  
     document.querySelector("#result").innerHTML = '';
     document.querySelector("#result").innerHTML = content;

}




