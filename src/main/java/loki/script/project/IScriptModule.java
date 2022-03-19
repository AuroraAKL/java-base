package loki.script.project;

import java.util.Map;

public interface IScriptModule {
    public Map<String, Object> getThreadParams();

    public void removeThreadParam(String key);

    public Map<String, Object> getGlobalParams();

    public void setGlobalParams(String key, Object val);

    public void removeGlobalParam(String key);

    /**
     * 重置以重新运行
     */
    public void reset();
}
