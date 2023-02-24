package me.chentao.library.span;

import android.text.Spannable;
import android.text.SpannableString;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.Px;
import java.util.ArrayDeque;
import java.util.Deque;

/**
 * 通过占位符的方式设置 Span。
 * <br>
 * 使用 {@link #HOLDER} 作为占位符，每次调用 {@link #color(String, int)}、{@link #click(String, int, View.OnClickListener)}
 * 等方法时，会按照调用方法的先后顺序依次替换文本最前面的占位符。
 * <br>
 * create by chentao on 2023-02-23.
 */
public final class PlaceholderSpans {

  public static final String HOLDER = "{$}";

  private final Deque<SpanIndexer.Element> elements;

  @NonNull
  private StringBuilder source;

  private int startIndex = 0;

  private boolean clickable = false;

  PlaceholderSpans(@NonNull String source) {
    this.source = new StringBuilder(source);
    this.elements = new ArrayDeque<>();
  }

  /**
   * 用指定文本替换占位符，并给改部分文本设置颜色
   *
   * @param data 替换占位符的文本
   */
  public PlaceholderSpans color(String data, @ColorInt int color) {
    handleHeadPlaceHolder(data, new SpanIndexer.ColorSpan(color));
    return this;
  }

  /**
   * 用指定的的文本替换占位符，并给该部分文字设置颜色与点击事件
   */
  public PlaceholderSpans click(String data, @ColorInt int color, @Nullable View.OnClickListener listener) {
    this.clickable = true;
    handleHeadPlaceHolder(data, new SpanIndexer.ClickSpan(color, listener));
    return this;
  }

  /**
   * 用指定的的文本替换占位符，并给该部分文字设置点击事件
   */
  public PlaceholderSpans click(String data, @Nullable View.OnClickListener listener) {
    return click(data, -1, listener);
  }

  /**
   * 给 "上一行" 文本设置颜色与点击事件
   */
  public PlaceholderSpans click(@ColorInt int color, @Nullable View.OnClickListener listener) {
    this.clickable = true;
    applySpanForLast(new SpanIndexer.ClickSpan(color, listener));
    return this;
  }

  /**
   * 给 "上一行" 设置点击事件
   */
  public PlaceholderSpans click(@Nullable View.OnClickListener listener) {
    return click(-1, listener);
  }

  public PlaceholderSpans clickable() {
    this.clickable = true;
    return this;
  }

  /**
   * 将 "上一行" 文本加粗
   */
  public PlaceholderSpans bold() {
    applySpanForLast(new SpanIndexer.BoldSpan());
    return this;
  }

  /**
   * 给 "上一行" 文本设置大小
   */
  public PlaceholderSpans size(@Px int size) {
    applySpanForLast(new SpanIndexer.SizeSpan(size));
    return this;
  }

  private void applySpanForLast(SpanIndexer span) {
    SpanIndexer.Element last = elements.getLast();
    if (last == null) {
      return;
    }
    elements.add(new SpanIndexer.Element(span, last.getStart(), last.getLength()));
  }

  /**
   * 返回处理后的 Span
   */
  public Spannable end() {
    Spannable spannable = new SpannableString(this.source);
    SpanIndexer.applyAll(spannable, elements);

    elements.clear();
    return spannable;
  }

  public void inject(TextView textView) {
    Spans.inject(textView, end(), clickable);
  }

  /**
   * 处理头部的占位符
   */
  private void handleHeadPlaceHolder(String data, SpanIndexer indexer) {
    int index = source.indexOf(HOLDER, startIndex);
    if (index == -1) {
      return;
    }
    startIndex = index;
    source = source.replace(index, index + HOLDER.length(), data);
    elements.add(new SpanIndexer.Element(indexer, index, data.length()));
  }

}
