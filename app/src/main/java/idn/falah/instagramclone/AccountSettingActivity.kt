package idn.falah.instagramclone

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.theartofdev.edmodo.cropper.CropImage
import idn.falah.instagramclone.databinding.ActivityAccountSettingBinding
import idn.falah.instagramclone.model.User

class AccountSettingActivity : AppCompatActivity() {
    // viewbinding untuk activity_account_setting.xml
    private lateinit var binding: ActivityAccountSettingBinding

    // buat variabel userInfo berisi database reference
    private lateinit var userInfo: DatabaseReference

    // buat variabel user yang berisi model user dari struktur data di firebase
    private lateinit var user: User

    // buat variabel untuk mengakses Storage Firebase
    private lateinit var firebaseStorage: StorageReference

    // buat variabel dialog untuk menampilkan loading
    private lateinit var dialog: LoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // pengaturan viewbinding dimulai
        val inflater = layoutInflater
        binding = ActivityAccountSettingBinding.inflate(inflater)
        setContentView(binding.root)
        // pengaturan viewbinding selesai

        // inisialisasi dialog
        dialog = LoadingDialog(this)

        // setting agar button logout bisa logout dari firebase
        // dan kembali ke activity login
        binding.btnLogout.setOnClickListener {
            // signout / logout dari firebase
            FirebaseAuth.getInstance().signOut()
            // intent menuju activity login
            val intent = Intent(this, LoginActivity::class.java)
            // add flags agar tombol back tidak bisa diklik
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            // mulai activity intent
            startActivity(intent)
            // finish (hapus) activity account setting activity
            finish()
        }

        // tombol close diberi setOnClickListener finish untuk menutup activity
        binding.btnClose.setOnClickListener {
            finish()
        }

        // tombol centang diberi setonclick untuk mengupdate info
        binding.btnAccept.setOnClickListener {
            updateUserInfo()
        }

