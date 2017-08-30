package lib.core.utils;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Locale;

public class ExFileUtil {

    public DecimalFormatSymbols decimalFormatSymbols;

    private ExFileUtil() {
        decimalFormatSymbols = new DecimalFormatSymbols(Locale.CHINA);
    }

    private static class ExFileUtilHolder {
        private static final ExFileUtil efu = new ExFileUtil();
    }

    public static final ExFileUtil getInstance() {
        return ExFileUtilHolder.efu;
    }

    private static final int SIZETYPE_B = 1;// 获取文件大小单位为B的double值
    private static final int SIZETYPE_KB = 2;// 获取文件大小单位为KB的double值
    private static final int SIZETYPE_MB = 3;// 获取文件大小单位为MB的double值
    private static final int SIZETYPE_GB = 4;// 获取文件大小单位为GB的double值

    /**
     * Method_计算剩余空间
     *
     * @param path 路径
     * @return 结果
     */
    private final long getAvailableSize(String path) {

        StatFs fileStats = new StatFs(path);
        fileStats.restat(path);

        return (long) fileStats.getAvailableBlocks() * fileStats.getBlockSize(); // 注意与fileStats.getFreeBlocks()的区别
    }

    /**
     * Method_计算总空间
     *
     * @param path 路径
     * @return 结果
     */
    private final long getTotalSize(String path) {

        StatFs fileStats = new StatFs(path);
        fileStats.restat(path);

        return (long) fileStats.getBlockCount() * fileStats.getBlockSize();
    }

    /**
     * Method_计算SD卡的剩余空间
     *
     * @return 结果
     */
    public final long getSDAvailableSize() {

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

            return getAvailableSize(Environment.getExternalStorageDirectory().toString());
        }

