package com.taozi.common.utils.file;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;

import com.taozi.common.utils.DateUtils;
import com.taozi.common.utils.log.TaoZiLog;
import com.taozi.common.utils.uuid.UUID;
import org.apache.poi.util.IOUtils;
import com.taozi.common.config.TaoZiConfig;
import com.taozi.common.constant.Constants;
import com.taozi.common.utils.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import net.coobird.thumbnailator.Thumbnails;

/**
 * 图片处理工具类
 *
 * @author taozi
 */
public class ImageUtils {

    public static byte[] getImage(String imagePath) {
        InputStream is = getFile(imagePath);
        try {
            return IOUtils.toByteArray(is);
        } catch (Exception e) {
            TaoZiLog.error("图片加载异常 {}" + e);
            return null;
        } finally {
            IOUtils.closeQuietly(is);
        }
    }

    public static InputStream getFile(String imagePath) {
        try {
            byte[] result = readFile(imagePath);
            result = Arrays.copyOf(result, result.length);
            return new ByteArrayInputStream(result);
        } catch (Exception e) {
            TaoZiLog.error("获取图片异常 {}" + e);
        }
        return null;
    }

    /**
     * 读取文件为字节数据
     *
     * @param url 地址
     * @return 字节数据
     */
    public static byte[] readFile(String url) {
        InputStream in = null;
        ByteArrayOutputStream baos = null;
        try {
            if (url.startsWith("http")) {
                // 网络地址
                URL urlObj = new URL(url);
                URLConnection urlConnection = urlObj.openConnection();
                urlConnection.setConnectTimeout(30 * 1000);
                urlConnection.setReadTimeout(60 * 1000);
                urlConnection.setDoInput(true);
                in = urlConnection.getInputStream();
            } else {
                // 本机地址
                String localPath = TaoZiConfig.getProfile();
                String downloadPath = localPath + StringUtils.substringAfter(url, Constants.RESOURCE_PREFIX);
                in = new FileInputStream(downloadPath);
            }
            return IOUtils.toByteArray(in);
        } catch (Exception e) {
            TaoZiLog.error("获取文件路径异常 {}" + e);
            return null;
        } finally {
            IOUtils.closeQuietly(baos);
        }
    }

    /**
     * 根据指定大小压缩图片
     *
     * @param imageBytes  源图片字节数组
     * @param imageSize 指定图片大小，单位kb
     * @return 压缩质量后的图片字节数组
     */
    public static byte[] compressPicForScale(byte[] imageBytes, long imageSize) {
        if (imageBytes == null || imageBytes.length <= 0 || imageBytes.length < imageSize * 1024) {
            return imageBytes;
        }
        long srcSize = imageBytes.length;
        double accuracy = getAccuracy(srcSize / 1024);
        try {
            while (imageBytes.length > imageSize * 1024) {
                ByteArrayInputStream inputStream = new ByteArrayInputStream(imageBytes);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream(imageBytes.length);
                Thumbnails.of(inputStream)
                        .scale(accuracy)
                        .outputQuality(accuracy)
                        .toOutputStream(outputStream);
                imageBytes = outputStream.toByteArray();
            }
            TaoZiLog.info("【图片压缩】 | 图片原大小 == " + srcSize / 1024 + "kb | 压缩后大小 == " + imageBytes.length / 1024 + "kb");
        } catch (Exception e) {
            TaoZiLog.error("【图片压缩】msg == 图片压缩失败!" + e);
        }
        return imageBytes;
    }

    /**
     * 获取图标压缩质量比
     * 自动调节精度(经验数值)
     *
     * @param size  源图片大小
     * @return      图片压缩质量比
     */
    private static double getAccuracy(long size) {
        double accuracy;
        if (size < 900) {
            accuracy = 0.85;
        } else if (size < 2047) {
            accuracy = 0.6;
        } else if (size < 3275) {
            accuracy = 0.44;
        } else {
            accuracy = 0.4;
        }
        return accuracy;
    }

