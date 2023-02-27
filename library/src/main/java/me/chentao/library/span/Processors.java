package me.chentao.library.span;

import android.text.Spannable;
import android.text.SpannableString;
import java.util.Collection;

/**
 * Processor 工具
 * <br>
 * create by chentao on 2023-02-27.
 */
public final class Processors {

  private Processors() {}

  public static Spannable applyAll(CharSequence source, Collection<Processor.Element> elements) {
    SpannableString spannable = new SpannableString(source);
    for (Processor.Element element : elements) {
      Processor processor = element.processor();
      int start = element.start();
      int end = element.end();
      processor.apply(spannable, start, end);
    }
    return spannable;
  }

}
