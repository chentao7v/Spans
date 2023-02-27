package me.chentao.span.simple

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import me.chentao.library.span.Configs
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
      .add(12, 21, Configs.text().color(colorRes(R.color.purple_700)))
      .add(3, 21, Configs.text().bold())
      .add(4, 21, Configs.text().size(18.dp))
      .add(7, 30, Configs.text().click {
        Toast.makeText(this, "哈哈哈哈", Toast.LENGTH_SHORT).show()
      })
      .addImage(7, Configs.image().drawable(drawableRes(R.drawable.mini_icon3)).width(50.dp))
      .inject(tvMsg)
  }

}