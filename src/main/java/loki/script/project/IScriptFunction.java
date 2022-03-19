package loki.script.project;

import java.util.Map;

public interface IScriptFunction {

    /**
     * function状态枚举
     */
    public enum FunctionStatus {
    }

    public FunctionStatus getStatus();

    /**
     * 获取当前函数所在的模块
     *
     * @return
     */
    public IScriptModule getModule();

    /**
     * 获取编码
     *
     * @return
     */
    public String getCode();

    /**
     * 获取脚本文本
     *
     * @return
     */
    public String getScriptText();

    /**
     * 执行
     *
     * @return 返回值
     */
    public Map<String, Object> doExec();

    /**
     * 请求参数
     *
     * @return
     */
    public Map<String, Object> getParams();

    /**
     * 添加参数
     *
     * @param params
     * @return
     */
    public IScriptFunction addParams(Map<String, Object> params);

    /**
     * 重置以重新运行
     */
    default IScriptFunction reset() {
        final Map<String, Object> params = getParams();
        if (params != null) {
            params.clear();
        }
        return this;
    }
}