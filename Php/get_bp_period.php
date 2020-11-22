<?PHP
$hostname="localhost";
$database="bp";
$username="root";
$password="sylka1234";

$json=array();

//$ini = "2020-11-16";
//$fin = "2020-11-21";

if(isset($_GET["ini"]) && isset($_GET["fin"])){
    $ini=$_GET['ini']; 
    $fin=$_GET['fin'];

    $conexion=mysqli_connect($hostname,$username,$password,$database);

    $consulta="SELECT * FROM blood WHERE blood_create > '{$ini}' AND blood_create < '{$fin}' ORDER BY blood_id DESC";
    $resultado=mysqli_query($conexion,$consulta);

    if($consulta){
		
        while($reg=mysqli_fetch_array($resultado)){
            $json['p'][]=$reg;
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
        $json['p'][]=$results;
        echo json_encode($json);
    }
}
else{
    $results["id"]='';
    $results["sisto"]='';
    $results["diasto"]='';
    $results["estado"]='';
    $results["create"]='';
    $json['p'][]=$results;
    echo json_encode($json);
}

?>