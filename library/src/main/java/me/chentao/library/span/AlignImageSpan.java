package me.chentao.library.span;

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
 * 固定图片宽度的 Span。
 * <br>
 * 图标高度会根据图片的宽高比自动计算
 * <br>
 * create by chentao on 2023-02-24.
 */
public class AlignImageSpan extends ImageSpan {

  public static final int VERTICAL_ALIGN_BOTTOM = 0;
  public static final int VERTICAL_ALIGN_BASELINE = 1;
  public static final int VERTICAL_ALIGN_CENTER = 2;

  @IntDef({ VERTICAL_ALIGN_BOTTOM, VERTICAL_ALIGN_BASELINE, VERTICAL_ALIGN_CENTER })
  @Retention(RetentionPolicy.SOURCE)
  public @interface VerticalAlign {}

  private WeakReference<Drawable> mDrawableRef;

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

  public void setBounds() {
    Drawable b = getCachedDrawable();
    if (b == null) {
      return;
    }

    int width = b.getIntrinsicWidth();
    int height = b.getMinimumHeight();
    setBounds(b, width, height);
  }

  public void setScaleBounds(int width) {
    Drawable b = getCachedDrawable();
    if (b == null) {
      return;
    }

    int realWidth = width;
    int realHeight = (int) (b.getIntrinsicHeight() * width / (b.getIntrinsicWidth() * 1.f) + 0.5f);
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
    Log.d("UUUUU", "---> " + rect.toString());

    if (fm != null) {

      Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
      Log.i("UUUUU", "origin font metrics -> " + fontMetrics.toString());

      // 字高
      int fontHeight = fontMetrics.bottom - fontMetrics.top;
      // Drawable 高度
      int drawableHeight = rect.height();

      int centerY = fontMetrics.top + fontHeight / 2;

      if (verticalAlign == VERTICAL_ALIGN_CENTER) {
        fm.ascent = centerY - drawableHeight / 2;
        fm.descent = centerY + drawableHeight / 2;
      } else if (verticalAlign == VERTICAL_ALIGN_BASELINE) {
        float topPercent = Math.abs(fontMetrics.top) / (fontHeight * 1.f);
        fm.ascent = (int) (drawableHeight * topPercent * -1);
        fm.descent = (int) (drawableHeight * (1 - topPercent));
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

    Log.d("UUUUU", "draw -> top = " + top + ", bottom =  " + bottom + ", y(baseline) = " + y);

    canvas.save();
    canvas.translate(x, top);
    b.draw(canvas);
    canvas.restore();
  }

  private Drawable getCachedDrawable() {
    WeakReference<Drawable> wr = mDrawableRef;
    Drawable d = null;

    if (wr != null) {d = wr.get();}

    if (d == null) {
      d = getDrawable();
      mDrawableRef = new WeakReference<>(d);
    }

    return d;
  }

}
