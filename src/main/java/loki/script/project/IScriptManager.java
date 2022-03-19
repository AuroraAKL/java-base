package loki.script.project;

import javax.script.CompiledScript;

public interface IScriptManager {

    public CompiledScript getEngine(String functionCode);

    default CompiledScript getEngine(IScriptFunction functionCode) {
        return getEngine(functionCode.getCode());
    }

    /**
     * 注册脚本到
     *
     * @param function
     */
    public void registerFunc(IScriptFunction function);

    public void compile();
}
