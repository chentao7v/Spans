package me.chentao.library.span;

import android.text.Spannable;
import android.widget.TextView;
import androidx.annotation.NonNull;
import me.chentao.library.span.image.SpanImageLoader;

/**
 * 通过拼接的方式设置 Span。
 * <br>
 * 每添加一段文本，就需要给改文本指定 Span，直到结束。
 * <br>
 * create by chentao on 2023-02-23.
 */
public final class PipelineFlow implements Flow<PipelineFlow> {

  @NonNull
  private final PipelineEngine engine = new PipelineEngine();

  /**
   * 添加文本，并给该文本设置 Span
   *
   * @param config Span 对应配置
   */
  public PipelineFlow add(@NonNull String source, @NonNull Config.Text config) {
    engine.add(source, config);
    return this;
  }

  /**
   * 添加图片
   */
  public PipelineFlow addImage(@NonNull Config.Image config) {
    engine.add(" ", config);
    return this;
  }

  @Override
  public PipelineFlow loader(@NonNull SpanImageLoader loader) {
    engine.setImageLoader(loader);
    return this;
  }

  @Override
  public Spannable execute() {
    return engine.execute();
  }

  @Override
  public void inject(@NonNull TextView textView) {
    engine.inject(textView);
  }

}
