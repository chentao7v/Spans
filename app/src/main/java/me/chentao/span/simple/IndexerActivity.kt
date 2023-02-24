package me.chentao.span.simple

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import me.chentao.library.span.Spans
import me.chentao.span.R

/**
 * create by chentao on 2023-02-23.
 */
class IndexerActivity : AppCompatActivity() {

  companion object {

    fun launch(context: Context) {
      val intent = Intent(context, IndexerActivity::class.java)
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

    val msg = "这是一段长文字，需要在第12到XXXX20处高亮，就撒快递费就开了撒金飞达拉卡萨激发的设计阿吉撒京东客服时间啊复健科洒基放大机洒基放大了刷卡机"
    Spans.indexer(msg)
      .color(colorRes(R.color.purple_700), 12, 21)
      .bold(12, 21)
      .size(18.dp, 12, 21)
      .inject(tvMsg)

  }

}