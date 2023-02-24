package me.chentao.library.span;

import android.text.Spannable;
import android.text.SpannableString;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.Px;

/**
 * 通过索引的方式设置 Span
 * <br>
 * 通过指定 {@code start/end}，给这区间的文本[含头部包尾] 设置指定的 Span。
 * <br>
 * create by chentao on 2023-02-23.
 */
public final class IndexerSpanFlow {

  @NonNull
  private final Spannable spannable;

  private boolean clickable = false;

  IndexerSpanFlow(@NonNull CharSequence source) {
    this.spannable = new SpannableString(source);
  }

  /**
   * 给指定区间 [start,end) 的文本设置颜色
   */
  public IndexerSpanFlow color(@ColorInt int color, int start, int end) {
    new IndexerProcessor.Color(color).apply(spannable, start, end);
    return this;
  }

  /**
   * 给指定区间 [start,end) 的文本设置点击事件与颜色
   */
  public IndexerSpanFlow click(@ColorInt int color, int start, int end, @Nullable View.OnClickListener listener) {
    clickable = true;
    new IndexerProcessor.Click(color, listener).apply(spannable, start, end);
    return this;
  }

  /**
   * 给指定区间 [start,end) 的文本设置点击事件
   */
  public IndexerSpanFlow click(int start, int end, @Nullable View.OnClickListener listener) {
    return click(-1, start, end, listener);
  }

  /**
   * 指定区间 [start,end) 的文本的文字大小
   */
  public IndexerSpanFlow size(@Px int size, int start, int end) {
    new IndexerProcessor.Size(size).apply(spannable, start, end);
    return this;
  }

  /**
   * 对指定区间 [start,end) 的文本加粗
   */
  public IndexerSpanFlow bold(int start, int end) {
    new IndexerProcessor.Bold().apply(spannable, start, end);
    return this;
  }

  public IndexerSpanFlow clickable() {
    this.clickable = true;
    return this;
  }

  public void inject(@NonNull TextView textView) {
    Spans.inject(textView, spannable, clickable);
  }

}
