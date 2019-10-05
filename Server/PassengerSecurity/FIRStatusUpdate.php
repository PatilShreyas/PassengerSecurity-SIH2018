<!doctype html>
<html>
	<head>
		<title>FIR Details</title>
		<meta charset="utf-8">
		<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

		<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
		<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
		<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>
		<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>
	</head>
	<body>
<?php

	require_once('Config.php');
	
	$firSe = $_POST['firSe'];
	$Status = $_POST['status'];
	
	$stringUrl = "ShowFirDetails.php?firNo=$firSe";

	$query = "UPDATE FIR SET STATUS ='$Status' Where FIR_NO ='$firSe'";
	 
	$res = mysqli_query($conn,$query);
	if ($res) {
		$alertStyle = "alert-primary";
		switch ($Status) {
			case "APPROVED" : $alertStyle = "alert-success"; break;
			case "PENDING" : $alertStyle = "alert-warning"; break;
			case "REJECTED" : $alertStyle = "alert-danger"; break;
		}
		
		?>
		<div class="container d-block mx-auto m-5" style="width:fit-content">
			<div class="alert alert-info" role="alert">
				  Status Updated Successfully!
			</div>
			<div class="alert <?php echo "$alertStyle"; ?>" role="alert">
			  <div class="h5">FIR Number : <?php echo "FIR/GRP/$firSe" ?></div>
			  <div class="h5">Status: <?php echo "<b>$Status</b>" ?></div>
			  
			</div>
			<p>Redirecting you to previous page in 3 seconds...</p>
			
			<script>
				    window.setTimeout(function(){
						window.location.href = <?php echo "\"$stringUrl\""; ?>;

					}, 3000);
			</script>
		</div>
		<?php
	}

?>
	</body>
</html>
	