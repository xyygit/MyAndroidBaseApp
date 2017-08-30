package lib.core.utils;

import android.app.Activity;
import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.StrikethroughSpan;
import android.view.WindowManager;
import android.widget.TextView;

import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExCommonUtil {

    private ExCommonUtil() {}

    private static class ExCommonUtilHolder {
        private static final ExCommonUtil ecu = new ExCommonUtil();
    }

    public static final ExCommonUtil getInstance() {
        return ExCommonUtilHolder.ecu;
    }

    public static final boolean isEmpty(String input) {
       return TextUtils.isEmpty(input);
    }

    public static final boolean isEmpty(List<?> list) {
        return list == null || list.size() == 0;
    }

    public static final boolean isEmpty(Map<?, ?> map) {
        return map == null || map.size() == 0;
    }

    public static final boolean isEmpty(int[] array) {
        return array == null || array.length == 0;
    }

    public static final boolean isEmpty(float[] array) {
        return array == null || array.length == 0;
    }

    public static final boolean isEmpty(long[] array) {
        return array == null || array.length == 0;
    }

    public static final boolean isEmpty(double[] array) {
        return array == null || array.length == 0;
    }

    public static final boolean isEmpty(Object[] array) {
        return array == null || array.length == 0;
    }

    public static final boolean isEmpty(Object obj) {
        return obj == null;
    }

    public static final String dealEmpty(String str) {
        return isEmpty(str) ? "" : str;
    }

    /**
     * UTF8编码
     * @method stringUTF8Encode
     * @param str
     * @return String
     * @author lightning
     */
    public static final String stringUTF8Encode(String str) {
        return stringEncode(str, "UTF-8");
    }

    /**
     * 自定义编码
     * @method stringEncode
     * @param str
     * @param charset
     * @return String
     * @author lightning
     */
    public static final String stringEncode(String str, String charset) {
        if (!ExCommonUtil.isEmpty(str)) {
            try {
                return URLEncoder.encode(str, charset);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return str;
    }

    /**
     * UTF8解码
     * @method stringUTF8Decode
     * @param str
     * @return String
     * @author lightning
     */
    public static final String stringUTF8Decode(String str) {
        return stringDecode(str, "UTF-8");
    }

    /**
     * 自定义解码
     * @method stringDecode
     * @param str
     * @param charset
     * @return String
     * @author lightning
     */
    public static final String stringDecode(String str, String charset) {
        if (!ExCommonUtil.isEmpty(str)) {
            try {
                return URLDecoder.decode(str, charset);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return str;
    }

    /**
     * unicode解码
     * @method unicodeDecode
     * @param str
     * @return String
     * @author lightning
     */
    public static final String unicodeDecode(String str) {
        StringBuffer sb = new StringBuffer();
        String[] arr = str.split("\\\\u");
        int len = arr.length;
        sb.append(arr[0]);
        for(int i=1; i<len; i++){
            String tmp = arr[i];
            char c = (char)Integer.parseInt(tmp.substring(0, 4), 16);
            sb.append(c);
            sb.append(tmp.substring(4));
        }
        return sb.toString();
    }

    /**
     * unicode编码
     * @method unicodeEncode
     * @param strText
     * @return
     * @return String
     * @author lightning
     */
    public static final String unicodeEncode(String strText) {
        char c;
        String strRet = "";
        int intAsc;
        String strHex;
        for (int i = 0; i < strText.length(); i++) {
            c = strText.charAt(i);
            intAsc = c;
            if (intAsc > 128) {
                strHex = Integer.toHexString(intAsc);
                strRet += "\\u" + strHex;
            } else {
                strRet = strRet + c;
            }
        }
        return strRet;
    }

    /**
     * Double 加
     * @param v1
     * @param v2
     * @return
     */
    public static final Double add(Double v1,Double v2){
        BigDecimal b1 = new BigDecimal(v1.toString());
        BigDecimal b2 = new BigDecimal(v2.toString());
        return b1.add(b2).doubleValue();
    }

    /**
     * Double 减
     * @param v1
     * @param v2
     * @return
     */
    public static final Double sub(Double v1,Double v2){
        BigDecimal b1 = new BigDecimal(v1.toString());
        BigDecimal b2 = new BigDecimal(v2.toString());
        return b1.subtract(b2).doubleValue();
    }

    /**
     * Double 乘
     * @param v1
     * @param v2
     * @return
     */
    public static final Double mul(Double v1,Double v2){
        BigDecimal b1 = new BigDecimal(v1.toString());
        BigDecimal b2 = new BigDecimal(v2.toString());
        return b1.multiply(b2).doubleValue();
    }

    /**
     * Double 除
     * @param v1
     * @param v2
     * @return
     */
    public static final Double div(Double v1,Double v2){
        BigDecimal b1 = new BigDecimal(v1.toString());
        BigDecimal b2 = new BigDecimal(v2.toString());
        return b1.divide(b2, 10, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 防多次点击
     */
    private static long lastClickTime;
    public static boolean canClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        lastClickTime = time;
        if (timeD > 1000) {
            return true;
        }
        return false;
    }

    /**
     * 复制到剪贴板
     * @param context
     * @param text
     */
    public static void copyToClipboard(Context context, String text) {
        android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context
                .getSystemService(Context.CLIPBOARD_SERVICE);
        clipboard.setText(text);
    }

    /**
     * 获取粘贴文本
     * @param context
     */
    public static void getLatestText(Context context) {
        android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context
                .getSystemService(Context.CLIPBOARD_SERVICE);
        clipboard.getText();
    }

    /**
     * 手机号判断规则（一般情况由后台验证）
     * @param number
     * @return
     */
    public static boolean validateMobileNumber(String number) {
        if (null == number || number.equals("")) {
            return false;
        }
        Pattern p = Pattern
                .compile("^((13[0-9])|(14[0-9])|(15[0-9])|(17[0-9])|(18[0-9]))\\d{8}$");
        Matcher m = p.matcher(number);
        return m.matches();
    }

    /**
     * 邮箱验证规则
     * @param email
     * @return
     */
    public static boolean validateEmail(String email) {
        if (null == email || email.equals("") || email.length() > 50) {
            return false;
        }
        Pattern p = Pattern
                .compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
        Matcher m = p.matcher(email);
        return m.matches();
    }

    /**
     * 手机号掩码显示
     * @param cellPhone
     * @return 手机掩码 如18717713732 返回187****3732
     */
    public static String getShowupFormatedCellPhone(String cellPhone) {
        if (isEmpty(cellPhone)) {
            return "";
        }
//        if (!validateMobileNumber(cellPhone)) {
//            return "";
//        }
        if(cellPhone.length() != 11){//本地校验手机号位数
            return "";
        }
        return cellPhone.replace(cellPhone.substring(3, 7), "****");
    }

    /**
     * 邮箱掩码处理
     * @param name
     * @return
     */
    public static String getShowupFormatedUserName(String name) {
        if (isEmpty(name)) {
            return "";
        }
        if (!validateEmail(name)) {
            return "";
        }
        String str1 = name.substring(0, name.indexOf("@"));
        String str2 = name.substring(name.indexOf("@"), name.length());
        int len = str1.length();
        String str3 = str1.substring(0, 1);
        String str4 = str1.substring(len - 1, len);
        String str5 = "";
        if (len > 2) {
            for (int i = 0; i < len - 2; i++) {
                str5 += "*";
            }
        }
        return str3 + str5 + str4 + str2;
    }

    /**
     * 设置背景透明
     *
     * @param bgAlpha 透明度
     */
    public static void setBackgroundAlpha(Context context, float bgAlpha) {
        if (context == null || !(context instanceof Activity)) return;

        WindowManager.LayoutParams lp = ((Activity) context).getWindow().getAttributes();
        lp.alpha = bgAlpha;
        //为兼容华为手机
        if (bgAlpha == 1) {
            ((Activity) context).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
        } else {
            ((Activity) context).getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//解决在华为手机上半透明效果无效的bug
        }
        ((Activity) context).getWindow().setAttributes(lp);
    }

    /**
     * 添加中划线
     * @param view
     * @param text
     * @param start
     * @param end
     */
    public static void addStrikethrough(final TextView view, CharSequence text,
                                        final int start, final int end) {
        SpannableString sp = new SpannableString(text);
        sp.setSpan(new StrikethroughSpan(), start, end,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        view.setText(sp);
    }

    /**
     * 添加中划线
     * @param view
     * @param text
     */
    public static void addStrikethrough(final TextView view, CharSequence text) {
        SpannableString sp = new SpannableString(text);
        sp.setSpan(new StrikethroughSpan(), 0, text.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        view.setText(sp);
    }

    /**
     * 添加中划线
     * @param text
     * @return
     */
    public static String addStrikethrough(CharSequence text) {
        SpannableString sp = new SpannableString(text);
        sp.setSpan(new StrikethroughSpan(), 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sp.toString();
    }
}
