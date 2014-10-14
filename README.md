# tinysou-android-sdk-demo

## 介绍
一个添加应用内微搜索功能的demo，实现功能有发送搜索请求，即输即搜，下拉刷新，上拉加载更多等。
这里，我们通过提供android module，可以导入到开发者的安卓应用中，通过简单的操作，就可以在应用中加入微搜索的功能。

##使用方法

### 一、准备工作
1. 注册微搜索帐号（http://tinysou.com/）
2. 设置engine，获取engine_token
 ![Image text](https://github.com/wangyeming/tinysou-android-sdk-demo/blob/master/ReadmePic/%E4%BB%8B%E7%BB%8D%E5%9B%BE%E7%89%871.png)
3. 添加您的内部站

### 二、导入安卓应用（以android studio为例）
1. 下载TinySouAndroid文件夹，作为模块导入您的安卓应用中（File > Import module）
2. 设置模块依赖（File > Project Structure > your app  >  Dependencies  > + > 3. Module dependency）
![Image text](https://github.com/wangyeming/tinysou-android-sdk-demo/blob/master/ReadmePic/%E4%BB%8B%E7%BB%8D%E5%9B%BE%E7%89%872.png)
3. 新建搜索Activity，继承TinySouSearchActivity，在String.xml文件中设置engine_token
![Image text](https://github.com/wangyeming/tinysou-android-sdk-demo/blob/develop/ReadmePic/%E4%BB%8B%E7%BB%8D%E5%9B%BE%E7%89%873.png)
4. 设置点击事件，启动搜索Activity

## 显示效果
![Image text](https://github.com/wangyeming/tinysou-android-sdk-demo/blob/develop/ReadmePic/%E4%BB%8B%E7%BB%8D%E5%9B%BE%E7%89%874.png)
