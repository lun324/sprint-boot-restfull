package net.fulugou.demo.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

//import net.fulugou.demo.exception.RestExceptionHandle;
import net.fulugou.demo.config.PageNumAndPageSizeConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@WebFilter
public class PageFilter implements Filter {

    private final static Logger logger = LoggerFactory.getLogger(PageFilter.class);

    public PageFilter() {}

    public void destroy() {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;

        PageNumAndPageSizeConfig.setPageNum(getPageNum(httpRequest));
        PageNumAndPageSizeConfig.setPageSize(getPageSize(httpRequest));

        try {
            chain.doFilter(request, response);
        }
        // 使用完Threadlocal，将其删除。使用finally确保一定将其删除
        finally {
            PageNumAndPageSizeConfig.removePageNum();
            PageNumAndPageSizeConfig.removePageSize();
        }
    }

    /**
     * 获得pager.offset参数的值
     *
     * @param request
     * @return
     */
    protected int getPageNum(HttpServletRequest request) {
        int pageNum = 1;
        try {
            String pageNums = request.getParameter("pageNum");
            if (pageNums != null && pageNums.matches("\\d+"))  {
                pageNum = Integer.parseInt(pageNums);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return pageNum;
    }

    /**
     * 设置默认每页大小
     *
     * @return
     */
    protected int getPageSize(HttpServletRequest request) {
        int pageSize = 10;    // 默认每页10条记录
        logger.info("1");
        try {
            String pageSizes = request.getParameter("pageSize");
            if (pageSizes != null && pageSizes.matches("\\d+")) {
                pageSize = Integer.parseInt(pageSizes);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return pageSize;
    }

    public void init(FilterConfig fConfig) throws ServletException {}

}

