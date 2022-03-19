package loki.script.project;

import java.util.List;

/**
 * 流程
 *
 * @author zhengquan
 */
public interface IFunctionProcess {

    /**
     * 获取当前正在执行的Action
     *
     * @return
     */
    public IScriptFunction getCurrAction();

    /**
     * 获取下一个将要执行的Action
     *
     * @return
     */
    public IScriptFunction getNextAction();

    /**
     * 执行当前要执行的Action
     */
    public void doAction();

    /**
     * 获取Action列表
     *
     * @return
     */
    public List<IScriptFunction> getActionList();

    public ProcessStatus getStatus();

    enum ProcessStatus {
        Init, Running, Waiting, Fin
    }

}
