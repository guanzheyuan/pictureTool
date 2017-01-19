package com.zxhy.pic.util;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Map.Entry;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import com.sun.image.codec.jpeg.ImageFormatException;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageDecoder;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import java.util.Map;
import java.util.List;
/**
 * 图片工具类
 * @author Arthur
 *
 */
public class pictureUtil{
	
	//标记文字
	private static final String PRESS_TEST=".....";
	//输出文件类型
	private static final String PICTRUE_FORMATE_JPG = ".jpg";
	//输出文件类型
	private static final String PICTRUE_FORMATE_JPEG = ".jpeg";
	public static String vaildCode;
	
 
	/**
	 * 输出浏览器下载
	 */
	@SuppressWarnings("resource")
	public  static  String downLoadFile(HttpServletRequest request,HttpServletResponse response,String filePath){
		 //将文件读入文件流
		try {
			InputStream inStream = new FileInputStream(filePath);
			//获得浏览器代理信息
			final String userAgent = request.getHeader("USER-AGENT");
			//判断浏览器代理并分别设置响应给浏览器的编码格式
			String finalFileName = null;
			filePath = filePath.substring(filePath.lastIndexOf("/")+1, filePath.length());
			if("MSIE".equals(userAgent)||"Trident".equals(userAgent)){//IE浏览器
                finalFileName = URLEncoder.encode(filePath,"UTF8");
            }else if("Mozilla".equals(userAgent)){//google,火狐浏览器
                finalFileName = new String(filePath.getBytes(), "ISO8859-1");
            }else{
                finalFileName = URLEncoder.encode(filePath,"UTF8");//其他浏览器
            }
			response.reset();//重置 响应头
			response.setContentType("application/x-download");//告知浏览器下载文件，而不是直接打开，浏览器默认为打开
			response.addHeader("Content-Disposition" ,"attachment;filename=\"" +finalFileName+ "\"");//下载文件的名称
			OutputStream os = response.getOutputStream();
			// 循环取出流中的数据
	        byte[] b = new byte[1024];
	        int len;
            while ((len = inStream.read(b)) > 0){
            		os.write(b, 0, len);
            }
            os.flush();
            os.close();
            response.flushBuffer();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}
		 return null;
	}
	/**
	 * 图片水印
	 * @param picFile
	 * @param waterFile
	 */
	public static String  pressImage(String fileFile,String waterFile,String outPath){
		int width = 0;
		int height = 0;
		Image src;
		String shareFileName = "";
		try {
			File _file = new File(fileFile);
			src = ImageIO.read(_file);
			width = src.getWidth(null);
	         height = src.getHeight(null);
	         BufferedImage bufferedimage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	         Graphics g = bufferedimage.createGraphics();
	         g.drawImage(src, 0, 0, width, height, null);
	         //水印文件
	         	File pressFile = new File(waterFile);
	            Image src_biao = ImageIO.read(pressFile);
	            int wideth_biao = src_biao.getWidth(null);
	            int height_biao = src_biao.getHeight(null);
	            g.drawImage(src_biao, (width - wideth_biao) / 2,
	            (height - height_biao) / 2, wideth_biao, height_biao, null);
	          //水印文件结束
	            g.dispose();
	            //输出参数地址
	            shareFileName = outPath+"/"+ System.currentTimeMillis() + PICTRUE_FORMATE_JPG;  
	            FileOutputStream out = new FileOutputStream(shareFileName);
	            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
	            encoder.encode(bufferedimage);
	            out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return shareFileName;
	}
	/**
	 * 文字水印
	 */
	public static String pressText(String filePath,String outPath){
         InputStream is= null;
         BufferedImage buffImg = null;
         JPEGImageDecoder jpegDecoder = null;
         String shareFileName = "";
			try {
					   is = new FileInputStream(filePath);
			           //通过JPEG图象流创建JPEG数据流解码器  
					   jpegDecoder  = JPEGCodec.createJPEGDecoder(is);  
			           //解码当前JPEG数据流，返回BufferedImage对象  
			           buffImg =  jpegDecoder.decodeAsBufferedImage();
			           //得到画笔对象  
			           Graphics2D g = buffImg.createGraphics();
			           //最后一个参数用来设置字体的大小  
			           g.setColor(new  Color(89,87,87));  
			           g.setFont(new Font("宋体",Font.BOLD,54));  
			           g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.0f));
			           //在图片范围内获取到像素点
			            List<Map<Integer, Integer>> listInt =  pictureUtil.getCoord(buffImg,100);
			            for (Map<Integer, Integer> map : listInt) {
			           	 for (Entry<Integer, Integer> vo : map.entrySet()) {
			    				  g.drawString(PRESS_TEST,vo.getKey(),vo.getValue());  
							}
						}
			            //执行关闭
			            g.dispose();  
			            //输出参数地址
			            shareFileName = outPath+"/"+System.currentTimeMillis() + PICTRUE_FORMATE_JPEG; 
			            	FileOutputStream out = new FileOutputStream(shareFileName);
			            //创键编码器，用于编码内存中的图象数据。            
			           JPEGImageEncoder en = JPEGCodec.createJPEGEncoder(out);  
					   en.encode(buffImg);
					   out.flush();
					   out.close();  
				       is.close();
			} catch (ImageFormatException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}  
			return  shareFileName;
	}
	
	/**
	 * 复制照片
	 * @param request
	 */
	@SuppressWarnings("deprecation")
	public static String copyPhoto(MultipartFile multipartFile,HttpServletRequest request){
		String fileName = multipartFile.getOriginalFilename();
		String filePath = "";
		if (!"".equals(fileName)) {
			filePath = request.getRealPath("/upload")+"/"+System.currentTimeMillis()+fileName.substring(fileName.lastIndexOf("."), fileName.length());
		}
		File file= new File(filePath);
		try {
			multipartFile.transferTo(file);
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}  
		return  filePath;
	}
	/**
	 * 得到图片坐标（随机）
	 * @return
	 */
	public static List<Map<Integer, Integer>> getCoord(BufferedImage  bi,int rank){
		 int x = 0;
         int y = 0; 
         //得到图片的像素坐标
		int  va= bi.getWidth();  
	     int cs = bi.getHeight();  
	     Random rand = new Random();
	     List<Map<Integer,Integer>> listInt = new ArrayList<>();
	     Map<Integer, Integer> _map = null;
	     //通过等级进行随机分配坐标
         for (int i = 0;i<rank;i++) {
        	 	x = rand.nextInt(va);
        	 	y = rand.nextInt(cs);
        	    _map = new HashMap<>();
        	    _map.put(x,y);
        	    listInt.add( _map);
		}
        return listInt;
	}
	
	/** 
     * 生成图片指纹 
     * @param filename 文件名 
     * @return 图片指纹 
     */  
    public static String produceFingerPrint(String filename) {  
    	BufferedImage source =readPNGImage(filename);// 读取文件 
        int width = 8;  
        int height = 8;  
        // 第一步，缩小尺寸。  
        // 将图片缩小到8x8的尺寸，总共64个像素。这一步的作用是去除图片的细节，只保留结构、明暗等基本信息，摒弃不同尺寸、比例带来的图片差异。  
        BufferedImage thumb = thumb(source, width, height, false);  
        // 第二步，简化色彩。  
        // 将缩小后的图片，转为64级灰度。也就是说，所有像素点总共只有64种颜色。  
        int[] pixels = new int[width * height];  
        for (int i = 0; i < width; i++) {  
            for (int j = 0; j < height; j++) {  
                pixels[i * height + j] = rgbToGray(thumb.getRGB(i, j));  
            }  
        }  
        // 第三步，计算平均值。  
        // 计算所有64个像素的灰度平均值。  
        int avgPixel = average(pixels);  
        // 第四步，比较像素的灰度。  
        // 将每个像素的灰度，与平均值进行比较。大于或等于平均值，记为1；小于平均值，记为0。  
        int[] comps = new int[width * height];  
        for (int i = 0; i < comps.length; i++) {  
            if (pixels[i] >= avgPixel) {  
                comps[i] = 1;  
            } else {  
                comps[i] = 0;  
            }  
        }  
        // 第五步，计算哈希值。  
        // 将上一步的比较结果，组合在一起，就构成了一个64位的整数，这就是这张图片的指纹。组合的次序并不重要，只要保证所有图片都采用同样次序就行了。  
        StringBuffer hashCode = new StringBuffer();  
        for (int i = 0; i < comps.length; i+= 4) {  
            int result = comps[i] * (int) Math.pow(2, 3) + comps[i + 1] * (int) Math.pow(2, 2) + comps[i + 2] * (int) Math.pow(2, 1) + comps[i + 3];  
            hashCode.append(binaryToHex(result));  
        }  
        // 得到指纹以后，就可以对比不同的图片，看看64位中有多少位是不一样的。  
        return hashCode.toString();  
    }  
	 /** 
     * 灰度值计算 
     * @param pixels 像素 
     * @return int 灰度值 
     */  
    public static int rgbToGray(int pixels) {  
        // int _alpha = (pixels >> 24) & 0xFF;  
        int _red = (pixels >> 16) & 0xFF;  
        int _green = (pixels >> 8) & 0xFF;  
        int _blue = (pixels) & 0xFF;  
        return (int) (0.3 * _red + 0.59 * _green + 0.11 * _blue);  
    }  
      
    /** 
     * 计算数组的平均值 
     * @param pixels 数组 
     * @return int 平均值 
     */  
    public static int average(int[] pixels) {  
        float m = 0;  
        for (int i = 0; i < pixels.length; ++i) {  
            m += pixels[i];  
        }  
        m = m / pixels.length;  
        return (int) m;  
    }
    /** 
     * 二进制转为十六进制 
     * @param int binary 
     * @return char hex 
     */  
    private static char binaryToHex(int binary) {  
        char ch = ' ';  
        switch (binary){  
        case 0:  
            ch = '0';  
            break;  
        case 1:  
            ch = '1';  
            break;  
        case 2:  
            ch = '2';  
            break;  
        case 3:  
            ch = '3';  
            break;  
        case 4:  
            ch = '4';  
            break;  
        case 5:  
            ch = '5';  
            break;  
        case 6:  
            ch = '6';  
            break;  
        case 7:  
            ch = '7';  
            break;  
        case 8:  
            ch = '8';  
            break;  
        case 9:  
            ch = '9';  
            break;  
        case 10:  
            ch = 'a';  
            break;  
        case 11:  
            ch = 'b';  
            break;  
        case 12:  
            ch = 'c';  
            break;  
        case 13:  
            ch = 'd';  
            break;  
        case 14:  
            ch = 'e';  
            break;  
        case 15:  
            ch = 'f';  
            break;  
        default:  
            ch = ' ';  
        }  
        return ch;  
}
    /** 
     * 生成缩略图 <br/> 
     * 保存:ImageIO.write(BufferedImage, imgType[jpg/png/...], File); 
     *  
     * @param source 
     *            原图片 
     * @param width 
     *            缩略图宽 
     * @param height 
     *            缩略图高 
     * @param b 
     *            是否等比缩放 
     * */  
    public static BufferedImage thumb(BufferedImage source, int width,int height, boolean b) {  
        // targetW，targetH分别表示目标长和宽  
        int type = source.getType();  
        BufferedImage target = null;  
        double sx = (double) width / source.getWidth();  
        double sy = (double) height / source.getHeight();  
        if (b) {  
            if (sx > sy) {  
                sx = sy;  
                width = (int) (sx * source.getWidth());  
            } else {  
                sy = sx;  
                height = (int) (sy * source.getHeight());  
            }  
        }  
        if (type == BufferedImage.TYPE_CUSTOM) {
            ColorModel cm = source.getColorModel();  
            WritableRaster raster = cm.createCompatibleWritableRaster(width,height);  
            boolean alphaPremultiplied = cm.isAlphaPremultiplied();  
            target = new BufferedImage(cm, raster, alphaPremultiplied, null);  
        } else  
            target = new BufferedImage(width, height, type);  
        		Graphics2D g = target.createGraphics();  
        		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);  
	        g.drawRenderedImage(source, AffineTransform.getScaleInstance(sx, sy));  
	        g.dispose();  
	        return target;  
    }  
    /** 
     * 读取JPEG图片 
     * @param filename 文件名 
     * @return BufferedImage 图片对象 
     */  
    public static BufferedImage readPNGImage(String filename){  
        try {  
            File inputFile = new File(filename);    
            BufferedImage sourceImage = ImageIO.read(inputFile);  
            return sourceImage;  
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
        } catch (ImageFormatException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        return null;  
    }  
	/**
	   * 获取单个文件的MD5值！
	   * @param file
	   * @return
	   */
	  public static String getFileMD5(File file){
		  //判断是否存在文件
//	    if (!file.isFile()){
//	      return null;
//	    }
	    //创建摘要算法对象
	    MessageDigest digest = null;
	    FileInputStream in=null;
	    byte buffer[] = new byte[1024];
	    int len;
	    try {
	      digest = MessageDigest.getInstance("MD5");
	      in = new FileInputStream(file);
	      while ((len = in.read(buffer, 0, 1024)) != -1) {
	        digest.update(buffer, 0, len);
	      }
	      in.close();
	    } catch (Exception e) {
	      e.printStackTrace();
	      return null;
	    }
	    BigInteger bigInt = new BigInteger(1, digest.digest());
	    return bigInt.toString(16);
	  }
	  
	  /**
	   * multipartFile 转型File
	   * @param multipartFile
	   * @return
	   */
	  public static File  transferTo(MultipartFile multipartFile){
		  CommonsMultipartFile cf= (CommonsMultipartFile)multipartFile; 
	       DiskFileItem fi = (DiskFileItem)cf.getFileItem(); 
	       return  fi.getStoreLocation();
	  }
	  /**
	   * 获取文件名称
	   * @param contentDisposition
	   * @return
	   */
	  public static String getFileName(String contentDisposition){
	        String[] strArr1 = contentDisposition.split(";");
	        if(strArr1.length < 3){
	            return "";
	        }else{
	            String[] strArr2 = strArr1[2].split("=");
	            String fileName = strArr2[1];
	            fileName = fileName.replaceAll("\"", "");//把双引号去除掉
	            return fileName;
	         }
	    } 
	  
	  /**
	   * 判断运行版本
	   * @return
	   */
	    public static int isBlankSystem(){
	    		int versions = 0;
	    		String sysName = System.getProperties().getProperty("os.name");
	    		if(sysName.toLowerCase().startsWith("win")){  
	    			  System.out.println(sysName + " can't gunzip");  
	    			  versions = 1;
	    		}else if(sysName.toLowerCase().startsWith("Mac OS X")){
	    			versions = 2;
	    		} 
	    		return versions;
	    }
}
