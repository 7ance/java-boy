package cn.lance.tlv;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Tag-Length-Value解析工具类
 */
public class TlvUtils {

    /**
     * 默认解析（深度2层）
     *
     * @param text TLV字符串
     * @return 解析对象
     */
    public static List<TlvObject> parse(String text) {
        return parse(text, 2);
    }

    /**
     * 自定义解析
     *
     * @param text  TLV字符串
     * @param depth 解析深度（解析 N 层就传数字 N ）
     * @return 解析对象
     */
    public static List<TlvObject> parse(String text, int depth) {
        List<TlvObject> result = new ArrayList<>();

        if (text == null || text.isEmpty()) {
            return result;
        }

        int index = 0;
        while (index < text.length()) {
            // tag
            if (index + 2 > text.length()) {
                break;
            }
            String tag = text.substring(index, index + 2);
            if (!StringUtils.isNumeric(tag)) {
                break;
            }
            index += 2;

            // length
            if (index + 2 > text.length()) {
                break;
            }
            String lengthStr = text.substring(index, index + 2);
            if (!StringUtils.isNumeric(lengthStr)) {
                break;
            }
            int length = Integer.parseInt(lengthStr);
            index += 2;

            // value
            String value = "";
            if (index + length > text.length()) {
                break;
            }
            List<TlvObject> subTags = null;
            if (length > 0) {
                value = text.substring(index, index + length);
                if (depth > 1) {
                    subTags = parse(value, depth - 1);
                }
                index += length;
            }

            // item
            TlvObject obj = new TlvObject();
            obj.setTag(tag);
            obj.setLength(length);
            obj.setValue(value);
            obj.setSubTags(subTags);
            result.add(obj);
        }

        return result;
    }

}
