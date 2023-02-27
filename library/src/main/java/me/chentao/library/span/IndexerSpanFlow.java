package me.chentao.library.span;

import android.text.Spannable;
import android.widget.TextView;
import androidx.annotation.NonNull;

/**
 * 通过索引的方式设置 Span
 * <br>
 * 通过指定 {@code start/end}，给这区间的文本[含头部包尾] 设置指定的 Span。
 * <br>
 * create by chentao on 2023-02-23.
 */
public final class IndexerSpanFlow {

  private final IndexerEngine engine;

  IndexerSpanFlow(@NonNull CharSequence source) {
    this.engine = new IndexerEngine(source);
  }

  public IndexerSpanFlow add(int start, int end, @NonNull Config config) {
    engine.add(start, end, config);
    return this;
  }

  public Spannable end() {
    return engine.end();
  }

  public void inject(@NonNull TextView textView) {
    engine.inject(textView);
  }

}
