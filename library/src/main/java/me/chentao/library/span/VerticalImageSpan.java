package me.chentao.library.span;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.style.ImageSpan;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * create by chentao on 2023-02-24.
 */
public class VerticalImageSpan extends ImageSpan {

  private final int width;

  public VerticalImageSpan(@NonNull Bitmap b, int width) {
    super(b);
    this.width = width;
  }

  public VerticalImageSpan(@NonNull Drawable drawable, int width) {
    super(drawable);
    this.width = width;
  }

  public void refreshSize() {
    Drawable b = getDrawable();
    if (b == null) {
      return;
    }

    int height = b.getMinimumHeight() / b.getIntrinsicWidth() * width;
    b.setBounds(0, 0, width, height);
  }

  @Override
  public int getSize(@NonNull Paint paint, CharSequence text, int start, int end, @Nullable Paint.FontMetricsInt fm) {
    return width;
  }

  @Override
  public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, @NonNull Paint paint) {
    Drawable b = getDrawable();
    if (b == null) {
      return;
    }
    canvas.save();
    int transY = top + (bottom - top) / 2 - b.getBounds().height() / 2;
    canvas.translate(x, transY);
    b.draw(canvas);
    canvas.restore();
  }

}
