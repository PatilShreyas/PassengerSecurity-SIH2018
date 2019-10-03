<?php

	require("Config.php");
	if(isset($_POST['uid']) && isset($_POST['uid'])){
	
		$alert_id = $_POST['uid'];
		$del = mysqli_query($conn,"DELETE FROM alerts WHERE alert_id = '$alert_id'");
		$query = "INSERT INTO alerts (alert_id) VALUES('$alert_id')";
		$res = mysqli_query($conn,$query);
		if($res){
			$data = [ 'status' => 'Success', 'Message' => 'Information Added Succesfully' ];
			header('Content-Type: application/json');
			$myJSON = json_encode($data);
			echo $myJSON;
		}
	}
	else{
		$data = [ 'status' => 'Failed', 'Message' => 'Connection Failed' ];
		header('Content-Type: application/json');
		$myJSON = json_encode($data);
		echo $myJSON;
	}

?>