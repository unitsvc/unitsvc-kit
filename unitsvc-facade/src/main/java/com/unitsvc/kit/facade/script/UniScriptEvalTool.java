package com.unitsvc.kit.facade.script;

import cn.hutool.crypto.digest.DigestUtil;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.unitsvc.kit.facade.script.exception.JsCalculateException;
import com.unitsvc.kit.facade.script.exception.JsCompileException;
import com.jpardus.spider.sccs.Log;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import javax.script.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 功能描述：脚本计算工具
 *
 * @author : jun
 * @version : v1.0.0
 * @since : 2023/5/24 10:50
 **/
public class UniScriptEvalTool {

    /**
     * 动态变量正则表达式
     */
    private static final String PARAM_REGEX = "\\{\\{([A-Za-z0-9_]*)}}";

    /**
     * 正则表达式预编译
     */
    private static final Pattern PATTERN = Pattern.compile(PARAM_REGEX);

    /**
     * 脚本函数编译正则
     */
    private static final String FUNCTION_REGEX = "function\\(\\)\\s*\\{";

    /**
     * 正则表达式预编译
     */
    private static final Pattern FUNCTION_PATTERN = Pattern.compile(FUNCTION_REGEX);

    /**
     * 函数标识常量
     */
    private static final String FUNCTION = "function";

    /**
     * 序列化
     */
    private static final Gson GSON = new GsonBuilder().disableHtmlEscaping().create();

    /**
     * 并发脚本编译容器
     */
    private static final Map<String, JsCompileMeta> FUNCTION_MAPPING = new ConcurrentHashMap<>();

    /**
     * 脚本引擎
     */
    private static final ScriptEngine SCRIPT_ENGINE = new ScriptEngineManager().getEngineByName("javascript");


    /**
     * 解析参数
     *
     * @param jsScript 脚本
     * @return 动态参数
     */
    public static Set<String> findParams(String jsScript) {
        Matcher matcher = PATTERN.matcher(jsScript);
        Set<String> params = new HashSet<>();
        while (matcher.find()) {
            String name = matcher.group(1);
            params.add(name);
        }
        matcher.reset();
        return params;
    }

    /**
     * 正则替换编译脚本
     *
     * @param function    原始函数
     * @param replacement 待替换值
     * @return
     */
    private static String replaceFirst(String function, String replacement) {
        return FUNCTION_PATTERN.matcher(function).replaceFirst(replacement);
    }

    /**
     * 函数编译
     *
     * <pre>
     * 说明：
     * 1.变量使用，{{变量名称}} 占位。
     * 2.简单函数书写，格式，return {{a}} + {{b}};
     * 3.复杂函数书写，格式，function() { return {{a}} + {{b}} };
     * 4.函数必须有return返回值。
     * </pre>
     *
     * @param originJs 源脚本
     * @return
     */
    public static JsCompileMeta compileFunctionAndCache(String originJs) {
        if (StringUtils.isEmpty(originJs)) {
            return JsCompileMeta.builder().build();
        }
        // 函数名称定义
        String functionId = "F_" + DigestUtil.sha256Hex(originJs);
        String compileJs = "";
        // 获取编译脚本
        JsCompileMeta compileMeta = FUNCTION_MAPPING.get(functionId);
        if (null == compileMeta) {
            try {
                String newJs = originJs;
                // 完善函数定义
                if (!originJs.contains(FUNCTION)) {
                    newJs = String.format("function(){ %s } ", originJs);
                }
                Set<String> params = findParams(originJs);
                if (CollectionUtils.isNotEmpty(params)) {
                    String paramJs = newJs;
                    for (String param : params) {
                        paramJs = paramJs.replace("{{" + param + "}}", "obj." + param);
                    }
                    compileJs = replaceFirst(paramJs, "function " + functionId + "(params) { " + "\n\tvar obj = JSON.parse(params);");
                } else {
                    compileJs = replaceFirst(newJs, "function " + functionId + "() {");
                }

                Log.trace(String.format("【脚本编译】原始JS：%s，编译JS：%s", originJs, compileJs));
                Compilable compilableEngine = (Compilable) SCRIPT_ENGINE;
                // 脚本编译
                CompiledScript compile = compilableEngine.compile(compileJs);
                // 实现函数注册
                compile.eval();
                // 去除引用
                compile = null;
                // 封装数据
                compileMeta = JsCompileMeta.builder()
                        .engine(SCRIPT_ENGINE)
                        .functionId(functionId)
                        .params(params)
                        .compileJs(compileJs)
                        .build();
                // 保存函数变量
                FUNCTION_MAPPING.put(functionId, compileMeta);
            } catch (Exception e) {
                Log.error(String.format("【脚本编译异常】原始JS：%s，编译JS：%s，异常：%s", originJs, compileJs, e.getMessage()), e);
                throw new JsCompileException(e.getMessage(), e);
            }
        }
        return compileMeta;
    }

