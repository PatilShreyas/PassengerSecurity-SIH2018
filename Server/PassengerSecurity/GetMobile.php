<?php

	require_once('Config.php');

	$uid = $_POST['aadhaarNo'];
	$query = "SELECT MOBILE_NO, NAME FROM AADHAAR WHERE UID_NO='$uid'";
	
	$res = mysqli_query($conn,$query);
	$mobNo = null;
	if (mysqli_num_rows($res) > 0) {
		// output data of each row
		while($row = mysqli_fetch_assoc($res)) {
			$mobNo = $row["MOBILE_NO"];
			$name = $row["NAME"];
		}
		$data = [ 'status' => 'Success', 'Message' => 'Retrieved Successfully' , 'MOBILE_NO' => $mobNo, 'NAME' => $name];
		header('Content-Type: application/json');
		$myJSON = json_encode($data);
		echo $myJSON;
	}
	else{
		$data = [ 'status' => 'Failed', 'Message' => 'Invalid Aadhaar Number' ];
		header('Content-Type: application/json');
		$myJSON = json_encode($data);
		echo $myJSON;
	}
?>