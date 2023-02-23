package me.chentao.span.simple

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import me.chentao.library.span.Spans
import me.chentao.span.R

/**
 * create by chentao on 2023-02-23.
 */
class PipelineActivity : AppCompatActivity() {

  companion object {

    private const val TAG = "Pipeline"

    fun launch(context: Context) {
      val intent = Intent(context, PipelineActivity::class.java)
      context.startActivity(intent)
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContentView(R.layout.activity_simple)

    val tvMsg: TextView = findViewById(R.id.tvMsg)
    simple(tvMsg)
  }

  private fun simple(tvMsg: TextView) {

    val msg1 = "文本AAAA"
    val msg2 = "文本BBBBBBB"
    val msg3 = "文本CCCCCCCCCCC"
    val msg4 = "文本DDDDD"

    Spans.pipeline()
      .color(msg1, getColor(R.color.purple_200))
      .click {
        Log.d(TAG, "click AAA")
      }
      .color(msg2, getColor(R.color.black))
      .click {
        Log.d(TAG, "click BBB")
      }
      .size(dpToPx(this, 20))
      .color(msg3, getColor(R.color.teal_200))
      .bold()
      .click(msg4) {
        Log.d(TAG, "click DDD")
      }
      .inject(tvMsg)
  }

}