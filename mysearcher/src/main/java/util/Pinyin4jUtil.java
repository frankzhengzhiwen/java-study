package util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.util.*;

public class Pinyin4jUtil {

    /**
     * 中文字符格式
     */
    private static final String CHINESE_PATTERN = "[\\u4E00-\\u9FA5]";

    /**
     * 拼音输出格式
     */
    private static final HanyuPinyinOutputFormat PINYIN_OUTPUT_FORMAT;

    static{
        PINYIN_OUTPUT_FORMAT = new HanyuPinyinOutputFormat();

        // 输出小写
        PINYIN_OUTPUT_FORMAT.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        // 不带音调
        PINYIN_OUTPUT_FORMAT.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        // 包含字符v
        PINYIN_OUTPUT_FORMAT.setVCharType(HanyuPinyinVCharType.WITH_V);
    }

    public static Set<String> get(String src){
        Set<String> r = new HashSet<>();
        Set<String> full = get(src, true);
        Set<String> first = get(src, false);
        if(full != null)
            r.addAll(full);
        if(first != null)
            r.addAll(first);
        return r;
    }

    /**
     * 获取汉语拼音
     * @param src 源字符串
     * @param fullSpell 是否为全拼
     * @return 包含有效汉字时输出，否则返回null
     */
    public static Set<String> get(String src, boolean fullSpell){
        if(src == null || src.trim().length() == 0 || !src.matches(".*"+CHINESE_PATTERN+".*")){
            return null;
        }
        char[] srcChars = src.toCharArray();
        String[][] srcArray = new String[srcChars.length][];
        for(int i=0; i<srcChars.length; i++){
            char c = srcChars[i];
            Set<String> set = new HashSet<>();
            if(String.valueOf(c).matches(CHINESE_PATTERN)){
                try {
                    String[] temp = PinyinHelper.toHanyuPinyinStringArray(c, PINYIN_OUTPUT_FORMAT);
                    if(temp == null || temp.length == 0){
                        set.add(String.valueOf(c));
                    }else if(fullSpell){
                        set.addAll(Arrays.asList(temp));
                    }else{
                        for(String s : temp){
                            set.add(String.valueOf(s.charAt(0)));
                        }
                    }
                } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
                    set.add(String.valueOf(c));
                }
            }else {
                set.add(String.valueOf(c));
            }
            srcArray[i] = set.toArray(new String[set.size()]);
        }
        return compose(srcArray);
    }

    /**
     * 字符串转化为汉语拼音二维数组后，进行排列组合
     * @param src
     * @return 不重复的排列组合
     */
    public static Set<String> compose(String[][] src){
        for(int i=1; i<src.length; i++){
            src[0] = compose(src[0], src[i]);
        }
        Set<String> set = new HashSet<>();
        set.addAll(Arrays.asList(src[0]));
        return set;
    }

    /**
     * 将两组字符串数组进行排列组合
     * @param fir 第一个字符串数组
     * @param sec 第二个字符串数组
     * @return 排列组合后的字符串数组
     */
    public static String[] compose(String[] fir, String[] sec){
        Set<String> set = new HashSet<>();
        for(int i=0; i<fir.length; i++){
            for(int j=0; j<sec.length; j++){
                set.add(fir[i] + sec[j]);
            }
        }
        return set.toArray(new String[set.size()]);
//        String[] r = new String[fir.length*sec.length];
//        for(int i=0; i<fir.length; i++){
//            for(int j=0; j<sec.length; j++){
//                r[i*sec.length+j] = fir[i] + sec[j];
//            }
//        }
//        return r;
    }

    public static void main(String[] args) throws BadHanyuPinyinOutputFormatCombination {
//        System.out.println(Arrays.toString(compose(new String[]{"a","b","c"}, new String[]{"1","2"})));

        System.out.println("=========================");
        System.out.println(get("长还"));
        System.out.println(get("abc"));
        System.out.println("=========================");
        System.out.println(get("中华人民共和国"));
    }
}