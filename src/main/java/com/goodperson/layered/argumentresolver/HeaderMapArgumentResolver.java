package com.goodperson.layered.argumentresolver;

import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class HeaderMapArgumentResolver implements HandlerMethodArgumentResolver {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    // 컨트롤러 메소드의 인자가 4개면 supportsParameter 함수가 4번 호출된다.
    // 인자의 정보를 파라미터로 전달한다.
    // 컨트롤러의 인자가 HeaderInfo headerInfo 라면 true를 반환한다.
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType() == HeaderInfo.class;
    }

    // supportsParameter 함수가 true를 반환할 때만 호출된다.
    // 이 함수에서 반환한 값은 컨트롤러의 인자로 전달된다.
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        HeaderInfo headerInfo = new HeaderInfo();
        Iterator<String> headerNames = webRequest.getHeaderNames();
        StringBuilder sb = new StringBuilder();
        sb.append("\n========== headerInfo=========\n");
        while (headerNames.hasNext()) {
            String headerName = headerNames.next();
            String headerValue = webRequest.getHeader(headerName);
            headerInfo.put(headerName, headerValue);
            sb.append(headerName);
            sb.append(": ");
            sb.append(headerValue);
            sb.append("\n");
        }
        sb.append("=============================");
        logger.debug(sb.toString());
        return headerInfo;
    }

}