<?php

include('connection.php');

$name           = $_POST['name'];
$nim            = $_POST['nim'];
$address        = $_POST['address'];
$hobby          = $_POST['hobby'];

date_default_timezone_set('Asia/Makassar');
$dateImg    = date('YmdHis');
$web        = "CRUD";
$photo      = $_POST['photo'];
$photo_name = $web."_".$dateImg.".jpg";

$photo_loc ='image/'.$photo_name;
file_put_contents($photo_loc, base64_decode($photo));

if(!empty($name) && !empty($nim)){

    $sql1 = "SELECT photo FROM table_mahasiswa WHERE nim = '$nim'";
    $query1 = mysqli_query($connect,$sql1);
    $photo = mysqli_fetch_array($query1);
 
    unlink('image/'.$photo['photo']);
 
    $sql = "UPDATE table_mahasiswa set name='$name', address='$address', hobby='$hobby', photo='$photo_name' WHERE nim='$nim' ";
 
    $query = mysqli_query($connect,$sql);
 

    if(mysqli_affected_rows($connect) > 0){
        $data['status'] = true;
        $data['result'] = "Berhasil";
    }else{
        $data['status'] = false;
        $data['result'] = "Gagal";
    }
 
}else{
    $data['status'] = false;
    $data['result'] = "Gagal, NIM dan Nama tidak boleh kosong!";
}

print_r(json_encode($data));


?>