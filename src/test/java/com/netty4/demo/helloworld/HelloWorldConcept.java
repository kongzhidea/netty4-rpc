package com.netty4.demo.helloworld;

/**
 * http://blog.csdn.net/column/details/enjoynetty.html
 * <p>
 * 在3.x版本中此处有很大区别。在3.x版本中write()方法是自动flush的。在4.x版本的前面几个版本也是一样的。但是在4.0.9之后修改为WriteAndFlush。普通的write方法将不会发送消息。需要手动在write之后flush()一次
 *
 * @author Bazingalyncc
 *         描述：
 *         时间  2016年4月29日
 */
public class HelloWorldConcept {

    /**
     *
     *
     *
     *
     *                   ________________________                                 __________________________
     *                  |                        |                               |                          |    
     *                  |   <-----Inbound-----   |                               |   ---inbound------- >    |   ________
     *                  |   _____        ______  |                               |    _______      ____     |  |        |
     *      _______     |  |     |       |    |  |                               |    |     |     |    |    |  |        |  
     *     |       |    |  |  ②  |       |  ③ |  |      ___________________      |    |  ⑤  |     |  ⑥ |    |  |        |
     *     |       |    |  |_____|       |____|  |     |                   |     |    |_____|     |____|    |  |        |     
     *     |client |----|-------______-----------|-----|      network      |-----|--------------------------|--| server |
     *     |       |    |       |     |          |     |___________________|     |          ______          |  |        |
     *     |       |    |       |  ①  |          |                               |          |     |         |  |        |         
     *     |       |    |       |_____|          |                               |          |  ④  |         |  |________|
     *     |       |    |                        |                               |          |_____|         |
     *     |_______|    |   -----Outbound--->    |                               |    <-----outbound----    | 
     *                  |___ChannelPipeline______|                               |______ChannelPipeline_____| 
     *
     *  ①：StringEncoder继承于MessageToMessageEncoder，而MessageToMessageEncoder又继承于ChannelOutboundHandlerAdapter
     *  ②：HelloWorldClientHandler.java
     *  ③：StringDecoder继承于MessageToMessageDecoder，而MessageToMessageDecoder又继承于ChannelInboundHandlerAdapter
     *  ④：StringEncoder 编码器
     *  ⑤：StringDecoder 解码器
     *  ⑥：HelloWorldServerHandler.java
     *
     *
     *
     */

}
