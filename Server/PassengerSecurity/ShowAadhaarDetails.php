<?php

	require_once('Config.php');
	
    $UID =$_GET['UID'];
	
	
	$query1 = "SELECT * FROM AADHAAR where UID_NO = $UID";
	
	$res = mysqli_query($conn,$query1);
	
	while($r = mysqli_fetch_assoc($res))
		{
			$data[] = $r;
		}
		
		$json = json_encode($data);
		//echo $json."<br>";
		
	$json_decoded = json_decode($json);
	
	
		echo '<html>';
		echo '<title>Aadhaar Information</title>';
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
		echo "<h1>Aadhaar Information ($UID)</h1>";
        echo '<table border="1">';  
		
        foreach($json_decoded as $result){
			echo "<tr>";
				echo "<td>Name</td>";
				echo '<td>'.$result->NAME.'</td>';
			echo '</tr>';
			echo "<tr>";
				echo "<td>Aadhaar Number</td>";
				echo '<td>'.$result->UID_NO.'</td>';
			echo '</tr>';
			echo "<tr>";
				echo "<td>Address</th>";
				echo '<td>'.$result->ADDRESS.'</td>';
			echo '</tr>';			
			echo "<tr>";
				echo "<td>Date of Birth</td>";
				echo '<td>'.$result->DATE_OF_BIRTH.'</td>';
			echo '</tr>';		            
			echo "<tr>";
				echo "<td>Mobile Number</td>";
				echo '<td>'.$result->MOBILE_NO.'</td>';
			echo '</tr>';		          	
		 
        }
	
	echo '</table></body></html>';

?>