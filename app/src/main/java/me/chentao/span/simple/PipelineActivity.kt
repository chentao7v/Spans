package me.chentao.span.simple

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import me.chentao.library.span.Configs
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

    val msg1 = "文本AAAA\n"
    val msg2 = "文本BBBBBBB\n"
    val msg3 = "文本CCCCCCCCCCC\n"
    val msg4 = "文本DDDDD"

    Spans.pipeline()
      .add(msg1, Configs.ofDefault().color(colorRes(R.color.purple_200)).click {
        Log.d(TAG, "click AAA")
      })
      .add(msg2, Configs.ofDefault().color(colorRes(R.color.black)).size(20.dp).click { Log.d(TAG, "click BBB") })
      .add(msg3, Configs.ofDefault().color(colorRes(R.color.teal_200)).bold())
      .addImage(Configs.ofImage().drawable(drawableRes(R.drawable.mini_icon6)))
      .add(msg4, Configs.ofDefault().color(colorRes(R.color.teal_700)))
      .inject(tvMsg)
  }

}