package idn.falah.instagramclone.recyclerview.viewholder

import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import idn.falah.instagramclone.databinding.ItemUserBinding
import idn.falah.instagramclone.model.User

/**
 * Created by Imam Fahrur Rofi on 28/08/2020.
 */

class ItemUserVH(private val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {

    private val currentProfile = FirebaseAuth.getInstance().currentUser?.uid.toString()

    // fungsi untuk memasukkan data dari adapter ke dalam view
    fun bind(user: User) {
        binding.run {
            checkFollow(user.uid)
            // mengisi textview dengan data yang dibutuhkan
            txtName.text = user.fullname
            txtUsername.text = user.username

            Glide.with(binding.root)
                // load diisi oleh url dari gambar
                .load(user.image)
                // CircleCrop agar gambar yang dimasukkan ke dalam glide berbentuk lingkaran
                .circleCrop()
                // into diisi oleh viewnya
                .into(imgProfile)

            btnFollow.setOnClickListener {
                // cek apakah teks pada tombol btnFollow itu "Follow"
                if (btnFollow.text.toString() == "Follow") {
                    // jika teks tombol masih follow berarti user belum difollow
                    // kuy kita follow di firebase
                    FirebaseDatabase.getInstance().reference
                        .child("Follow").child(currentProfile).child("Following")
                        .child(user.uid)
                        .setValue(true).addOnCompleteListener { followingTask ->
                            if (followingTask.isSuccessful) {
                                FirebaseDatabase.getInstance().reference.child("Follow")
                                    .child(user.uid)
                                    .child("Followers").child(currentProfile).setValue(true)
                                    .addOnCompleteListener { followerTask ->
                                        if (followingTask.isSuccessful) Toast.makeText(
                                            this.root.context,
                                            "Berhasil Follow",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                            }
                        }
                } else if (btnFollow.text.toString() == "Following") {
                    FirebaseDatabase.getInstance().reference
                        .child("Follow").child(currentProfile).child("Following")
                        .child(user.uid)
                        .removeValue().addOnCompleteListener { followingTask ->
                            if (followingTask.isSuccessful) {
                                FirebaseDatabase.getInstance().reference.child("Follow")
                                    .child(user.uid)
                                    .child("Followers").child(currentProfile).removeValue()
                                    .addOnCompleteListener { followerTask ->
                                        if (followingTask.isSuccessful) Toast.makeText(
                                            this.root.context,
                                            "Berhasil Unfollow",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                            }
                        }
                }

            }
        }
    }

    private fun checkFollow(userID: String) {
        val userRef = FirebaseDatabase.getInstance().reference
            .child("Follow").child(currentProfile).child("Following")

        userRef.addValueEventListener(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    binding.btnFollow.text =
                        if (snapshot.child(userID).exists()) "Following" else "Follow"
                }

                override fun onCancelled(error: DatabaseError) {
                }
            }
        )
    }
}