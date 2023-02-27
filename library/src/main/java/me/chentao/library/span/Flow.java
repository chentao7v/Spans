package me.chentao.library.span;

import android.text.Spannable;
import android.widget.TextView;
import androidx.annotation.NonNull;
import me.chentao.library.span.image.SpanImageLoader;

/**
 * create by chentao on 2023-02-27.
 */
interface Flow<T extends Flow<T>> {

  /**
   * @see Engine#setImageLoader(SpanImageLoader)
   */
  public T loader(@NonNull SpanImageLoader loader);

  /**
   * @see Engine#execute()
   */
  public Spannable execute();

  /**
   * @see Engine#inject(TextView)
   */
  public void into(TextView textView);

}
