package com.yufeng;

import cn.hutool.core.bean.BeanUtil;
import com.yufeng.domain.po.User;
import com.yufeng.domain.vo.VoUser;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

//@SpringBootTest
class UserCenterBackendMasterApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void testCopyProperties(){
        User user = new User();
        user.setUsername("222");
        user.setPassword("3333");
        VoUser voUser = BeanUtil.copyProperties(user, VoUser.class);
        System.out.println(voUser);
    }

}
