package framework.disruptor;

import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import framework.disruptor.consumer.LogEventConsumer;
import framework.disruptor.event.LogEvent;
import framework.disruptor.factory.LogEventFactory;
import framework.disruptor.producer.LogEventProducer;

import java.util.concurrent.ThreadFactory;

/**
 * @author vonzhou
 * @version 2018/9/21
 */
public class LogEventMain {
    public static void main(String[] args) {
        long start = System.currentTimeMillis();

        // 环形数组的容量，必须要是2的次幂
        int bufferSize = 1024;

        // 构造 Disruptor
        Disruptor<LogEvent> disruptor = new Disruptor<>(new LogEventFactory(), bufferSize, new ThreadFactory() {
            private int counter = 0;
            private String prefix = "DisruptorWorker";

            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r, prefix + "-" + counter++);
                t.setDaemon(true);
                return t;
            }
        }, ProducerType.SINGLE, new YieldingWaitStrategy());

        // 设置消费者
        disruptor.handleEventsWith(new LogEventConsumer());

        // 启动 Disruptor
        disruptor.start();

        // 生产者要使用 Disruptor 的环形数组
        LogEventProducer producer = new LogEventProducer(disruptor.getRingBuffer());

        // 模拟消息发送
        for (int i = 0; i < 5000; i++) {
            producer.onData(String.format("M%s", i));
        }
        System.out.println(String.format("== Total cost %s seconds ==", (System.currentTimeMillis() - start) / 1000));
    }
}
