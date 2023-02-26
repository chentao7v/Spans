package me.chentao.library.span;

import android.text.Spannable;
import android.widget.TextView;
import androidx.annotation.NonNull;

/**
 * 通过占位符的方式设置 Span。
 * <br>
 * create by chentao on 2023-02-23.
 */
public final class PlaceholderSpanFlow {

  public static final String HOLDER = "{$}";

  @NonNull
  private final IndexerEngine engine;

  PlaceholderSpanFlow(@NonNull String source) {
    engine = new IndexerEngine(source);
  }

  public PlaceholderSpanFlow replace(@NonNull String data, @NonNull Config config) {
    engine.replace(data, config);
    return this;
  }

  public PlaceholderSpanFlow replace(@NonNull Config.Image image) {
    engine.replace(" ", image);
    return this;
  }

  public PlaceholderSpanFlow loader(@NonNull SpanImageLoader loader) {
    engine.setImageLoader(loader);
    return this;
  }

  /**
   * 返回处理后的 Span
   */
  public Spannable end() {
    return engine.finish();
  }

  public void inject(TextView textView) {
    engine.inject(textView);
  }

}
