import { html, render } from "../lib/lit-html-module.js";
import { replaceOrCreateTag } from "../lib/utils.js";
import { PostboxModal } from "./postbox-modal.js"

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
  //PostboxModal;
  	 const dialog = replaceOrCreateTag("postbox-modal");
     await document.body.appendChild(dialog);
     dialog.load('/postbox/service/postboxEmail?filename='+file); 		
		  
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
				    <p class="mb-1">${item["preview"]}</p>
				    
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