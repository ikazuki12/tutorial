package jp.co.bbs.filter;

import jp.co.bbs.dto.UserDto;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

@WebFilter(urlPatterns = { "/*" })
public class LoginFilter implements Filter {

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException{
		try{
			UserDto loginUser = (UserDto) ((HttpServletRequest)request).getSession().getAttribute("loginUser");
	
			if (loginUser == null) {
				request.getRequestDispatcher("/login/").forward(request, response);
				return;
			} else {
				chain.doFilter(request, response);
			}
		} catch (ServletException e){
	    } catch (IOException e){
	    }
	}

	public void init(FilterConfig config) {

	}

	public void destroy() {
	}

}
