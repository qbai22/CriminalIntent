package qbai22.com.criminalintent;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;

public class PictureUtils {
    public static Bitmap getScaledBitmap(String path, int desWidth,
                                         int desHeight) {
        //Чтение размеров изображения на диске
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        float srcWidth = options.outWidth;
        float srcHeight = options.outHeight;

        //Вычисление степени масштабирования
        //Степень вычисляется отношением меньшего направления, для того чтобы
        //Коэф. скалирования не превзошел одно из направлений
        int inSampleSize = 1;
        if (srcHeight > desHeight || srcWidth > desWidth) {
            if (srcWidth > srcHeight) {
                inSampleSize = Math.round(srcHeight / desHeight);
            } else {
                inSampleSize = Math.round(srcWidth / desWidth);
            }
        }

        options = new BitmapFactory.Options();
        options.inSampleSize = inSampleSize;

        //Чтение данных и создание итогового изображения
        return BitmapFactory.decodeFile(path, options);

    }
    //Метод для масштабирования битмапа под размер конкретой активности
    public static Bitmap getScaledBitmap(String path, Activity activity){
        Point size = new Point();
        activity.getWindowManager().getDefaultDisplay()
                .getSize(size);
        return getScaledBitmap(path, size.x, size.y);
    }
}
