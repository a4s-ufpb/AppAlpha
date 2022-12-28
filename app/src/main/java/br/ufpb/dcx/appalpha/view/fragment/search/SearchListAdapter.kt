package br.ufpb.dcx.appalpha.view.fragment.search

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import br.ufpb.dcx.appalpha.R
import br.ufpb.dcx.appalpha.view.activities.CreateThemeActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.EncodeStrategy
import com.bumptech.glide.load.engine.DiskCacheStrategy
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.net.URL
import java.net.URLConnection


class SearchListAdapter(private var resultado: List<HashMap<String?, Any?>?>, context:Context,
                        fragment: Fragment,
                        tag:Int) : RecyclerView.Adapter<SearchListAdapter.ViewHolder>() {

    private var fragmentContext : Context = context
    private var TAG : Int = tag
    private var fragmento : Fragment = fragment

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.search_card_grid, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return resultado.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        var imageUrl : String? = null;

        try {
            imageUrl = resultado.get(position)?.get("thumbnailUrl").toString()
        } catch(e : Exception){
            Toast.makeText(fragmentContext,"Imagem Invalida, Tente escolher Outra",Toast.LENGTH_SHORT).show();
        }

        if(imageUrl != null) {
            loadImage(imageUrl, holder.img)

            holder.itemView.setOnClickListener {

                fragmento.view?.findViewById<View>(R.id.loadingframeLayout2)?.setVisibility(View.VISIBLE)

                val executor = GetImageExecutor(imageUrl, fragmento, TAG)
                executor.start()
            }
        }
    }

    internal class GetImageExecutor(
        imageUrl: String,
        fragmento: Fragment,
        TAG: Int
    ) :
        Thread() {
        private val imageUrl: String
        private val TAG: Int
        private val fragmento: Fragment
        override fun run() {
            try {

                val url = URL(imageUrl)
                val connection: URLConnection = url.openConnection()
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);
                val input = connection.getInputStream()

                val outputDir: File? = fragmento.activity?.getCacheDir()
                val outputFile = File.createTempFile("image", ".jpg", outputDir)

                val outStream: OutputStream = FileOutputStream(outputFile)

                val buffer = ByteArray(8 * 1024)
                var bytesRead: Int
                while (input.read(buffer).also { bytesRead = it } != -1) {
                    outStream.write(buffer, 0, bytesRead)
                }
                input.close()

                fragmento.activity?.runOnUiThread {
                    fragmento.view?.findViewById<View>(R.id.loadingframeLayout2)?.setVisibility(View.GONE)

                    var actv: CreateThemeActivity = fragmento.activity as CreateThemeActivity;

                    actv.sendImage(outputFile, TAG)

                    actv.onBackPressed()
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        init {
            this.imageUrl = imageUrl
            this.fragmento = fragmento
            this.TAG = TAG
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var img : ImageView = itemView.findViewById(R.id.grid_image)
    }

    private fun loadImage(imageUrl: String?, themeImageLeft: ImageView) {
        val diskCacheStrategy = object : DiskCacheStrategy() {
            override fun isDataCacheable(dataSource: com.bumptech.glide.load.DataSource?): Boolean {
                return true
            }

            override fun isResourceCacheable(isFromAlternateCacheKey: Boolean, dataSource: com.bumptech.glide.load.DataSource?, encodeStrategy: EncodeStrategy?): Boolean {
                return true
            }

            override fun decodeCachedResource(): Boolean {
                return true
            }

            override fun decodeCachedData(): Boolean {
                return true
            }

        }
        var erroImg : Int
        try {
            erroImg = Integer.parseInt(imageUrl.toString())
        } catch (e: NumberFormatException) {
            erroImg = R.drawable.no_image
        }

        Glide.with(fragmentContext)
            .load(imageUrl)
            .error(erroImg)
            .diskCacheStrategy(diskCacheStrategy)
            .into(themeImageLeft)
    }


}