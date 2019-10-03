<?php

	require_once('Config.php');
	
    $uid = $_POST['UID'];

	$query = "SELECT CONCAT('FIR/GRP/', FIR_NO) AS FIR_NO, TIMESTAMP ,STATUS FROM FIR WHERE UID='$uid'";
	
	$res = mysqli_query($conn,$query);
	$firNo = null;
	$timeStamp = null;
	$status = null;
	$address = null;
	
	if (mysqli_num_rows($res) > 0) {

			$data = array();
			while($row = mysqli_fetch_assoc($res)) {
				$data[]=$row;
			}
			echo json_encode($data);
						
	}

	else{
		$data = [ 'status' => 'Failed', 'Message' => 'Error...' ];
		header('Content-Type: application/json');
		$myJSON = json_encode($data);
		echo $myJSON;
	}
?>