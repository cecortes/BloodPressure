<?PHP
$hostname="localhost";
$database="bp";
$username="root";
$password="sylka1234";

$json=array();

if(isset($_GET["sisto"]) && ($_GET["diasto"]) && ($_GET["edo"])){
    $sisto=$_GET['sisto'];
    $diasto=$_GET['diasto'];  
    $edo=$_GET['edo']; 

    $conexion=mysqli_connect($hostname,$username,$password,$database);
		
    $consulta="INSERT INTO blood(blood_id, sisto, diasto, estado) VALUES (0,'{$sisto}','{$diasto}','{$edo}')";
    $resultado=mysqli_query($conexion,$consulta);

    if($consulta){

        $consulta="SELECT * FROM blood";
        $resultado=mysqli_query($conexion,$consulta);

        if($reg=mysqli_fetch_array($resultado)){
            $json['blood'][]=$reg;
        }

    mysqli_close($conexion);
    echo json_encode($json);
    }

    else{
        $results["id"]='';
        $results["sisto"]='';
        $results["diasto"]='';
        $results["estado"]='';
        $results["create"]='';
        $json['blood'][]=$results;
        echo json_encode($json);
    }
}
else{
    $results["id"]='';
    $results["sisto"]='';
    $results["diasto"]='';
    $results["estado"]='';
    $results["create"]='';
    $json['blood'][]=$results;
    echo json_encode($json);
}
?>