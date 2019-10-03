<?php
error_reporting(0);

$alert_id = $_GET["alert_id"];

$response = file_get_contents('https://passenger-security-61486.firebaseio.com/user/'."$alert_id".'.json');
$var = $response;

$myJSON = json_decode($var,true);
$latitude= $myJSON['alert']['latitude'];
$longitude= $myJSON['alert']['longitude'];
$time = $myJSON['alert']['time'];
$message = $myJSON['alert']['message'];
$name = $myJSON['name'];
$mobNo =$myJSON['phoneNumber'];

$voiceUrl = null;
$picUrl = null;

$voiceUrl= $myJSON['alert']['voiceUrl'];
$picUrl = $myJSON['alert']['picUrl'];


?>

<html>
	<head>
		<title>Alert Details</title>
		<meta charset="utf-8">
		<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

		<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
		<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
		<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>
		<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>
	
		<style>
		  #map {
			height: 100%;
			width: 100%;
		  }
		 
		  html, body {
			height: 100%;
			margin: 0;
			padding: 0;
		  }
		</style>
		<script>

		  function initMap() {
			var myLatLng = {lat: <?php echo $latitude ?>, lng: <?php echo $longitude ?>};

			var map = new google.maps.Map(document.getElementById('map'), {
			  zoom: 16,
			  center: myLatLng
			});

			var marker = new google.maps.Marker({
			  position: myLatLng,
			  map: map,
			  title: ''
			});
		  }
    </script>
    <script async defer
		src="https://maps.googleapis.com/maps/api/js?key=AIzaSyBylRbLPrGNuAlJ9Oue3-d6FOQSFu90WF8&callback=initMap">
    </script>
	</head>

<script>
$(".my_audio").trigger('load');

function play_audio(task) {
      if(task == 'play'){
           $(".my_audio").trigger('play');
      }
      if(task == 'stop'){
           $(".my_audio").trigger('pause');
           $(".my_audio").prop("currentTime",0);
      }
 }
</script>
<body>
	<div class="container">
			
		<div class="h1 text-center">Alert Details</div>
		<div class="card text-center m-3 d-block mx-auto" style="width:fit-content">
			<div class="card-header">
				<div class="h3 text-center m-3">Person Details</div>
			</div>
			<div class="card-body p-5">
			<table class="table table-responsive">
				<tr>
					<td>Name : </td>
					<td><b><?php echo $name;?></b></td>
				<tr>
				<tr>
					<td>Mobile Number : </td>
					<td><b><?php echo $mobNo;?></b></td>
				<tr>
				<tr>
					<td>Time : </td>
					<td><b><?php echo $time;?></b></td>
				<tr>
				<tr>
					<td>Message : </td>
					<td><b><?php echo $message;?></b></td>
				<tr>
			</table>
			</div>
		</div>
		
		<div class="row">
			<div class="col-6">
				<div class="card text-center m-5">
					<div class="card-header">
						<div class="h3 text-center m-3">Media</div>
						<a href="<?php echo $picUrl; ?>" target="_blank">
						<?php if ($picUrl != null) {?>
							<img src="<?php echo $picUrl; ?>" alt="crime scene" class="card-img-top"></img>
						<?php } else { ?>	
							<div class="h5 text-danger">IMAGE UNAVAILABLE!</div>
						<?php } ?>	
						</a>
					</div>
					<div class="card-body">
						<h4>Voice Recording :</h4>
						
						<?php if ($voiceUrl != null) {?>
							<audio class="my_audio" controls preload="none">
								<source src=<?php echo $voiceUrl ?> type="audio/aac">
								<button onClick="play_audio('play')">PLAY</button>
								<button onClick="play_audio('stop')">STOP</button>
							</audio>
						<?php } else { ?>	
							<div class="h5 text-danger">VOICE RECORDING UNAVAILABLE!</div>
						<?php } ?>	
						
					</div>
				</div>
			</div>
			
			<div class="col-6">
				<div class="card text-center m-5">
					<div class="card-header">
						<div class="h3 text-center m-3">Location</div>
						
					</div>
					<div class="card-body" style="height:500px">
						<div id="map" style="width:fit-content%; height:90%"></div>
						<div class="h5 m-2"><a class="link" href="<?php echo "http://maps.google.com/maps?q=loc:$latitude,$longitude" ?>" target="_blank">VIEW ON GOOGLE MAPS</a></div>
					
					</div>
				</div>
			</div>
			
			
		</div>
	</div>		
</body>
</html>