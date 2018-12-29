
#### netty rpc
* netty4 
    * 4.1.*， 默认采用内存池
* 采用hession序列化
    * 注意内存溢出问题，记得释放ByteBuf。  重要！！！
    * HessianEncoder 目前客户端和服务端使用的类型为：Serializable，但是继承的MessageToByteEncoder是可以指定泛型类型的，不是此类型则不处理。 
       * 推荐写法为：public class HessianEncoder extends MessageToByteEncoder&lt;RpcBaseModel&gt;
       * 好处：客户端和服务端通信时候 不用传其他类型，还可以设置其他的编解码工具。
       * RpcBaseModel 是 RpcRequest和RpcResponse 的基类。
       * 对应的handler需要处理，如：RpcServerHandler和RpcClientHandler
       * 发消息地方需要处理，需要传RemotingCommand类型，如RpcServerHandler和RpcClient
* 粘包拆包
* 调用样例：
    * server 启动入口见:DeamonRunner, 启动脚本见 start.sh，停机脚本见：stop.sh
        * 服务自动治理, 见 RpcServer
        * 是否注册到zk上，见参数：registryFlag， 入口在 resources/server.properties。
    * client 调用样例：ServiceTest, ServiceSpringTest, ServiceSpringZKTest
        * zk 负载均衡,zk 配置文件见：com/netty4/rpc/core/registry/impl/zookeeper/zk.properties
        * 支持本地绑环境测试, 如 -Dnetty4.hosts.com.netty4.rpc.demo.api.UserService=localhost:9301
        * netty 同步调用


#### 参考[NettyRpc](https://github.com/luxiaoxun/NettyRpc)
* 采用 protostuff 序列化

#### 参考[hrpc](https://github.com/hshenCode/hrpc)
