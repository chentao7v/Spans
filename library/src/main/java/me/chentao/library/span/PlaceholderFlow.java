package me.chentao.library.span;

import android.text.Spannable;
import android.widget.TextView;
import androidx.annotation.NonNull;
import me.chentao.library.span.image.SpanImageLoader;

/**
 * 通过占位符的方式设置 Span。
 * <br>
 * create by chentao on 2023-02-23.
 */
public final class PlaceholderFlow {

  public static final String HOLDER = "{$}";

  @NonNull
  private final PlaceholderEngine engine;

  PlaceholderFlow(@NonNull String source) {
    engine = new PlaceholderEngine(source);
  }

  /**
   * 使用 data 替换对应占位符，并给 data 设置 Span。
   *
   * @param data 文本
   * @param config Span 配置
   */
  public PlaceholderFlow with(@NonNull String data, @NonNull Config.Text config) {
    engine.replace(data, config);
    return this;
  }

  /**
   * 使用图片替换对应占位符，
   *
   * @param image 图片的配置
   */
  public PlaceholderFlow withImage(@NonNull Config.Image image) {
    engine.replace(" ", image);
    return this;
  }

  /**
   * @see Engine#setImageLoader(SpanImageLoader)
   */
  public PlaceholderFlow loader(@NonNull SpanImageLoader loader) {
    engine.setImageLoader(loader);
    return this;
  }

  /**
   * @see Engine#execute()
   */
  public Spannable execute() {
    return engine.execute();
  }

  /**
   * @see Engine#inject(TextView)
   */
  public void inject(TextView textView) {
    engine.inject(textView);
  }

}
