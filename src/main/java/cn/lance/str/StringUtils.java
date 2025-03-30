package cn.lance.str;

public class StringUtils {

    private StringUtils() {
    }

    /**
     * 字符串模版拼接
     *
     * @param template 模版字符串，占位符为 {}
     * @param args     参数
     * @return 拼接后的字符串
     */
    public static String format(String template, Object... args) {
        if (args == null || args.length == 0) {
            return template;
        }

        int i = 0;
        StringBuilder sb = new StringBuilder();
        int startIdx = 0;
        while (true) {
            int placeholderIdx = template.indexOf("{}", startIdx);
            if (placeholderIdx == -1 || i >= args.length) {
                break;
            }
            sb.append(template, startIdx, placeholderIdx);
            sb.append(args[i++]);
            startIdx = placeholderIdx + 2;
        }
        sb.append(template.substring(startIdx));
        return sb.toString();
    }

}
