package com.feijimiao.xianyuassistant.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

/**
 * XSS过滤工具类
 * 用于过滤用户输入中的恶意脚本
 */
@Slf4j
@Component
public class XssFilterUtils {

    // 禁止的标签模式（i表示忽略大小写）
    private static final Pattern[] FORBIDDEN_PATTERNS = {
        // script标签
        Pattern.compile("<script[^>]*>[\\s\\S]*?</script>", Pattern.CASE_INSENSITIVE),
        // 事件处理器
        Pattern.compile("on\\w+\\s*=\\s*['\"]?[^'\"\\s>]*['\"]?", Pattern.CASE_INSENSITIVE),
        // javascript:伪协议
        Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE),
        // vbscript:伪协议
        Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE),
        // data:伪协议
        Pattern.compile("data:", Pattern.CASE_INSENSITIVE),
        // iframe标签
        Pattern.compile("<iframe[^>]*>[\\s\\S]*?</iframe>", Pattern.CASE_INSENSITIVE),
        // object标签
        Pattern.compile("<object[^>]*>[\\s\\S]*?</object>", Pattern.CASE_INSENSITIVE),
        // embed标签
        Pattern.compile("<embed[^>]*>", Pattern.CASE_INSENSITIVE),
        // form标签
        Pattern.compile("<form[^>]*>[\\s\\S]*?</form>", Pattern.CASE_INSENSITIVE),
        // style标签中的expression
        Pattern.compile("expression\\s*\\(", Pattern.CASE_INSENSITIVE),
        // import语句
        Pattern.compile("@import", Pattern.CASE_INSENSITIVE),
        // eval函数
        Pattern.compile("eval\\s*\\(", Pattern.CASE_INSENSITIVE),
    };

    // 需要转义的HTML特殊字符
    private static final String[] ESCAPE_CHARS = {
        "<", ">", "\"", "'", "&"
    };

    private static final String[] ESCAPE_REPLACEMENTS = {
        "&lt;", "&gt;", "&quot;", "&#x27;", "&amp;"
    };

    /**
     * 过滤XSS攻击向量
     * 移除危险的HTML标签和脚本
     *
     * @param input 原始输入
     * @return 过滤后的安全字符串
     */
    public static String filter(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        String result = input;

        // 移除禁止的模式
        for (Pattern pattern : FORBIDDEN_PATTERNS) {
            result = pattern.matcher(result).replaceAll("");
        }

        log.debug("XSS过滤: 输入长度={}, 输出长度={}", input.length(), result.length());

        return result;
    }

    /**
     * 转义HTML特殊字符
     * 将所有HTML标签转为实体编码
     *
     * @param input 原始输入
     * @return 转义后的字符串
     */
    public static String escapeHtml(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        String result = input;
        for (int i = 0; i < ESCAPE_CHARS.length; i++) {
            result = result.replace(ESCAPE_CHARS[i], ESCAPE_REPLACEMENTS[i]);
        }

        return result;
    }

    /**
     * 过滤并保留安全的换行和空格
     * 适用于需要保留格式的文本内容
     *
     * @param input 原始输入
     * @return 过滤后保留格式的字符串
     */
    public static String filterWithFormatting(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        // 先进行XSS过滤
        String result = filter(input);

        // 保留换行符和空格
        result = result.replace("\n", "<br>");
        result = result.replace("  ", " &nbsp;");

        return result;
    }

    /**
     * 检查是否包含XSS攻击向量
     *
     * @param input 待检查的字符串
     * @return 如果包含XSS攻击向量返回true
     */
    public static boolean containsXss(String input) {
        if (input == null || input.isEmpty()) {
            return false;
        }

        String filtered = filter(input);
        return !filtered.equals(input);
    }
}
