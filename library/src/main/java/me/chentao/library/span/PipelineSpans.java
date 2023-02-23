package me.chentao.library.span;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.Px;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Queue;

/**
 * 通过拼接的方式设置 Span。
 * <br>
 * 每添加一段文本，就需要给改文本指定 Span，直到结束。
 * <br>
 * create by chentao on 2023-02-23.
 */
public final class PipelineSpans {

    private final Deque<Spannable> spans;

    private boolean clickable;

    PipelineSpans() {
        spans = new ArrayDeque<>();
    }

    /**
     * 给指定的 {@link CharSequence} 添加点击事件和颜色
     */
    public PipelineSpans click(CharSequence source, @ColorInt int color, @Nullable View.OnClickListener listener) {
        this.clickable = true;
        spans.add(new SpanPipeline.ClickSpan(color, listener).apply(source));
        return this;
    }

    /**
     * 给指定的 {@link CharSequence} 添加点击事件
     */
    public PipelineSpans click(CharSequence source, @Nullable View.OnClickListener listener) {
        return click(source, -1, listener);
    }

    /**
     * 给最近一个 Span 添加点击事件和颜色
     */
    public PipelineSpans click(@ColorInt int color, @Nullable View.OnClickListener listener) {
        applySpanForLast(new SpanPipeline.OverlayClickSpan(color, listener));
        return this;
    }

    /**
     * 给最近一个 Span 添加点击事件
     */
    public PipelineSpans click(@Nullable View.OnClickListener listener) {
        return click(-1, listener);
    }

    /**
     * 给指定的 {@link CharSequence} 添加设置颜色
     */
    public PipelineSpans color(CharSequence source, @ColorInt int color) {
        spans.add(new SpanPipeline.ColorSpan(color).apply(source));
        return this;
    }

    /**
     * 对最近的一个 Span 进行加粗
     */
    public PipelineSpans bold() {
        applySpanForLast(new SpanPipeline.BoldSpan());
        return this;
    }

    /**
     * 最近一个 Span 设置指定大小
     */
    public PipelineSpans size(@Px int size) {
        applySpanForLast(new SpanPipeline.SizeSpan(size));
        return this;
    }

    private void applySpanForLast(SpanPipeline<Spannable, ?> pipeline) {
        Spannable last = spans.getLast();
        if (last == null) {
            return;
        }
        pipeline.apply(last);
    }

    public PipelineSpans clickable() {
        this.clickable = true;
        return this;
    }

    /**
     * 将所有 Span 注入到 {@link TextView} 中
     */
    public void inject(TextView textView) {
        Spans.inject(textView, end(), clickable);
    }

    /**
     * 返回生成的 Span
     */
    public Spannable end() {
        Spannable data = convert(spans);
        spans.clear();
        return data;
    }

    /**
     * 转换为 {@link IndexerSpans}
     */
    public IndexerSpans toIndex() {
        Spannable spannable = end();
        return Spans.indexer(spannable);
    }

    private static Spannable convert(@NonNull Queue<Spannable> queue) {
        SpannableStringBuilder sb = new SpannableStringBuilder();
        for (Spannable spannable : queue) {
            sb.append(spannable);
        }
        return sb;
    }

}
