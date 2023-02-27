package me.chentao.span.simple

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import me.chentao.library.span.image.AlignImageSpan.BOTTOM
import me.chentao.library.span.image.AlignImageSpan.CENTER
import me.chentao.library.span.Configs
import me.chentao.library.span.Spans
import me.chentao.span.IMAGE_URL
import me.chentao.span.R

/**
 * create by chentao on 2023-02-23.
 */
class PlaceholderActivity : AppCompatActivity() {

  companion object {

    private const val TAG = "Placeholder"

    fun launch(context: Context) {
      val intent = Intent(context, PlaceholderActivity::class.java)
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

    val msg = "对不起，因您已注册的{$}，我们需要核实您的身份{$}及手机号码信息后注册，请插入对应的手机卡{$}并打开该改手机卡{$}流量开关哟。注册成功后，此手机号才会仅分配给您使用。"

    Spans.placeholder(msg)
      .with("188****8888",
        Configs.ofDefault()
          .color(colorRes(R.color.teal_200))
          .size(30.dp)
          .click {
            Log.i(TAG, "click A ~~~")
          })
      .with("199****9999",
        Configs.ofDefault()
          .color(colorRes(R.color.teal_700))
          .size(25.dp)
          .click {
            Log.i(TAG, "click B B !!!")
          })
      .withImage(
        Configs.ofImage()
          .drawable(drawableRes(R.drawable.mini_icon3))
          .verticalAlign(CENTER)
          .width(50.dp)
      )
      .withImage(
        Configs.ofImage()
          .url(IMAGE_URL)
          .verticalAlign(BOTTOM)
          .width(80.dp)
          .click {
            Log.d(TAG, "哈哈哈哈哈")
          }
      )
      .loader(GlideImageLoader())
      .inject(tvMsg)
  }

}