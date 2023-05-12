package com.graduation;

import com.graduation.exception.ServiceException;
import com.graduation.mapper.AnswerMapper;
import com.graduation.mapper.TagMapper;
import com.graduation.mapper.UserMapper;
import com.graduation.model.*;
import com.graduation.service.IUserService;
import com.graduation.vo.RegisterVo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.annotation.Resource;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class OnlineApplicationTests {

	@Test
	void contextLoads() {

	}

	@Autowired
	TagMapper tagMapper;
	@Test
	void testTag(){
		List<Tag> tags = tagMapper.selectList(null);
		for (Tag tag : tags){
			System.out.println("tag = " + tag);
		}
	}

	@Autowired
	UserMapper userMapper;

	@Test
	void testUser(){
		User user = userMapper.findUserByUsername("st2");
		System.out.println("user = " + user);
		List<Permission> ps = userMapper.findUserPermissionsById(user.getId());
		for (Permission p: ps ) {
			System.out.println("Permission = " + p);
		}
	}

	@Autowired
	IUserService userService;
	@Test
	void addStu(){
		RegisterVo registerVo=new RegisterVo()
				.setPhone("13833148996")
				.setNickname("addStuTest")
				.setPassword("888888")
				.setInviteCode("JAVA2023-005803");
		try {
			userService.registerStudent(registerVo);
		}catch (ServiceException e){
			System.out.println(e.getMessage());
		}
		System.out.println("完成");
	}


	@Test
	void role(){
		List<Role> roles=userMapper.findUserRolesById(3);
		for(Role r:roles){
			System.out.println(r);
		}
	}

	@Resource
	AnswerMapper answerMapper;
	@Test
	void run(){
		List<Answer> answers=answerMapper
				.findAnswersWithCommentsByQuestionId(151L);
		for(Answer a: answers){
			System.out.println(a);
		}
	}

	@Resource
	RedisTemplate<String,List<Tag>> redisTemplate;
	@Test
	void redisTest(){
		List<Tag> tags = tagMapper.selectList(null);
		redisTemplate.opsForValue().set("allTags",tags);
		System.out.println("Ok");
	}

	@Test
	void getRedis(){
		List<Tag> tags = redisTemplate.opsForValue().get("allTags");
		for (Tag tag : tags){
			System.out.println("tag = " + tag);

		}
	}

	private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
	@Test
	public void testPassword(){
		String pwd = "888888";
		String encodePwd = encoder.encode(pwd);
		System.out.println(encodePwd);
		boolean matches = encoder.matches(pwd, encodePwd);
		System.out.println(matches);

		System.out.println("+++++++++++++++++");
		User user = userMapper.selectById(76);
		String password = user.getPassword();
		String passwd = password.replace("{bcrypt}","");
		System.out.println(passwd);
		boolean matches1 = encoder.matches(pwd, passwd);
		System.out.println(matches1);
	}

	@Test
	public void testString(){
		String a = "{bcrypt}123456";
		String b = a.replace("{bcrypt}","");
		System.out.println(b);
	}





}
