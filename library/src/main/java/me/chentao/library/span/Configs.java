package me.chentao.library.span;

/**
 * Config 配置工具
 * <br>
 * create by chentao on 2023-02-27.
 */
public final class Configs {

  private Configs() {}

  public static Config.Default ofDefault() {
    return new Config.Default();
  }

  public static Config.Image ofImage() {
    return new Config.Image();
  }

}
