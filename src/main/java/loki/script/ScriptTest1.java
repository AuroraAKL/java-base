package loki.script;

import javax.script.*;

public class ScriptTest1 {

    public static void main(String[] args) throws ScriptException {

//        testCompile();
//        exec();

        testScriptReturn();

    }

    private static void testScriptReturn() throws ScriptException {
        ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
        ScriptEngine engine = scriptEngineManager.getEngineByExtension("js");

        Object o = engine.eval("var a = 100 + 100;");
        engine.eval("var b = 100 + 300;");
        System.out.println(o);// null
        Object o2 = engine.eval("(function(){return a;})()");
        System.out.println(o2); // (function(){return a;})()
        Object o3 = engine.eval("a;b;");
        System.out.println(o3); // b

    }

    /**
     * 执行脚本
     * @throws ScriptException
     */
    public static void exec() throws ScriptException {
        ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
        ScriptEngine engine = scriptEngineManager.getEngineByExtension("js");

        final long start = System.currentTimeMillis();
        engine.eval("print('Hello!');");
        System.out.println("spend:" + (System.currentTimeMillis() - start));
        engine.eval("var name = 10;");
        engine.eval("print(name);");
        engine.eval("for (var i = 0; i < name; i++) {print(i);}");
        engine.eval("var func = function(){print('this is func');};");
        engine.eval("func();");
        System.out.println("spend:" + (System.currentTimeMillis() - start));
        engine.put("sout", System.out);
        engine.eval("sout.println('System.out')");
        engine.eval("var testJavaStr = new java.lang.StringBuilder('this is java.lang.StringBuilder').toString();");
        engine.eval("var testJavaStr2 = new java.lang.StringBuilder('this is java.lang.StringBuilder').toString();");
        engine.eval("print(testJavaStr)");
        engine.eval("print(testJavaStr2)");
        System.out.println(engine.getContext().getAttribute("name"));

        System.out.println("spend:" + (System.currentTimeMillis() - start));

    }

    public static void testCompile() throws ScriptException {
        final long start = System.currentTimeMillis();
        String javaVal = "java Val";
        String text = "print('Hello!');"
                + "var name = 10000;"
                + "print(name);"
                + "var sum = 0;"
                + "for (var i = 0; i < name; i++) { sum  += name;}"
                + "print(sum);"
                + "var t = {};"
                + "for (var i = 0; i <= name; i++) { t[i + ''] = i;}"
                + "print('js map:' + t[name + '']);"
                + "var func = function(){print('this is func');};"
                + "var testJavaStr = new java.lang.StringBuilder('this is java.lang.StringBuilder').toString();"
                + "print(javaVal);"
                + "func();";
        final CompiledScript script = compile(text);
        System.out.println("spend:" + (System.currentTimeMillis() - start));
        final SimpleBindings bindings = new SimpleBindings() {{ put("javaVal", javaVal); }};
        script.eval(bindings);
        script.eval(bindings);
        System.out.println("spend:" + (System.currentTimeMillis() - start));
    }

    /**
     * 编译脚本
     *
     */
    public static CompiledScript compile(String text) throws ScriptException {
        ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
        ScriptEngine engine = scriptEngineManager.getEngineByExtension("js");
        CompiledScript script = ((Compilable) engine).compile(text);
        return script;
    }
}
