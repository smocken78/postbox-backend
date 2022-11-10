import { html, render } from "../lib/lit-html-module.js";
import { navigationViewheight } from "../lib/defaults.js";


class PostboxClient extends HTMLElement {
  
  constructor() {
    super();

    this.heightCal = (100 - navigationViewheight);

  }
  
  onresize ()
   {
    
   }


  disconnectedCallback() {
  }

  async connectedCallback() {
    render (this.view(),this);
  }
  
  
	async getEntities() {
		document.querySelector("#result").innerHTML = 
		`<div class="spinner-border" role="status">
		  <span class="sr-only">Loading...</span>
		</div>`;	
		const response = await fetch (`/postbox/service/postboxEntities`);
		let files; 
		let content;
		try {
			files = await response.json();
			    content = `<div class="row">
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
	
		}
		catch {
			content = `Nix da`;
		}
			  
	     document.querySelector("#result").innerHTML = '';
	     document.querySelector("#result").innerHTML = content;
	
	}  
  
    
  view() {
     return html`
     
     <div class="row g-1 mt-1">
        <div class="col-md-9">
            <input id="searchField" placeholder="Search..." class="form-control">
        </div>
        
        <div class="col-md-3">
            <button type="submit" class="search btn btn-primary w-100 text-uppercase" @click=${() => {
                 this.getCustomerForVoucher(searchField.value);
               }}>Search</button>
        </div>
        
    </div>
     
     
       <br>
       <div id="result"></div>
 
   `;
  }

  async show(){

	spinner.classList.remove("spinner-border");
    }
}


customElements.define("postbox-main", PostboxClient);