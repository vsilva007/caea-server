package ad.smartstudio.caea.aspect;

import ad.smartstudio.caea.model.entity.Usuari;
import ad.smartstudio.caea.service.AuditService;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class AuditMethod {
	private AuditService service;

	@Around("@annotation(Audit)")
	public Object auditMethod(ProceedingJoinPoint joinPoint) throws Throwable {
		Object proceed = joinPoint.proceed();
		try {
			MethodSignature signature = (MethodSignature) joinPoint.getSignature();
			Method method = signature.getMethod();
			Audit audit = method.getAnnotation(Audit.class);
			service.audit(((Usuari) ((UsernamePasswordAuthenticationToken) joinPoint.getArgs()[0]).getPrincipal()).getLogin(), audit.message());
		} catch (ClassCastException | IndexOutOfBoundsException | NullPointerException e) {
			LogFactory.getLog("AOP").info("In order to audit a method correctly, first argument should be Principal");
		}
		return proceed;
	}

	@Autowired
	public void setService(AuditService service) {
		this.service = service;
	}
}