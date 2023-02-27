package me.chentao.library.span;

import android.text.Spannable;
import android.text.SpannableString;
import androidx.annotation.NonNull;

/**
 * create by chentao on 2023-02-27.
 */
public final class IndexerEngine extends Engine {

  @NonNull
  private final Spannable spannable;

  public IndexerEngine(@NonNull CharSequence source) {
    super();
    this.spannable = new SpannableString(source);
  }

  public void add(int start, int end, @NonNull Config config) {
    IndexerProcessor processor = parse(config);
    processor.apply(spannable, start, end);
  }

  @Override
  public Spannable end() {
    return this.spannable;
  }

}
