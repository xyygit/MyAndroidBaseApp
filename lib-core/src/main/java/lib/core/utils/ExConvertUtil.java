package lib.core.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.alibaba.fastjson.JSON;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExConvertUtil {

    private ExConvertUtil() {}

    private static class ExConvertUtilHolder {
        private static final ExConvertUtil ecu = new ExConvertUtil();
    }

    public static final ExConvertUtil getInstance() {
        return ExConvertUtilHolder.ecu;
    }

    /**
     * Method_流转换字符串 (默认字符集)
     *
     * @param in 输入流对象
     * @return 返回字符串对象
     */
    public final String getInStream2Str(InputStream in) {

        return InputStreamToStr(in, Charset.defaultCharset());
    }

    /**
     * Method_流转换字符串 (字符串处理字符集)
     *
     * @param in       输入流对象
     * @param encoding 编码
     * @return 返回字符串对象
     */
    public final String InputStreamToStr(InputStream in, String encoding) {

        return InputStreamToStr(in, Charset.forName(encoding));
    }

    /**
     * Method_流转换字符串 (处理字符集)
     *
     * @param in       输入流对象
     * @param encoding 编码
     * @return 返回字符串对象
     */
    public final String InputStreamToStr(InputStream in, Charset encoding) {

        InputStreamReader input = new InputStreamReader(in, encoding);
        StringWriter output = new StringWriter();
        String result = null;
        try {
            char[] buffer = new char[4096];
            int n = 0;
            while (-1 != (n = input.read(buffer))) {
                output.write(buffer, 0, n);
            }
            result = output.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (output != null) {
                output.flush();
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return result;
    }

    /**
     * Method_Bitmap 转换字节流
     *
     * @param bm 图片对象
     * @return 字节流
     */
    public final byte[] bitmapToBytes(Bitmap bm) {

        if (bm == null) {

            return null;
        }

        byte[] result = null;
        ByteArrayOutputStream baos = null;

        try {
            baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.PNG, 100, baos);

            result = baos.toByteArray();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    /**
     * Method_字节流转换 Bitmap
     *
     * @param b 字节流
     * @return 图片对象
     */
    public final Bitmap bytesToBitmap(byte[] b) {

        if (b.length == 0) {

            return null;
        }

        return BitmapFactory.decodeByteArray(b, 0, b.length);
    }

    /**
     * BitmapToDrawable
     */
    @SuppressWarnings("deprecation")
    public final Drawable bitmapToDrawable(Bitmap bitmap) {
        return new BitmapDrawable(bitmap);
    }

    /**
     * DrawableToBitmap
     */
    public final Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable == null) return null;
        return ((BitmapDrawable) drawable).getBitmap();
    }

    /**
     * Method_网页内容转换文本
     *
     * @param htmlStr 网页字符串
     * @return 返回字符串对象
     */
    public final String htmlToString(String htmlStr) {

        String result = "";

        Pattern p_script;
        Matcher m_script;
        Pattern p_style;
        Matcher m_style;
        Pattern p_html;
        Matcher m_html;

        String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>";
        String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>";
        String regEx_html = "<[^>]+>";

        try {
            p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
            m_script = p_script.matcher(htmlStr);
            htmlStr = m_script.replaceAll("");

            p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
            m_style = p_style.matcher(htmlStr);
            htmlStr = m_style.replaceAll("");

            p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
            m_html = p_html.matcher(regEx_html);
            htmlStr = m_html.replaceAll("");

            result = htmlStr;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * Method_对象转换 Byte
     *
     * @param obj 对象
     * @return 返回字节流
     */
    public final byte[] objToByte(Object obj) {

        byte[] bytes = null;
        ByteArrayOutputStream bos = null;
        ObjectOutputStream oos = null;

        try {
            bos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(bos);

            oos.writeObject(obj);

            oos.flush();

            bytes = bos.toByteArray();

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return bytes;
    }

    /**
     * Method_Byte 转换 对象
     *
     * @param bytes 字节流
     * @return 对象
     */
    public final Object byteToObject(byte[] bytes) {

        Object obj = null;
        ByteArrayInputStream bis = null;
        ObjectInputStream ois = null;

        try {
            bis = new ByteArrayInputStream(bytes);
            ois = new ObjectInputStream(bis);

            obj = ois.readObject();

        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } finally {
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return obj;
    }

    /**
     * Method_字符串转换 Int
     *
     * @param str      字符串内容
     * @param defValue 默认值
     * @return Int 类型
     */
    public final int getStringToInt(String str, int defValue) {

        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
        }

        return defValue;
    }

    /**
     * Method_字符串转换 long
     *
     * @param str      字符串内容
     * @param defValue 默认值
     * @return long 类型
     */
    public final long getStringToLong(String str, long defValue) {

        try {
            return Long.parseLong(str);
        } catch (Exception e) {
        }

        return defValue;
    }

    /**
     * Method_字符串转换 double
     *
     * @param str      字符串内容
     * @param defValue 默认值
     * @return double 类型
     */
    public final double getStringToDouble(String str, double defValue) {

        try {
            return Double.parseDouble(str);
        } catch (Exception e) {
        }

        return defValue;
    }

    /**
     * Method_字符串转换 float
     *
     * @param str      字符串内容
     * @param defValue 默认值
     * @return float 类型
     */
    public final float getStringToFloat(String str, float defValue) {

        try {
            return Float.parseFloat(str);
        } catch (Exception e) {
        }

        return defValue;
    }

    /**
     * Method_字符串转换 bool
     *
     * @param str      字符串内容
     * @param defValue 默认值
     * @return boolean 类型
     */
    public final boolean getStringToBool(String str, boolean defValue) {

        try {
            return Boolean.parseBoolean(str);
        } catch (Exception e) {
        }

        return defValue;
    }

    /**
     * Method_Dip 转换 Px
     *
     * @param context 上下文
     * @param dpValue dp 值
     * @return px 值
     */
    public final int dipToPx(Context context, float dpValue) {

        final float scale = context.getResources().getDisplayMetrics().density;

        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * Method_Px 转换 Dip
     *
     * @param context 上下文
     * @param pxValue px 值
     * @return dp 值
     */
    public final int pxToDip(Context context, float pxValue) {

        final float scale = context.getResources().getDisplayMetrics().density;

        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * Method_Px 转换 Sp
     *
     * @param context 上下文
     * @param pxValue px 值
     * @return sp 值
     */
    public final int pxToSp(Context context, float pxValue) {

        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;

        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * Method_Sp 转换 Px
     *
     * @param context 上下文
     * @param spValue sp 值
     * @return px 值
     */
    public final int spToPx(Context context, float spValue) {

        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;

        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * Method_将字符串转换为集合对象
     *
     * @param array 字符串
     * @param cls   指定对象
     * @return 集合对象
     */
    public final <T> List<T> getStringToList(String array, Class<T> cls) {

        List<T> list = new ArrayList<T>();

        try {
            JSONArray jsonArray = new JSONArray(array);

            for (int i = 0; i < jsonArray.length(); i++) {
                T t = strToObj(jsonArray.get(i).toString(), cls);
                list.add(t);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
    }

    /**
     * Method_字符串转换为对象
     *
     * @param bean 字符串
     * @param cls  指定对象
     * @return 对象
     */
    public final <T> T strToObj(String bean, Class<T> cls) {

        if (ExCommonUtil.isEmpty(bean)) {

            return null;
        }
        try {
            return JSON.parseObject(bean, cls);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            return cls.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Method_对象转换为字符串
     *
     * @param t 对象类型
     * @return 字符串
     */
    public final <T> String objToStr(T t) {
        return JSON.toJSONString(t);
    }

    /**
     * 文件转化为字节数组
     *
     * @Author Sean.guo
     * @EditTime 2007-8-13 上午11:45:28
     */
    public final byte[] getBytesFromFile(File f) {
        if (f == null) {
            return null;
        }
        try {
            FileInputStream stream = new FileInputStream(f);
            ByteArrayOutputStream out = new ByteArrayOutputStream(4096);
            byte[] b = new byte[4096];
            for (int n; (n = stream.read(b)) != -1; ) {
                out.write(b, 0, n);
            }
            stream.close();
            out.close();
            return out.toByteArray();
        } catch (IOException e) {
        }
        return null;
    }

    public final String fromDBCToSBC(String input) {
        // 半角转全角：
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 32) {
                c[i] = (char) 12288;
                continue;
            }
            if (c[i] < 127)
                c[i] = (char) (c[i] + 65248);
        }
        return new String(c);
    }

    /**
     * 全角转换为半角
     *
     * @param input
     * @return
     */
    public final String fromSBCToDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }

    public InputStream byteToInputStream(byte[] bytes) {
        return new ByteArrayInputStream(bytes);
    }
}
