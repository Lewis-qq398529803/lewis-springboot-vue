# Lewis后台管理系统框架

## 简介
自定义若依框架-分离版（Vue），作为后台管理系统的框架。作为个人的魔改版，主要都是对我自身的使用进行的优化，若需要原版，请移步gitee搜ruoyi-vue。

- 前端采用Vue、Element UI。
- 后端采用Spring Boot、Spring Security、Redis & Jwt。
- 权限认证使用Jwt，支持多终端认证系统。
- 支持加载动态权限菜单，多方式轻松权限控制。
- 高效率开发，使用代码生成器可以一键生成前后端代码。

演示地址：[http://115.159.99.186/](http://115.159.99.186/)

## 功能

### 若依自带：

1. 用户管理：用户是系统操作者，该功能主要完成系统用户配置。
2. 部门管理：配置系统组织机构（公司、部门、小组），树结构展现支持数据权限。
3. 岗位管理：配置系统用户所属担任职务。
4. 菜单管理：配置系统菜单，操作权限，按钮权限标识等。
5. 角色管理：角色菜单权限分配、设置角色按机构进行数据范围权限划分。
6. 字典管理：对系统中经常使用的一些较为固定的数据进行维护。
7. 参数管理：对系统动态配置常用参数。
8. 通知公告：系统通知公告信息发布维护。
9. 操作日志：系统正常操作日志记录和查询；系统异常信息日志记录和查询。
10. 登录日志：系统登录日志记录查询包含登录异常。
11. 在线用户：当前系统中活跃用户状态监控。
12. 定时任务：在线（添加、修改、删除)任务调度包含执行结果日志。
13. 代码生成：前后端代码的生成（java、html、xml、sql）支持CRUD下载 。
14. 系统接口：根据业务代码自动生成相关的api接口文档。
15. 服务监控：监视当前系统CPU、内存、磁盘、堆栈等相关信息。
16. 缓存监控：对系统的缓存信息查询，命令统计等。
17. 在线构建器：拖动表单元素生成相应的HTML代码。
18. 连接池监视：监视当前系统数据库连接池状态，可进行分析SQL找出系统性能瓶颈。

### Lewis魔改：

1. 多模块 --> 单模块（项目结构巨型变化，请斟酌使用）
2. mybatis --> mybatisplus
3. 自动生成代码模块，加入mybatisplus元素，使其拥有mybatisplus的条件构造器等所有方法
4. 新增了swagger的bootstrap-ui包，开启swagger的bootstrap-ui的接口
5. 自定义一个全局链式返回对象BaseResult
6. 新增多个工具类
7. 加入swagger配置类，实现参数隐藏、显示

## 项目结构

lewis-springboot-vue 项目目录

​	lewis-ui	前端项目

​	sql	数据库文件

​	src	后端目录

​		com

​			lewis

​				config	配置文件

​				core	通用文件

​				mvc	controller、service、mapper、entity以模块名进行分割

​	resource	资源目录

​		i18n	国际化文件

​		mapper	mapper.xml文件

​		META-INF	配置文件

​		mybatis	mybatis配置文件

​		vm	自动化模板文件

## 启动

### 后端

#### linux（后台运行并且指定配置文件）

```bash
nohup java -jar lewis-springboot-vue.jar --spring.profiles.active=prod  > lewis-springboot-vue.log 2>&1 &
```

#### 前端

```bash
npm install
npm run dev
```

# 支持

感谢 jetbrains 公司提供的 IDEA 开源支持：[https://www.jetbrains.com/all/?_ga=2.66126894.1201218590.1634089382-575836656.1633926128](https://www.jetbrains.com/all/?_ga=2.66126894.1201218590.1634089382-575836656.1633926128)

# 联系

若有任何疑问，可以——

加我QQ：[398529803](https://qm.qq.com/cgi-bin/qm/qr?k=nh1Na88Ead5K7jSWzgXa2XH1lja_IRNB&noverify=0)

**一起冲鸭！！**

