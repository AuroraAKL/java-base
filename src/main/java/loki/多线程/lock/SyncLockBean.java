package loki.多线程.lock;


import loki.多线程.util.Tools;

public class SyncLockBean extends LockBean {
    @Override
    public void setName(String name) {
        synchronized (this){
            Tools.sleep(1000);
            this.name = name;
        }
    }

    @Override
    public String getName() {
        synchronized (this){
            Tools.sleep(1000);
        }
        return name;
    }
}
