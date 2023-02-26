package me.chentao.library.span;

import android.text.Spannable;
import android.text.SpannableString;
import android.widget.TextView;
import androidx.annotation.NonNull;
import java.util.ArrayDeque;
import java.util.Deque;

/**
 * create by chentao on 2023-02-26.
 */
public class IndexerEngine {

  @NonNull
  private StringBuilder source;

  private final Deque<IndexerProcessor.Element> elements;

  @NonNull
  private final AsyncImageSpanEngine asyncEngine;

  private int startIndex = 0;

  private boolean clickable;

  public IndexerEngine(@NonNull String source) {
    this.source = new StringBuilder(source);

    this.elements = new ArrayDeque<>();
    this.asyncEngine = new AsyncImageSpanEngine();
  }

  public void setImageLoader(@NonNull SpanImageLoader loader) {
    this.asyncEngine.setImageLoader(loader);
  }

  public void replace(@NonNull String data, @NonNull Config config) {
    if (config instanceof Config.Default) {
      // 处理常见操作
      Config.Default c = (Config.Default) config;

      IndexerProcessor.ComposeProcessor compose = new IndexerProcessor.ComposeProcessor();

      if (c.getListener() != null) {
        this.clickable = true;
        compose.addProcessor(new IndexerProcessor.Click(c.getColor(), c.getListener()));
      }

      if (c.getColor() != Config.NONE) {
        compose.addProcessor(new IndexerProcessor.Color(c.getColor()));
      }

      if (c.isBold()) {
        compose.addProcessor(new IndexerProcessor.Bold());
      }

      if (c.getSize() != Config.NONE) {
        compose.addProcessor(new IndexerProcessor.Size(c.getSize()));
      }

      handleHeadPlaceHolder(data, compose);

    } else if (config instanceof Config.Image) {
      // 处理图片
      Config.Image c = (Config.Image) config;
      IndexerProcessor.ComposeProcessor compose = new IndexerProcessor.ComposeProcessor();

      if (c.getListener() != null) {
        this.clickable = true;
        compose.addProcessor(new IndexerProcessor.Click(Config.NONE, c.getListener()));
      }

      if (c.getBitmap() != null) {
        compose.addProcessor(new IndexerProcessor.Image(c.getBitmap(), c.getWidth(), c.getVerticalAlign()));
      } else if (c.getDrawable() != null) {
        compose.addProcessor(new IndexerProcessor.Image(c.getDrawable(), c.getWidth(), c.getVerticalAlign()));
      } else if (c.getUrl() != null) {
        IndexerProcessor.DynamicProxy proxy = new IndexerProcessor.DynamicProxy(c.getWidth());
        compose.addProcessor(proxy);
        asyncEngine.register(c.getUrl(), proxy);
      }

      handleHeadPlaceHolder(data, compose);
    }
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

  public void inject(TextView textView) {
    Spannable spannable = finish();
    boolean async = asyncEngine.containsAsync();
    Spans.inject(textView, spannable, clickable, async);
    if (async) {
      Spannable text = (Spannable) textView.getText();
      asyncEngine.loadAllImages(text, new AsyncImageSpanEngine.Listener() {
        @Override
        public void onComplete() {
          textView.invalidate();
        }
      });
    }
  }

  public Spannable finish() {
    SpannableString spannable = new SpannableString(this.source);
    IndexerProcessor.applyAll(spannable, elements);
    this.elements.clear();
    return spannable;
  }

}
