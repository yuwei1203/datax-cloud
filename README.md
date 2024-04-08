## 关于智数通
- 由于智数通1.0开发年限较早，数据治理概念不符合现代化理念，现推出商业大数据版智数通2.0（https://github.com/yuwei1203/wishdata-cloud）
- 智数通服务理念：平台化、资产化、服务化，及围绕“平台化”、“资产化”、“服务化”，聚焦数智场景，赋能行业生态，让数据产生价值。<br>
- 智数通应用微服务云原生技术，封装了大量技术开发包、技术应用组件、技术场景实现能力，提供了一个可支持企业各业务系统或产品快速开发实现的微服务应用数字化融合平台，富含各类开箱即用的组件、微服务业务系统，助力企业跨越Cloud与自身数字化的鸿沟，共享业务服务的组合重用，为企业服务化中台整合、数字化转型提供强力支撑，也为企业提供了最佳架构实践。智数通不仅提供了企业级应用系统所包含的常用开箱即用的模块，并支持灵活的可配置性和拓展性。智数通还是一套基于Spring Cloud的微服务应用程序框架，可帮助公司更快，更高效地进行微服务开发。<br>
- 智数通理念认为，企业首先应当建立集中统一的数据中台，将全域的数据都同步到统一的数据中台，基于数中台来进行数据治理，事半功倍。数据存储统一，可以在后续的数据治理和数据业务化的过程中实现数据不搬动，降低数据的处理成本；数据计算统一，可以实现基础元数据的自动维护，为数据治理打下最坚实的基础。<br>
- 智数通提供了统一的元数据自动采集和维护入口，在元数据模块中为数据管理提供了准确实时的基础数据，在此之上结合企业的现状调研和咨询，可以构建企业级的数据资产类目体系和数据安全体系；同时，通过数据质量模块，实现数据处理的质量管控。然后通过数据API模块，为上层丰富的数据应用价值实现提供引擎能力。通过可视化模块无需搭配任何开发工具，简单拖拽和开发，即可完成复杂的数据分析任务等等。<br>
- 从数据同步、到数据开发、到数据管理、到数据质量控制、到数据应用价值实现，智数通在产品层面对于指引都能提供支撑，这是将多年大数据实践经验产品化的结果。智数通，就是为数据治理而生的企业级一站式数据中台，为数据市场抢占先机。
## 联系方式
- QQ群: 1群（159552203）2群（372650543）3群（879605765）4群（793867203）（备注来源：datax-cloud）部署文档和sql文件都在群里，群满加不进每个都加一下试试
## 架构图
- 智数通采用 J2EE 技术体系，基于Spring Cloud微服务框架进行封装，平台设计灵活可扩展、可移植、可应对高并发需求。同时兼顾本地化、私有云、公有云部署。
![输入图片说明](https://images.gitee.com/uploads/images/2020/1203/165417_f69306f2_772907.png "Group%2078.png")
## 主要技术栈
#### 后端技术栈
- 开发框架：Spring Boot 2.3
- 微服务框架：Spring Cloud Hoxton.SR9
- 安全框架：Spring Security + Spring OAuth 2.0
- 任务调度：Quartz 
- 持久层框架：MyBatis Plus
- 数据库连接池：Hikaricp
- 服务注册与发现: Spring Cloud Config
- 客户端负载均衡：Ribbon
- 熔断组件：Hystrix
- 网关组件：Spring Cloud Gateway
- 消息队列：Rabbitmq
- 缓存：Redis
- 日志管理：Logback
- 运行容器：Undertow
- 工作流: Flowable 6.5.0
#### 前端技术栈
- JS框架：Vue、nodejs
- CSS框架：sass
- 组件库：ElementUI
- 打包构建工具：Webpack
## 模块说明
```
datax-cloud
├── datax-ui -- 前端工程[9528]
├── datax-eureka -- 注册中心[8610]
├── datax-config -- 配置中心[8611]
├── datax-gateway -- Spring Cloud Gateway网关[8612]
├── datax-auth -- 授权服务提供[8613]
├── datax-common -- 系统公共模块
├    ├── datax-common-core -- 公共工具类核心包
├    ├── datax-common-database -- 数据库相关工具类包
├    ├── datax-common-dictionary -- 基于注解的字典翻译类包（未使用）
├    ├── datax-common-jasperreport -- 报表工具类包（未使用）
├    ├── datax-common-log -- 日志服务类包
├    ├── datax-common-mybatis -- 数据源类包
├    ├── datax-common-office -- word工具类包
├    ├── datax-common-qrcode -- 二维码工具类包（未使用）
├    ├── datax-common-rabbitmq -- rabbitmq消息队列类包
├    ├── datax-common-redis -- redis类包
├    ├── datax-common-security -- 安全工具类包
├── datax-modules -- 业务模块
├    ├── codegen-service-parent -- 后端代码生成管理模块[8830]
├    ├── data-market-service-parent -- 数据集市管理模块[8822]
├    ├── data-masterdata-service-parent -- 主数据管理模块[8828]
├    ├── data-metadata-service-parent -- 元数据管理模块[8820]
├    ├── data-quality-service-parent -- 数据质量管理模块[8826]
├    ├── data-standard-service-parent -- 数据标准管理模块[8825]
├    ├── data-visual-service-parent -- 可视化管理模块[8827]
├    ├── email-service-parent -- 邮件管理模块[8812]
├    ├── file-service-parent -- 文件管理模块[8811]
├    ├── quartz-service-parent -- 定时任务管理模块[8813]
├    ├── system-service-parent -- 平台基础设置管理模块[8810]
├    ├── workflow-service-parent -- 流程管理模块[8814]
└── datax-tools -- 图形化模块
├    ├── datax-tool-monitor -- Spring Boot Admin监控 [8711]
```
## 功能说明
- 平台基础设置
   - 系统管理
        - 岗位管理：配置系统用户所属担任职务。
        - 部门管理：配置系统组织机构，树结构展现支持数据权限。
        - 菜单管理：配置系统菜单，操作权限，按钮权限标识等。
        - 角色管理：角色菜单权限分配、设置角色按机构进行数据范围权限划分。
        - 用户管理：用户是系统操作者，该功能主要完成系统用户配置。
        - 参数管理：对系统动态配置常用参数。
        - 字典管理：对系统中经常使用的一些较为固定的数据进行维护。
    - 系统监控
        - 登录日志：系统登录日志记录查询。
        - 操作日志：系统正常操作日志记录和查询；系统异常信息日志记录和查询。
    - 任务调度
        - 任务管理：在线（添加、修改、删除)任务调度。
        - 日志管理：任务调度执行结果日志。
