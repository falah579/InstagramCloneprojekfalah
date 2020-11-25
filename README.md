# InstagramClone
Instagram Clone SMK IDN

## Cara ambil project ini

  1. Ambil Project dari github berupa zip.
  2. Extract Zip tersebut ke suatu folder
  3. Buka Android Studio, Import Project
  4. Pilih folder hasil extract
  5. Tunggu sampai gradle selesai
  6. Hapus google-services.json yang ada di dalam project
  7. Ganti nama package sesuai yang antum inginkan
  8. Ganti ApplicationID yang ada di build.gradle module app sesuai nama package dari no 7 diatas
  9. Daftarkan aplikasi ke firebase, dengan memasukkan nama package dan SHA1
  Note: SHA1 didapatkan dari Gradle - Signin Report
  10. Tambahkan google-services.json ke dalam project folder app
  11. Sync Now Aplikasi
  12. Jalankan Aplikasi
  
## Urutan mempelajari aplikasi instagram clone

1. Import Dulu Dependecies yang dibutuhkan

2. Buat Activity Kosong :

	ActivityMain, ActivityRegister, ActivityLogin
	
3. Desain XML dari Activity

	activity_login.xml, activity_register.xml, activity_main.xml
	
4. Ngoding LoginActivity.kt

	  Buat variabel viewbinding untuk activity_login.xml
    
	  ngoding bagian oncreate, berisi button Signin dan register
    
	  membuat fungsi loginuser untuk ditempatkan pada button btnLogin.setonclicklistener
    
	  ngoding bagian onstart, berisi cek adanya user yang login, jika login maka lanjut ke main activity
	
5. Ngoding RegisterActivity.kt

	Buat variabel viewbinding untuk activity_register.xml
  
	ngoding oncreate, berisi button Signin dan register
  
	membuat fungsi createAccount untuk ditempatkan pada button btnRegister.setonclicklistener
  
	membuat fungsi saveUserInfo untuk menempatkan data User pada realtimeDatabase
	
6. Ngoding MainActivity.kt

	Buat 4 fragment kosong yaitu : homeFragment, NotificationFragment, ProfileFragment, dan SearchFragment
  
	Buat resource navigation button_navigation berisi 4 fragment tersebut
  
	Buat menu button_nav.xml
  
	Modifikasi activity_main.xml
  
	Memasukkan bottom navigation pada MainActivity.kt

7. Membuat Model User.kt berisi struktur data yang akan diambil dari Firebase Realtime Database

8. Membuat View item recyclerview item_user.xml

	Membuat Recyclerview ViewHolder ItemUserVH.kt
  
	Membuat Recyclerview Adapter ItemUserAdapter.kt

9. Membuat AccountSettingActivity untuk mengubah informasi user

	Memasukkan foto menggunakan Glide dengan efek circleCrop
  
	Ambil foto dari galeri dan crop foto menggunakan Android Image Cropper
  
	Mengubah Nama, Username, Bio
  
	Menghapus Akun : Menghapus Informasi yang ada di realtime database dan authentication Firebase
	
10. Modifikasi ProfileFragment.kt

	memasukkan informasi user ke dalam profile fragment
  
	Ngoding pertama kali membuat variabel viewbinding
  
	Ngoding bagian oncreateView untuk menginisialisasi binding
  
	Ngoding bagian onViewCreated untuk logika aplikasi atau olah data
	
11. Modifikasi SearchFragment.kt

	Bagian OnViewCreated diawali dengan fungsi menampilkan semua data
  
	Bagian SearchView berisi fungsi untuk menampilkan data sesuai keyword
	
