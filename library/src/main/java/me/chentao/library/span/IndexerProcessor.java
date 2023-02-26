package me.chentao.library.span;

import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.Px;
import java.util.Collection;

/**
 * 索引 Span 接口
 * <br>
 * create by chentao on 2023-02-23.
 */
public interface IndexerProcessor {

  final class Color implements IndexerProcessor {
    @ColorInt
    private final int color;

    public Color(int color) {
      this.color = color;
    }

    @Override
    public void apply(Spannable spannable, int start, int end) {
      spannable.setSpan(new ForegroundColorSpan(color), start, end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
    }
  }

  final class Click implements IndexerProcessor {

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

  final class Bold implements IndexerProcessor {

    @Override
    public void apply(Spannable spannable, int start, int end) {
      spannable.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
    }
  }

  final class Size implements IndexerProcessor {

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
   * 本地图片
   */
  class Image implements IndexerProcessor {

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
      image.setScaleBounds(width);
      spannable.setSpan(image, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }
  }

  class DynamicProxy implements IndexerProcessor {

    @Nullable
    private Spannable spannable;
    private int start;
    private int end;

    @Px
    private final int size;

    public DynamicProxy(@Px int size) {
      this.size = size;
    }

    @Override
    public void apply(Spannable spannable, int start, int end) {
      this.spannable = spannable;
      this.start = start;
      this.end = end;
    }

    public void invalidate(@NonNull IndexerProcessor processor) {
      processor.apply(spannable, start, end);
    }

    @Px
    public int getSize() {
      return size;
    }
  }

  /**
   * 封装一段文本的各个 Span。
   */
  class Element {
    private final int start;
    private final int length;
    private final IndexerProcessor processor;

    public Element(IndexerProcessor processor, int start, int length) {
      this.processor = processor;
      this.start = start;
      this.length = length;
    }

    public int getStart() {
      return start;
    }

    public int getEnd() {
      return start + length;
    }

    public int getLength() {
      return length;
    }

    public IndexerProcessor processor() {
      return processor;
    }
  }

  /**
   * 实现该方法，给 {@link SpannableString} 的 [start,end) 之间的文本设置特定的 Span
   */
  void apply(Spannable spannable, int start, int end);

  /**
   * 给对应 {@link Spannable} 实现所有的 Span 效果
   *
   * @param elements Span 效果
   */
  static void applyAll(Spannable spannable, Collection<Element> elements) {
    for (Element element : elements) {
      IndexerProcessor indexer = element.processor();
      int start = element.getStart();
      int end = element.getEnd();
      indexer.apply(spannable, start, end);
    }
  }
}