        return 0;
    }

    /**
     * Method_计算系统的剩余空间
     *
     * @return 结果
     */
    public final long getSystemAvailableSize() {

        return getAvailableSize(ExAppUtil.getApplicationContext().getFilesDir().getAbsolutePath());
    }

    /**
     * Method_获取SD卡的总空间
     *
     * @return 结果
     */
    public final long getSDTotalSize() {

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

            return getTotalSize(Environment.getExternalStorageDirectory().toString());
        }

        return 0;
    }

    /**
     * Method_获取系统可读写的总空间
     *
     * @return 结果
     */
    public final long getSysTotalSize() {

        return getTotalSize(ExAppUtil.getApplicationContext().getFilesDir().getAbsolutePath());
    }

    /**
     * Method_将文件保存到本地
     *
     * @param fileData   字节流
     * @param folderPath 文件
     * @param fileName   文件名
     */
    public final boolean saveFile(byte[] fileData, String folderPath, String fileName) {

        File folder = new File(folderPath);
        folder.mkdirs();

        File file = new File(folderPath, fileName);
        ByteArrayInputStream is = new ByteArrayInputStream(fileData);

        OutputStream os = null;

        if (!file.exists()) {
            try {
                file.createNewFile();
                os = new FileOutputStream(file);
                byte[] buffer = new byte[1024];
                int len = 0;

                while (-1 != (len = is.read(buffer))) {
                    os.write(buffer, 0, len);
                }

                os.flush();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            } finally {
                closeIO(is, os);
            }
        }

        return true;
    }

    /**
     * Method_复制文件
     *
     * @param from 来源
     * @param to   目标
     */
    public final void copyFile(File from, File to) {

        if (null == from || !from.exists()) {

            return;
        }
        if (null == to) {

            return;
        }
        InputStream is = null;
        OutputStream os = null;

        try {
            is = new FileInputStream(from);

            if (!to.exists()) {
                to.createNewFile();
            }

            os = new FileOutputStream(to);

            byte[] buffer = new byte[1024];
            int len = 0;

            while (-1 != (len = is.read(buffer))) {
                os.write(buffer, 0, len);
            }

            os.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeIO(is, os);
        }
    }

    /**
     * Method_关闭流
     *
     * @param closeables 需要关闭的流对象
     */
    public final void closeIO(Closeable... closeables) {

        if (null == closeables || closeables.length <= 0) {

            return;
        }

        for (Closeable cb : closeables) {
            try {
                if (null == cb) {

                    continue;
                }

                cb.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Method_从文件中读取文本
     *
     * @param filePath 文件路径
     * @return 结果
     */
    public final String readFile(String filePath) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath), 8192);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append("\n").append(line);
            }
            bufferedReader.close();
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Method_从 assets 中读取文本
     *
     * @param name 名字
     * @return 结果
     */
    public final String readFileFromAssets(String name) {

        InputStream is = null;

        try {
            is = ExAppUtil.getApplicationContext().getResources().getAssets().open(name);
        } catch (Exception e) {
        }

        return ExConvertUtil.getInstance().getInStream2Str(is);
    }

    /**
     * Method_获取文件指定文件的指定单位的大小
     *
     * @param filePath 文件路径
     * @param sizeType 获取大小的类型1为B、2为KB、3为MB、4为GB
     * @return double值的大小
     */
    public final double getFileOrFilesSize(String filePath, int sizeType) {
        File file = new File(filePath);
        long blockSize = 0;
        try {
            if (file.isDirectory()) {
                blockSize = getFileSizes(file);
            } else {
                blockSize = getFileSize(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return formetFileSize(blockSize, sizeType);
    }

    /**
     * Method_调用此方法自动计算指定文件或指定文件夹的大小
     *
     * @param filePath 文件路径
     * @return 计算好的带B、KB、MB、GB的字符串
     */
    public final String getAutoFileOrFilesSize(String filePath) {
        File file = new File(filePath);
        long blockSize = 0;
        try {
            if (file.isDirectory()) {
                blockSize = getFileSizes(file);
            } else {
                blockSize = getFileSize(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return formetFileSize(blockSize);
    }

    /**
     * Method_获取指定文件大小
     *
     * @param file 文件对象
     * @return 大小
     * @throws Exception
     */
    public final long getFileSize(File file) {
        long size = 0;

        try {
            if (file.exists()) {
                FileInputStream fis = null;
                fis = new FileInputStream(file);
                size = fis.available();
                fis.close();
            } else {
                file.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return size;
    }

    /**
     * Method_获取指定文件夹
     *
     * @param f 文件对象
     * @return 大小
     * @throws Exception
     */
    public final long getFileSizes(File f) {
        long size = 0;
        try {
            File flist[] = f.listFiles();
            for (int i = 0; i < flist.length; i++) {
                if (flist[i].isDirectory()) {
                    size = size + getFileSizes(flist[i]);
                } else {
                    size = size + getFileSize(flist[i]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    /**
     * 转换文件大小
     *
     * @param fileS 文件大小
     * @return 转换后大小
     */
    public final String formetFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00", decimalFormatSymbols);
        String fileSizeString = "";
        String wrongSize = "0B";
        if (fileS == 0) {
            return wrongSize;
        }
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
    }

    /**
     * Method_转换文件大小,指定转换的类型
     *
     * @param fileS    文件大小
     * @param sizeType 指定类型
     * @return 按类型转换大小
     */
    public final double formetFileSize(long fileS, int sizeType) {
        DecimalFormat df = new DecimalFormat("#.00", decimalFormatSymbols);
        double fileSizeLong = 0;
        switch (sizeType) {
            case SIZETYPE_B:
                fileSizeLong = Double.valueOf(df.format((double) fileS));
                break;
            case SIZETYPE_KB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1024));
                break;
            case SIZETYPE_MB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1048576));
                break;
            case SIZETYPE_GB:
                fileSizeLong = Double.valueOf(df
                        .format((double) fileS / 1073741824));
                break;
            default:
                break;
        }
        return fileSizeLong;
    }


    /**
     * Method_文件删除
     *
     * @param file 文件对象
     */
    public final void delete(File file) {

        if (file.isFile()) {
            file.delete();
            return;
        }
        if (file.isDirectory()) {
            File[] childFiles = file.listFiles();
            if (childFiles == null || childFiles.length == 0) {
                file.delete();
                return;
            }
            for (int i = 0; i < childFiles.length; i++) {
                delete(childFiles[i]);
            }
            file.delete();
        }
    }

    /**
     * Method_文件夹内容删除
     *
     * @param file 文件对象
     */
    public final void deleteDir(File file) {

        if (file.isDirectory()) {
            File[] childFiles = file.listFiles();
            if (childFiles == null || childFiles.length == 0) {
                return;
            }
            for (int i = 0; i < childFiles.length; i++) {
                delete(childFiles[i]);
            }
        }
    }

    /**
     * Method_删除文件
     *
     * @param file 文件对象
     * @param time 时间
     */
    public final void delete(File file, long time) {

        if (file.isFile() && (System.currentTimeMillis() - file.lastModified()) > time) {
            file.delete();

            return;
        }

        if (file.isDirectory()) {
            File[] childFiles = file.listFiles();

            if (childFiles == null || childFiles.length == 0) {
                if ((System.currentTimeMillis() - file.lastModified()) > time) {
                    file.delete();
                }

                return;
            }

            for (int i = 0; i < childFiles.length; i++) {
                delete(childFiles[i], time);
            }

            if ((System.currentTimeMillis() - file.lastModified()) > time) {
                file.delete();
            }
        }
    }

    /**
     * 写SD卡或私有文件
     *
     * @param fileName
     * @param message
     */
    public final void writeFileSdcardOrData(String fileName, String message) {
        createDir(fileName);
        FileOutputStream fout = null;
        OutputStreamWriter out = null;
        try {
            fout = new FileOutputStream(fileName);
            out = new OutputStreamWriter(fout, "UTF-8");
            out.write(message);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fout != null) {
                try {
                    fout.flush();
                    fout.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(out != null) {
                try {
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 读SD卡或私有文件
     *
     * @param fileName
     * @return
     */
    public final String readFileSdcardOrData(String fileName) {
        if (!isFileExist(fileName)) return null;
        String res = null;
        FileInputStream fin = null;
        try {
            fin = new FileInputStream(fileName);
            int length = fin.available();
            byte[] buffer = new byte[length];
            fin.read(buffer);
            res = new String(buffer, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fin != null) {
                try {
                    fin.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return res;
    }

    /**
     * 写私有目录文件
     *
     * @param fileName
     * @param message
     */
    public final void writeFileData(String fileName, String message) {
        FileOutputStream fout = null;
        try {
            fout = ExAppUtil.getApplicationContext().openFileOutput(fileName, Context.MODE_PRIVATE);
            fout.write(message.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fout != null) {
                try {
                    fout.flush();
                    fout.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 读私有目录文件文件
     *
     * @param fileName
     * @return
     */
    public final String readFileData(String fileName) {
        if (!isFileExist(ExAppUtil.getApplicationContext().getFilesDir() + "/" + fileName))
            return null;
        String res = null;
        FileInputStream fin = null;
        try {
            fin = ExAppUtil.getApplicationContext().openFileInput(fileName);
            int length = fin.available();
            byte[] buffer = new byte[length];
            fin.read(buffer);
            res = new String(buffer, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fin != null) {
                try {
                    fin.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return res;
    }

    /**
     * 判断sd卡的存在
     *
     * @return
     */
    public final boolean existSDcard(String file) {
        File check = new File(file);
        boolean hasMake;
        try {
            hasMake = check.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            hasMake = false;
        }
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) && hasMake;
    }

    /**
     * 判断文件存在
     *
     * @param fileName
     * @return
     */
    public final boolean isFileExist(String fileName) {
        File file = new File(fileName);
        boolean flag = file.exists();
        if (!flag) {
            File path1 = new File(fileName.substring(0, fileName.lastIndexOf("/")));
            path1.mkdirs();
        }
        return flag;
    }

    /**
     * 创建文件目录
     *
     * @param dirName
     */
    public final void createDir(String dirName) {
        File path1 = new File(dirName.substring(0, dirName.lastIndexOf("/")));
        if (!path1.exists()) {
            path1.mkdirs();
        }
    }

    /**
     * 清理缓存
     *
     * @param cachePath
     * @param storeTime 7*24*60*60*1000
     */
    public final void removeCache(String cachePath, long storeTime) {
        File dir = new File(cachePath);
        File[] files = dir.listFiles();
        if (files == null) {
            return;
        }
        Arrays.sort(files, new FileLastModifSort());
        int length = files.length;
        long currentTime = System.currentTimeMillis();
        for (int i = 0; i < length; i++) {
            File file = files[i];
            if (currentTime - file.lastModified() > storeTime)
                file.delete();
        }
    }

    /**
     * 根据文件的最后修改时间进行排序 *
     */
    class FileLastModifSort implements Comparator<File> {
        @Override
        public int compare(File arg0, File arg1) {
            if (arg0.lastModified() > arg1.lastModified()) {
                return 1;
            } else if (arg0.lastModified() == arg1.lastModified()) {
                return 0;
            } else {
                return -1;
            }
        }
    }

}
