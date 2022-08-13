package com.luguz.aspect;

import com.luguz.entity.RequestLog;
import com.luguz.service.IRequestLogService;
import com.luguz.util.IpUtil;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import org.apache.commons.logging.Log;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Aspect// 使用@Aspect注解声明一个切面
@Component
public class MyLogAspect {

    @Autowired
    private IRequestLogService iRequestLogService;
    private static Log logger = LogFactory.getLog(MyLogAspect.class);
    /**
     * 这里我们使用注解的形式
     * 当然，我们也可以通过切点表达式直接指定需要拦截的package,需要拦截的class 以及 method
     * 切点表达式:   execution(...)
     */
    @Before("execution(public * com.luguz.controller..*(..)) && @annotation(myLog)")
    public void doBefore(JoinPoint joinPoint,MyLog myLog) {

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String url = request.getRequestURI();
        String ipAddress = IpUtil.getIpAddress(request);
        String classMethod = joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        String argsStr=Arrays.toString(args);
        if(argsStr.length()>=255){argsStr=argsStr.substring(0,245)+"......";}
        iRequestLogService.save(RequestLog.builder().url(url).ipAddress(ipAddress).classMethod(classMethod).args(argsStr).build());
    }

    /**
     * 统计方法执行耗时Around环绕通知
     *
     * @param joinPoint
     * @return
     */
//    @Around("execution(public * com.luguz.controller..*(..))")
    //等价于@Around("execution (* com.packtpub.restapp.*.*(..))";)
    public Object timeAround(ProceedingJoinPoint joinPoint) {
        // 定义返回对象、得到方法需要的参数
        Object obj = null;
        Object[] args = joinPoint.getArgs();
        long startTime = System.currentTimeMillis();

        try {
            obj = joinPoint.proceed(args);
        } catch (Throwable e) {
            logger.error("统计某方法执行耗时环绕通知出错", e);
        }

        // 获取执行的方法名
        long endTime = System.currentTimeMillis();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getDeclaringTypeName() + "." + signature.getName();

        // 打印耗时的信息
        this.printExecTime(methodName, startTime, endTime);

        return obj;
    }

    /**
     * 打印方法执行耗时的信息，如果超过了一定的时间，才打印
     *
     * @param methodName
     * @param startTime
     * @param endTime
     */
    private void printExecTime(String methodName, long startTime, long endTime) {
        long diffTime = endTime - startTime;
            logger.info("-----" + methodName + " 方法执行耗时：" + diffTime + " ms");
    }


}
