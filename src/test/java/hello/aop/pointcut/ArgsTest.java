package hello.aop.pointcut;

import hello.aop.member.MemberServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class ArgsTest {

    Method helloMethod;

    @BeforeEach
    public void init() throws NoSuchMethodException {
        helloMethod = MemberServiceImpl.class.getMethod("hello", String.class);
    }

    private AspectJExpressionPointcut pointcut(String expression) throws NoSuchMethodException {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression(expression);
        return pointcut;
    }

    @Test
    void testArgs() throws NoSuchMethodException {
        // hello(String) 과 매칭
        assertThat(pointcut("args(String)")
                .matches(helloMethod,MemberServiceImpl.class)).isTrue();

        assertThat(pointcut("args(Object)")
                .matches(helloMethod,MemberServiceImpl.class)).isTrue();

        assertThat(pointcut("args()")
                .matches(helloMethod,MemberServiceImpl.class)).isFalse();

        assertThat(pointcut("args(..)")
                .matches(helloMethod,MemberServiceImpl.class)).isTrue();

        assertThat(pointcut("args(*)")
                .matches(helloMethod,MemberServiceImpl.class)).isTrue();

        assertThat(pointcut("args(String,..)")
                .matches(helloMethod,MemberServiceImpl.class)).isTrue();
    }


    @Test
    @DisplayName("args 는 상위 타입도 허용한다.")
    void testArgsVsExecution() throws NoSuchMethodException {
        // hello(String) 과 매칭
        assertThat(pointcut("args(String)")
                .matches(helloMethod,MemberServiceImpl.class)).isTrue();
        assertThat(pointcut("args(Object)")
                .matches(helloMethod,MemberServiceImpl.class)).isTrue();
        assertThat(pointcut("args(java.io.Serializable)")
                .matches(helloMethod,MemberServiceImpl.class)).isTrue();

        assertThat(pointcut("execution(* *(String))")
                .matches(helloMethod,MemberServiceImpl.class)).isTrue();
        assertThat(pointcut("execution(* *(java.io.Serializable))")
                .matches(helloMethod,MemberServiceImpl.class)).isFalse();
        assertThat(pointcut("execution(* *(Object))")
                .matches(helloMethod,MemberServiceImpl.class)).isFalse();
    }

}
