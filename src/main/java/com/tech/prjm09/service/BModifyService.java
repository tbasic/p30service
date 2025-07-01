package com.tech.prjm09.service;

import java.util.ArrayList;
import java.util.Map;

import org.springframework.ui.Model;

import com.tech.prjm09.dao.IDao;
import com.tech.prjm09.dto.BDto;
import com.tech.prjm09.dto.ReBrdimgDto;
import com.tech.prjm09.util.SearchVO;

import jakarta.servlet.http.HttpServletRequest;

public class BModifyService implements BServiceInter{

	private IDao iDao;
	public BModifyService(IDao iDao) {
		this.iDao=iDao;
	}

	@Override
	public void execute(Model model) {
		// TODO Auto-generated method stub
		System.out.println(">>>>>BModifyService");
		Map<String, Object> map=model.asMap();
		HttpServletRequest request=
				(HttpServletRequest) map.get("request");
		
		String bid=request.getParameter("bid");
		String bname=request.getParameter("bname");
		String btitle=request.getParameter("btitle");
		String bcontent=request.getParameter("bcontent");
		iDao.modify(bid, bname, btitle, bcontent);
		
		
	}

}
