package io.github.chentao7v.span;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.Px;
import io.github.chentao7v.span.image.AlignImageSpan;

/**
 * Span 的配置。
 * <br>
 * 普通文字的配置：{@link Text}，通过 {@link Configs#text()} 完成。
 * <br>
 * 图片的配置：{@link Image}，通过 {@link Configs#image()} 完成。
 * <br>
 * create by chentao on 2023-02-26.
 */
public interface Config {

  int NONE = -1;

  /**
   * 点击事件
   */
  class Click<T extends Config> implements Config {
    @Nullable
    private View.OnClickListener listener;

    @Nullable
    View.OnClickListener getListener() {
      return listener;
    }

    public T click(@NonNull View.OnClickListener listener) {
      this.listener = listener;
      return (T) this;
    }
  }

  /**
   * 默认配置
   */
  final class Text extends Click<Text> {

    @ColorInt
    private int color = NONE;

    @Px
    private int size = NONE;

    private boolean bold = false;

    private boolean strikethrough = false;

    private boolean underline = false;

    @ColorInt
    private int backgroundColor = NONE;

    Text() {}

    int getColor() {
      return color;
    }

    int getSize() {
      return size;
    }

    boolean isBold() {
      return bold;
    }

    boolean isStrikethrough() {
      return strikethrough;
    }

    boolean isUnderline() {
      return underline;
    }

    int getBackgroundColor() {
      return backgroundColor;
    }

    public Text color(@ColorInt int color) {
      this.color = color;
      return this;
    }

    public Text bold() {
      this.bold = true;
      return this;
    }

    public Text size(@Px int size) {
      this.size = size;
      return this;
    }

    /**
     * 删除线
     */
    public Text strikethrough() {
      this.strikethrough = true;
      return this;
    }

    public Text underline() {
      this.underline = true;
      return this;
    }

    public Text backgroundColor(@ColorInt int color) {
      this.backgroundColor = color;
      return this;
    }

  }

  /**
   * 图片配置
   */
  final class Image extends Click<Image> {

    @Px
    private int width = NONE;

    @Nullable
    private Bitmap bitmap;

    @Nullable
    private Drawable drawable;

    @Nullable
    private String url;

    @AlignImageSpan.VerticalAlign
    private int verticalAlign = AlignImageSpan.CENTER;

    Image() {}

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

    public Image verticalAlign(@AlignImageSpan.VerticalAlign int verticalAlign) {
      this.verticalAlign = verticalAlign;
      return this;
    }

    int getWidth() {
      return width;
    }

    @Nullable
    Bitmap getBitmap() {
      return bitmap;
    }

    @Nullable
    Drawable getDrawable() {
      return drawable;
    }

    int getVerticalAlign() {
      return verticalAlign;
    }

    @Nullable
    String getUrl() {
      return url;
    }
  }

}
