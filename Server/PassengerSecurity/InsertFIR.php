<?php

	require_once('Config.php');
	
	if(isset($_POST['uid']) && isset($_POST['pnrNo']) && isset($_POST['lastStation']) && isset($_POST['crime']) && isset($_POST['addInfo'])){
		$uid1 = $_POST['uid'];
		$pnrNo1 = $_POST['pnrNo'];

		$trainNo1 = $_POST['trainNo'];
		$lastStation1 = $_POST['lastStation'];
		$crime1 = $_POST['crime'];
		$addInfo1 = $_POST['addInfo'];
		
		$authquery = "SELECT * FROM USER WHERE UID = '$uid1'";
		$authres = mysqli_query($conn,$authquery);
		
		if($authres){
			if (mysqli_num_rows($authres) > 0) {
				while($row = mysqli_fetch_assoc($authres)) {
					$aadhaarNo1 = $row["AADHAAR_NO"];
				}
				$query = "INSERT INTO FIR (UID,PNR_NO,LAST_STATION,CRIME,INFO) VALUES('$uid1','$pnrNo1','$lastStation1','$crime1','$addInfo1')";

				$res = mysqli_query($conn,$query);
				$fir_id = null;
				if($res){
					$select_query = "SELECT *, CONCAT('FIR/GRP/', FIR_NO) AS FORMATED_FIR_NO FROM FIR ORDER BY FIR_NO DESC LIMIT 1";
					$res1 = mysqli_query($conn,$select_query);
					
					if (mysqli_num_rows($res1) > 0) {
					// output data of each row
					while($row = mysqli_fetch_assoc($res1)) {
						$fir_id = $row["FORMATED_FIR_NO"];
					}
				}
				
				$data = [ 'status' => 'Success', 'Message' => 'FIR Registered Successfully' , 'FIR_NO' => $fir_id];
				header('Content-Type: application/json');
				$myJSON = json_encode($data);
				echo $myJSON;
				}
			}
			else{
				echo "Unauthorized Access";
			}
		}else{
			echo "Error";
		}
	}
	else{
		echo "Error";
	}

?>