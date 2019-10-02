<?php

	require("Config.php");
	
	$uid = $_POST['uid'];
	$name = $_POST['name'];
	$mobNo = $_POST['mobNo'];
	$aadhaarNo = $_POST['aadhaarNo'];
	$fcmToken = $_POST['FcmToken'];
	
	$query = "INSERT INTO USER (UID,NAME,MOBILE_NO,AADHAAR_NO,FCM_TOKEN) VALUES('$uid','$name','$mobNo','$aadhaarNo','$fcmToken')";

	
	$res = mysqli_query($conn,$query);

	
	if($res){
		$data = [ 'status' => 'Success', 'Message' => 'User added Succesfully' ];
		header('Content-Type: application/json');
		$myJSON = json_encode($data);
		echo $myJSON;
	}
	
	else{
		$data = [ 'status' => 'Failed', 'Message' => 'Connection Failed' ];
		header('Content-Type: application/json');
		$myJSON = json_encode($data);
		echo $myJSON;
	}

?>