package com.tech.prjm09.service;

import java.util.Map;

import org.springframework.ui.Model;

import com.tech.prjm09.dao.IDao;
import com.tech.prjm09.util.SearchVO;

import jakarta.servlet.http.HttpServletRequest;

public class BListService implements BServiceInter{

	private IDao iDao;
	public BListService(IDao iDao) {
		this.iDao=iDao;
	}

	@Override
	public void execute(Model model) {
		// TODO Auto-generated method stub
		System.out.println(">>>>>BListService");
		Map<String, Object> map=model.asMap();
		HttpServletRequest request=
				(HttpServletRequest) map.get("request");
		SearchVO searchVO=(SearchVO) map.get("searchVO");
		
//		searching
		String btitle="";
		String bcontent="";
		
		
		String[] brdTitle=request.getParameterValues("searchType");
		if(brdTitle!=null) {
			for (int i = 0; i < brdTitle.length; i++) {
				System.out.println("brdtitle : "+brdTitle[i]);
			}
		}
		if(brdTitle!=null) {
			for (String val : brdTitle) {
				if (val.equals("btitle")) {
					model.addAttribute("btitle","true");
					btitle="btitle";
				}
				if (val.equals("bcontent")) {
					model.addAttribute("bcontent","true");
					bcontent="bcontent";
				}
			}
		}
		
		String searchKeyword=request.getParameter("sk");
		if(searchKeyword==null)
			searchKeyword="";
		model.addAttribute("searchKeyword",searchKeyword);
//		---------------------------
//		전체글의 갯수 변형
		int total=0;
		
		if (btitle.equals("btitle") && bcontent.equals("")) {
			total=iDao.selectBoardCount(searchKeyword,"1");
			System.out.println("total11111111111");
		}else if (btitle.equals("") && bcontent.equals("bcontent")) {
			total=iDao.selectBoardCount(searchKeyword,"2");
			System.out.println("total2222222222");
		}else if (btitle.equals("btitle") && bcontent.equals("bcontent")) {
			total=iDao.selectBoardCount(searchKeyword,"3");
			System.out.println("total3333333333");
		}else if (btitle.equals("") && bcontent.equals("")) {
			total=iDao.selectBoardCount(searchKeyword,"4");
			System.out.println("total4444444444");
		}
		
		
//		글의 총갯수
		//int total=iDao.selectBoardCount();
		
		
		System.out.println("totla : "+total);
		searchVO.pageCalculate(total);
		
		
		
//		paging
		String strPage=request.getParameter("page");
		//null검사
		if(strPage==null) {
			strPage="1";
		}
		int page=Integer.parseInt(strPage);
		searchVO.setPage(page);
		

		
		System.out.println("total : "+total);
		System.out.println("click page : "+strPage);
		System.out.println("pageStart : "+searchVO.getPageStart());
		System.out.println("pageEnd : "+searchVO.getPageEnd());
		System.out.println("pageTotal : "+searchVO.getTotPage());
		System.out.println("rowStart : "+searchVO.getRowStart());
		System.out.println("rowEnd : "+searchVO.getRowEnd());
		
		int rowStart=searchVO.getRowStart();
		int rowEnd=searchVO.getRowEnd();
		
		
//		ArrayList<BDto> list=null;
		if (btitle.equals("btitle") && bcontent.equals("")) {
			model.addAttribute("list",iDao.list(rowStart,rowEnd,searchKeyword,"1"));
			System.out.println("total11111111111");
		}else if (btitle.equals("") && bcontent.equals("bcontent")) {
			model.addAttribute("list",iDao.list(rowStart,rowEnd,searchKeyword,"2"));
			System.out.println("total2222222222");
		}else if (btitle.equals("btitle") && bcontent.equals("bcontent")) {
//			list=;
			model.addAttribute("list",iDao.list(rowStart,rowEnd,searchKeyword,"3"));
			System.out.println("total3333333333");
		}else if (btitle.equals("") && bcontent.equals("")) {
			model.addAttribute("list",iDao.list(rowStart,rowEnd,searchKeyword,"4"));
			System.out.println("total4444444444");
		}
			
//		model.addAttribute("list",list);
		
		model.addAttribute("totRowCnt",total);
		model.addAttribute("searchVo",searchVO);
		
		
	}

}
