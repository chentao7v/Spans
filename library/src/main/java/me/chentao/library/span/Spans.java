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
   * 通过流水线的方式设置 Span。
   *
   * @see PipelineSpans
   */
  public static PipelineSpans pipeline() {
    return new PipelineSpans();
  }

  /**
   * 通过索引的方式设置 Span。
   *
   * @see IndexerSpans
   */
  public static IndexerSpans indexer(CharSequence source) {
    return new IndexerSpans(source);
  }

  /**
   * 通过替换占位符的方式设置 Span。
   *
   * @param source 包含占位符 {@link PlaceholderSpans#HOLDER} 的字符串
   * @see PlaceholderSpans
   */
  public static PlaceholderSpans placeholder(String source) {
    return new PlaceholderSpans(source);
  }

  private static void markClickable(@NonNull TextView textView) {
    textView.setMovementMethod(LinkMovementMethod.getInstance());
    textView.setLongClickable(false);
    textView.setHighlightColor(Color.TRANSPARENT);
  }

  static void inject(@NonNull TextView textView, Spannable spannable, boolean clickable) {
    textView.setText(spannable);
    if (clickable) {
      markClickable(textView);
    }
  }

}