        // jika ada user yang login
        FirebaseAuth.getInstance().currentUser?.let { currentUser ->
            // dapatkan UID dari User yang login
            val uidUser = currentUser.uid
            // dapatkan user info yang login berdasarkan UIDnya
            userInfo = FirebaseDatabase.getInstance()
                .reference
                // child users berisi nama folder dari user yang ada di firebase realtime database
                // nama ini harus persis
                .child("users")
                .child(uidUser)

            // Aktifkan firebasestorage
            // Buat folder bernama ProfilPict atau terserah antum dah nama foldernya
            firebaseStorage = FirebaseStorage.getInstance().reference.child("ProfilPict")

            // Mengaktifkan tombol ganti gambar agar membuka Crop Gambar
            binding.btnChange.setOnClickListener {
                CropImage.activity().setAspectRatio(1, 1).start(this)
            }

            // jika ada user yang login maka tombol delete akun bisa diklik
            // tombol delete akun setonclick untuk menghapus akun
            binding.btnDelete.setOnClickListener {

                // buat credential berisi email dan password dari user
                val password = binding.inputPassword.text.toString()
                val emailUser = currentUser.email.toString()

                if (password.isEmpty()) {
                    Toast.makeText(this, "Masukkan Ulang Password", Toast.LENGTH_SHORT).show()
                    binding.inputPassword.visibility = View.VISIBLE
                } else {
                    binding.inputPassword.visibility = View.GONE

                    val credential = EmailAuthProvider.getCredential(emailUser, password)
                    // reauthenticate untuk login ulang dari sistem
                    currentUser.reauthenticate(credential).addOnCompleteListener { task ->
                        if (task.isSuccessful) { // jika berhasil login
                            // hapus user saat ini yang ada di authenticate firebase
                            currentUser.delete()
                            // hapus user infonya juga yang ada di realtime database firebase
                            userInfo.removeValue()
                            // hapus user selesai
                            // Logout dari firebase
                            FirebaseAuth.getInstance().signOut()
                            // intent menuju activity login
                            val intent = Intent(this, LoginActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                            // finish (hapus) activity account setting activity
                            finish()
                        } else {
                            Toast.makeText(
                                this,
                                "Error : " + task.exception.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                            binding.inputPassword.visibility = View.VISIBLE
                            binding.inputPassword.text.clear()
                        }
                    }
                }
            }

            // ambil data dari userInfo
            userInfo.addValueEventListener(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            // jadikan data dari firebase menjadi data class User
                            user = snapshot.getValue(User::class.java) as User
                            binding.run {
                                // Masukkan data name, username, dan Bio ke dalam EditText
                                inputName.text = SpannableStringBuilder(user.fullname)
                                inputUsername.text = SpannableStringBuilder(user.username)
                                inputBio.text = SpannableStringBuilder(user.Bio)
                                // tambahkan glide untuk menambah gambar
                                // cek jika user memiliki gambar
                                var urlImage = user.image
                                // jika url gambar kosong maka ganti dengan gambar standar
                                if (urlImage.isEmpty()) urlImage =
                                    "https://tanjungpinangkota.bawaslu.go.id/wp-content/uploads/2020/05/default-1.jpg"
                                // masukkan gambar ke dalam imageView
                                Glide.with(this@AccountSettingActivity)
                                    .load(urlImage)
                                    .circleCrop()
                                    .into(imgProfile)
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                }
            )
        }
    }

    // onActivityResult digunakan untuk menerima data dari activity lain
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // karena banyak jenis request Code
        // maka untuk mengambil data dari Activity CropImage kita gunakan request kode CropImage
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE
            && resultCode == Activity.RESULT_OK
            && data != null
        ) {
            // Jika TIDAK ada eror maupun cancel dari user saat crop image
            // maka fungsi di dalam ini akan dijalankan

            // ambil gambar dari cropimage
            val resultUriImage = CropImage.getActivityResult(data).uri
            // mulai loading dialog
            dialog.startLoadingDialog()
            // buat URL gambar di firebase
            val fileRef = firebaseStorage.child(user.uid + ".jpg")
            // upload gambar
            val uploadImage = fileRef.putFile(resultUriImage)
            // https://firebase.google.com/docs/storage/android/upload-files?hl=id#get_a_download_url
            uploadImage.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                fileRef.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // jika upload sukses
                    // dapatkan URL Download dari foto profil
                    val downloadUri = task.result
                    user.image = downloadUri.toString()
                    // Masukkan foto profil baru menggunakan Glide
                    Glide.with(this@AccountSettingActivity)
                        .load(user.image)
                        .circleCrop()
                        .into(binding.imgProfile)
                    // update user info
                    updateUserInfo()
                    // hilangkan loading bar
                    dialog.dismissDialog()
                    Toast.makeText(this, "Sukses Upload Foto Profil", Toast.LENGTH_SHORT).show()
                } else {
                    // Handle failures
                    dialog.dismissDialog()
                    Toast.makeText(this, "Gagal Upload Foto Profil", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // buat private fungsi updateUserInfo() untuk menyimpan info user ke dalam database
    // private fun itu fungsi yang hanya bisa diakses oleh fungsi lain di dalam class yang sama
    private fun updateUserInfo() {
        // akses semua input text yang ada di account setting activity
        binding.run {
            val fullName = inputName.text.toString()
            val userName = inputUsername.text.toString()
            val userBio = inputBio.text.toString()

            // cek apakah semua data terisi
            if (fullName.isEmpty()) {
                Toast.makeText(
                    this@AccountSettingActivity,
                    "Nama Harus Diisi !",
                    Toast.LENGTH_SHORT
                )
                    .show()
                return
            }
            if (userName.isEmpty()) {
                Toast.makeText(
                    this@AccountSettingActivity,
                    "Username Harus Diisi !",
                    Toast.LENGTH_SHORT
                )
                    .show()
                return
            }

            // buat userMap yang menyimpan data terupdate
            // nama variabel dalam userMap harus persis sama dengan yang ada di firebase
            val userMap = HashMap<String, Any>()
            userMap["Bio"] = userBio
            userMap["email"] = user.email
            userMap["fullname"] = fullName
            userMap["image"] = user.image
            userMap["uid"] = user.uid
            userMap["username"] = userName

            // Update data yang ada pada firebase
            userInfo.updateChildren(userMap)
            Toast.makeText(this@AccountSettingActivity, "User Telah Diupdate", Toast.LENGTH_SHORT)
                .show()

            // finish untuk keluar dari accountsetting
            finish()
        }
    }
}