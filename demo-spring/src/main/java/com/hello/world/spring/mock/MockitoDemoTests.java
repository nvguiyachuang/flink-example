package com.hello.world.spring.mock;

import com.hello.world.spring.mock.dep.User;
import com.hello.world.spring.mock.dep.UserMapper;
import com.hello.world.spring.mock.dep.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.mockito.Mockito.when;

@SpringBootTest
class MockitoDemoTests {

    @MockBean
    UserMapper userMapper;

    @Autowired
    UserService userService;

    @Test
    void mockUserMapper() {
        when(userMapper.selectOne(0))
                .thenReturn(new User()); //这里mock的是userMapper的方法，service间接调用。
        User user = userService.getOne(0);
        assertThat(user, notNullValue()); //断言不为空
    }

}
