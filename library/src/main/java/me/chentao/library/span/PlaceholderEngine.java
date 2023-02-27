package me.chentao.library.span;

import android.text.Spannable;
import android.text.SpannableString;
import androidx.annotation.NonNull;
import java.util.ArrayDeque;

/**
 * create by chentao on 2023-02-26.
 */
public final class PlaceholderEngine extends Engine {

  @NonNull
  private StringBuilder source;

  private final ArrayDeque<IndexerProcessor.Element> elements;

  private int startIndex = 0;

  public PlaceholderEngine(@NonNull String source) {
    super();
    this.elements = new ArrayDeque<>();
    this.source = new StringBuilder(source);
  }

  public void replace(@NonNull String data, @NonNull Config config) {
    IndexerProcessor processor = convert(config);
    handleHeadPlaceHolder(data, processor);
  }

  /**
   * 处理头部的占位符
   */
  private void handleHeadPlaceHolder(@NonNull String data, @NonNull IndexerProcessor processor) {
    int index = source.indexOf(PlaceholderSpanFlow.HOLDER, startIndex);
    if (index == -1) {
      return;
    }
    startIndex = index;
    source = source.replace(index, index + PlaceholderSpanFlow.HOLDER.length(), data);
    elements.add(new IndexerProcessor.Element(processor, index, data.length()));
  }

  @Override
  public Spannable end() {
    SpannableString spannable = new SpannableString(source);
    IndexerProcessor.applyAll(spannable, elements);
    this.elements.clear();
    return spannable;
  }

}
