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
  
  		let modal = html`
  		<div class="modal fade" id="emailModal" tabindex="-1" aria-labelledby="emailModal" aria-hidden="true">
		  <div class="modal-dialog">
		    <div class="modal-content">
		      <div class="modal-header">
		        <div id="emailModalHeader"><h1 class="modal-title fs-5" id="staticBackdropLabel">Loading</h1></div>
		        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
		      </div>
		      <div class="modal-body">
		        <div id="emailModalBody"><span class="sr-only">Loading...</span></div>
		      </div>
		      <div class="modal-footer">
		        <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
		      </div>
		    </div>
		  </div>
		</div>
  		`;
  		render(modal,document.querySelector("#body"));
  		
		
		const response = await fetch ('/postbox/service/postboxEmail?filename='+file);
		const body = await response.text();
		
		render(html`<p class="text-center"><h2>${body["subject"]}</h2></p>`,document.querySelector("#emailModalHeader"))
		     
		  
	};  
  
  async getEntities() {
		const response = await fetch (`/postbox/service/postboxEntities`);
		let files; 
		
		try {
			files = await response.json();
			const options = { day: "2-digit", month: "2-digit", year: "numeric" };
			
			if (files.length == 0) {
				document.querySelector("#result").innerHTML = `Nix da`;
			}
			else {
			var arr = [];
			arr.push(html `<div class="row">`);
			files.forEach((item) => {
				
				const date = new Date(item["insertation_dt"]);
			    const d = date.toLocaleDateString("de-DE", options);
		    	
				arr.push(html `
				<div class="col-12">
				<a href='#' id='${item["filename"]}'
				  @click=${(event) => {
                 	event.preventDefault();
                 	this.openEmail(item["filename"]);
               			}} 
               			 class="list-group-item list-group-item-action flex-column align-items-start">
				    <div class="d-flex w-100 justify-content-between">
				      <h5 class="mb-1">${item["subject"]}</h5>
				      <small>${d}</small>
				    </div>
				    <p class="mb-1">${item["filename"]}</p>
				    
				  </a>
				  </div>
				  `);
			
		    });
		      
		     arr.push(html `</div>
		      <div class="col-8">
				   <div id="msg"></div>
				  </div>
			   </div>`);
			   render(arr,document.querySelector("#result"));
			}
			
		
		}
		catch (e){
			console.log(e);
		    document.querySelector("#result").innerHTML = 'Fehler';
		}
  

	
	}
	
    
  view() {
     return html`
     
     <div id="result">
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
     </div>
 
   `;
  }

  async show(){

	spinner.classList.remove("spinner-border");
    }
}


customElements.define("postbox-client", PostboxClient);