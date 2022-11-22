import { html, render } from "../lib/lit-html-module.js";


class PostboxClient extends HTMLElement {
  
  constructor() {
    super();
  }
  
  onresize ()
   {
    
   }


  disconnectedCallback() {
  }

  async connectedCallback() {
    render (this.view(),this);
    this.getEntities();
    
  }
  
  async openEmail (file) {

		document.querySelector("#msg").innerHTML = 
		`<div class="spinner-border" role="status">
		  <span class="sr-only">Loading...</span>
		</div>`;
			const response = await fetch ('/postbox/service/postboxFile?filename='+file);
			const body = await response.text();
		     
	        document.querySelector("#msg").innerHTML = body;
		  
	};  
  
  async getEntities() {
		const response = await fetch (`/postbox/service/postboxEntities`);
		let files; 
		let content;
		let that = this;
		try {
			files = await response.json();
			    content = `<div class="row">`;
			const options = { day: "2-digit", month: "2-digit", year: "numeric" };
			
			if (files.length == 0) {
				content = `Nix da`;
			}
			else {
				files.forEach((item) => {
				
				const date = new Date(item["insertation_dt"]);
			    const d = date.toLocaleDateString("de-DE", options);
		
			    
				content+= `
				<div class="col-12">
				<a href='#' id='${item["filename"]}'
				  @click=${() => that.openEmail(this.id)} 
				  	class="list-group-item list-group-item-action flex-column align-items-start">
				    <div class="d-flex w-100 justify-content-between">
				      <h5 class="mb-1">${item["subject"]}</h5>
				      <small>${d}</small>
				    </div>
				    <p class="mb-1">${item["filename"]}</p>
				    
				  </a>
				  </div>
				  `;
			
		    });
		      
		      content+= `</div>
		      <div class="col-8">
				   <div id="msg"></div>
				  </div>
			   </div>`
			}
			
		
		}
		catch {
			content = `Nix da`;
		}
  
	     this.innerHTML ="";
	     this.innerHTML = content;
	
	}
	
    
  view() {
     return html`
     
     <div class="row g-1 mt-1">
        <div class="col-md-5">
        </div>
        
        <div class="col-md-2">
            <div class="spinner-border" role="status">
			  <span class="sr-only">Loading...</span>
			</div>
        </div>
        <div class="col-md-5">
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


customElements.define("postbox-client", PostboxClient);