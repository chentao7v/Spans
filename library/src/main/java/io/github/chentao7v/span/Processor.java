package io.github.chentao7v.span;

import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.Px;
import io.github.chentao7v.span.image.AlignImageSpan;
import java.util.ArrayList;
import java.util.List;

/**
 * Span 处理器。
 * <br>
 * create by chentao on 2023-02-23.
 */
public interface Processor {

  /**
   * 处理效果处理器。可完成对一组效果的处理。
   */
  final class Compose implements Processor {

    @NonNull
    private final List<Processor> processors = new ArrayList<>();

    /**
     * 添加一个处理器。
     */
    public void addProcessor(@NonNull Processor processor) {
      this.processors.add(processor);
    }

    /**
     * 完成所有效果的处理
     */
    @Override
    public void apply(Spannable spannable, int start, int end) {
      for (Processor processor : this.processors) {
        processor.apply(spannable, start, end);
      }
    }
  }

  /**
   * 颜色处理器
   */
  final class Color implements Processor {
    @ColorInt
    private final int color;

    public Color(@ColorInt int color) {
      this.color = color;
    }

    @Override
    public void apply(Spannable spannable, int start, int end) {
      spannable.setSpan(new ForegroundColorSpan(color), start, end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
    }
  }

  /**
   * 点击事件处理器
   */
  final class Click implements Processor {

    @ColorInt
    private final int color;

    @Nullable
    private final View.OnClickListener listener;

    public Click(int color, @Nullable View.OnClickListener listener) {
      this.color = color;
      this.listener = listener;
    }

    @Override
    public void apply(Spannable spannable, int start, int end) {
      spannable.setSpan(new ClickableSpan() {
        @Override
        public void onClick(@NonNull View view) {
          if (listener != null) {
            listener.onClick(view);
          }
        }

        @Override
        public void updateDrawState(@NonNull TextPaint ds) {
          if (color != -1) {
            ds.setColor(color);
          }
        }
      }, start, end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
    }
  }

  /**
   * 加粗处理器
   */
  final class Bold implements Processor {

    @Override
    public void apply(Spannable spannable, int start, int end) {
      spannable.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
    }
  }

  /**
   * 下划线
   */
  final class Underline implements Processor {

    @Override
    public void apply(Spannable spannable, int start, int end) {
      spannable.setSpan(new UnderlineSpan(), start, end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
    }
  }

  /**
   * 删除线
   */
  final class Strikethrough implements Processor {

    @Override
    public void apply(Spannable spannable, int start, int end) {
      spannable.setSpan(new StrikethroughSpan(), start, end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
    }
  }

  /**
   * 背景色
   */
  final class BackgroundColor implements Processor {

    @ColorInt
    private final int color;

    public BackgroundColor(@ColorInt int color) {
      this.color = color;
    }

    @Override
    public void apply(Spannable spannable, int start, int end) {
      spannable.setSpan(new BackgroundColorSpan(color), start, end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
    }
  }

  /**
   * 文字大小处理器
   */
  final class Size implements Processor {

    private final int size;

    public Size(int size) {
      this.size = size;
    }

    @Override
    public void apply(Spannable spannable, int start, int end) {
      spannable.setSpan(new AbsoluteSizeSpan(size), start, end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
    }
  }

  /**
   * 本地图片处理器
   */
  class Image implements Processor {

    @NonNull
    private final AlignImageSpan image;

    private final int width;

    public Image(@NonNull Bitmap bitmap, int width, @AlignImageSpan.VerticalAlign int verticalAlign) {
      image = new AlignImageSpan(bitmap, verticalAlign);
      this.width = width;
    }

    public Image(@NonNull Drawable drawable, int width, @AlignImageSpan.VerticalAlign int verticalAlign) {
      image = new AlignImageSpan(drawable, verticalAlign);
      this.width = width;
    }

    @Override
    public void apply(Spannable spannable, int start, int end) {
      if (width == Config.NONE) {
        image.setBounds();
      } else {
        image.setScaleBounds(width);
      }
      spannable.setSpan(image, start, end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
    }
  }

  /**
   * 异步代理处理器。用来处理异步下载图片的场景。
   */
  class AsyncProxy implements Processor {

    private int start;
    private int end;

    @Px
    private final int size;

    @AlignImageSpan.VerticalAlign
    private final int verticalAlign;

    public AsyncProxy(@Px int size, @AlignImageSpan.VerticalAlign int verticalAlign) {
      this.size = size;
      this.verticalAlign = verticalAlign;
    }

    @Override
    public void apply(Spannable spannable, int start, int end) {
      this.start = start;
      this.end = end;
    }

    /**
     * 图片下载完成后，再更新 Span。
     */
    public void update(@NonNull Spannable spannable, @NonNull Processor processor) {
      processor.apply(spannable, start, end);
    }

    @Px
    public int size() {
      return size;
    }

    public int verticalAlign() {
      return verticalAlign;
    }
  }

  /**
   * 处理器元素
   */
  class Element {
    private final int start;
    private final int length;
    private final Processor processor;

    public Element(Processor processor, int start, int length) {
      this.processor = processor;
      this.start = start;
      this.length = length;
    }

    public int start() {
      return start;
    }

    public int end() {
      return start + length;
    }

    public int length() {
      return length;
    }

    public Processor processor() {
      return processor;
    }
  }

  /**
   * 实现该方法，给 {@link SpannableString} 的 [start,end) 之间的文本设置特定的 Span
   */
  void apply(Spannable spannable, int start, int end);

}
