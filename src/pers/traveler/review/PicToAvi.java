package pers.traveler.review;

/**
 * Created by quqing on 16/5/17.
 */

import pers.traveler.review.core.*;
import pers.traveler.review.utils.MovieUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

public class PicToAvi {

    /**
     * 将图片转换成视频
     *
     * @param picDir      图片文件夹绝对路径
     * @param suffix      图片后缀
     * @param aviFileName 生成的avi视频文件名
     * @param fps         每秒帧数
     * @param mWidth      视频的宽度
     * @param mHeight     视频的高度
     * @throws Exception
     */
    public static void convertPicToAvi(String picDir, final String suffix, String aviFileName, int fps, int mWidth, int mHeight) throws MovieSaveException, Exception {
        final File[] pics = new File(picDir).listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith("." + suffix);
            }
        });

        if (pics == null || pics.length == 0) {
            return;
        }

        Arrays.sort(pics, new Comparator<File>() {
            public int compare(File file1, File file2) {
                String numberName1 = file1.getName().replace("." + suffix, "");
                String numberName2 = file2.getName().replace("." + suffix, "");
                return new Integer(numberName1) - new Integer(numberName2);
            }
        });

        DefaultMovieInfoProvider dmip = new DefaultMovieInfoProvider(aviFileName);
        dmip.setFPS(fps > 0 ? fps : 1);
        dmip.setNumberOfFrames(pics.length);
        dmip.setMWidth(mWidth > 0 ? mWidth : 1440);
        dmip.setMHeight(mHeight > 0 ? mHeight : 860);

        new Jim2Mov(new ImageProvider() {
            public byte[] getImage(int frame) {
                try {
                    BufferedImage bufferedImage = ImageIO.read(pics[frame]);
                    if (null != bufferedImage)
                        return MovieUtils.convertImageToJPEG(bufferedImage, 1.0f);
                } catch (IOException e) {
                    System.out.println(e);
                }
                return null;
            }
        }, dmip, null).saveMovie(MovieInfoProvider.TYPE_AVI_MJPEG);
    }
}