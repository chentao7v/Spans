package me.chentao.span

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import me.chentao.span.simple.IndexerActivity
import me.chentao.span.simple.PipelineActivity
import me.chentao.span.simple.PlaceholderActivity

lateinit var appContext: Context

const val IMAGE_URL = "https://img2.baidu.com/it/u=3202947311,1179654885&fm=253&fmt=auto&app=138&f=JPEG?w=800&h=500"

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    appContext = applicationContext
    setContentView(R.layout.activity_main)

//    loadImage()
  }

  private fun loadImage() {
    val ivImage = findViewById<ImageView>(R.id.ivImage)
    Glide.with(ivImage)
      .load(IMAGE_URL)
      .into(ivImage)
  }

  fun clickPlaceholder(view: View) {
    PlaceholderActivity.launch(this)
  }

  fun clickIndexer(view: View) {
    IndexerActivity.launch(this)
  }

  fun clickPipeline(view: View) {
    PipelineActivity.launch(this)
  }
}