package com.taozi.common.utils.file;

import com.taozi.common.config.TaoZiConfig;
import com.taozi.common.utils.DateUtils;
import com.taozi.common.utils.StringUtils;
import com.taozi.common.utils.uuid.UUID;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.poi.util.IOUtils;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;

/**
 * 图片处理工具类
 *
 * @author taozi
 */
public class ImageUtils {

    /**
     * 资源映射路径 前缀
     */
    public static final String RESOURCE_PREFIX = "/profile";

    /**
     * 根据图片路径获取byte数组
     * @param imagePath
     * @return byte[]
     */
    public static byte[] getImage(String imagePath) {
        InputStream is = getFile(imagePath);
        if (is == null) {
            return null;
        }
        try {
            return IOUtils.toByteArray(is);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            IOUtils.closeQuietly(is);
        }
    }

    /**
     * 根据图片路径获取输入流
     * @param imagePath
     * @return InputStream
     */
    public static InputStream getFile(String imagePath) {
        try {
            byte[] result = readFile(imagePath);
            if (result == null) {
                return null;
            }
            result = Arrays.copyOf(result, result.length);
            return new ByteArrayInputStream(result);
        } catch (Exception e) {
            e.printStackTrace();
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
        InputStream in;
        ByteArrayOutputStream baos = null;
        try {
            String urlStartsWithStr = "http";
            if (url.startsWith(urlStartsWithStr)) {
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
                String downloadPath = localPath + StringUtils.substringAfter(url, RESOURCE_PREFIX);
                in = new FileInputStream(downloadPath);
            }
            return IOUtils.toByteArray(in);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            IOUtils.closeQuietly(baos);
        }
    }

    /**
     * 根据指定大小压缩图片
     *
     * @param imageBytes 源图片字节数组
     * @param imageSize  指定图片大小，单位kb
     * @return 压缩质量后的图片字节数组
     */
    public static byte[] compressPicForScale(byte[] imageBytes, long imageSize) {
        // 1M 大小
        int aM = 1024;
        if (imageBytes == null || imageBytes.length <= 0 || imageBytes.length < imageSize * aM) {
            return imageBytes;
        }
        long srcSize = imageBytes.length;
        double accuracy = getAccuracy(srcSize / aM);
        try {
            while (imageBytes.length > imageSize * aM) {
                ByteArrayInputStream inputStream = new ByteArrayInputStream(imageBytes);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream(imageBytes.length);
                Thumbnails.of(inputStream)
                        .scale(accuracy)
                        .outputQuality(accuracy)
                        .toOutputStream(outputStream);
                imageBytes = outputStream.toByteArray();
            }
            System.out.println("【图片压缩】 | 图片原大小 == " + srcSize / 1024 + "kb | 压缩后大小 == " + imageBytes.length / 1024 + "kb");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imageBytes;
    }

    /**
     * 获取图标压缩质量比
     * 自动调节精度(经验数值)
     *
     * @param size 源图片大小
     * @return 图片压缩质量比
     */
    private static double getAccuracy(long size) {
        double accuracy;
        int size1 = 900;
        int size2 = 2047;
        int size3 = 3275;
        if (size < size1) {
            accuracy = 0.85;
        } else if (size < size2) {
            accuracy = 0.6;
        } else if (size < size3) {
            accuracy = 0.44;
        } else {
            accuracy = 0.4;
        }
        return accuracy;
    }

    /**
     * base64图片存入本地磁盘
     *
     * @param base64 base64字符串
     * @param savePath 预备保存到的位置
     * @return 本地图片绝对全路径
     */
    public static String base642Local(String base64, String savePath) {
        //项目根路径
        String projectPath = System.getProperty("user.dir");
        //拼接基础path
        String basePath = "/images/face/";
        //图片名
        String imgName = "";
        //本地真实路径
        String localImgReadPath = "";
        String imgReadPath = "";
        // 只允许jpg
        String header = "data:image/jpeg;base64,";
        if (base64.length() < header.length()) {
            return "";
        }
        // 去掉头部
        base64 = base64.substring(header.length());
        try {
            // 写入磁盘
            BASE64Decoder decoder = new BASE64Decoder();
            byte[] decodedBytes = decoder.decodeBuffer(base64);
            for (int i = 0; i < decodedBytes.length; ++i) {
                // 调整异常数据
                if (decodedBytes[i] < 0) {
                    decodedBytes[i] += 256;
                }
            }
            int max = 100, min = 1;
            int ran2 = (int) (Math.random() * (max - min) + min);
            imgName = System.currentTimeMillis() + ran2 + "";
            localImgReadPath = projectPath + basePath + imgName + ".jpg";
            System.out.println("localImgReadPath : " + localImgReadPath);
            File testImgReadPathIsExists = new File(projectPath + basePath);
            //文件路径不存在则创建
            if (!testImgReadPathIsExists.exists()) {
                testImgReadPathIsExists.mkdirs();
            }
            FileOutputStream out = new FileOutputStream(localImgReadPath);
            imgReadPath = "/static" + basePath + imgName + ".jpg";
            out.write(decodedBytes);

            out.close();
            return imgReadPath;
        } catch (Exception e) {
            e.printStackTrace();
        }
        //返回服务器本地绝对全路径
        return localImgReadPath;
    }

    /**
     * 编码该路径的文件成base64
     *
     * @param path 文件路径
     * @return base64字符串
     */
    public static String encodeBase64File(String path) {
        File file = new File(path);
        //文件不存在
        if (!file.exists()) {
            return null;
        }
        FileInputStream inputFile = null;
        byte[] buffer = new byte[(int) file.length()];
        try {
            inputFile = new FileInputStream(file);
            inputFile.read(buffer);
            inputFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Base64Utils.encode(StringUtils.byteToString(buffer));
    }

    /**
     * 将base64字符解码保存文件
     *
     * @param base64Code 待解码的base64字符串
     * @param targetPath 输出路径
     */
    public static void decodeBase64File(String base64Code, String targetPath) {
        byte[] buffer = Base64Utils.decode(base64Code).getBytes();
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(targetPath);
            out.write(buffer);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将base64字符不解码的情况下保存文本文件
     *
     * @param base64Code base64字符串
     * @param targetPath 输出路径
     * @throws Exception
     */
    public static void unDecodeBase64File(String base64Code, String targetPath) throws Exception {
        byte[] buffer = base64Code.getBytes();
        FileOutputStream out = new FileOutputStream(targetPath);
        out.write(buffer);
        out.close();
    }

    /**
     * 获取文件大小
     *
     * @param filePath 文件路径
     * @return 大小
     */
    public static long getFileSize(String filePath) {
        File file = new File(filePath);
        if (!file.exists() || !file.isFile()) {
            System.out.println(filePath + ": 文件不存在");
            return -1;
        }
        return file.length();
    }

    /**
     * 将本地图片转base64
     * 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
     * @param imgFile
     * @return
     */
    public static String getImageStr(String imgFile) {
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
        // 返回Base64编码过的字节数组字符串
        return encoder.encode(data);
    }

    /**
     * base64字符串转化成图片
     * @param imgStr
     * @param imgFilePath
     * @return boolean
     * @throws Exception
     */
    public static boolean generateImage(String imgStr, String imgFilePath) throws Exception {
        // 图像数据为空
        if (imgStr == null) {
            return false;
        }
        BASE64Decoder decoder = new BASE64Decoder();

        // Base64解码,对字节数组字符串进行Base64解码并生成图片
        byte[] b = decoder.decodeBuffer(imgStr);
        for (int i = 0; i < b.length; ++i) {
            // 调整异常数据
            if (b[i] < 0) {
                b[i] += 256;
            }
        }
        // 生成jpeg图片
        OutputStream out = new FileOutputStream(imgFilePath);
        out.write(b);
        out.flush();
        out.close();
        return true;
    }

    /**
     * multipartfile文件保存到本地
     *
     * @param file
     * @param localPath
     * @return 拼接url
     */
    public static String multipartFile2Local(MultipartFile file, String localPath) {
        //获取上传文件名,包含后缀
        String originalFilename = file.getOriginalFilename();
        //获取后缀
        String substring = originalFilename.substring(originalFilename.lastIndexOf("."));
        //保存的文件名
        String dFileName = UUID.randomUUID() + substring;
        //保存路径
        //springboot 默认情况下只能加载 resource文件夹下静态资源文件
        //确保文件路径存在
        File file1 = new File(localPath + "/" + DateUtils.getDate());
        if (!file1.exists()) {
            file1.mkdirs();
        }
        //生成保存文件
        File uploadFile = new File(localPath + "/" + DateUtils.getDate() + "/" + dFileName);
        System.out.println("完整上传路径为： " + uploadFile);
        //将上传文件保存到路径
        try {
            file.transferTo(uploadFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return DateUtils.getDate() + "/" + dFileName;
    }

    /**
     * base64 转图片
     * @param base64
     * @return String
     */
    public String base642Img(String base64) {

        String basePath = "";
        String path = "";
        String uploadUrlPath = "";
        String image = base64;
        String imgReadPath = "";

        // 只允许jpg
        String header = "data:image/jpeg;base64,";
        if (image.length() < header.length()) {
            return "";
        }
        // 去掉头部
        image = image.substring(header.length());
        // 写入磁盘
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            byte[] decodedBytes = decoder.decodeBuffer(image);
            for (int i = 0; i < decodedBytes.length; ++i) {
                // 调整异常数据
                if (decodedBytes[i] < 0) {
                    decodedBytes[i] += 256;
                }
            }
            int max = 100, min = 1;
            int ran2 = (int) (Math.random() * (max - min) + min);
            String imgName = System.currentTimeMillis() + ran2 + "";
            String xdFilePath = uploadUrlPath + imgName + ".jpg";
            String imgFilePath = path + xdFilePath;
            FileOutputStream out = new FileOutputStream(imgFilePath);
            imgReadPath = basePath + uploadUrlPath + imgName + ".jpg";
            out.write(decodedBytes);
            out.close();
            return xdFilePath;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/static" + imgReadPath;
    }
}
