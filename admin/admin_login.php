<?php

include '../components/connect.php';
//Mengimpor file "connect.php" yang kemungkinan berisi koneksi ke database.

session_start();

if(isset($_POST['submit'])){
   //Memeriksa apakah formulir login telah dikirim (tombol submit ditekan).

   $name = $_POST['name'];
   //Mengambil nilai dari field username.
   $name = filter_var($name, FILTER_SANITIZE_STRING);
   $pass = sha1($_POST['pass']);
   //Mengenkripsi password menggunakan SHA-1. (Catatan: Penggunaan SHA-1 sekarang dianggap kurang aman; lebih baik menggunakan algoritma hashing yang lebih kuat seperti bcrypt.)
   //Mengambil nilai dari field password.
   $pass = filter_var($pass, FILTER_SANITIZE_STRING);
   // filter_var : Digunakan untuk membersihkan nilai dari potensi masalah keamanan.

   $select_admin = $conn->prepare("SELECT * FROM `admin` WHERE name = ? AND password = ?");
   $select_admin->execute([$name, $pass]);
   //Mempersiapkan dan menjalankan query SQL untuk memeriksa keberadaan admin dengan nama dan kata sandi yang sesuai

   if($select_admin->rowCount() > 0){
   // Mengecek apakah ada admin dengan nama dan kata sandi yang sesuai

      $fetch_admin_id = $select_admin->fetch(PDO::FETCH_ASSOC);
      $_SESSION['admin_id'] = $fetch_admin_id['id'];
      header('location:dashboard.php');
      // Jika ada, menyimpan ID admin ke dalam sesi dan mengarahkan ke dashboard
   }else{
      $message[] = 'incorrect username or password!';
   }
   // Jika tidak, menampilkan pesan kesalahan

}

?>

<!DOCTYPE html>
<html lang="en">
<head>
     <!-- Meta tags untuk karakter set, kompatibilitas IE, dan responsif -->
   <meta charset="UTF-8">
   <meta http-equiv="X-UA-Compatible" content="IE=edge">
   <meta name="viewport" content="width=device-width, initial-scale=1.0">
   <title>login</title>

   <!-- font awesome cdn link  -->
   <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.1.1/css/all.min.css">

   <!-- custom css file link  -->
   <link rel="stylesheet" href="../css/admin_style.css">
   <!-- Mengimpor Font Awesome dari CDN dan file CSS kustom -->

</head>
<body>

<?php
if(isset($message)){
   foreach($message as $message){
      // Melakukan iterasi (perulangan) melalui setiap elemen dalam array $message. Dalam konteks ini, variabel $message bisa saja berisi beberapa pesan kesalahan.
      echo '
      <div class="message">
         <span>'.$message.'</span>
         <i class="fas fa-times" onclick="this.parentElement.remove();"></i>
      </div>
      ';
   }
}
?>
<!-- Menampilkan pesan kesalahan jika ada -->

<!-- admin login form section starts  -->

<section class="form-container">

   <form action="" method="POST">
      <h3>login now</h3>
      <p>default username = <span>admin</span> & password = <span>111</span></p>
      <input type="text" name="name" maxlength="20" required placeholder="enter your username" class="box" oninput="this.value = this.value.replace(/\s/g, '')">
      <input type="password" name="pass" maxlength="20" required placeholder="enter your password" class="box" oninput="this.value = this.value.replace(/\s/g, '')">
      <input type="submit" value="login now" name="submit" class="btn">
   </form>

</section>

<!-- admin login form section ends -->











</body>
</html>

<!--
   Alur Umum:
Pengguna membuka halaman login dan melihat formulir login.

Jika pengguna menekan tombol "login now", formulir akan dikirim ke halaman itu sendiri.

PHP akan memproses data formulir, membersihkan, mengenkripsi password, dan menjalankan query untuk memeriksa keberadaan admin.

Jika admin ditemukan, pengguna akan diarahkan ke halaman dashboard. Jika tidak, pesan kesalahan akan ditampilkan di bagian atas halaman.

Ini adalah alur dasar untuk formulir login. Harapannya, ini memberikan gambaran umum tentang apa yang terjadi saat pengguna mencoba untuk login pada halaman ini.

-->