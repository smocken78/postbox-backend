async function login (user,password) {


		document.querySelector("#msg").innerHTML = 
		`<div class="spinner-border" role="status">
		  <span class="sr-only">Loading...</span>
		</div>`;
		
		const config = {
	        method: 'POST',
	        headers: {
	            'Accept': 'application/json',
	            'Content-Type': 'application/json',
	        },
	        body: "email="+user+"&password="+password
    	}
		
		const response = await fetch ('/postbox/login',config);
		
		if (response.ok) {
			console.log("Okay");
		}
		else {
			console.log("Nöööö");
		    document.querySelector("#msg").innerHTML = `<div>Das hat nicht funktioniert</div>`;
		}

		  
};

