# Android Span 工具
在 Android 项目中，如果想要对一段长文本添加布局点击事件、局部文字变色、加粗等功能，需要通过 `Spannable` 来实现。但现有框架中，`Spannable` 使用相对复杂，使用过程中存在大量的模板代码。
`Spans` 工具封装了常用的 `Span`，使用起来更加方便。



## 支持功能

### 占位符替换的方式

`Spans` 支持使用占位符 `{##}` 来替换可变文本，如下文本：

```shell
"对不起，因您已注册的{$$}，我们需要核实您的身份及手机号码信息后注册，请插入对应的手机卡{$$}并打开该改手机卡流量开关哟。注册成功后，此手机号才会仅分配给您使用。"
```

两个占位符中的文本都可变的，且我们需要分别在两个占位符处设置点击事件，且颜色高亮，借助该框架，使用如下代码即可轻松完成：

```kotlin
val msg = "对不起，因您已注册的{$$}，我们需要核实您的身份及手机号码信息后注册，请插入对应的手机卡{$$}并打开该改手机卡流量开关哟。注册成功后，此手机号才会仅分配给您使用。"

Spans.placeholder(msg)
    .color("188****8888", getColor(R.color.teal_200))
    .size(dpToPx(this, 30))
    .click {
        Log.i(TAG, "click A ~~~")
    }
    .color("199****9999", getColor(R.color.teal_700))
    .click {
        Log.i(TAG ,"click B B !!!")
    }
    .size(dpToPx(this, 25))
    .inject(tvMsg)
```

最终效果如下：

<img src="./images/img1.png" style="zoom:50%;" />

