package loki.多线程.lock;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * 实现锁
 * 1. 实现队列
 * 2. 锁状态变量
 * 3. 等待队列
 * 4. 获取锁的线程
 */
public class MyFairLock {

    /**
     * 锁状态
     */
    private volatile int state;

    /**
     * 当前持有锁的线程
     */
    private volatile Thread lockThread;

    /**
     * unsafe
     */
    private static final Unsafe unsafe = getUnsafe();

    private static Unsafe getUnsafe() {
        try {
            Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafe.setAccessible(true);
            return (Unsafe) theUnsafe.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private static long getStateOffset() {
        try {
            Field state = MyFairLock.class.getDeclaredField("state");
            return unsafe.objectFieldOffset(state);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * state的偏移量
     */
    private static final long stateOffset = getStateOffset();


    /**
     * 更新状态
     */
    private boolean casState(int except, int update) {
        return unsafe.compareAndSwapInt(this, stateOffset, except, update);
    }

    private final SyncLinkedList list = new SyncLinkedList();

    private static class SyncLinkedList {
        /**
         * 链表头节点
         */
        private volatile Node<Thread> head = new Node<>();
        /**
         * 链表尾
         */
        private volatile Node<Thread> tail = head;

        /**
         * 更新状态
         */
        private boolean casTail(Node<Thread> except, Node<Thread> update) {
            return unsafe.compareAndSwapObject(this, tailOffset, except, update);
        }

        /**
         * tail的偏移量
         */
        private static final long tailOffset = getTailOffset();

        private static long getTailOffset() {
            try {
                Field state = SyncLinkedList.class.getDeclaredField("tail");
                return unsafe.objectFieldOffset(state);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }

        public Node<Thread> enqueueCurrentThread() {
            Thread thread = Thread.currentThread();
            while (true) {
                Node<Thread> t = this.tail;
                Node<Thread> node = Node.build(thread, t);
                if (casTail(t, node)) {
                    // 更新尾节点成功了，让原尾节点的next指针指向当前节点 -- 这里访问同一个 t 只会有一个线程
                    // cas成功代表 tail的下一个节点 一定是 node
                    t.next = node;
                    return node;
                }
            }
        }
        public void setHead(Node<Thread> newHead) {
            head.node = null;
            head.next = null;
            head = newHead;
        }
        public Node<Thread> getFirstNode() {
            return head.next;
        }

        public boolean isFirstNode(Node<Thread> node) {
            return getFirstNode() == node;
        }

        public boolean isEmpty() {
            return head == tail;
        }

        @Override
        public String toString() {
            Node<Thread> t = getFirstNode();
            StringBuilder s = new StringBuilder().append('[');
            while(t != null){
                s.append(t.node.getName()).append(", ");
                t = t.next;
            }
            return s.append(']').toString();
        }
    }

    /**
     * 阻塞链表节点
     */
    private static class Node<T> {
        private Node<T> pre;
        private Node<T> next;
        private T node;
        public static <T> Node<T> build(T node, Node<T> pre) {
            Node<T> tNode = new Node<>();
            tNode.node = node;
            tNode.pre = pre;
            return tNode;
        }

        public Node<T> pre() {
            return pre;
        }
    }


    public void lock() {
        // 尝试更新state字段，更新成功说明占有了锁
        System.out.println("try lock:" + Thread.currentThread().getName());
        if (list.isEmpty()){
            if(casState(0, 1)) {
                System.out.println("quick lock:" + Thread.currentThread().getName());
                // 一个线程
                return;
            }
        }

        // 占有失败 -- 会有多个线程
        // 未更新成功则入队
        Node<Thread> node = list.enqueueCurrentThread();
        Node<Thread> pre = node.pre();
        // 再次尝试获取锁，需要检测上一个节点是不是head，按入队顺序解锁
        // 如果不是队列的头, 那么就入队
        unsafe.park(false, 0L);
        System.out.println("post-pack:" + node.node.getName());
        // 1. 同队头线程是可能被唤起多次的, 这并不影响.    当线程被唤起但是没有抢到锁时, 此时锁被其他线程抢占 运行结束后会再次唤起队头线程.
        // 2. 设置首节点是通过获取同步状态成功的线程完成的 由于只有一个线程能够获取到同步状态，因此设置头节点的方法并不需要CAS来保障
        // 3. 每个被park的线程是可能被中断的, 这里使用的 list是FIFO的非线程安全的双向队列. 因此 这里要保证只能由头节点出队持有锁
        // park 可被3个事件唤起 其中之一就是无缘由的: The call spuriously (that is, for no reason) returns.
        while (!list.isFirstNode(node) || !casState(0, 1)) {
            if(!list.isFirstNode(node)) {
                System.out.println("pack:" + node.node.getName() + list);
                unsafe.park(false, 0L);
            }
        }
        System.out.println("lock:" + Thread.currentThread().getName());
//            while (!list.isFirstNode(node) || !casState(0, 1)){
//               unsafe.park(false, 0L);
//            }

        // 下面不需要原子更新，因为同时只有一个线程访问到这里

        // head后移一位
        list.setHead(node);
        // 将上一个节点从链表中剔除，协助GC
        pre.next = null;
    }

    // 解锁
    public void unlock() {
        // 把state更新成0，这里不需要原子更新，因为同时只有一个线程访问到这里
        state = 0;
        // 下一个待唤醒的节点
        Node<Thread> node = list.getFirstNode();
        // 下一个节点不为空，就唤醒它
        if (node != null) {
            System.out.println("unpark:" + node.node.getName());
            unsafe.unpark(node.node);
        }
    }

}
