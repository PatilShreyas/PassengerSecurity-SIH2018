<?php
	define("HOST","localhost");
	define("USER","shreyas");
	define("PASS","shreyas");
	define("DB","railway");
	
	$conn = mysqli_connect(HOST,USER,PASS,DB);
	
	if($conn){
		
	}
	else{
		$data = [ 'status' => 'Failed', 'Message' => 'Connection Not Established' ];
		header('Content-Type: application/json');
		$myJSON = json_encode($data);
		echo $myJSON;
	}
?>