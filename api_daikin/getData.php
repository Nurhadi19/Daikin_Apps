<?php

    include("connection.php");

    $sql = "SELECT * FROM table_mahasiswa order by id desc";

    $query = mysqli_query($connect,$sql);

    if(mysqli_num_rows($query) > 0){
        while($row = mysqli_fetch_object($query)){
            $data['status'] = true;
            $data['result'][] = $row;

            // $data2 = respond(true, $row)
        }
    }else{
        $data['status'] = false;
        $data['result'] = "Data not found";
    }

    print_r(json_encode($data));

?>