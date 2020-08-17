# **`Curator操作Zookeeper`**

CuratorOperator：具体操作类 
    创建 、 增 、 删 、 改 、 查 
   　　createNode()：创建节点 >>  withMode()：节点类型  withAcl()：节点数据  
    　　updateNode()：更新节点  
    　　getNode()：查询节点 >> storingStatIn()：获取节点Stat  
    　　getChildNode()：查询子节点 getChild()  
    　　deleteNode()：删除节点>>guaranteed()：保证删除  deletingChildrenIfNeeded()：删除子节点  
    　　existNode()：判断节点是否存在返回Stat   
    
CuratorWatcherAndAcl：监听事件类
　　watcherNode()：watcher事件监听，监听只会触发一次，监听当前路径的操作>>useingWatcher(Watcher) >>useingWatcher(CuratorWatcher)  
　　watcher()：watcher事件监听，永久性事件。采用NodeCache监听当前路径事件  
　　childWatcher()：子节点事件监听，PathChildrenCache()方法，start后初始化方式  
 
CuratorAcl：权限操作类  
　　setAcl()：设置权限 >>Acl >> Id >>
DigestAuthenticationProvider.generateDigest(userPwd)  
　　.authorization("digest","user:pwd")：登录验证  
  
Zookeeper分布式锁流程：  

访问资源->获得锁   

判断锁是否被占用   

->创建zk节点->拥有锁->释放锁 | 占用（等待释放）