import { html, render } from "../lib/lit-html-module.js";
import { Modal } from "../lib/bootstrap.esm.js"


export class PostboxModalTestData extends HTMLElement {
  
  constructor() {
    super();
  }
  
  onresize ()
   {
    
   }


  disconnectedCallback() {
  }

  connectedCallback() {
     this.classList.add ("modal");
     this.classList.add ("fade")
     this.setAttribute("role","dialog");
     this.setAttribute("aria-hidden","false");
     this.setAttribute("tabindex","-1");
     //this.setAttribute("data-bs-target","#staticBackdrop");
     this.setAttribute("data-bs-backdrop","static");
     this.setAttribute("data-bs-keyboard","false");
     
     render (this.view(), this);
     this.modal = Modal.getOrCreateInstance(this);
     this.modal.s
     this.modal.show();
  }
  
  close ()
  {
     this.modal.dispose();
     this.parentElement.removeChild(this);
  }
  
  
  view() {
     return html`
		  <div class="modal-dialog" role="document">
		    <div class="modal-content">
		      <div class="modal-header">
		        <div id="emailModalHeader"><h1 class="modal-title fs-5" id="staticBackdropLabel">Test Email generieren</h1></div>
		      </div>
		      <div class="modal-body">
		        <div id="emailModalBody"
		        	
		        </div>
		        <br>
		        <div id="emailModalBtn">
		        	<button id="addTestData" type="button" class="btn btn-lg mb-3 w-100 text-uppercase" style="background-color:#E95D0F;" @click=${() => { this.sendEmail() } }>Email senden</button
		        </div>
		      </div>
		      <div class="modal-footer">
		        <button type="button" class="btn-close" aria-label="Close" @click=${(e) => this.close()}>
		      </div>
		    </div>
		  </div>`;
  }
  
   async sendEmail ()
  {
     try {
        const request = await fetch ("/service/addTestEmail");

        if (request.status == 200) {
           document.getElementById("emailModalBody").innerHTML="Email versendet";
           document.getElementById("emailModalBtn").innerHTML="";
           
        }
        else {
          this.show_error (`Laden fehlgeschlagen: (${request.status}): ${request.statusText}`);
        }
      } catch(_e) {
         this.show_error (`Laden fehlgeschlagen: ${_e}`);
      }
  }
  
  close ()
  {
     this.modal.dispose();
     this.parentElement.removeChild(this);
  }
    
  show_error (error)
  {
     document.getElementById("emailModalHeader").innerHTML="Fehler";
     document.getElementById("emailModalBody").innerHTML="";
     document.getElementById("emailModalBtn").innerHTML="";
     const div = document.getElementById("loading-error");
     div.innerText = error;
     div.classList.remove("d-none");
  }
	
}


customElements.define("postbox-modal-testdata", PostboxModalTestData);
