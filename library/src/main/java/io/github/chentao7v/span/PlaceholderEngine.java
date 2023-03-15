package io.github.chentao7v.span;

import android.text.Spannable;
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

  private final List<Processor.Element> elements;

  private int startIndex = 0;

  public PlaceholderEngine(@NonNull String source) {
    this.elements = new ArrayList<>();
    this.source = new StringBuilder(source);
  }

  public void replace(@NonNull String data, @NonNull Config config) {
    Processor processor = parse(config);
    handle(data, processor);
  }

  /**
   * 处理头部的占位符
   */
  private void handle(@NonNull String data, @NonNull Processor processor) {
    int index = source.indexOf(PlaceholderFlow.HOLDER, startIndex);
    if (index == -1) {
      return;
    }
    startIndex = index;
    source = source.replace(index, index + PlaceholderFlow.HOLDER.length(), data);
    elements.add(new Processor.Element(processor, index, data.length()));
  }

  @NonNull
  @Override
  public Spannable execute() {
    return Processors.applyAll(source, elements);
  }

}
