<?php

include '../components/connect.php';
//Baris ini mengimpor file connect.php, yang kemungkinan besar berisi koneksi database.

session_start();
//Memulai sesi PHP. Sesi digunakan untuk menyimpan informasi di antara halaman web.

$admin_id = $_SESSION['admin_id'];
//Menyimpan nilai dari 'admin_id' yang ada dalam sesi ke dalam variabel $admin_id.

if(!isset($admin_id)){
   header('location:admin_login.php');
}
// Mengecek apakah variabel $admin_id sudah di-set. Jika tidak, pengguna diarahkan kembali ke halaman login admin.

if(isset($_GET['delete'])){
//Mengecek apakah ada parameter GET dengan nama 'delete' di URL. Jika ada, maka akan menjalankan proses penghapusan admin.
   $delete_id = $_GET['delete'];
   //Menyimpan nilai dari parameter 'delete' yang ada dalam URL ke dalam variabel $delete_id.
   $delete_admin = $conn->prepare("DELETE FROM `admin` WHERE id = ?");
   //Mempersiapkan pernyataan SQL untuk menghapus admin dengan ID yang sesuai.
   $delete_admin->execute([$delete_id]);
   //Menjalankan pernyataan SQL untuk menghapus admin dengan ID yang sesuai.
   header('location:admin_accounts.php');
   //Mengarahkan ulang pengguna kembali ke halaman "admin_accounts.php" setelah menghapus admin.
}

?>

<!DOCTYPE html>
<html lang="en">
<head>
   <meta charset="UTF-8">
   <meta http-equiv="X-UA-Compatible" content="IE=edge">
   <meta name="viewport" content="width=device-width, initial-scale=1.0">
   <!-- Pengaturan meta untuk mengonfigurasi karakter set, kompatibilitas IE, dan tampilan responsif.-->
   <title>Akun Admin</title>
   <!-- Menentukan judul halaman. -->

   <!-- font awesome cdn link  
   Mengimpor Font Awesome dan file CSS kustom dari CDN untuk styling.-->
   <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.1.1/css/all.min.css">

   <!-- custom css file link  -->
   <link rel="stylesheet" href="../css/admin_style.css">

</head>
<body>

<?php include '../components/admin_header.php' ?>
<!-- Memasukkan file "admin_header.php" yang mungkin berisi bagian kepala admin.-->

<!-- admins accounts section starts  -->

<section class="accounts">

   <h1 class="heading">Akun Admin</h1>

   <div class="box-container">

   <!-- bagian konten halaman yang menampilkan informasi akun admin -->

   <div class="box">
      <p>register new admin</p>
      <a href="register_admin.php" class="option-btn">register</a>
   </div>
   <!-- sebuah div dengan kelas "box" yang berisi informasi tentang pendaftaran admin baru, seperti teks "register new admin" dan tautan untuk mendaftar.-->


   <?php
      $select_account = $conn->prepare("SELECT * FROM `admin`");
      // Ini adalah pernyataan SQL untuk memilih semua data dari tabel admin 
      $select_account->execute();
      //Menjalankan pernyataan SQL untuk mengeksekusi query dan mengambil hasilnya.
      
      if($select_account->rowCount() > 0){
         //Memeriksa apakah terdapat admin dalam basis data.
         while($fetch_accounts = $select_account->fetch(PDO::FETCH_ASSOC)){  
   ?>
         <!-- Menggunakan perulangan while untuk mengambil setiap baris hasil query dan menyimpannya dalam array asosiatif $fetch_accounts.-->
   
      <div class="box">
      <p> admin id : <span><?= $fetch_accounts['id']; ?></span> </p>
      <p> username : <span><?= $fetch_accounts['name']; ?></span> </p>
      <div class="flex-btn">
         <a href="admin_accounts.php?delete=<?= $fetch_accounts['id']; ?>" class="delete-btn" onclick="return confirm('delete this account?');">hapus</a>
         <?php
            if($fetch_accounts['id'] == $admin_id){
               echo '<a href="update_profile.php" class="option-btn">Perbarui</a>';
            }
         ?>
      </div>
      <!-- Menampilkan informasi setiap akun admin, seperti ID dan nama pengguna. Juga, menambahkan tautan untuk menghapus akun dan, jika akun tersebut adalah akun admin saat ini, menambahkan tautan untuk memperbarui profil.-->

   </div>
   <?php
      }
   }else{
      echo '<p class="empty">no accounts available</p>';
   }
   // Jika tidak ada akun admin yang ditemukan, menampilkan pesan bahwa tidak ada akun yang tersedia.
   ?>

   </div>

</section>

<!-- admins accounts section ends -->


<!-- custom js file link  -->
<script src="../js/admin_script.js"></script>

</body>
</html>

<!--
   Inti dari file "admin_accounts.php" adalah untuk menangani tampilan dan interaksi yang berkaitan dengan akun admin di suatu sistem. Berikut adalah beberapa inti kegunaan dari file ini:

Menampilkan Daftar Admin:
Melakukan query ke database untuk mendapatkan semua informasi akun admin dari tabel admin.
Menampilkan informasi tersebut di halaman web, termasuk ID admin dan nama pengguna.

Mengelola Akun Admin:

Menyediakan opsi untuk menghapus akun admin. Jika pengguna mengklik tombol "delete", file ini akan menghapus akun admin terkait dari database.
Menyediakan opsi untuk memperbarui profil. Jika admin saat ini sedang dilihat, akan muncul tautan untuk memperbarui profil.

Menambah Admin Baru:
Menyediakan tautan atau tombol untuk mendaftar admin baru. Jika pengguna mengklik tautan ini, mereka akan diarahkan ke halaman "register_admin.php".

Penanganan Sesi dan Keamanan:
Memeriksa sesi untuk memastikan bahwa pengguna yang mengakses halaman ini adalah admin yang telah login. Jika tidak, pengguna akan diarahkan ke halaman login admin.

Presentasi Visual:
Menampilkan informasi secara terstruktur dengan menggunakan elemen HTML dan CSS. Menggunakan kelas CSS seperti "box" untuk merapikan tata letak halaman.
Dengan kata lain, file ini bertindak sebagai antarmuka untuk administrasi akun admin, memungkinkan operasi-operasi dasar seperti melihat, menghapus, dan memperbarui akun admin. Selain itu, file ini juga menangani aspek keamanan dengan memastikan bahwa hanya admin yang sah yang dapat mengakses halaman ini.






-->