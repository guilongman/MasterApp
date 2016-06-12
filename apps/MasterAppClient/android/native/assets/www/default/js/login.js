
/* JavaScript content from js/login.js in folder common */
		// Load the SDK Asynchronously
      (function(d){
         var js, id = 'facebook-jssdk', ref = d.getElementsByTagName('script')[0];
         if (d.getElementById(id)) {return;}
         js = d.createElement('script'); js.id = id; js.async = true;
         js.src = "http://connect.facebook.net/en_US/all.js";
         ref.parentNode.insertBefore(js, ref);
       }(document));

      // Init the SDK upon load
      window.fbAsyncInit = function() {
        FB.init({
          appId      : '1649050102086054', // App ID
          channelUrl : 'http://'+window.location.hostname+'/channel', // Path to your Channel File
          status     : true, // check login status
          cookie     : true, // enable cookies to allow the server to access the session
          xfbml      : true  // parse XFBML
          
        });

        // listen for and handle auth.statusChange events
        FB.Event.subscribe('auth.statusChange', function(response) {
          if (response.authResponse) {
        	  // get user top 100 like pages
        	    FB.api('/me/likes?limit=100', function(response){
        		//get user likes, and redirect JSON to Watson API (Web Service)
        		console.log(response);
        		//Redirect user to main screen (chat) 
        		$("#main").load("pages/chat.html");
        		$("#mainmenu").load("pages/menu.html");
        	}, {scope: 'user_likes'})
        	
          } 
        });


        
        document.getElementById('auth-logoutlink').addEventListener('click', function(){
          FB.logout();
        }); 
      } 
      // respond to clicks on the login and logout links
      $('#auth-loginlink').click(function(){
    	  $("#login-geral").hide();
    	  $('.masterWrapper').show();
    	  $("#main").load("pages/chat.html");
    	  $("#mainmenu").load("pages/menu.html");
        
      });