<?php
	// Turn off error or warning reports
	error_reporting(E_ERROR | E_PARSE);
	
	require_once('Config.php');
	
	$query = "SELECT A.alert_id, U.name, U.mobile_no, A.time from ALERTS as A INNER JOIN User as U on A.alert_id = U.uid;";
	
	$res = mysqli_query($conn,$query);
	
	$isAv;
	
	while($r = mysqli_fetch_assoc($res))
	{
		$data[] = $r;
		$isAv = true;
	}
	
	if (isAv == false) {
		echo 'No Alerts Found';
		return;
	}
	
	$json = json_encode($data);
	$json_decoded = json_decode($json);
	
		echo '<html>';
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
		echo '<h1>Alerts</h1>';
        echo '<table border="1">';
		echo "<thead>";
		echo "<tr>";
        echo "<th>Name</th>";
		echo "<th>Mobile Number</th>";
		echo "<th>Time</th>";
		echo "</tr>";
		echo "</thead>";   
        foreach($json_decoded as $result){
          echo '<tr>';
            echo "<td><a href='AlertDetails.php?alert_id=".$result->alert_id."'>".$result->name."</a></td>";
			echo '<td>'.$result->mobile_no.'</td>';
			echo '<td>'.$result->time.'</td>';
          echo '</tr>';
        }
		
        echo '</table>';
		echo '</body>';
		echo '</html>';

?>