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

    val msg = "对不起，因您已注册的{$}，我们需要核实您的身份及手机号码信息后注册，请插入对应的手机卡{$}并打开该改手机卡{$}流量开关哟。注册成功后，此手机号才会仅分配给您使用。"

    Spans.placeholder(msg)
      .color("188****8888", getColor(R.color.teal_200))
      .size(dpToPx(this, 30))
      .click {
        Log.i(TAG, "click A ~~~")
      }
      .color("199****9999", getColor(R.color.teal_700))
      .click {
        Log.i(TAG, "click B B !!!")
      }
      .image(getDrawable(R.drawable.mini_icon1), dpToPx(this, 60))
      .size(dpToPx(this, 25))
      .inject(tvMsg)
  }

}