- 元数据管理
    - 数据源：数据源连接信息管理，可生成数据库文档。
    - 元数据：数据库表的元数据信息管理。
    - 数据授权：设置元数据信息权限划分。
    - 变更记录：元数据信息变更记录信息管理。
    - 数据检索：数据源、数据表、元数据等信息查询。
    - 数据地图：元数据的隶属数据表、数据库的图形展示。
    - 血缘流向：元数据的来源与去向记录。
    - SQL工作台：在线执行查询sql。
- 数据标准管理
    - 标准字典：国标数据维护。
    - 对照表：本地数据中需要对照标准的数据维护。
    - 字典对照：本地数据与国标数据的对照关系。
    - 对照统计：本地数据与国标数据的对照结果统计分析。
- 数据质量管理
    - 规则配置：数据质量规则配置。
    - 问题统计：数据质量规则统计。
    - 质量报告：数据质量结果统计分析。
    - 定时任务：数据质量定时任务。
    - 任务日志：数据质量定时任务日志。
- 主数据管理
    - 数据模型：主数据数据模型维护。
    - 数据管理：主数据数据管理。
- 数据集市管理
    - 数据服务：动态开发api数据服务，可生成数据服务文档。
    - 数据脱敏：api数据服务返回结果动态脱敏。
    - 接口日志：api数据服务调用日志。
    - 服务集成：三方数据服务集成管理。
    - 服务日志：三方数据服务集成调用日志。
- 可视化管理
    - 数据集：基于sql的查询结果维护。
    - 图表配置：动态echarts图表配置，支持多维表格、折线、柱状、饼图、雷达、散点等多种图表。
    - 看板配置：拖拽式添加图表组件，调整位置、大小。
- 流程管理
    - 流程定义：流程定义管理。
    - 流程实例
        - 运行中的流程：运行中的流程实例管理。
        - 我发起的流程：我发起的流程实例管理。
        - 我参与的流程：我参与的流程实例管理。
    - 流程任务
        - 待办任务：待办任务管理。
        - 已办任务：已办任务管理。
    - 业务配置：配置业务系统与流程的相关属性。
## 部署文档
(https://www.yuque.com/docs/share/143da288-525a-4b77-a46b-e480547108a2?# 《智数通产品部署文档》)
## 系统截图
![输入图片说明](https://images.gitee.com/uploads/images/2020/1203/170637_303df10c_772907.png "image.png")
![输入图片说明](https://images.gitee.com/uploads/images/2020/1203/170659_dc49bb20_772907.png "image (1).png")
![输入图片说明](https://images.gitee.com/uploads/images/2020/1203/170857_1a913ab5_772907.png "image (2).png")
![输入图片说明](https://images.gitee.com/uploads/images/2020/1203/170908_a932cfe0_772907.png "image (3).png")
![输入图片说明](https://images.gitee.com/uploads/images/2020/1203/170931_37f2352e_772907.png "image (4).png")
![输入图片说明](https://images.gitee.com/uploads/images/2020/1203/170919_1669630b_772907.png "image (5).png")
![输入图片说明](https://images.gitee.com/uploads/images/2020/1203/170939_43774735_772907.png "image (6).png")
![输入图片说明](https://images.gitee.com/uploads/images/2020/1203/170949_44790dec_772907.png "image (7).png")
![输入图片说明](https://images.gitee.com/uploads/images/2020/1203/170959_fac6ee46_772907.png "image (8).png")
![输入图片说明](https://images.gitee.com/uploads/images/2020/1203/171008_895f18a2_772907.png "image (9).png")
![输入图片说明](https://images.gitee.com/uploads/images/2020/1203/171021_ec4deafb_772907.png "image (10).png")
![输入图片说明](https://images.gitee.com/uploads/images/2020/1203/171033_26e1a3cb_772907.png "image (11).png")
![输入图片说明](https://images.gitee.com/uploads/images/2020/1203/171044_bc69d0a2_772907.png "image (12).png")
![输入图片说明](https://images.gitee.com/uploads/images/2020/1203/171056_1f7528b0_772907.png "image (13).png")
![输入图片说明](https://images.gitee.com/uploads/images/2020/1203/171105_2ffc9153_772907.png "image (14).png")
![输入图片说明](https://images.gitee.com/uploads/images/2020/1203/171124_252d3669_772907.png "image (15).png")
