package loki.script.project;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class AbstractFunctionProcess implements IFunctionProcess {

    protected ArrayList<IScriptFunction> taskList;
    protected int currentIndex;

    private ProcessStatus status;

    @Override
    public IScriptFunction getCurrAction() {
        return getActionList().get(currentIndex);
    }

    @Override
    public IScriptFunction getNextAction() {
        if (getActionList().size() <= currentIndex + 1) {
            return null;
        }
        return getActionList().get(currentIndex + 1);
    }

    protected void doProcess() {
        while (getStatus() != ProcessStatus.Fin) {
            if (getCurrAction() == null) {
                this.status = ProcessStatus.Fin;
                return;
            }
            doAction();
        }
    }

    @Override
    public void doAction() {
        // 执行当前流程
        final IScriptFunction currAction = getCurrAction();
        Map<String, Object> returnObj = currAction.doExec();

        // 将当前返回值, 加入下面流程的返回值
        final IScriptFunction nextAction = getNextAction();
        if (nextAction != null) {
            // 将返回值加入到参数中
            nextAction.addParams(returnObj);
        }

        // 向下移动
        this.currentIndex++;
    }

    @Override
    public List<IScriptFunction> getActionList() {
        return taskList;
    }
}
