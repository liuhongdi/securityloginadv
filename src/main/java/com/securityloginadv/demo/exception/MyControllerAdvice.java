package com.securityloginadv.demo.exception;

import com.securityloginadv.demo.constant.ResponseCode;
import com.securityloginadv.demo.result.RestResult;
import com.securityloginadv.demo.util.ServletUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolationException;

/*
*@author:liuhongdi
*@date:2020/7/10 上午10:58
*@description:spring对错误的集中处理
*/

@ControllerAdvice
public class MyControllerAdvice {

    private static Logger logger = LogManager.getLogger(MyControllerAdvice.class.getName());
    private static Logger loggerBE = LogManager.getLogger("BusinessErrorFile");

    //验证用户信息时出错
    @ResponseBody
    @ExceptionHandler(value = InternalAuthenticationServiceException.class)
    public RestResult loginfailHandler(InternalAuthenticationServiceException e) {
        System.out.println("loginfailHandler");
        //loggerBE.error("ConstraintViolationException: \n"+ ServletUtil.getUrl()+"\n"+e.getMessage(), e);
        ResponseCode.LOGIN_FAIL.setMsg(e.getMessage());
        return RestResult.error(ResponseCode.LOGIN_FAIL);
    }

    //验证参数时不符合要求
    @ResponseBody
    @ExceptionHandler(value = ConstraintViolationException.class)
    public RestResult violationHandler(ConstraintViolationException e) {

        loggerBE.error("ConstraintViolationException: \n"+ ServletUtil.getUrl()+"\n"+e.getMessage(), e);
        ResponseCode.ARG_VIOLATION.setMsg(e.getMessage());
        return RestResult.error(ResponseCode.ARG_VIOLATION);
    }

    //缺少应该传递的参数
    @ResponseBody
    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public RestResult missingParameterHandler(MissingServletRequestParameterException e) {
        loggerBE.error("MissingServletRequestParameterException: \n"+ ServletUtil.getUrl()+"\n"+e.getMessage(), e);
        ResponseCode.ARG_MISSING.setMsg(e.getMessage());
        return RestResult.error(ResponseCode.ARG_MISSING);
    }

    //参数类型不匹配，用户输入的参数类型有错误时会报这个
    @ResponseBody
    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    public RestResult misMatchErrorHandler(MethodArgumentTypeMismatchException e) {

        loggerBE.error("MethodArgumentTypeMismatchException: \n"+ ServletUtil.getUrl()+"\n"+e.getMessage(), e);
        ResponseCode.ARG_TYPE_MISMATCH.setMsg(e.getMessage());
        return RestResult.error(ResponseCode.ARG_TYPE_MISMATCH);
    }

    //验证时绑定错误
    @ResponseBody
    @ExceptionHandler(value = BindException.class)
    public RestResult errorHandler(BindException ex) {
        BindingResult result = ex.getBindingResult();
        StringBuilder errorMsg = new StringBuilder();
        for (ObjectError error : result.getAllErrors()) {
            errorMsg.append(error.getDefaultMessage()).append(",");
        }
        errorMsg.delete(errorMsg.length() - 1, errorMsg.length());
        ResponseCode.ARG_BIND_EXCEPTION.setMsg(errorMsg.toString());
        loggerBE.error("BindException: \n"+ ServletUtil.getUrl()+"\n"+errorMsg.toString(), ex);
        return RestResult.error(ResponseCode.ARG_BIND_EXCEPTION);
    }

    /*
    *@author:liuhongdi
    *@date:2020/7/7 下午3:06
    *@description:自定义的业务类异常的处理
     * @param se
    *@return:
    */
    @ResponseBody
    @ExceptionHandler(BusinessException.class)
    public RestResult serviceExceptionHandler(BusinessException se) {
        //loggerBE.error("ServiceException: \n"+ ServletUtil.getUrl()+"\n"+se.getResponseCode(), se);
        System.out.println("serviceExceptionHandler");
        ResponseCode rcode = se.getResponseCode();
        return RestResult.error(rcode);
    }

    /*
    *@author:liuhongdi
    *@date:2020/7/7 下午3:05
    *@description:通用的对异常的处理
     * @param e
    *@return:
    */
    @ResponseBody
    @ExceptionHandler(Exception.class)
    public RestResult exceptionHandler(Exception e) {
        //logger.error("Exception: \n"+ ServletUtil.getUrl(), e);
        System.out.println("this is exceptionHandler");
        return RestResult.error(ResponseCode.ERROR);
    }

    //主动抛出的异常的处理
    @ResponseBody
    @ExceptionHandler(ThrowException.class)
    public RestResult throwExceptionHandler(ThrowException e) {
        logger.error("ThrowException: \n"+ ServletUtil.getUrl()+"\n" +e.getMsg(), e);
        return RestResult.error(ResponseCode.ERROR);
    }
}