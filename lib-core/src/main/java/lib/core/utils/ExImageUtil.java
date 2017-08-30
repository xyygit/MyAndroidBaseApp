package lib.core.utils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Random;

/**
 * 图片处理
 */
public class ExImageUtil {

	private ExImageUtil() {}

	private static class ExImageUtilHolder {
		private static final ExImageUtil xiu = new ExImageUtil();
	}

	public static final ExImageUtil getInstance() {
		return ExImageUtilHolder.xiu;
	}

	/**
	 * 图片旋转
	 *
	 * @param bmp
	 *            要旋转的图片
	 * @param degree
	 *            图片旋转的角度，负值为逆时针旋转，正值为顺时针旋转
	 * @return
	 */
	public final Bitmap rotateBitmap(Bitmap bmp, float degree) {
		Matrix matrix = new Matrix();
		matrix.postRotate(degree);
		return Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
	}

	/**
	 * 图片缩放
	 *
	 * @param bm
	 * @param scale
	 *            值小于1则为缩小，否则为放大
	 * @return
	 */
	public final Bitmap resizeBitmap(Bitmap bm, float scale) {
		Matrix matrix = new Matrix();
		matrix.postScale(scale, scale);
		return Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
	}

	/**
	 * 图片缩放
	 *
	 * @param bm
	 * @param w
	 *            缩小或放大成的宽
	 * @param h
	 *            缩小或放大成的高
	 * @return
	 */
	public final Bitmap resizeBitmap(Bitmap bm, int w, int h) {
		Bitmap BitmapOrg = bm;

		int width = BitmapOrg.getWidth();
		int height = BitmapOrg.getHeight();

		float scaleWidth = ((float) w) / width;
		float scaleHeight = ((float) h) / height;

		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		return Bitmap.createBitmap(BitmapOrg, 0, 0, width, height, matrix, true);
	}

	/**
	 * 图片反转
	 *
	 * @param bmp
	 * @param flag
	 *            0为水平反转，1为垂直反转
	 * @return
	 */
	public final Bitmap reverseBitmap(Bitmap bmp, int flag) {
		float[] floats = null;
		switch (flag) {
			case 0: // 水平反转
				floats = new float[] { -1f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 1f };
				break;
			case 1: // 垂直反转
				floats = new float[] { 1f, 0f, 0f, 0f, -1f, 0f, 0f, 0f, 1f };
				break;
		}

		if (floats != null) {
			Matrix matrix = new Matrix();
			matrix.setValues(floats);
			return Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
		}

		return null;
	}

	/**
	 * 剪切图片(居中裁剪)
	 * @param bm 被剪切的图片
	 * @param dstWidth 剪切后的宽度
	 * @param dstHeight 剪切后的高度
	 * @return
	 */

	public final Bitmap cropCenterBitmap(Bitmap bm,int dstWidth,int dstHeight){

		int startWidth = (bm.getWidth() - dstWidth)/2;
		int startHeight = ((bm.getHeight() - dstHeight) / 2);
		Rect src = new Rect(startWidth, startHeight, startWidth + dstWidth, startHeight + dstHeight);
		return dividePart(bm, src);
	}

	/**
	 * 剪切图片
	 * @param bmp 被剪切的图片
	 * @param src 剪切的位置
	 * @return 剪切后的图片
	 */
	private final Bitmap dividePart(Bitmap bmp, Rect src)
	{
		int width = src.width();
		int height = src.height();
		Rect des = new Rect(0, 0, width, height);
		Bitmap croppedImage = Bitmap.createBitmap(width, height, Config.RGB_565);
		Canvas canvas = new Canvas(croppedImage);
		canvas.drawBitmap(bmp, src, des, null);
		return croppedImage;
	}


