package me.chentao.library.span;

import android.graphics.Typeface;
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

/**
 * create by chentao on 2023-02-23.
 */
public interface SpanPipeline<T, R> {

  /**
   * 给传入的 {@link CharSequence} 设置新的 Span 的类
   */
  abstract class NewSpan implements SpanPipeline<CharSequence, Spannable> {}

  final class ColorSpan extends NewSpan {

    public final int color;

    public ColorSpan(@ColorInt int color) {
      this.color = color;
    }

    @Override
    public Spannable apply(CharSequence source) {
      SpannableString spannable = new SpannableString(source);
      spannable.setSpan(new ForegroundColorSpan(color), 0, source.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
      return spannable;
    }
  }

  final class ClickSpan extends NewSpan {

    @NonNull
    private final OverlaySpan click;

    public ClickSpan(@ColorInt int color, @Nullable View.OnClickListener listener) {
      this.click = new OverlayClickSpan(color, listener);
    }

    @Override
    public Spannable apply(CharSequence source) {
      return click.apply(new SpannableString(source));
    }
  }

  /**
   * 给现有 {@link Spannable} 叠加 Span 的类
   */
  abstract class OverlaySpan implements SpanPipeline<Spannable, Spannable> {}

  final class BoldSpan extends OverlaySpan {

    @Override
    public Spannable apply(Spannable source) {
      source.setSpan(new StyleSpan(Typeface.BOLD), 0, source.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
      return source;
    }
  }

  final class SizeSpan extends OverlaySpan {

    private final int size;

    public SizeSpan(int size) {
      this.size = size;
    }

    @Override
    public Spannable apply(Spannable source) {
      source.setSpan(new AbsoluteSizeSpan(size), 0, source.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
      return source;
    }
  }

  final class OverlayClickSpan extends OverlaySpan {

    @ColorInt
    public final int color;

    private final View.OnClickListener listener;

    public OverlayClickSpan(@ColorInt int color, @Nullable View.OnClickListener listener) {
      this.color = color;
      this.listener = listener;
    }

    @Override
    public Spannable apply(Spannable source) {
      ClickableSpan click = new ClickableSpan() {
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
      };
      source.setSpan(click, 0, source.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
      return source;
    }
  }

  R apply(T source);
}
