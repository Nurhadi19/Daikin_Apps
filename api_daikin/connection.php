<?php 

$connect = mysqli_connect("localhost","tekg1589_kelompok10","Kelompok_10","tekg1589_daikin");

if($connect){
  echo "connect";
} else {
  echo "no connection";
}

?>