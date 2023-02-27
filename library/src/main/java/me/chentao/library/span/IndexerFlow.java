package me.chentao.library.span;

import android.text.Spannable;
import android.widget.TextView;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;

/**
 * 通过索引的方式设置 Span
 * <br>
 * 通过指定 {@code start/end}，给这区间的文本[含头部包尾] 设置指定的 Span。
 * <br>
 * create by chentao on 2023-02-23.
 */
public final class IndexerFlow {

  private final IndexerEngine engine;

  IndexerFlow(@NonNull CharSequence source) {
    this.engine = new IndexerEngine(source);
  }

  /**
   * 给对应子串 [start,end) 添加 Span。
   *
   * @param config 具体的效果
   */
  public IndexerFlow add(@IntRange(from = 0) int start, @IntRange(from = 0) int end, @NonNull Config config) {
    if (start >= end) {
      throw new IllegalArgumentException("start(" + start + ") < end(" + end + ")");
    }

    engine.add(start, end, config);
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
  public void inject(@NonNull TextView textView) {
    engine.inject(textView);
  }

}
