package io.github.chentao7v.span;

/**
 * Config 配置工具
 * <br>
 * create by chentao on 2023-02-27.
 */
public final class Configs {

  private Configs() {}

  public static Config.Text text() {
    return new Config.Text();
  }

  public static Config.Image image() {
    return new Config.Image();
  }

}
