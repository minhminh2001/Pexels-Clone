package com.example.image.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.image.R
import com.example.image.utils.Constants
import com.example.image.utils.OnEventControlListener
import com.example.model.data.Photo
import java.lang.ref.WeakReference
import java.net.URL


class ListImageAdapter(context: Context, listener: OnEventControlListener) :
    RecyclerView.Adapter<ListImageAdapter.ViewHolder>() {
    private var weakContext: WeakReference<Context>
    private var listener: OnEventControlListener

    private val differCallback = object : DiffUtil.ItemCallback<Photo>() {
        override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean {
            return oldItem.source.large2x == newItem.source.large2x
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, differCallback)

    init {
        this.weakContext = WeakReference(context)
        this.listener = listener
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvName: TextView = view.findViewById(R.id.tvName)
        var ivImage: ImageView = view.findViewById(R.id.ivImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_photo_list,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val photo = differ.currentList[position]
        viewHolder.itemView.apply{
            viewHolder.tvName.text = photo.photographer
            val downloadImageTask = DownloadImageTask(viewHolder.ivImage)
            downloadImageTask.execute(differ.currentList[position].source.large)
            setOnClickListener {
                listener.onEvent(Constants.ACTION_CLICK_ITEM_PHOTO, it, photo)
            }
        }
    }

    class DownloadImageTask(var bmImage: ImageView) : AsyncTask<String?, Void?, Bitmap?>() {
        @Deprecated("Deprecated in Java", ReplaceWith("bmImage.setImageBitmap(result)"))
        override fun onPostExecute(result: Bitmap?) {
            bmImage.setImageBitmap(result)
        }

        @Deprecated("Deprecated in Java")
        override fun doInBackground(vararg params: String?): Bitmap? {
            val urlDisplay = params[0]
            var mIcon11: Bitmap? = null
            try {
                val bitmap = URL(urlDisplay).openStream()
                mIcon11 = BitmapFactory.decodeStream(bitmap)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return mIcon11
        }
    }
}