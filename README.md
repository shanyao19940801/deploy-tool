# 部署升级工具

因开发环境限制很多软件都不能用，这就导致我升级新版本时的很多麻烦所以利用空余时间写了一个升级工具。功能虽然比较单一。
这个工具因为急用，是我花了几个小时写出来的，所以在代码质量以及可拓展性方面有很多不足，以后有时间在重构一下吧。

<br>
 
## 需要环境
 * 系统
	 * windows
 * 环境
	 * 系统安装java
	 * java版本：最好1.8以上
 * 开发工具
	 * 我的是idea开发，其他工具暂时没用不知道行不行

## 主要功能

* 生成部署发布包
  这个工具主要功能是根据SVN的log生成一个发布包，详情：[使用说明](https://github.com/shanyao19940801/deploy-tool/blob/master/deploy.md)

* 升级代码
  基于上面生成的发布包，到生产环境通过此工具完成升级，并自动备份，以及log打印，详情：[使用说明](https://github.com/shanyao19940801/deploy-tool/blob/master/upgrade.md)

## 下载地址

百度云：[下载地址](https://pan.baidu.com/s/19NfArStWhiWAQi2L4POdmA) 密码：4dkd

## 每天必须有产出，哪怕只是一行代码，半页书
* 《我曾其次鄙视自己的灵魂》<br>
第一次，当它本可进取时，却故作谦卑；<br>
第二次，当它在空虚时，用爱欲来填充；<br>
第三次，在困难和容易之间，它选择了容易；<br>
第四次，它犯了错，却借由别人也会犯错来宽慰自己；<br>
第五次，它自由软弱，却把它认为是生命的坚韧；<br>
第六次，当它鄙夷一张丑恶的嘴脸时，却不知那正是自己面具中的一副；<br>
第七次，它侧身于生活的污泥中，虽不甘心，却又畏首畏尾<br>