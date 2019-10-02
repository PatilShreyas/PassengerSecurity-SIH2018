<?php

	require_once('Config.php');
	
    $PNR =$_GET['PNR'];
	
	$query1 = "SELECT * FROM RESERVATION where PNR_NO = $PNR";
	
	$res = mysqli_query($conn,$query1);
	
	while($r = mysqli_fetch_assoc($res))
		{
			$data[] = $r;
		}
		
		$json = json_encode($data);
		//echo $json."<br>";
		
	$json_decoded = json_decode($json);
	
	
		echo '<html>';
		echo '<title>PNR Details</title>';
		echo '<style>
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
	</style>';
		echo '<body>';
		echo "<h1>PNR Details ($PNR)</h1>";
        echo '<table border="1">';  
		
        foreach($json_decoded as $result){
			echo "<tr>";
				echo "<td>PNR Number</td>";
				echo '<td>'.$result->PNR_NO.'</td>';
			echo '</tr>';
			echo "<tr>";
				echo "<td>Train Number</td>";
				echo '<td>'.$result->TRAIN_NO.'</td>';
			echo '</tr>';
			echo "<tr>";
				echo "<td>Train Name</th>";
				echo '<td>'.$result->TRAIN_NAME.'</td>';
			echo '</tr>';			
			echo "<tr>";
				echo "<td>Source Station</td>";
				echo '<td>'.$result->SRC_STATION.'</td>';
			echo '</tr>';		            
			echo "<tr>";
				echo "<td>Destination Station</td>";
				echo '<td>'.$result->DEST_STATION.'</td>';
			echo '</tr>';	
			echo "<tr>";
				echo "<td>Seat Details</td>";
				echo '<td>'.$result->SEAT_DETAIL.'</td>';
			echo '</tr>';				
		 
        }
	
	echo '</table></body></html>';

?>