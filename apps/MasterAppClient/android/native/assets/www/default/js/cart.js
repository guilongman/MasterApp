
/* JavaScript content from js/cart.js in folder common */
var cont = 0;
var html = "";

function appendHtml(){
		
	if (cont == 0){
		html = "<li class='collection-item avatar'><img src='images/coca-cola-verde.jpg' alt='' class='circle'><span class='title' style='color: black !important;'><b>Coca-Cola Verde</b></span><p style='color: grey;'>R$5,39 <br><br>7894900060010</p><div style='width: 30px;' class='price'><input type='number'/></div></li>";
		$( ".collection" ).append(html);
	}
	
	else if (cont == 1){
		html = "<li class='collection-item avatar'><img src='images/coca-cola.jpg' alt='' class='circle'><span class='title' style='color: black !important;'><b>Coca-Cola</b></span><p style='color: grey;'>R$5,39 <br><br>7894900060010</p><div style='width: 30px;' class='price'><input type='number'/></div></li>";
		$( ".collection" ).append(html);
	}
	
	cont++;
}

var pictureSource;   // picture source
var destinationType; // sets the format of returned value

// Wait for device API libraries to load
//
document.addEventListener("deviceready",onDeviceReady,false);

// device APIs are available
//
function onDeviceReady() {
    pictureSource=navigator.camera.PictureSourceType;
    destinationType=navigator.camera.DestinationType;
}

// Called when a photo is successfully retrieved
//
function onPhotoDataSuccess(imageData) {
  appendHtml();
}

// A button will call this function
//
function capturePhoto() {
  // Take picture using device camera and retrieve image as base64-encoded string
  navigator.camera.getPicture(onPhotoDataSuccess, onFail, { quality: 50,
    destinationType: destinationType.DATA_URL });
}

// Called if something bad happens.
//
function onFail(message) {
  alert('Failed because: ' + message);
}