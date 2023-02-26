package me.chentao.library.span;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.Px;

/**
 * create by chentao on 2023-02-26.
 */
public interface Config {

  int NONE = -1;

  static Default ofDefault() {
    return new Default();
  }

  static Image ofImage(){
    return new Image();
  }

  class Click implements Config {
    @Nullable
    private View.OnClickListener listener;

    @Nullable
    public View.OnClickListener getListener() {
      return listener;
    }

    public Click click(@NonNull View.OnClickListener listener) {
      this.listener = listener;
      return this;
    }
  }

  /**
   * 默认配置
   */
  final class Default extends Click {

    @ColorInt
    private int color = NONE;

    @Px
    private int size = NONE;

    private boolean bold = false;

    private Default() {}

    public int getColor() {
      return color;
    }

    public int getSize() {
      return size;
    }

    public Default color(@ColorInt int color) {
      this.color = color;
      return this;
    }

    public Default bold() {
      this.bold = true;
      return this;
    }

    public Default size(@Px int size) {
      this.size = size;
      return this;
    }

    public boolean isBold() {
      return bold;
    }
  }

  /**
   * 图片配置
   */
  final class Image extends Click {

    @Px
    private int width = NONE;

    @Nullable
    private Bitmap bitmap;

    @Nullable
    private Drawable drawable;

    @Nullable
    private String url;

    @AlignImageSpan.VerticalAlign
    private int verticalAlign = AlignImageSpan.VERTICAL_ALIGN_CENTER;

    private Image() {}

    public Image width(@Px int width) {
      this.width = width;
      return this;
    }

    public Image bitmap(@NonNull Bitmap bitmap) {
      this.bitmap = bitmap;
      return this;
    }

    public Image drawable(@NonNull Drawable drawable) {
      this.drawable = drawable;
      return this;
    }

    public Image url(@NonNull String url) {
      this.url = url;
      return this;
    }

    public int getWidth() {
      return width;
    }

    @Nullable
    public Bitmap getBitmap() {
      return bitmap;
    }

    @Nullable
    public Drawable getDrawable() {
      return drawable;
    }

    public Image verticalAlign(@AlignImageSpan.VerticalAlign int verticalAlign) {
      this.verticalAlign = verticalAlign;
      return this;
    }

    public int getVerticalAlign() {
      return verticalAlign;
    }

    @Nullable
    public String getUrl() {
      return url;
    }
  }

}
