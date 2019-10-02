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
	<style>
		body {
			padding: 16px;
		}
		table, td, th {    
   				border: 1px solid #ddd;
    			text-align: left;
				}

				table {
   				 border-collapse: collapse;
				 width: 100%;
				}

				th, td {
    			padding: 15px;
		}
	</style>
	<body>

<?php

	require_once('Config.php');
	
    $firNo =$_GET['firNo'];
	
	
	$query1 = "SELECT * FROM FIR, USER where FIR_NO = $firNo AND FIR.UID = USER.UID";
	
	$res = mysqli_query($conn,$query1);
	
	while($r = mysqli_fetch_assoc($res))
		{
			$data[] = $r;
		}
		
		$json = json_encode($data);
		//echo $json."<br>";
		
	$json_decoded = json_decode($json);
	
	
		echo '<html>';
		echo "<title>FIR Details $firNo</title>";
		
		echo '<body>';
        echo '<table border="1">';
		echo "<thead>";
		echo "<tr>";
		echo "<th>Name</th>";
        echo "<th>Mobile No</th>";
		echo "<th>Aadhaar No</th>";
		echo "<th>PNR</th>";
		echo "<th>Last Station</th>";
		echo "<th>Crime</th>";
		echo "<th>Information</th>";
		echo "<th>Status</th>";
		echo "</tr>";
		echo "</thead>";   
		
        foreach($json_decoded as $result){
          	echo '<tr>';
			echo '<td>'.$result->NAME.'</td>';
            echo '<td>'.$result->MOBILE_NO.'</td>';
			echo "<td><a href='ShowAadhaarDetails.php?UID=".$result->AADHAAR_NO."'>".$result->AADHAAR_NO."</a></td>";
			echo "<td><a href='ShowPNRDetails.php?PNR=".$result->PNR_NO."'>".$result->PNR_NO."</a></td>";
			echo '<td>'.$result->LAST_STATION.'</td>';
			echo '<td>'.$result->CRIME.'</td>';
			echo '<td>'.$result->INFO.'</td>';
			
			echo '<td style="color:';
			switch($result->STATUS) {
				case "APPROVED": 
					echo 'green';
					break;
				case "REJECTED": 
					echo 'red';
					break;	
					
				case "PENDING": 
					echo 'orange';
					break;
			}
			echo '">'.$result->STATUS.'</td>';
          	
		  
		  	$query2 = "SELECT ADDRESS FROM AADHAAR WHERE UID_NO=".$result->AADHAAR_NO."";
	
			$res2 = mysqli_query($conn,$query2);
			
			while($r2 = mysqli_fetch_assoc($res2))
				{
					$data2[] = $r2;
				}
				
				$json2 = json_encode($data2);
			
		
			$json_decoded2 = json_decode($json2);
	
			echo '</tr>';	
        }
	echo '<form name="FIRForm" action="FIRStatusUpdate.php" method="post">';
	echo '<div align="center">';
	echo "<b>FIR No:</b> FIR/GRP/<input type='text'  name = 'firSe' value ='$firNo' readonly  />";
	echo '</div>';
	echo '<div align="center">';
	echo '<div align ="centre">Status :<select name=status><option value="APPROVED">APPROVED</option><option value="PENDING">PENDING</option><option value="REJECTED">REJECTED</option></select></div> <br>';
	echo '</div>';
	echo '<div align ="center"><input type ="submit" value = "Update Status"></div> <br>';

?>
	</body>
</html>