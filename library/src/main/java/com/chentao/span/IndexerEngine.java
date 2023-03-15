package com.chentao.span;

import android.text.Spannable;
import android.text.SpannableString;
import androidx.annotation.NonNull;

/**
 * 索引器引擎
 * <br>
 * create by chentao on 2023-02-27.
 */
public final class IndexerEngine extends Engine {

  @NonNull
  private final Spannable spannable;

  public IndexerEngine(@NonNull CharSequence source) {
    this.spannable = new SpannableString(source);
  }

  public void add(int start, int end, @NonNull Config config) {
    Processor processor = parse(config);
    // 添加时完成 Span 的处理。
    processor.apply(spannable, start, end);
  }

  @NonNull
  @Override
  public Spannable execute() {
    return this.spannable;
  }

}
