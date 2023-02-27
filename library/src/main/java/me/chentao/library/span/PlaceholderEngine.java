package me.chentao.library.span;

import android.text.Spannable;
import android.text.SpannableString;
import androidx.annotation.NonNull;
import java.util.ArrayList;
import java.util.List;

/**
 * 占位符引擎
 * <br>
 * create by chentao on 2023-02-26.
 */
public final class PlaceholderEngine extends Engine {

  @NonNull
  private StringBuilder source;

  private final List<IndexerProcessor.Element> elements;

  private int startIndex = 0;

  public PlaceholderEngine(@NonNull String source) {
    this.elements = new ArrayList<>();
    this.source = new StringBuilder(source);
  }

  public void replace(@NonNull String data, @NonNull Config config) {
    IndexerProcessor processor = parse(config);
    handleHeadPlaceHolder(data, processor);
  }

  /**
   * 处理头部的占位符
   */
  private void handleHeadPlaceHolder(@NonNull String data, @NonNull IndexerProcessor processor) {
    int index = source.indexOf(PlaceholderFlow.HOLDER, startIndex);
    if (index == -1) {
      return;
    }
    startIndex = index;
    source = source.replace(index, index + PlaceholderFlow.HOLDER.length(), data);
    elements.add(new IndexerProcessor.Element(processor, index, data.length()));
  }

  @Override
  public Spannable execute() {
    SpannableString spannable = new SpannableString(source);

    for (IndexerProcessor.Element element : elements) {
      IndexerProcessor processor = element.processor();
      int start = element.start();
      int end = element.end();
      processor.apply(spannable, start, end);
    }

    this.elements.clear();
    return spannable;
  }

}
