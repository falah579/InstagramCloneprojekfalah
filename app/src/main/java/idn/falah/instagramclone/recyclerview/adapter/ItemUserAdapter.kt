package idn.falah.instagramclone.recyclerview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import idn.falah.instagramclone.databinding.ItemUserBinding
import idn.falah.instagramclone.model.User
import idn.falah.instagramclone.recyclerview.viewholder.ItemUserVH

/**
 * Created by Imam Fahrur Rofi on 28/08/2020.
 */
class ItemUserAdapter : RecyclerView.Adapter<ItemUserVH>() {
    // buat variabel wadah data yang akan ditampilkan di recyclerview
    private val listData = arrayListOf<User>()

    // viewbinding untuk item_user.xml
    private lateinit var binding: ItemUserBinding

    // buat fungsi untuk menambah data ke dalam listData
    fun addData(userData: List<User>) {
        // hapus isi yang ada di dalam listData
        listData.clear()
        // tambahkan data terbaru ke dalam listData
        listData.addAll(userData)
        // notify recylerview bahwa ada penambahan data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemUserVH {
        val inflater = LayoutInflater.from(parent.context)
        binding = ItemUserBinding.inflate(inflater)
        return ItemUserVH(binding)
    }

    // buat fungsi konversi dp (density) ke px (pixel)
    private fun dpToPx(dp: Int): Int {
        val px = dp * binding.root.resources.displayMetrics.density
        return px.toInt()
    }

    override fun onBindViewHolder(holder: ItemUserVH, position: Int) {
        // margin untuk layout
        val marginLeft = dpToPx(8)
        val marginTop = dpToPx(8)
        val marginRight = dpToPx(8)
        val marginBottom = dpToPx(8)
        // buat layout parameter baru untuk item_user
        val newLayoutParams = FrameLayout.LayoutParams(
            // width = MatchParent
            ViewGroup.LayoutParams.MATCH_PARENT,
            // height = Wrap Content
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        // set margin parameter terbaru
        newLayoutParams.setMargins(marginLeft, marginTop, marginRight, marginBottom)
        // masukkan layout parameter baru ke dalam viewholder
        holder.itemView.layoutParams = newLayoutParams
        // ambil data sesuai posisinya
        val data = listData[position]
        // masukkan ke dalam viewholder
        holder.bind(data)
    }

    override fun getItemCount(): Int {
        // return ukuran data yang dimasukkan
        // agar jumlah item di recyclerview sesuai dengan jumlah data
        return listData.size
    }
}