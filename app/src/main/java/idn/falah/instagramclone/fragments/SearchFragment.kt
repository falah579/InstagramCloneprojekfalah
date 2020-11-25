package idn.falah.instagramclone.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import idn.falah.instagramclone.databinding.FragmentSearchBinding
import idn.falah.instagramclone.model.User
import idn.falah.instagramclone.recyclerview.adapter.ItemUserAdapter

class SearchFragment : Fragment() {

    // view binding untuk fragment_search.xml
    private lateinit var binding: FragmentSearchBinding

    // variabel adapterRV yang berisi ItemUserAdapter
    private lateinit var adapterRV: ItemUserAdapter

    // fragment binding diawali di onCreateView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // viewbinding fragmentsearch
        binding = FragmentSearchBinding.inflate(inflater, container, false)

        // inisialisasi adapter terlebih dahulu
        adapterRV = ItemUserAdapter()
        // setting recyclerview di onCreateView
        binding.rvSearch.run {
            setHasFixedSize(true)
            // gunakan layoutmanager Grid sehingga terbagi 2
            layoutManager = GridLayoutManager(context, 2)
            adapter = adapterRV
        }
        return binding.root
    }

    // onViewCreated itu artinya setelah tampilan dibuat
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // letakkan getAllUsers() di paling atas agar fungsi ini berjalan lebih dahulu
        // sehingga yang pertama kali tampil ketika SearchFragment dibuka adalah semua users yang ada
        getAllUsers()

        // mendeteksi perubahan keyword pada searchView
        binding.searchView.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    // Jika keyword dirubah dan tombol enter ditekan maka fungsi ini berjalan
                    Toast.makeText(view.context, query.toString(), Toast.LENGTH_SHORT).show()
                    // panggil fungsi getAllUsers jika tidak ada keyword yang dimasukkan
                    if (query.isNullOrBlank()) {
                        // query.isNullEmpty itu untuk cek kalau variabel query tidak null atau kosong
                        getAllUsers()
                    } else {
                        // jalankan fungsi searchUser untuk mencari user sesuai keyword
                        searchUser(query)
                    }
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    // Fungsi akan berjalan setiap kali ada perubahan keyword
                    // cek jika keyword tidak berisi kata-kata maka tampilkan semua user
                    if (newText.isNullOrBlank()) getAllUsers()
                    return false
                }
            }
        )
    }

    // buat fungsi untuk mendapatkan semua User, getAllUsers()
    private fun getAllUsers() {
        // tentukan Tabel yang akan diambil datanya
        val usersDB = FirebaseDatabase.getInstance().reference
            // ambil data dari tabel users
            .child("users")
            // urutkan berdasarkan fullname
            .orderByChild("fullname")
            // batasi data yang diambil sebanyak 10 data pertama
            .limitToFirst(10)

        // ambil nilai dari usersDB (value itu artinya nilai)
        usersDB.addValueEventListener(
            object : ValueEventListener {
                // fungsi onDataChange berjalan jika sukses
                override fun onDataChange(snapshot: DataSnapshot) {
                    // pastikan snapshot ada, snapshot adalah data hasil pengambilan dari database
                    if (snapshot.exists()) {
                        // buat array kosong yang hanya bisa diisi oleh model User
                        val users = arrayListOf<User>()
                        // buat variabel berisi ukuran data yang didapatkan
                        val dataSize = snapshot.childrenCount
                        // jika ukuran data lebih besar dari 0, maka lakukan pengambilan data
                        if (dataSize > 0) {
                            // lakukan perulangan masing-masing data
                            for (data in snapshot.children) {
                                val user = data.getValue(User::class.java) as User
                                // tambahkan data user ke dalam array
                                users.add(user)
                            }
                            // masukkan kumpulan data users ke dalam adapter recyclerview
                            adapterRV.addData(users)
                        }
                    }
                }

                // fungsi onCancelled berjalan ketika ada error
                override fun onCancelled(error: DatabaseError) {
                    Log.e("Error Search", error.details)
                }
            }
        )

    }

    // buat fungsi searchUser() berdasarkan keyword fullname
    private fun searchUser(keyword: String) {
        val usersDB = FirebaseDatabase.getInstance().reference
            .child("users")
            .orderByChild("fullname")
            .startAt(keyword)
            .endAt("$keyword \uf8ff")

        usersDB.addValueEventListener(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    // pastikan snapshot ada, snapshot adalah data hasil pengambilan dari database
                    if (snapshot.exists()) {
                        // buat array kosong yang hanya bisa diisi oleh model User
                        val users = arrayListOf<User>()
                        // buat variabel berisi ukuran data yang didapatkan
                        val dataSize = snapshot.childrenCount
                        // jika ukuran data lebih besar dari 0, maka lakukan pengambilan data
                        if (dataSize > 0) {
                            // lakukan perulangan masing-masing data
                            for (data in snapshot.children) {
                                val user = data.getValue(User::class.java) as User
                                // tambahkan data user ke dalam array
                                users.add(user)
                            }
                            // masukkan kumpulan data users ke dalam adapter recyclerview
                            adapterRV.addData(users)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("Error Search", error.details)
                }
            }
        )
    }

}