	/**
	 * 倒影效果
	 * @param bitmap
	 * @return
	 */
	public final Bitmap createReflectionImageWithOrigin(Bitmap bitmap) {
		final int reflectionGap = 4;
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();

		Matrix matrix = new Matrix();
		matrix.preScale(1, -1);

		Bitmap reflectionImage = Bitmap.createBitmap(bitmap, 0, h / 2, w,
				h / 2, matrix, false);

		Bitmap bitmapWithReflection = Bitmap.createBitmap(w, (h + h / 2),
				Config.ARGB_8888);

		Canvas canvas = new Canvas(bitmapWithReflection);
		canvas.drawBitmap(bitmap, 0, 0, null);
		Paint deafalutPaint = new Paint();
		canvas.drawRect(0, h, w, h + reflectionGap, deafalutPaint);

		canvas.drawBitmap(reflectionImage, 0, h + reflectionGap, null);

		Paint paint = new Paint();
		LinearGradient shader = new LinearGradient(0, bitmap.getHeight(), 0,
				bitmapWithReflection.getHeight() + reflectionGap, 0x70ffffff,
				0x00ffffff, TileMode.CLAMP);
		paint.setShader(shader);
		// Set the Transfer mode to be porter duff and destination in
		paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
		// Draw a rectangle using the paint with our linear gradient
		canvas.drawRect(0, h, w, bitmapWithReflection.getHeight()
				+ reflectionGap, paint);

		return bitmapWithReflection;
	}
	/**
	 * Bitmap缩放
	 * @param bitmap
	 * @param width
	 * @param height
	 * @return
	 */
	public final Bitmap zoomBitmap(Bitmap bitmap, int width, int height) {
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		Matrix matrix = new Matrix();
		float scaleWidth = ((float) width / w);
		float scaleHeight = ((float) height / h);
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
		return newbmp;
	}
	/**
	 * drawable to Bitmap
	 * @param drawable
	 * @return
	 */
	public final Bitmap layerDrawableToBitmap(Drawable drawable) {
		// 取 drawable 的长宽
		int w = drawable.getIntrinsicWidth();
		int h = drawable.getIntrinsicHeight();

		// 取 drawable 的颜色格式
		Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Config.ARGB_8888
				: Config.RGB_565;
		// 建立对应 bitmap
		Bitmap bitmap = Bitmap.createBitmap(w, h, config);
		// 建立对应 bitmap 的画布
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, w, h);
		// 把 drawable 内容画到画布中
		drawable.draw(canvas);
		return bitmap;
	}
	/**
	 * 圆角
	 * @param bitmap
	 * @param roundPx
	 * @return
	 */
	public final Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		Bitmap output = Bitmap.createBitmap(w, h, Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, w, h);
		final RectF rectF = new RectF(rect);
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}

	/**
	 * Drawable缩放
	 * @param drawable
	 * @param w
	 * @param h
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public final Drawable zoomDrawable(Drawable drawable, int w, int h) {
		int width = drawable.getIntrinsicWidth();
		int height = drawable.getIntrinsicHeight();
		// drawable转换成bitmap
		Bitmap oldbmp = ExConvertUtil.getInstance().drawableToBitmap(drawable);
		// 创建操作图片用的Matrix对象
		Matrix matrix = new Matrix();
		// 计算缩放比例
		float sx = ((float) w / width);
		float sy = ((float) h / height);
		// 设置缩放比例
		matrix.postScale(sx, sy);
		// 建立新的bitmap，其内容是对原bitmap的缩放后的图
		Bitmap newbmp = Bitmap.createBitmap(oldbmp, 0, 0, width, height,
				matrix, true);
		return new BitmapDrawable(newbmp);
	}

	/**
	 * 纵向平铺
	 * @param height
	 * @param drawable
	 * @return
	 */
	public final Drawable createRepeatY(int height,Drawable drawable){
		Bitmap	src = ExConvertUtil.getInstance().drawableToBitmap(drawable);
		if(height<=0){
			height=1;
		}
		int count = height /src.getHeight();
		Bitmap bitmap = Bitmap.createBitmap(src.getWidth(), height,Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		for(int y = 0; y <= count; y++){
			canvas.drawBitmap(src, 0, y * src.getHeight(), null);
		}
		return ExConvertUtil.getInstance().bitmapToDrawable(bitmap);
	}

	/**
	 * 横向平铺
	 * @param width
	 * @param drawable
	 * @return
	 */
	public final Drawable createRepeatX(int width,Drawable drawable){
		Bitmap	src = ExConvertUtil.getInstance().drawableToBitmap(drawable);
		int count = width /src.getWidth();
		Bitmap bitmap = Bitmap.createBitmap(width, src.getWidth(),Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		for(int x = 0; x <= count; x++){
			canvas.drawBitmap(src, x*src.getWidth(), 0, null);
		}
		return ExConvertUtil.getInstance().bitmapToDrawable(bitmap);
	}

	// 截取图片
	public final void cropImage(Activity activity, Uri uri, int outputX,
								 int outputY, int requestCode) {
		// 裁剪图片意图
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		// 裁剪框的比例，1：1
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// 裁剪后输出图片的尺寸大小
		intent.putExtra("outputX", outputX);
		intent.putExtra("outputY", outputY);
		// 图片格式
		intent.putExtra("outputFormat", "JPEG");
		intent.putExtra("noFaceDetection", true);
		intent.putExtra("return-data", true);
		activity.startActivityForResult(intent, requestCode);
	}

	/**
	 * Method_灰白处理 就是利用了ColorMatrix 类自带的设置饱和度的方法setSaturation()。
	 * 不过其方法内部实现的更深一层是利用颜色矩阵的乘法实现的，对于颜色矩阵的乘法下面还有使用
	 *
	 * @param bmpOriginal 图片对象
	 * @return 图片对象
	 */
	public final Bitmap getBitmap2Grayscale(Bitmap bmpOriginal) {

		int height = bmpOriginal.getHeight();
		int width = bmpOriginal.getWidth();

		Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Config.RGB_565);
		Canvas c = new Canvas(bmpGrayscale);
		Paint paint = new Paint();

		ColorMatrix cm = new ColorMatrix();
		cm.setSaturation(0);

		ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);

		paint.setColorFilter(f);
		c.drawBitmap(bmpOriginal, 0, 0, paint);

		return bmpGrayscale;
	}

	/**
	 * Method_黑白处理 这张图片不同于灰白处理的那张，不同之处是灰白处理虽然没有了颜色，
	 * 但是黑白的程度层次依然存在，而此张图片连层次都没有了，只有两个区别十分明显的黑白 颜色。
	 * 实现的算法也很简单，对于每个像素的rgb值求平均数，如果高于100算白色，低于100算黑色。 不过感觉100这个标准值太大了，导致图片白色区
	 * 域太多，把它降低点可能效果会更好
	 *
	 * @param mBitmap 图片对象
	 * @return 图片对象
	 */
	public final Bitmap getBitmap2BlackAndwhite(Bitmap mBitmap) {

		int mBitmapWidth = 0;
		int mBitmapHeight = 0;

		mBitmapWidth = mBitmap.getWidth();
		mBitmapHeight = mBitmap.getHeight();
		Bitmap bmpReturn = Bitmap.createBitmap(mBitmapWidth, mBitmapHeight, Config.ARGB_8888);

		int iPixel = 0;
		for (int i = 0; i < mBitmapWidth; i++) {
			for (int j = 0; j < mBitmapHeight; j++) {
				int curr_color = mBitmap.getPixel(i, j);

				int avg = (Color.red(curr_color) + Color.green(curr_color) + Color.blue(curr_color)) / 3;
				if (avg >= 100) {
					iPixel = 255;
				} else {
					iPixel = 0;
				}
				int modif_color = Color.argb(255, iPixel, iPixel, iPixel);

				bmpReturn.setPixel(i, j, modif_color);
			}
		}

		return bmpReturn;
	}

	/**
	 * Method_镜像处理 原理就是将原图片反转一下，调整一 下它的颜色作出倒影效果，再将两张图片续加在一起，
	 * 不过如果在反转的同时再利用Matrix加上一些倾斜角度就更好了，不过那样做的话加工后的图片的高度需要同比例计算出来，
	 * 不能简单的相加了，否则就图片大小就容不下现有的像素内容。
	 *
	 * @param bitmap 图片对象
	 * @return 图片对象
	 */
	public final Bitmap getBitmap2ReflectionImageWithOrigin(Bitmap bitmap) {

		final int reflectionGap = 4;
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();

		Matrix matrix = new Matrix();
		matrix.preScale(1, -1);

		Bitmap reflectionImage = Bitmap.createBitmap(bitmap, 0, height / 2, width, height / 2, matrix, false);
		Bitmap bitmapWithReflection = Bitmap.createBitmap(width, (height + height / 2), Config.ARGB_8888);

		Canvas canvas = new Canvas(bitmapWithReflection);
		canvas.drawBitmap(bitmap, 0, 0, null);
		Paint deafalutPaint = new Paint();
		canvas.drawRect(0, height, width, height + reflectionGap, deafalutPaint);

		canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);

		Paint paint = new Paint();
		LinearGradient shader = new LinearGradient(0, bitmap.getHeight(), 0, bitmapWithReflection.getHeight() + reflectionGap, 0x70ffffff, 0x00ffffff, TileMode.CLAMP);
		paint.setShader(shader);
		// Set the Transfer mode to be porter duff and destination in
		paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
		// Draw a rectangle using the paint with our linear gradient
		canvas.drawRect(0, height, width, bitmapWithReflection.getHeight() + reflectionGap, paint);

		return bitmapWithReflection;
	}

	/**
	 * Method_加旧处理
	 *
	 * @param bitmap 图片对象
	 * @return 图片对象
	 */
	public final Bitmap getBitmap2OldBitmap(Bitmap bitmap) {

		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.RGB_565);

		Canvas canvas = new Canvas(output);

		Paint paint = new Paint();
		ColorMatrix cm = new ColorMatrix();
		float[] array = {1, 0, 0, 0, 50, 0, 1, 0, 0, 50, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0};
		cm.set(array);
		paint.setColorFilter(new ColorMatrixColorFilter(cm));

		canvas.drawBitmap(bitmap, 0, 0, paint);

		return output;
	}

	/**
	 * Method_浮雕处理 观察浮雕就不难发现，其实浮雕的特点就是在颜色有跳变的地方就刻条痕迹。127，127,127为深灰色，
	 * 近似于石头的颜色，此处取该颜色为底色。算法是将上一个点的rgba值减去当前点的rgba值然后加上127得到当前点的颜色。
	 *
	 * @param mBitmap 图片对象
	 * @return 图片对象
	 */
	public final Bitmap getBitmap2Embossment(Bitmap mBitmap) {

		int mBitmapWidth = 0;
		int mBitmapHeight = 0;

		mBitmapWidth = mBitmap.getWidth();
		mBitmapHeight = mBitmap.getHeight();

		Bitmap bmpReturn = Bitmap.createBitmap(mBitmapWidth, mBitmapHeight, Config.RGB_565);
		int preColor = 0;
		int prepreColor = 0;

		preColor = mBitmap.getPixel(0, 0);

		for (int i = 0; i < mBitmapWidth; i++) {
			for (int j = 0; j < mBitmapHeight; j++) {
				int curr_color = mBitmap.getPixel(i, j);
				int r = Color.red(curr_color) - Color.red(prepreColor) + 127;
				int g = Color.green(curr_color) - Color.red(prepreColor) + 127;
				int b = Color.green(curr_color) - Color.blue(prepreColor) + 127;
				int a = Color.alpha(curr_color);
				int modif_color = Color.argb(a, r, g, b);
				bmpReturn.setPixel(i, j, modif_color);
				prepreColor = preColor;
				preColor = curr_color;
			}
		}

		Canvas c = new Canvas(bmpReturn);
		Paint paint = new Paint();
		ColorMatrix cm = new ColorMatrix();
		cm.setSaturation(0);
		ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
		paint.setColorFilter(f);
		c.drawBitmap(bmpReturn, 0, 0, paint);

		return bmpReturn;
	}

	/**
	 * Method_油画处理 其实油画因为是用画笔画的，彩笔画的时候没有那么精确会将本该这点的颜色滑到另一个点处。
	 * 算法实现就是取一个一定范围内的随机数，每个点的颜色是该点减去随机数坐标后所得坐标的颜色。
	 *
	 * @param bmpSource 图片对象
	 * @return 图片对象
	 */
	public final Bitmap getBitmap2OilPainting(Bitmap bmpSource) {

		Bitmap bmpReturn = Bitmap.createBitmap(bmpSource.getWidth(), bmpSource.getHeight(), Config.RGB_565);
		int color = 0;
		int Radio = 0;
		int width = bmpSource.getWidth();
		int height = bmpSource.getHeight();

		Random rnd = new Random();
		int iModel = 10;
		int i = width - iModel;
		while (i > 1) {
			int j = height - iModel;
			while (j > 1) {
				int iPos = rnd.nextInt(100000) % iModel;
				color = bmpSource.getPixel(i + iPos, j + iPos);
				bmpReturn.setPixel(i, j, color);
				j = j - 1;
			}
			i = i - 1;
		}

		return bmpReturn;
	}

	/**
	 * Method_模糊处理 算法实现其实是取每三点的平均值做为当前点颜色，这样看上去就变得模糊了。
	 * 这个算法是三点的平均值，如果能够将范围扩大，并且不是单纯的平均值， 而是加权
	 * 平均肯定效果会更好。不过处理速度实在是太慢了，而Muzei这种软件在处理的时候 ，不仅仅速度特别快，而且还有逐渐变模糊的变化过程，显然人家不是用这
	 * 种算法实现的。 他们的实现方法正在猜测中，实现后也来更新。
	 *
	 * @param bmpSource 图片对象
	 * @param Blur      模糊程度
	 * @return 图片对象
	 */
	public final Bitmap getBitmap2Blur(Bitmap bmpSource, int Blur) {

		int mode = 5;

		Bitmap bmpReturn = Bitmap.createBitmap(bmpSource.getWidth(), bmpSource.getHeight(), Config.ARGB_8888);
		int pixels[] = new int[bmpSource.getWidth() * bmpSource.getHeight()];
		int pixelsRawSource[] = new int[bmpSource.getWidth() * bmpSource.getHeight() * 3];
		int pixelsRawNew[] = new int[bmpSource.getWidth() * bmpSource.getHeight() * 3];

		bmpSource.getPixels(pixels, 0, bmpSource.getWidth(), 0, 0, bmpSource.getWidth(), bmpSource.getHeight());

		for (int k = 1; k <= Blur; k++) {

			for (int i = 0; i < pixels.length; i++) {
				pixelsRawSource[i * 3 + 0] = Color.red(pixels[i]);
				pixelsRawSource[i * 3 + 1] = Color.green(pixels[i]);
				pixelsRawSource[i * 3 + 2] = Color.blue(pixels[i]);
			}

			int CurrentPixel = bmpSource.getWidth() * 3 + 3;

			for (int i = 0; i < bmpSource.getHeight() - 3; i++) {
				for (int j = 0; j < bmpSource.getWidth() * 3; j++) {
					CurrentPixel += 1;
					int sumColor = 0;
					sumColor = pixelsRawSource[CurrentPixel - bmpSource.getWidth() * 3];
					sumColor = sumColor + pixelsRawSource[CurrentPixel - 3];
					sumColor = sumColor + pixelsRawSource[CurrentPixel + 3];
					sumColor = sumColor + pixelsRawSource[CurrentPixel + bmpSource.getWidth() * 3];
					pixelsRawNew[CurrentPixel] = Math.round(sumColor / 4);
				}
			}

			for (int i = 0; i < pixels.length; i++) {
				pixels[i] = Color.rgb(pixelsRawNew[i * 3 + 0], pixelsRawNew[i * 3 + 1], pixelsRawNew[i * 3 + 2]);
			}
		}

		bmpReturn.setPixels(pixels, 0, bmpSource.getWidth(), 0, 0, bmpSource.getWidth(), bmpSource.getHeight());

		return bmpReturn;
	}

	/**
	 * Method_图片合并
	 *
	 * @param bitmap1 图片对象
	 * @param bitmap2 图片对象
	 * @param path    保存路径
	 * @return 图片对象
	 * @throws FileNotFoundException
	 */
	public final Bitmap getBitmap2Join(Bitmap bitmap1, Bitmap bitmap2, String path) throws FileNotFoundException {

		Bitmap bitmap3 = Bitmap.createBitmap(bitmap1.getWidth(), bitmap1.getHeight(), bitmap1.getConfig());
		Canvas canvas = new Canvas(bitmap3);
		canvas.drawBitmap(bitmap1, new Matrix(), null);
		canvas.drawBitmap(bitmap2, 120, 350, null); // 120、350为bitmap2写入点的x、y坐标

		// 将合并后的bitmap3保存为png图片到本地
		FileOutputStream out = new FileOutputStream(path + "/image3.png");
		bitmap3.compress(Bitmap.CompressFormat.PNG, 90, out);

		return bitmap3;
	}

	/**
	 * Method_文字保存为png图片
	 *
	 * @param path 文件保存路径
	 * @param data 保存数据
	 * @throws FileNotFoundException
	 */
	public final void getText2Image(String path, ArrayList<String> data) throws FileNotFoundException {

		int height = data.size() * 20; // 图片高
		Bitmap bitmap = Bitmap.createBitmap(270, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		canvas.drawColor(Color.WHITE); // 背景颜色

		Paint p = new Paint();
		p.setColor(Color.BLACK); // 画笔颜色
		p.setTextSize(15); // 画笔粗细

		for (int i = 0; i < data.size(); i++) {
			canvas.drawText(data.get(i), 20, (i + 1) * 20, p);
		}

		FileOutputStream out = new FileOutputStream(path);
		bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
	}

}
