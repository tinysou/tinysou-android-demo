TinySouAndroid-api
==================
简介
-----
提供android module，可以导入到开发者的安卓应用中，通过简单的操作，就可以在应用中加入微搜索的功能。
使用方法
--------

一、准备工作
--------
1. 注册微搜索帐号（http://tinysou.com/）
2. 设置engine，获取engine_token
3. 添加您的内部站

二、导入安卓应用（以android studio为例）
--------
1. 下载TingSouAndroid文件夹，作为模块导入您的安卓应用中（File > Import module）
2. 设置模块依赖（File > Project Structure > your app  >  Dependencies  > + > 3. Module dependency）
3. 新建搜索Activity，继承TinySouSearchActivity，设置engine_token
4. 设置点击事件，启动搜索Activity
