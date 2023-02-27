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
   * @see PipelineFlow
   */
  public static PipelineFlow pipeline() {
    return new PipelineFlow();
  }

  /**
   * 通过索引的方式设置 Span。
   *
   * @see IndexerFlow
   */
  public static IndexerFlow indexer(CharSequence source) {
    return new IndexerFlow(source);
  }

  /**
   * 通过替换占位符的方式设置 Span。
   *
   * @param source 包含占位符 {@link PlaceholderFlow#HOLDER} 的字符串
   * @see PlaceholderFlow
   */
  public static PlaceholderFlow placeholder(String source) {
    return new PlaceholderFlow(source);
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
