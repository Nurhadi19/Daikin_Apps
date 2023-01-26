<?php

    include('connection.php');

    $email          =$_POST['email'];
    $nama_lengkap   =$_POST['nama_lengkap'];
    $alamat         =$_POST['alamat'];
    $password       =$_POST['password'];


    if(!empty($email)){

        $sqlCheck = "SELECT COUNT(*) FROM tb_user WHERE email='$email'";
        $queryCheck = mysqli_query($connect,$sqlCheck);
        $hasilCheck = mysqli_fetch_array($queryCheck);

        if($hasilCheck[0] == 0){
            $sql = "INSERT INTO tb_user (password, nama_lengkap , email , alamat) VALUES('$password','$nama_lengkap','$email','$alamat')";
            $query  =  mysqli_query($connect,$sql);

            if(mysqli_affected_rows($connect) > 0){
                $data['status'] = true;
                $data['result'] = "Berhasil";
            }else{
                $data['status'] = false;
                $data['result'] = "Gagal";
            }
        }else{
            $data['status'] = false;
            $data['result'] = "Gagal, Data Sudah Ada";
        }

    }
    
    else{
        $data['status'] = false;
        $data['result'] = "Gagal, NIM dan Nama tidak boleh kosong!";
    }

    print_r(json_encode($data));

?>