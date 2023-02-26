package me.chentao.library.span;

import android.graphics.Color;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;
import androidx.annotation.NonNull;

/**
 * 对 Span 进行处理的工具。
 * <br>
 * create by chentao on 2023-02-23.
 */
public final class Spans {

  private Spans() {}

  /**
   * 通过流水线依次拼接的方式设置 Span。
   *
   * @see SpliceSpanFlow
   */
  public static SpliceSpanFlow pipeline() {
    return new SpliceSpanFlow();
  }

  /**
   * 通过索引的方式设置 Span。
   *
   * @see IndexerSpanFlow
   */
  public static IndexerSpanFlow indexer(CharSequence source) {
    return new IndexerSpanFlow(source);
  }

  /**
   * 通过替换占位符的方式设置 Span。
   *
   * @param source 包含占位符 {@link PlaceholderSpanFlow#HOLDER} 的字符串
   * @see PlaceholderSpanFlow
   */
  public static PlaceholderSpanFlow placeholder(String source) {
    return new PlaceholderSpanFlow(source);
  }

  private static void markClickable(@NonNull TextView textView) {
    textView.setMovementMethod(LinkMovementMethod.getInstance());
    textView.setLongClickable(false);
    textView.setHighlightColor(Color.TRANSPARENT);
  }

  static void inject(@NonNull TextView textView, Spannable spannable, boolean clickable, boolean async) {
    if (async) {
      textView.setText(spannable, TextView.BufferType.SPANNABLE);
    } else {
      textView.setText(spannable);
    }
    if (clickable) {
      markClickable(textView);
    }
  }

}
