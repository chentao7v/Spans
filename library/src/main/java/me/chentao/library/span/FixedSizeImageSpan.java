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
public class FixedSizeImageSpan extends ImageSpan {

  public static final int VERTICAL_ALIGN_BOTTOM = 0;
  public static final int VERTICAL_ALIGN_BASELINE = 1;
  public static final int VERTICAL_ALIGN_CENTER = 2;

  @IntDef({ VERTICAL_ALIGN_BOTTOM, VERTICAL_ALIGN_BASELINE, VERTICAL_ALIGN_CENTER })
  @Retention(RetentionPolicy.SOURCE)
  public @interface VerticalAlign {}

  private WeakReference<Drawable> mDrawableRef;

  private final int width;
  @VerticalAlign
  private final int verticalAlign;

  public FixedSizeImageSpan(@NonNull Bitmap b, int width, @VerticalAlign int verticalAlign) {
    super(b);
    this.width = width;
    this.verticalAlign = verticalAlign;
  }

  public FixedSizeImageSpan(@NonNull Drawable drawable, int width, @VerticalAlign int verticalAlign) {
    super(drawable);
    this.width = width;
    this.verticalAlign = verticalAlign;
  }

  public void refreshSize() {
    Drawable b = getCachedDrawable();
    if (b == null) {
      return;
    }

    int height = b.getMinimumHeight() / b.getIntrinsicWidth() * width;
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
      int fontHeight = fontMetrics.descent - fontMetrics.ascent;
      // Drawable 高度
      int drawableHeight = rect.height();

      int centerY = fontMetrics.top + fontHeight / 2;

      if (verticalAlign == VERTICAL_ALIGN_CENTER) {
        fm.ascent = centerY - drawableHeight / 2;
        fm.descent = centerY + drawableHeight / 2;
      } else if (verticalAlign == VERTICAL_ALIGN_BASELINE) {
        int top = (int) (drawableHeight / (1 + (Math.abs(fontMetrics.bottom / (fontMetrics.top * 1.0f)))));
        int bottom = drawableHeight - top;
        fm.ascent = top * -1;
        fm.descent = bottom;
      } else {
        fm.ascent = centerY + fontMetrics.bottom + drawableHeight;
        fm.descent = centerY + fontMetrics.bottom;
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
    canvas.save();
    canvas.translate(x, 0);
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
