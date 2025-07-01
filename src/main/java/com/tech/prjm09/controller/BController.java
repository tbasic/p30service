package com.tech.prjm09.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.tags.shaded.org.apache.bcel.classfile.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.tech.command.BCommand;
import com.tech.command.BContentCommand;
import com.tech.command.BDeleteCommand;
import com.tech.command.BListCommand;
import com.tech.command.BModifyCommand;
import com.tech.command.BModifyViewCommand;
import com.tech.command.BReplyCommand;
import com.tech.command.BReplyViewCommand;
import com.tech.command.BWriteCommand;
import com.tech.prjm09.dao.IDao;
import com.tech.prjm09.dto.BDto;
import com.tech.prjm09.dto.ReBrdimgDto;
import com.tech.prjm09.service.BContentViewService;
import com.tech.prjm09.service.BListService;
import com.tech.prjm09.service.BServiceInter;
import com.tech.prjm09.util.SearchVO;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import oracle.jdbc.proxy.annotation.Post;

@Controller
public class BController {
	//dev
	BCommand command;
	
	BServiceInter bServiceInter;
	
	@Autowired
	private IDao iDao;
	
	
	@RequestMapping("/")
	public String index() {
		return "index";
	}
	
	@RequestMapping("/list")
	public String list(HttpServletRequest request,
			SearchVO searchVO, Model model) {
		System.out.println("list() ctr");
//		command=new BListCommand();
//		command.execute(model);

		model.addAttribute("request",request);
		model.addAttribute("searchVO",searchVO);
		bServiceInter=new BListService(iDao);
		bServiceInter.execute(model);
		
		return "list";
	}

	@RequestMapping("/write_view")
	public String write_view(Model model) {
		System.out.println("write_view() ctr");
		
		return "write_view";
	}
//	@RequestMapping("/write")
//	public String write(HttpServletRequest request,
//			Model model) {
//		System.out.println("write() ctr");
//
//		String bname=request.getParameter("bname");
//		String btitle=request.getParameter("btitle");
//		String bcontent=request.getParameter("bcontent");
//
//		
//		iDao.write(bname, btitle, bcontent);
//		return "redirect:list";
//	}
	
	@RequestMapping("/write")
	public String write(MultipartHttpServletRequest mtfRequest,
			Model model) {
		System.out.println("write() ctr");

		String bname=mtfRequest.getParameter("bname");
		String btitle=mtfRequest.getParameter("btitle");
		String bcontent=mtfRequest.getParameter("bcontent");

		System.out.println("title : "+btitle);
		iDao.write(bname, btitle, bcontent);
		
		String workPath=System.getProperty("user.dir");

//		String root="C:\\hsts2025\\sts25_work\\prjm29replyboard_mpsupdown_multi\\"
//				+ "src\\main\\resources\\static\\files";
		String root=workPath+"\\src\\main\\resources\\static\\files";
		
		
		List<MultipartFile> fileList=mtfRequest.getFiles("file");
		
		int bid=iDao.selBid();
		System.out.println("bid>>>"+bid);
		
		for (MultipartFile mf : fileList) {
			String originalFile=mf.getOriginalFilename();
			System.out.println("orinal files : "+originalFile);
			long longtime=System.currentTimeMillis();
			String changeFile=longtime+"_"+originalFile;
			System.out.println("change files : "+changeFile);
			
			String pathfile=root+"\\"+changeFile;
			try {
				if (!originalFile.equals("")) {
					mf.transferTo(new File(pathfile));
					System.out.println("upload success~~");
					//db에 기록
					iDao.imgwrite(bid,originalFile,changeFile);
					System.out.println("rebrdimgtb write sucess");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
		return "redirect:list";
	}
	
	@RequestMapping("/download")
	public String download(HttpServletRequest request,
			HttpServletResponse response,
			Model model) throws Exception {
		
		String fname=request.getParameter("f");
		String bid=request.getParameter("bid");
		System.out.println(fname+":"+bid);
		
//		첨부파일이다
		response.setHeader("Content-Disposition",
				"Attachment;filename="+URLEncoder.encode(fname,"utf-8"));
		String workPath=System.getProperty("user.dir");
		String realPath=workPath+"\\src\\main\\resources\\static\\files\\"+fname;
		
		FileInputStream fin=new FileInputStream(realPath);
		ServletOutputStream sout=response.getOutputStream();
		
		byte[] buf=new byte[1024];
		int size=0;
		while ((size=fin.read(buf,0,1024))!=-1) {
			sout.write(buf,0,size);
		}
		fin.close();
		sout.close();
	
		return "content_view?bid="+bid;
	}
	
	@RequestMapping("/content_view")
	public String content_view(HttpServletRequest request,
			Model model) {
		System.out.println("content_view() ctr");

		model.addAttribute("request",request);
		bServiceInter=new BContentViewService(iDao);
		bServiceInter.execute(model);
		
		return "content_view";
	}
	@RequestMapping("/modify_view")
	public String modify_view(HttpServletRequest request,
			Model model) {
		System.out.println("modify_view() ctr");
		
//		command=new BModifyViewCommand();
//		command.execute(model);
		String bid=request.getParameter("bid");
		BDto dto=iDao.modifyView(bid);
		model.addAttribute("content_view",dto);
		
		return "modify_view";
	}
	@PostMapping("/modify")
	public String modify(HttpServletRequest request,
			Model model) {
		System.out.println("modify() ctr");
//		model.addAttribute("request",request);
//		command=new BModifyCommand();
//		command.execute(model);
		
		String bid=request.getParameter("bid");
		String bname=request.getParameter("bname");
		String btitle=request.getParameter("btitle");
		String bcontent=request.getParameter("bcontent");
		iDao.modify(bid, bname, btitle, bcontent);
		return "redirect:list";
	}
	@RequestMapping("/reply_view")
	public String reply_view(HttpServletRequest request,
			Model model) {
		System.out.println("reply_view() ctr");
		
//		model.addAttribute("request",request);
//		command=new BReplyViewCommand();
//		command.execute(model);
		String bid=request.getParameter("bid");
		BDto dto=iDao.reply_view(bid);
		
		model.addAttribute("reply_view",dto);
		return "reply_view";
	}
	@PostMapping("/reply")
	public String reply(HttpServletRequest request,
			Model model) {
		System.out.println("reply() ctr");
//		model.addAttribute("request",request);
//		command=new BReplyCommand();
//		command.execute(model);
		String bid=request.getParameter("bid");
		String bname=request.getParameter("bname");
		String btitle=request.getParameter("btitle");
		String bcontent=request.getParameter("bcontent");
		
		String bgroup=request.getParameter("bgroup");
		String bstep=request.getParameter("bstep");
		String bindent=request.getParameter("bindent");
		iDao.replyShape(bgroup, bstep);
		iDao.reply(bid, bname, btitle,
				bcontent, bgroup, bstep,
				bindent);
		
		return "redirect:list";
	}
	
	@RequestMapping("/delete")
	public String delete(HttpServletRequest request,
			Model model) {
		System.out.println("delete() ctr");
//		model.addAttribute("request",request);
//		command=new BDeleteCommand();
//		command.execute(model);
		String bid=request.getParameter("bid");
		iDao.delete(bid);
		
		return "redirect:list";
	}
	
	
	
	
}
