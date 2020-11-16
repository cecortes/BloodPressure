<?PHP
$hostname="localhost";
$database="bp";
$username="root";
$password="sylka1234";

$json=array();
$sisto="0";
$diasto="0";
$edo="TEST";

$conexion=mysqli_connect($hostname,$username,$password,$database);
		
$consulta="INSERT INTO blood(blood_id, sisto, diasto, estado) VALUES (0,'{$sisto}','{$diasto}','{$edo}')";
$resultado=mysqli_query($conexion,$consulta);

echo("Insert BLOOD");
?>