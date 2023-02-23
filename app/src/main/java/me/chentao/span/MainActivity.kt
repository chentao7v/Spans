package me.chentao.span

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import me.chentao.span.simple.IndexerActivity
import me.chentao.span.simple.PipelineActivity
import me.chentao.span.simple.PlaceholderActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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