package com.github.ttaf.gpsphotogallery.adapter

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.github.ttaf.gpsphotogallery.R
import com.github.ttaf.gpsphotogallery.image.ImageActivity
import com.github.ttaf.gpsphotogallery.model.photo.Photo
import com.squareup.picasso.Picasso
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable

class PhotoAdapter(
        private val rxPhotos: Flowable<List<Photo>>,
        private val lifecycle: Lifecycle) : RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder>() {

    class PhotoViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val imageView: ImageView = view.findViewById(R.id.img)

        fun setImage(photo: Photo) {
            println(photo.path)
            Picasso.get()
                    .load("file://"+photo.path)
                    .into(imageView)
        }
    }

    inner class AdapterLifecycleObserver : LifecycleObserver {
        private var subscription: Disposable? = null

        @OnLifecycleEvent(Lifecycle.Event.ON_START)
        fun subscribe() {
            subscription = rxPhotos.observeOn(AndroidSchedulers.mainThread()).subscribe {
                photos.clear()
                photos.addAll(it)
                notifyDataSetChanged()
            }
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
        fun unsubscribe() {
            subscription?.dispose()
        }
    }

    private val photos: MutableList<Photo> = arrayListOf()

    init {
        lifecycle.addObserver(AdapterLifecycleObserver())
    }

    override fun getItemCount(): Int = photos.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {

        val root = LayoutInflater.from(parent.context).inflate(R.layout.adapter_photo, parent, false)

        return PhotoViewHolder(root)
    }

    override fun onBindViewHolder(viewHolder: PhotoViewHolder, position: Int) {
        val photo = photos[position]
        viewHolder.setImage(photo)

        viewHolder.imageView.setOnClickListener {
            val intent = Intent(it.context, ImageActivity::class.java)
            intent.putExtra(ImageActivity.EXTRA_IMAGE, photo.path)
            it.context.startActivity(intent)
        }
    }

}