    /**
     * 编译元数据
     */
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class JsCompileMeta implements Serializable {

        private static final long serialVersionUID = -5604895260299814022L;

        /**
         * 脚本引擎
         */
        private ScriptEngine engine;

        /**
         * 函数名称
         */
        private String functionId;

        /**
         * 函数参数
         */
        private Set<String> params;

        /**
         * 编译脚本
         */
        private String compileJs;

        /**
         * 执行函数
         * <pre>
         *     示例1：
         *         Map<String, Object> paramMap1 = Maps.newHashMap();
         *         paramMap1.put("a", 1);
         *         paramMap1.put("b", 2);
         *     示例2：
         *         JsonObject paramMap2 = new JsonObject();
         *         paramMap2.addProperty("a", 1);
         *         paramMap2.addProperty("b", 2);
         * </pre>
         *
         * @param paramObj 接口参数，可选，示例：JsonObject、Map、对象
         * @return 计算结果
         * @throws JsCalculateException 计算异常
         */
        public Object executeFunction(Object paramObj) throws JsCalculateException {
            try {
                if (null == engine) {
                    return null;
                }
                Object result = null;
                Invocable invocable = (Invocable) engine;
                if (null != paramObj) {
                    result = invocable.invokeFunction(functionId, GSON.toJson(paramObj));
                } else {
                    result = invocable.invokeFunction(functionId, "{}");
                }
                // 若是javascript数组对象，则返回集合
                if (result instanceof ScriptObjectMirror && ((ScriptObjectMirror) result).isArray()) {
                    ScriptObjectMirror array = (ScriptObjectMirror) result;
                    return array.values();
                }
                return result;
            } catch (Exception e) {
                Log.error(String.format("【脚本计算异常】脚本：%s，参数：%s，异常：%s", compileJs, paramObj, e.getMessage()), e);
                throw new JsCalculateException(e.getMessage(), e);
            }
        }
    }

    public static void main(String[] args) {
        String originJs = "return {{a}}+{{b}}";

        Map<String, Object> paramMap1 = Maps.newHashMap();
        paramMap1.put("a", 2);
        paramMap1.put("b", 2);
        Object r1 = UniScriptEvalTool.compileFunctionAndCache(originJs).executeFunction(paramMap1);
        System.out.println("r1 = " + r1);

        JsonObject paramMap2 = new JsonObject();
        paramMap2.addProperty("a", 2);
        paramMap2.addProperty("b", 10);
        Object r2 = UniScriptEvalTool.compileFunctionAndCache(originJs).executeFunction(paramMap2);
        System.out.println("r2 = " + r2);

        String js2 = "function() {\n" +
                "    var value;\n" +
                "    switch ({{month}}) {\n" +
                "        case 1:\n" +
                "            value = \"Version 1+11\";\n" +
                "            break;\n" +
                "        case 2:\n" +
                "            value = \"Version 2+10\";\n" +
                "            break;\n" +
                "        case 3:\n" +
                "            value = \"Version 3+9\";\n" +
                "            break;\n" +
                "        case 4:\n" +
                "            value = \"Version 4+8\";\n" +
                "            break;\n" +
                "        case 5:\n" +
                "            value = \"Version 5+7\";\n" +
                "            break;\n" +
                "        case 6:\n" +
                "            value = \"Version 6+6\";\n" +
                "            break;\n" +
                "        case 7:\n" +
                "            value = \"Version 7+5\";\n" +
                "            break;\n" +
                "        case 8:\n" +
                "            value = \"Version 8+4\";\n" +
                "            break;\n" +
                "        case 9:\n" +
                "            value = \"Version 9+3\";\n" +
                "            break;\n" +
                "        case 10:\n" +
                "            value = \"Version 10+2\";\n" +
                "            break;\n" +
                "        case 11:\n" +
                "            value = \"Version 11+1\";\n" +
                "            break;\n" +
                "        case 12:\n" +
                "            value = \"Version 12+0\";\n" +
                "            break;\n" +
                "        default:\n" +
                "            value = \"\";\n" +
                "    }\n" +
                "    return value;\n" +
                "}";
        System.out.println("js2 = \n" + js2);
        JsonObject paramMap = new JsonObject();
        paramMap.addProperty("month", new Random().nextInt(12));
        Object r3 = UniScriptEvalTool.compileFunctionAndCache(js2).executeFunction(paramMap);
        System.out.println("r3 = " + r3);

        // 2000->4577ms,335ms

        JsonObject vParams = new JsonObject();
        vParams.addProperty("version", "2023 12+0");
        // VersionN-1 A月预测
        String v1Js = "function() {\n" +
                "\tvar temp = {{version}};\n" +
                "\tvar version = String(temp.split(' ')[1]);\n" +
                "\tvar month = parseInt(version.split('+')[0]);\n" +
                "\treturn (month-1) + '+' + (13-month) + ' ' + month + '月预测';\n" +
                "}";
        System.out.println("v1Js = \n" + v1Js);
        Object v1Result = UniScriptEvalTool.compileFunctionAndCache(v1Js).executeFunction(vParams);
        System.out.println("预测动态表头列 = " + v1Result);

        // VersionN A月实际
        String v2Js = "function() {\n" +
                "\tvar temp = {{version}};\n" +
                "\tvar version = String(temp.split(' ')[1]);\n" +
                "\tvar month = parseInt(version.split('+')[0]);\n" +
                "\treturn version + ' ' + month + '月实际';\n" +
                "}";
        System.out.println("v2Js = \n" + v2Js);
        Object v2Result = UniScriptEvalTool.compileFunctionAndCache(v2Js).executeFunction(vParams);
        System.out.println("预测实际表头列 = " + v2Result);
    }

}
