package me.chentao.span

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import me.chentao.span.simple.IndexerActivity
import me.chentao.span.simple.PipelineActivity
import me.chentao.span.simple.PlaceholderActivity

lateinit var appContext: Context

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    appContext = applicationContext
    setContentView(R.layout.activity_main)
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