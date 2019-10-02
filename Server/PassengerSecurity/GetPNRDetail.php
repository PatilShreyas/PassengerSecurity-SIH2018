<?php

	require_once('Config.php');
	
	
		$pnrNo = $_POST['pnrNo'];
		$query = "SELECT TRAIN_NO, TRAIN_NAME ,SEAT_DETAIL,SRC_STATION,DEST_STATION FROM RESERVATION WHERE PNR_NO='$pnrNo'";
		
		$res = mysqli_query($conn,$query);
		$pnrNo = null;
		if (mysqli_num_rows($res) > 0) {
			// output data of each row
			while($row = mysqli_fetch_assoc($res)) {
				$trainNo = $row["TRAIN_NO"];
				$trainName = $row["TRAIN_NAME"];
				$seatDe =$row["SEAT_DETAIL"];
				$src = $row["SRC_STATION"];
				$dest = $row["DEST_STATION"];
			}
			$data = [ 'status' => 'Success', 'Message' => 'Retrieved Successfully' , 'TRAIN_NO' => $trainNo, 'TRAIN_NAME' => $trainName,'SRC_STATION' => $src, 'DEST_STATION' => $dest, 'SEAT_DETAIL' => $seatDe];
			header('Content-Type: application/json');
			$myJSON = json_encode($data);
			echo $myJSON;
		}
		else{
			$data = [ 'status' => 'Failed', 'Message' => 'Error...' ];
			header('Content-Type: application/json');
			$myJSON = json_encode($data);
			echo $myJSON;
		}
	

?>