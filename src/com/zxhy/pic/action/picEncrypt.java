package com.zxhy.pic.action;
import java.io.File;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.zxhy.pic.util.pictureUtil;
/**
 * 图片加密
 * @author Arthur
 *
 */
@Controller
public class picEncrypt {

	
	/**
	 * 跳转到图片工具页面
	 */
	@RequestMapping(value = "/toPicTool",method=RequestMethod.GET)
	public ModelAndView toPicManager(HttpServletRequest request,HttpServletResponse response){
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("/picManager");
		return  modelAndView;  
	}
	 
	/**
	 * 执行图片上传
	 * @return
	 */
	@RequestMapping(value = "/doUpload",method=RequestMethod.POST)
	public ModelAndView doUploadPicture(HttpServletRequest request,HttpServletResponse response){
		ModelAndView modelAndView = new ModelAndView();
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile multipartFile = multipartRequest.getFile("firstFile");
		if (null == multipartFile) {
			modelAndView.addObject("msg","上传图片为空!");
		}else{
			File file = pictureUtil.transferTo(multipartFile);
			String filePath = file== null ?"":file.getPath();
			@SuppressWarnings("deprecation")
			String outPath = pictureUtil.pressText(filePath,request.getRealPath("/upload"));
		    pictureUtil.vaildCode = pictureUtil.getFileMD5(new File(outPath));
			pictureUtil.downLoadFile(request,response, outPath);
		     modelAndView.addObject("msg","图片加密成功!");
		}
		modelAndView.setViewName("/picManager");
		return modelAndView;
	}
	
	/**
	 * 上传图片水印
	 * @return
	 */
	@SuppressWarnings("deprecation")
	@RequestMapping(value = "/doUploadWatermark",method=RequestMethod.POST)
	public ModelAndView doUploadWatermarkPic(HttpServletRequest request,HttpServletResponse response){
		ModelAndView modelAndView = new ModelAndView();
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile logoMultFile = multipartRequest.getFile("logoFile");
		MultipartFile picMultFile = multipartRequest.getFile("picFile");
		modelAndView.setViewName("/picManager");
		if (null == logoMultFile) {
			modelAndView.addObject("msg","上传图片为空!");
			return modelAndView;
		}
		if (null == picMultFile) {
			modelAndView.addObject("msg","上传图片为空!");
			return modelAndView;
		}
		File picFile = pictureUtil.transferTo(picMultFile);
		File waterFile = pictureUtil.transferTo(logoMultFile);
		String outPath = pictureUtil.pressImage(picFile.getPath(), waterFile.getPath(),request.getRealPath("/upload"));
		pictureUtil.downLoadFile(request, response, outPath);
		 modelAndView.addObject("msg","增加水印成功!");
		return modelAndView;
	}
	
	/**
	 * 验证图片是否为系统生成
	 * @return
	 */
	@RequestMapping(value = "/doVerifyPic",method=RequestMethod.POST)
	public ModelAndView verifyPicture(HttpServletRequest request,HttpServletResponse response){
		ModelAndView modelAndView = new ModelAndView();
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile multipartFile = multipartRequest.getFile("firstFile3");
		if (null == multipartFile) {
			modelAndView.addObject("msg","上传图片为空!");
			return modelAndView;
		}
		File file = pictureUtil.transferTo(multipartFile);
		String fileCode = pictureUtil.getFileMD5(file);
		if (null != pictureUtil.vaildCode) {
			if (pictureUtil.vaildCode.equals(fileCode)) {
				modelAndView.addObject("msg","验证成功：图片是系统生成");
			}else{
				modelAndView.addObject("msg","验证失败：图片不是系统生成");
			}
		}else {
			modelAndView.addObject("msg","验证失败：目前系统从未生成过图片");
		}
		modelAndView.setViewName("/picManager");
		return modelAndView;
	}
}
