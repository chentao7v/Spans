package io.github.chentao7v.span;

/**
 * 对 Span 进行处理的工具。
 * <br>
 * create by chentao on 2023-02-23.
 */
public final class Spans {

  private Spans() {}

  /**
   * 通过流水线依次拼接的方式设置 Span。
   *
   * @see PipelineFlow
   */
  public static PipelineFlow pipeline() {
    return new PipelineFlow();
  }

  /**
   * 通过索引的方式设置 Span。
   *
   * @see IndexerFlow
   */
  public static IndexerFlow indexer(CharSequence source) {
    return new IndexerFlow(source);
  }

  /**
   * 通过替换占位符的方式设置 Span。
   *
   * @param source 包含占位符 {@link PlaceholderFlow#HOLDER} 的字符串
   * @see PlaceholderFlow
   */
  public static PlaceholderFlow placeholder(String source) {
    return new PlaceholderFlow(source);
  }

}
