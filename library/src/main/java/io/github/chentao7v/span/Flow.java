package io.github.chentao7v.span;

import android.text.Spannable;
import android.widget.TextView;
import androidx.annotation.NonNull;
import io.github.chentao7v.span.image.SpanImageLoader;

/**
 * create by chentao on 2023-02-27.
 */
interface Flow<T extends Flow<T>> {

  /**
   * @see Engine#setImageLoader(SpanImageLoader)
   */
  T loader(@NonNull SpanImageLoader loader);

  /**
   * @see Engine#execute()
   */
  Spannable execute();

  /**
   * @see Engine#inject(TextView)
   */
  void into(TextView textView);

}
