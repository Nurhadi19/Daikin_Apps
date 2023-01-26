<?php
 
include('connection.php');
 
$nim = $_POST['nim'];
 
if(!empty($nim)){

    $sql1 = "SELECT photo FROM table_mahasiswa WHERE nim = '$nim'";
    $query1 = mysqli_query($connect,$sql1);
    $photo = mysqli_fetch_array($query1);
    unlink('image/'.$photo['photo']);


    $sql = "DELETE FROM table_mahasiswa WHERE nim='$nim' ";
 
    $query = mysqli_query($connect,$sql);
 
    $data['status'] = true;
    $data['result'] = 'Berhasil';
}else{
    $data['status'] = false;
    $data['result'] = 'Gagal';
}
 
print_r(json_encode($data));
 
 
?>