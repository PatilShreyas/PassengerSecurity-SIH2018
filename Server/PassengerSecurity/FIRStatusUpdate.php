<?php

	require_once('Config.php');
	
	$firSe = $_POST['firSe'];
	$Status = $_POST['status'];

	$query = "UPDATE FIR SET STATUS ='$Status' Where FIR_NO ='$firSe'";
	 
	$res = mysqli_query($conn,$query);
	$firNo = null;
	echo "Status update successfully";

?>
	