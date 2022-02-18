- # 新闻网站

    系统包含了3端，门户端，作家中心以及运营管理admin中心

    - 门户端

        首页，搜索，登录注册，文章详情，文章列表，文章分类，作家主页，热点新闻，文章评论，友情链接

    - 作家中心

         发文，内容管理，评论管理，粉丝管理，粉丝画像，账号设置

    - admin中心

        用户管理，内容审核，友情链接管理，文章分类管理，人脸管理，IP黑名单

    ##### admin模块

    ​	用户管理，内容审核，友情链接管理，文章分类管理，人脸管理，IP黑名单

    ##### common模块

    ​	各种工具类

    ##### model模块

    ​	各种对象，请求对象以及返回对象，实体类

    ##### api模块

    ​	controller接口，以及各种使用于controller层的配置文件，例如swagger2，拦截器的配置等

    ##### article模块

    ​	文章的增删改查

    ##### article-html模块

    ​	将文章页面进行部分静态化，不用将文章内容每次都查询数据库

    ##### files模块

    ​	文件的存储，使用FastDFS进行存储文章的图片及用户头像等内容，后改成阿里的OSS文件存储服务

    ##### user模块

    ​	用户模块

    ##### mybatis-generator-database

    ​	逆向生成xml，Mapper的工具项目

    ##### springcloud-config

    ​	配置中心

    ##### eureka-cluster

    ​	多实例注册中心

    ##### springcloud-zuul

    ​	项目入口网关

    ![QQ截图20220218220201](https://user-images.githubusercontent.com/86520979/154696758-b9529d89-2364-456c-b1ca-45ef3c2e1959.png)


   
