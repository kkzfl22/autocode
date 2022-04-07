package com.liujun.auto.config.menutree;

import com.liujun.auto.config.generate.GenerateConfigProcess;
import com.liujun.auto.constant.JavaDomainTreeKey;
import com.liujun.auto.constant.Symbol;

import java.io.File;
import java.net.URL;

/**
 * 领域设计的目录树构建
 *
 * @author liujun
 * @version 0.0.1
 */
public class DirTree {

  /** 顶层根节点 */
  private static final String ROOT = Symbol.PATH;

  private static final String BASE_CONFIG_PATH = "./codedir/ddd/java/";

  /** 实例信息 */
  public static final DirTree INSTANCE =
      new DirTree(
          GenerateConfigProcess.INSTANCE
              .getCfgEntity()
              .getGenerate()
              .getCodeMenuTree()
              .getDomainName());

  /** 逻辑根节点 */
  private final DirNode defineRoot;

  /** 模块名称 */
  private final String modelName;

  public DirTree(String modelName) {
    // 模块名称
    this.modelName = modelName;

    defineRoot = getDirTree();
  }

  /**
   * 获取工程的目录结构
   *
   * @return
   */
  public static DirNode getDirTree() {
    URL configUrl = DirTree.class.getClassLoader().getResource(BASE_CONFIG_PATH);
    if (null == configUrl) {
      throw new IllegalArgumentException("config path config is null");
    }

    String readerPath = configUrl.getPath();

    DirNode treeMenu = new DirNode(Symbol.PATH);
    File fileItem = new File(readerPath);

    for (File item : fileItem.listFiles()) {
      DirNode one = treeMenu.addChildren(item.getName());
      readFileDir(item, one);
    }

    return treeMenu;
  }

  /**
   * 读取文件目录信息
   *
   * @param file
   * @param node 节点信息
   */
  public static void readFileDir(File file, DirNode node) {
    if (file.isDirectory()) {
      for (File item : file.listFiles()) {
        DirNode childrenNode = node.addChildren(item.getName());

        readFileDir(item, childrenNode);
      }
    }

    if (file.isFile()) {
      node.addChildrenOnly(file.getName());
      return;
    }

    return;
  }

  /** 构建基础的目录结构 */
  private DirNode builder() {
    // 应用层
    defineRoot.addChildrenOnly(JavaDomainTreeKey.APPLICATION);
    // 构建领域层
    builderDomain(modelName);
    // 构建基础资源层
    builderInfrastructure();
    // 对外的接口层
    builderInterface();

    return defineRoot;
  }

  /**
   * 构建领域层的层级关系
   *
   * @param modelName
   */
  private void builderDomain(String modelName) {
    DirNode model =
        defineRoot
            .addChildren(JavaDomainTreeKey.DOMAIN)
            // 模块名
            .addChildren(modelName);

    // 领域层中的静态资源学量
    model.addChildrenOnly(JavaDomainTreeKey.DOMAIN_CONSTANT);
    // 添加领域层中实体
    model.addChildrenOnly(JavaDomainTreeKey.DOMAIN_ENTITY);
    // 添加服务层
    model.addChildrenOnly(JavaDomainTreeKey.DOMAIN_SERVICE);

    // 添加存储层资源
    // 优先添加模块名称
    DirNode repositoryNode = model.addChildren(JavaDomainTreeKey.DOMAIN_REPOSITORY);
    // 领域层的转换对象
    repositoryNode.addChildrenOnly(JavaDomainTreeKey.REPOSITORY_ASSEMBLER);
    // 交互接口
    repositoryNode.addChildrenOnly(JavaDomainTreeKey.REPOSITORY_FACADE);
    // 数据库接口层
    repositoryNode.addChildrenOnly(JavaDomainTreeKey.REPOSITORY_MAPPER);
    // 交互接口实现
    repositoryNode.addChildrenOnly(JavaDomainTreeKey.REPOSITORY_PERSISTENCE);
    // 存储实体
    repositoryNode.addChildrenOnly(JavaDomainTreeKey.REPOSITORY_PO);
  }

