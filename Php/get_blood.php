<?PHP
$hostname="localhost";
$database="bp";
$username="root";
$password="sylka1234";

$json=array();

$conexion=mysqli_connect($hostname,$username,$password,$database);
$consulta="SELECT * FROM blood";
$resultado=mysqli_query($conexion,$consulta);

if($consulta){

    while ($reg=mysqli_fetch_array($resultado)){
        $json['bp'][]=$reg;
    }

    mysqli_close($conexion);
    echo json_encode($json);
}else{
    $results["id"]='';
    $results["sisto"]='';
    $results["diasto"]='';
    $results["edo"]='';
    $results["created"]='';
    $json['bp'][]=$results;
    echo json_encode($json);
}

?>