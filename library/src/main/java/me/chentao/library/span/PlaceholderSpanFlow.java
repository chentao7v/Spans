package me.chentao.library.span;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
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
public final class PlaceholderSpanFlow {

  public static final String HOLDER = "{$}";

  private final Deque<IndexerProcessor.Element> elements;

  @NonNull
  private StringBuilder source;

  private int startIndex = 0;

  private boolean clickable = false;

  @NonNull
  private final AsyncImageSpanEngine asyncEngine;

  PlaceholderSpanFlow(@NonNull String source) {
    this.source = new StringBuilder(source);
    this.elements = new ArrayDeque<>();
    this.asyncEngine = new AsyncImageSpanEngine();
  }

  public PlaceholderSpanFlow loader(@NonNull SpanImageLoader loader) {
    this.asyncEngine.setImageLoader(loader);
    return this;
  }

  /**
   * 用指定文本替换占位符，并给改部分文本设置颜色
   *
   * @param data 替换占位符的文本
   */
  public PlaceholderSpanFlow color(String data, @ColorInt int color) {
    handleHeadPlaceHolder(data, new IndexerProcessor.Color(color));
    return this;
  }

  /**
   * 用指定的的文本替换占位符，并给该部分文字设置颜色与点击事件
   */
  public PlaceholderSpanFlow click(String data, @ColorInt int color, @Nullable View.OnClickListener listener) {
    this.clickable = true;
    handleHeadPlaceHolder(data, new IndexerProcessor.Click(color, listener));
    return this;
  }

  /**
   * 用指定的的文本替换占位符，并给该部分文字设置点击事件
   */
  public PlaceholderSpanFlow click(String data, @Nullable View.OnClickListener listener) {
    return click(data, -1, listener);
  }

  /**
   * 给 "上一行" 文本设置颜色与点击事件
   */
  public PlaceholderSpanFlow click(@ColorInt int color, @Nullable View.OnClickListener listener) {
    this.clickable = true;
    applySpanForLast(new IndexerProcessor.Click(color, listener));
    return this;
  }

  /**
   * 给 "上一行" 设置点击事件
   */
  public PlaceholderSpanFlow click(@Nullable View.OnClickListener listener) {
    return click(-1, listener);
  }

  public PlaceholderSpanFlow clickable() {
    this.clickable = true;
    return this;
  }

  /**
   * 将 "上一行" 文本加粗
   */
  public PlaceholderSpanFlow bold() {
    applySpanForLast(new IndexerProcessor.Bold());
    return this;
  }

  /**
   * 给 "上一行" 文本设置大小
   */
  public PlaceholderSpanFlow size(@Px int size) {
    applySpanForLast(new IndexerProcessor.Size(size));
    return this;
  }

  /**
   * 用指定的图片替换替换占位符
   */
  public PlaceholderSpanFlow image(Drawable drawable, @Px int size) {
    // 添加一个空文本
    handleHeadPlaceHolder(" ", new IndexerProcessor.Image(drawable, size, AlignImageSpan.VERTICAL_ALIGN_CENTER));
    return this;
  }

  public PlaceholderSpanFlow image(Bitmap bitmap, @Px int size) {
    handleHeadPlaceHolder(" ", new IndexerProcessor.Image(bitmap, size, AlignImageSpan.VERTICAL_ALIGN_CENTER));
    return this;
  }

  public PlaceholderSpanFlow image(@NonNull String url, @Px int size) {
    IndexerProcessor.DynamicProxy proxy = new IndexerProcessor.DynamicProxy(size);
    handleHeadPlaceHolder(" ", proxy);
    asyncEngine.register(url, proxy);
    return this;
  }

  private void applySpanForLast(IndexerProcessor span) {
    IndexerProcessor.Element last = elements.getLast();
    if (last == null) {
      return;
    }
    elements.add(new IndexerProcessor.Element(span, last.getStart(), last.getLength()));
  }

  /**
   * 返回处理后的 Span
   */
  public Spannable end() {
    Spannable spannable = new SpannableString(this.source);
    IndexerProcessor.applyAll(spannable, elements);

    return spannable;
  }

  public void inject(TextView textView) {
    boolean async = asyncEngine.containsAsync();
    Spannable spannable = end();
    Spans.inject(textView, spannable, clickable, false);
    if (async) {
      asyncEngine.loadAllImages(new AsyncImageSpanEngine.Listener() {
        @Override
        public void onFinish() {
          textView.setText(spannable);
        }
      });
    }
  }

  /**
   * 处理头部的占位符
   */
  private void handleHeadPlaceHolder(String data, IndexerProcessor indexer) {
    int index = source.indexOf(HOLDER, startIndex);
    if (index == -1) {
      return;
    }
    startIndex = index;
    source = source.replace(index, index + HOLDER.length(), data);
    elements.add(new IndexerProcessor.Element(indexer, index, data.length()));
  }

}