    /**
     * base64图片存入本地磁盘
     *
     * @param base64Img base64字符串
     * @return 本地图片绝对全路径
     */
    public static String saveBase64ImgToLocal(String base64Img){

        String projectPath = System.getProperty("user.dir");//项目根路径
        //     projectPath = projectPath+"\\src\\main\\resources\\static" ;
        System.out.println("projectPath-------->"+projectPath);
        String basePath = "/images/face/";//拼接基础path
        String imgName = "";//图片名
        String localImgReadPath = "" ;//本地真实路径
        String imgReadPath = "" ;

        // 只允许jpg
        String header = "data:image/jpeg;base64,";
        if(base64Img.length() < header.length()){
            return "" ;
        }

        // 去掉头部
        //      base64Img = base64Img.substring(header.length());
        // 写入磁盘
        try {
            BASE64Decoder decoder = new BASE64Decoder();
            byte[] decodedBytes = decoder.decodeBuffer(base64Img);
            for (int i = 0; i < decodedBytes.length; ++i) {
                if (decodedBytes[i] < 0) {// 调整异常数据
                    decodedBytes[i] += 256;
                }
            }
            int max=100, min=1;
            int ran2 = (int) (Math.random() * (max - min) + min);
            imgName = System.currentTimeMillis() + ran2 + "";
            localImgReadPath = projectPath + basePath + imgName + ".jpg";
            System.out.println("localImgReadPath--->"+localImgReadPath);
            File testImgReadPathIsExists = new File(projectPath + basePath);
            if (!testImgReadPathIsExists.exists()) {//文件路径不存在则创建
                testImgReadPathIsExists.mkdirs();
            }
            FileOutputStream out = new FileOutputStream(localImgReadPath);
            imgReadPath = "/static"+basePath  + imgName + ".jpg";
            out.write(decodedBytes);

            out.close();
            return imgReadPath;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return localImgReadPath;//返回服务器本地绝对全路径
    }

    /**
     * 编码该路径的文件成base64
     * @param path 文件路径
     * @return base64字符串
     * @throws Exception
     */
    public static String encodeBase64File(String path) throws Exception {
        File file = new File(path);
        if (!file.exists()) {//文件不存在
            return null;
        }
        FileInputStream inputFile = new FileInputStream(file);
        byte[] buffer = new byte[(int)file.length()];
        inputFile.read(buffer);
        inputFile.close();
        return Base64Utils.encode(StringUtils.byteToString(buffer));
    }

    /**
     * 将base64字符解码保存文件
     * @param base64Code 待解码的base64字符串
     * @param targetPath 输出路径
     * @throws Exception
     */
    public static void decodeBase64File(String base64Code,String targetPath) throws Exception {
        byte[] buffer = Base64Utils.decode(base64Code).getBytes();
        FileOutputStream out = new FileOutputStream(targetPath);
        out.write(buffer);
        out.close();
    }

    /**
     * 将base64字符不解码的情况下保存文本文件
     * @param base64Code base64字符串
     * @param targetPath 输出路径
     * @throws Exception
     */
    public static void unDecodeBase64File(String base64Code,String targetPath) throws Exception {
        byte[] buffer = base64Code.getBytes();
        FileOutputStream out = new FileOutputStream(targetPath);
        out.write(buffer);
        out.close();
    }

    /**
     * 获取文件大小
     * @param filePath 文件路径
     * @return 大小
     */
    public static long getFileSize(String filePath) {
        File file = new File(filePath);
        if (!file.exists() || !file.isFile()) {
            TaoZiLog.error(filePath + ": 文件不存在");
            return -1;
        }
        return file.length();
    }

    public String base64ImgToImg(String base64Img){

        String basePath = "";
        String path = "";
        String uploadUrlPath = "";
        String image = base64Img;
        String imgReadPath = "" ;

        // 只允许jpg
        String header = "data:image/jpeg;base64,";
        if(image.length() < header.length()){
            return "" ;
        }
        // 去掉头部
        image = image.substring(header.length());
        // 写入磁盘
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            byte[] decodedBytes = decoder.decodeBuffer(image);
            for (int i = 0; i < decodedBytes.length; ++i) {
                if (decodedBytes[i] < 0) {// 调整异常数据
                    decodedBytes[i] += 256;
                }
            }
            int max=100,min=1;
            int ran2 = (int) (Math.random()*(max-min)+min);
            String imgName = System.currentTimeMillis()+ran2+ "";
            String xdFilePath = uploadUrlPath + imgName + ".jpg";
            String imgFilePath = path + xdFilePath ;
            FileOutputStream out = new FileOutputStream(imgFilePath);
            imgReadPath = basePath + uploadUrlPath + imgName + ".jpg";
            out.write(decodedBytes);
            out.close();
            return xdFilePath ;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/static"+imgReadPath;
    }

    //将本地图片转base64
    public static String GetImageStr(String imgFile) {// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        InputStream in = null;
        byte[] data = null;
        // 读取图片字节数组
        try {
            in = new FileInputStream(imgFile);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();

        return encoder.encode(data);// 返回Base64编码过的字节数组字符串
    }


    // base64字符串转化成图片
    public static boolean GenerateImage(String imgStr, String imgFilePath) throws Exception {
        if (imgStr == null) // 图像数据为空
            return false;
        BASE64Decoder decoder = new BASE64Decoder();

        // Base64解码,对字节数组字符串进行Base64解码并生成图片
        byte[] b = decoder.decodeBuffer(imgStr);
        for (int i = 0; i < b.length; ++i) {
            if (b[i] < 0) {// 调整异常数据
                b[i] += 256;
            }
        }
        // 生成jpeg图片
        // String imgFilePath = "c://temp_kjbl_001_ab_010.jpg";//新生成的图片
        OutputStream out = new FileOutputStream(imgFilePath);
        out.write(b);
        out.flush();
        out.close();
        return true;
    }


    /**
     * multipartfile文件保存到本地
     * @param file
     * @param localPath
     * @return 拼接url
     */
    public static String multipartfileToLocal(MultipartFile file, String localPath) {
        int flag = 0;
        //获取上传文件名,包含后缀
        String originalFilename = file.getOriginalFilename();
        //获取后缀
        String substring = originalFilename.substring(originalFilename.lastIndexOf("."));
        //保存的文件名
        String dFileName = UUID.randomUUID()+substring;
        //保存路径
        //springboot 默认情况下只能加载 resource文件夹下静态资源文件
        //确保文件路径存在
        File file1 = new File(localPath+ "/" + DateUtils.getDate());
        if (!file1.exists()) {
            file1.mkdirs();
        }
        //生成保存文件
        File uploadFile = new File(localPath + "/" + DateUtils.getDate() + "/" + dFileName);
        TaoZiLog.info("完整上传路径为： " + uploadFile);
        //将上传文件保存到路径
        try {
            file.transferTo(uploadFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return DateUtils.getDate() + "/" + dFileName;
    }

    public static void main(String[] args) throws IOException {
        /*byte[] bytes = FileUtils.readFileToByteArray(new File("D:\\pic\\1.jpg"));
        long l = System.currentTimeMillis();
        bytes = PicUtils.compressPicForScale(bytes, 300, "x");// 图片小于300kb
        logger.info(System.currentTimeMillis() - l + "");
        FileUtils.writeByteArrayToFile(new File("D:\\pic\\dd1.jpg"), bytes);*/
        String base64Str = "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 /9k=";
        try {
            System.out.println(ImageUtils.GenerateImage(base64Str.replaceAll(" ", "\r\n"),"d://aa.jpg"));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
