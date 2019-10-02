<!doctype html>
<html>
	<head>
		<title>Registered FIRs</title>
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
	

	$query = "SELECT *, CONCAT('FIR/GRP/', FIR_NO) AS FORMATED_FIR_NO FROM FIR, USER WHERE FIR.UID = USER.UID";
	
	$res = mysqli_query($conn,$query);
	
	while($r = mysqli_fetch_assoc($res))
		{
			$data[] = $r;
		}
		$json = json_encode($data);
		//echo $json."<br>";
		
	$json_decoded = json_decode($json);
	
	echo '<form name="MyForm" action="ShowFirDetails.php" method="get">';
	
	echo '<div align="center">';
	echo '<div align ="centre"><b>FIR Number:</b> FIR/GRP/<input type ="text" name="firNo" placeholder="No (e.g. 1868)"></div> <br>';
	echo '</div>';
	echo '<div align ="center"><input type ="submit" value = "Get Deatils"></div> <br>';
	
		echo '<html>';
		echo '<title>Registered FIRs</title>';
	
		echo '<body>';
        echo '<table border="1">';
		echo "<thead>";
		echo "<tr>";
        echo "<th>FIR No</th>";
		echo "<th>Applicant Name</th>";
		echo "<th>Date & Time</th>";
		echo "</tr>";
		echo "</thead>";   
        foreach($json_decoded as $result){
          echo '<tr>';
            echo "<td><a href='ShowFirDetails.php?firNo=".$result->FIR_NO."'>".$result->FORMATED_FIR_NO."</a></td>";
			echo '<td>'.$result->NAME.'</td>';
			echo '<td>'.$result->TIMESTAMP.'</td>';
          echo '</tr>';
        }
		
        echo '</table>';
		echo '</body>';
		echo '</html>';

?>
	</body>
</html>