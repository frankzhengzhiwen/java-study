package util;

public interface Const {

    /**
     * 字符编码
     */
    String CHARSET = PropertiesUtil.get("mymq.charset");

    /**
     * 服务端主机地址：IP或域名
     */
    String SERVER_HOST = PropertiesUtil.get("mymq.server.host");

    /**
     * 服务端端口号
     */
    Integer SERVER_PORT = Integer.parseInt(PropertiesUtil.get("mymq.server.port"));

    /**
     * 服务端线程数
     */
    Integer SERVER_THREAD_NUM = Integer.parseInt(PropertiesUtil.get("mymq.server.thread-num"));

    /**
     * 服务端消息持久化保存的文件路径
     */
    String SERVER_REPO_DIR = PropertiesUtil.get("mymq.server.repo.dir");

    /**
     * 客户端主机地址：IP或域名
     */
    String CLIENT_HOST = PropertiesUtil.get("mymq.client.host");

    /**
     * 客户端口号
     */
    Integer CLIENT_PORT = Integer.parseInt(PropertiesUtil.get("mymq.client.port"));

    /**
     * 消息结束符
     */
    String MESSAGE_EOF = PropertiesUtil.get("mymq.message.eof");

    /**
     * 消息类型：发布消息
     */
    String MESSAGE_TYPE_PUB = PropertiesUtil.get("mymq.message.type.pub");

    /**
     * 消息类型：订阅消息
     */
    String MESSAGE_TYPE_SUB = PropertiesUtil.get("mymq.message.type.sub");
}
