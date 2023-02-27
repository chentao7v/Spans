package me.chentao.library.span.image;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.style.ImageSpan;
import android.util.Log;
import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;

/**
 * 支持自动对齐的 {@link ImageSpan}。
 * 对齐方式有一下三种：
 * <br>{@link #BASELINE} 图片和文字的 baseline 对齐，对齐时也模拟了图片的 baseline；
 * <br>{@link #BOTTOM} 图片的底部和文字的底部对齐
 * <br>{@link #BOTTOM} 图片的文字居中对齐
 * <br>
 * create by chentao on 2023-02-24.
 */
public class AlignImageSpan extends ImageSpan {

  private static final String TAG = "AlignImageSpan";

  public static final int BOTTOM = 0;
  public static final int BASELINE = 1;
  public static final int CENTER = 2;

  @IntDef({ BOTTOM, BASELINE, CENTER })
  @Retention(RetentionPolicy.SOURCE)
  public @interface VerticalAlign {}

  private WeakReference<Drawable> drawableRef;

  @VerticalAlign
  private final int verticalAlign;

  public AlignImageSpan(@NonNull Bitmap bitmap, @VerticalAlign int verticalAlign) {
    super(bitmap);
    this.verticalAlign = verticalAlign;
  }

  public AlignImageSpan(@NonNull Drawable drawable, @VerticalAlign int verticalAlign) {
    super(drawable);
    this.verticalAlign = verticalAlign;
  }

  /**
   * 设置原始的宽高
   */
  public void setBounds() {
    Drawable b = getCachedDrawable();
    if (b == null) {
      return;
    }

    int width = b.getIntrinsicWidth();
    int height = b.getMinimumHeight();
    setBounds(b, width, height);
  }

  /**
   * 所指缩放的宽高。高度将自动完成比例缩放
   */
  public void setScaleBounds(int realWidth) {
    Drawable b = getCachedDrawable();
    if (b == null) {
      return;
    }

    int realHeight = (int) (b.getIntrinsicHeight() * realWidth / (b.getIntrinsicWidth() * 1.f) + 0.5f);
    setBounds(b, realWidth, realHeight);
  }

  private void setBounds(Drawable b, int width, int height) {
    b.setBounds(0, 0, width, height);
  }

  @Override
  public int getSize(Paint paint, CharSequence text, int start, int end, @Nullable Paint.FontMetricsInt fm) {
    Drawable drawable = getCachedDrawable();
    if (drawable == null) {
      return 0;
    }

    Rect rect = drawable.getBounds();
    Log.d(TAG, "drawable：width=" + rect.width() + ", height=" + rect.height());

    if (fm != null) {

      Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
      Log.i(TAG, "origin font metrics -> " + fontMetrics.toString());

      // 字高
      int fontHeight = fontMetrics.bottom - fontMetrics.top;
      // Drawable 高度
      int drawableHeight = rect.height();

      if (verticalAlign == CENTER) {
        int centerY = (int) (fontMetrics.top + fontHeight / 2f);
        fm.ascent = (int) (centerY - drawableHeight / 2f);
        fm.descent = (int) (centerY + drawableHeight / 2f);
      } else if (verticalAlign == BASELINE) {
        float topPercent = Math.abs(fontMetrics.top) / (fontHeight * 1.f);
        fm.ascent = (int) (drawableHeight * topPercent * -1);
        fm.descent = (int) (drawableHeight * (1f - topPercent));
      } else {
        fm.descent = fontMetrics.descent;
        fm.ascent = fontMetrics.descent - drawableHeight;
      }

      fm.top = fm.ascent;
      fm.bottom = fm.descent;
    }
    return rect.right;
  }

  @Override
  public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, @NonNull Paint paint) {
    Drawable b = getCachedDrawable();
    if (b == null) {
      return;
    }

    Log.d(TAG, "draw -> top = " + top + ", bottom =  " + bottom + ", y(baseline) = " + y);

    canvas.save();

    int drawableHeight = b.getBounds().height();

    Log.i(TAG, "drawable height=" + drawableHeight);

    Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
    if (verticalAlign == CENTER) {
      int offset = (int) (-(fontMetrics.top + fontMetrics.bottom) / 2f);
      canvas.translate(x, y - drawableHeight / 2f - offset);
    } else if (verticalAlign == BASELINE) {
      float percent = (y - top) / (1f * (bottom - top));
      canvas.translate(x, y - drawableHeight * percent);
    } else {
      canvas.translate(x, y - drawableHeight + fontMetrics.descent);
    }

    b.draw(canvas);
    canvas.restore();
  }

  private Drawable getCachedDrawable() {
    WeakReference<Drawable> wr = drawableRef;
    Drawable d = null;

    if (wr != null) {d = wr.get();}

    if (d == null) {
      d = getDrawable();
      drawableRef = new WeakReference<>(d);
    }

    return d;
  }

}
