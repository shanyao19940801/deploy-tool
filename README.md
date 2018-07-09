# 部署升级工具

因开发环境限制很多软件都不能用，这就导致我升级新版本时的很多麻烦所以利用空余时间写了一个升级工具。功能虽然比较单一

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