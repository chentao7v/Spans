package com.chentao.span;

import android.text.Spannable;
import androidx.annotation.NonNull;
import java.util.ArrayList;
import java.util.List;

/**
 * 流水线 Span 处理引擎
 * <br>
 * create by chentao on 2023-02-27.
 */
public final class PipelineEngine extends Engine {

  @NonNull
  private final List<Processor.Element> elements = new ArrayList<>();

  private int startIndex = 0;

  private final StringBuilder source = new StringBuilder();

  public void add(@NonNull String data, @NonNull Config config) {
    source.append(data);

    Processor processor = parse(config);
    handle(data, processor);
  }

  private void handle(@NonNull String data, Processor processor) {
    Processor.Element element = new Processor.Element(processor, startIndex, data.length());
    elements.add(element);
    startIndex = startIndex + data.length();
  }

  @NonNull
  @Override
  public Spannable execute() {
    return Processors.applyAll(source, elements);
  }
}