  /** 构建基础资源层，例如mq等设施 */
  private void builderInfrastructure() {
    // 基础资源层
    DirNode infraStructureNode = defineRoot.addChildren(JavaDomainTreeKey.INFRASTRUCTURE);
    // 添加公共算法目录
    infraStructureNode.addChildrenOnly(JavaDomainTreeKey.INFRASTRUCTURE_ALGORITHM);
    // api的目录
    infraStructureNode.addChildrenOnly(JavaDomainTreeKey.INFRASTRUCTURE_API);
    // client目录
    infraStructureNode.addChildrenOnly(JavaDomainTreeKey.INFRASTRUCTURE_CLIENT);
    // 添加持久层
    DirNode repositoryChildren =
        infraStructureNode.addChildren(JavaDomainTreeKey.DOMAIN_REPOSITORY);
    // 持久层下添加模块名
    DirNode repositoryModuleChildren = repositoryChildren.addChildren(modelName);

    // 持久层下添加转换
    repositoryModuleChildren.addChildrenOnly(JavaDomainTreeKey.REPOSITORY_ASSEMBLER);
    // 持久层下添加数据库持久的接口
    DirNode mapper = repositoryModuleChildren.addChildren(JavaDomainTreeKey.REPOSITORY_MAPPER);
    // 在数据库持久层下添加配制目标
    mapper.addChildrenOnly(JavaDomainTreeKey.MAPPER_CONFIG);

    // 持久层下添加持久转转换的实现
    repositoryModuleChildren.addChildrenOnly(JavaDomainTreeKey.REPOSITORY_PERSISTENCE);
    // 持久层的实现
    repositoryModuleChildren.addChildrenOnly(JavaDomainTreeKey.REPOSITORY_PO);

    // 公共目录
    DirNode commonNode = infraStructureNode.addChildren(JavaDomainTreeKey.INFRASTRUCTURE_COMM);
    // 公共目录下的验证
    commonNode.addChildrenOnly(JavaDomainTreeKey.COMM_CHECK);
    // 公共目录下的异常
    commonNode.addChildrenOnly(JavaDomainTreeKey.COMM_EXCEPTION);
    // 公共目录下的事件通道
    DirNode rabbitQueueNode = commonNode.addChildren(JavaDomainTreeKey.COMM_RABBITMQ);
    // 事件通道下的转换目录
    rabbitQueueNode.addChildrenOnly(JavaDomainTreeKey.RABBITMQ_ASSEMBLER);
    // 事件通道下的配制目录
    rabbitQueueNode.addChildrenOnly(JavaDomainTreeKey.RABBITMQ_CONFIG);
    // 事件通道下的实体目录
    rabbitQueueNode.addChildrenOnly(JavaDomainTreeKey.RABBITMQ_ENTITY);
    // 事件通道下的事件目录
    rabbitQueueNode.addChildrenOnly(JavaDomainTreeKey.RABBITMQ_EVENT);
    // 公共类
    commonNode.addChildrenOnly(JavaDomainTreeKey.COMM_UTILS);
    // 静态常量
    infraStructureNode.addChildrenOnly(JavaDomainTreeKey.INFRASTRUCTURE_CONSTANT);
    // 实体
    infraStructureNode.addChildrenOnly(JavaDomainTreeKey.INFRASTRUCTURE_ENTITY);
  }

  /** 构建与前端交互的接口 */
  private void builderInterface() {
    // 接口资源
    DirNode interfaceNode = defineRoot.addChildren(JavaDomainTreeKey.INTERFACE);
    // 类转换处理
    interfaceNode.addChildrenOnly(JavaDomainTreeKey.INTERFACE_ASSEMBLER);
    // 校验处理
    interfaceNode.addChildrenOnly(JavaDomainTreeKey.INTERFACE_CHECK);
    // 公共类处理
    interfaceNode.addChildrenOnly(JavaDomainTreeKey.INTERFACE_COMM);
    // 接口接收传输
    interfaceNode.addChildrenOnly(JavaDomainTreeKey.INTERFACE_DTO);
    // 对外的接口服务
    interfaceNode.addChildrenOnly(JavaDomainTreeKey.INTERFACE_FACADE);
    // 添加错误码文件
    interfaceNode.addChildrenOnly(JavaDomainTreeKey.INTERFACE_ERROR_CODE);
    // 添加静态常量
    interfaceNode.addChildrenOnly(JavaDomainTreeKey.INTERFACE_CONSTANT);
    // 资源加载
    interfaceNode.addChildrenOnly(JavaDomainTreeKey.INTERFACE_CONFIG);
    // 添加参数校验的类
    interfaceNode.addChildrenOnly(JavaDomainTreeKey.INTERFACE_VALID);
  }

  /**
   * 获取逻辑定义的根节点
   *
   * @return
   */
  public DirNode getRoot() {
    return defineRoot;
  }

  public String getModelName() {
    return modelName;
  }
}
