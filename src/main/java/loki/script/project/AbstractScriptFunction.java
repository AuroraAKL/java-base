package loki.script.project;

import javax.annotation.Nullable;
import javax.script.CompiledScript;
import javax.script.ScriptException;
import javax.script.SimpleBindings;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractScriptFunction implements IScriptFunction {
    protected Map<String, Object> localParams;
    protected String scriptName;
    protected CompiledScript engine;
    private FunctionStatus status;
    private IScriptModule module;
    private String scriptText;
    private String code;
    private String name;


    @Nullable
    @Override
    public Map<String, Object> doExec() {
        final SimpleBindings bindings = new SimpleBindings();
        if (localParams != null) {
            bindings.putAll(localParams);
        }
        try {
            final Object returnVal = engine.eval(bindings);
            return toMap(returnVal);
        } catch (ScriptException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    protected Map<String, Object> toMap(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof Map) {
            return (Map<String, Object>) obj;
        }
        return new HashMap<String, Object>(2) {{
            put(getCode(), obj);
        }};
    }

    @Override
    public FunctionStatus getStatus() {
        return status;
    }

    @Override
    public IScriptFunction addParams(Map<String, Object> params) {
        if (params == null) {
            return this;
        }
        if (this.localParams == null) {
            this.localParams = new HashMap<>(params.size() << 1);
        }
        this.localParams.putAll(params);
        return null;
    }

    @Override
    public IScriptModule getModule() {
        return module;
    }

    @Override
    public Map<String, Object> getParams() {
        return localParams;
    }

    @Override
    public String getScriptText() {
        return scriptText;
    }

    @Override
    public String getCode() {
        return code;
    }
}
