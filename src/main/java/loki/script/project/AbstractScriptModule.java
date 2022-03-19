package loki.script.project;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public abstract class AbstractScriptModule implements IScriptModule {

    /**
     * 全局参数
     */
    private static final ConcurrentMap<String, Object> globalParams = new ConcurrentHashMap<>();

    /**
     * 线程全局参数
     */
    private static final ThreadLocal<Map<String, Object>> threadParams = InheritableThreadLocal.withInitial(HashMap::new);

    @Override
    public Map<String, Object> getThreadParams() {
        final HashMap<String, Object> map = new HashMap<>();
        threadParams.get().forEach(map::put);
        return map;
    }

    @Override
    public void removeThreadParam(String key) {
        getGlobalParams().remove(key);
    }

    @Override
    public Map<String, Object> getGlobalParams() {
        final HashMap<String, Object> map = new HashMap<>();
        globalParams.forEach(map::put);
        return map;
    }

    @Override
    public void setGlobalParams(String key, Object val) {
        if (key == null) {
            return;
        }
        globalParams.put(key, val);
    }

    @Override
    public void removeGlobalParam(String key) {
        globalParams.remove(key);
    }

    @Override
    public void reset() {
        threadParams.remove();
    }
}
