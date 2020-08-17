package com.xiaoshu.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import com.xiaoshu.dao.CourseMapper;
import com.xiaoshu.dao.StudentMapper;
import com.xiaoshu.dao.UserMapper;
import com.xiaoshu.entity.Course;
import com.xiaoshu.entity.Student;
import com.xiaoshu.entity.User;
import com.xiaoshu.entity.UserExample;
import com.xiaoshu.entity.UserExample.Criteria;

import redis.clients.jedis.Jedis;

@Service
public class StuService {

	@Autowired
	UserMapper userMapper;
	
	@Autowired
	private StudentMapper sm;
	
	@Autowired
	private CourseMapper cm;
	
	public void addKc(Course course){
		cm.addKc(course);
	}
	
	// 查询所有
	public List<User> findUser(User t) throws Exception {
		return userMapper.select(t);
	};

	// 数量
	public int countUser(User t) throws Exception {
		return userMapper.selectCount(t);
	};

	// 通过ID查询
	public User findOneUser(Integer id) throws Exception {
		return userMapper.selectByPrimaryKey(id);
	};

	// 新增
	public void addStu(Student stu) throws Exception {
		sm.insert(stu);
		Jedis jedis = new Jedis("127.0.0.1",6379);
		jedis.set(stu.getSname(), stu.getAge());
	};

	// 修改
	public void updateStu(Student stu) throws Exception {
		sm.updateByPrimaryKey(stu);
	};

	// 删除
	public void deleteUser(Integer id) throws Exception {
		userMapper.deleteByPrimaryKey(id);
	};

	// 登录
	public User loginUser(User user) throws Exception {
		UserExample example = new UserExample();
		Criteria criteria = example.createCriteria();
		criteria.andPasswordEqualTo(user.getPassword()).andUsernameEqualTo(user.getUsername());
		List<User> userList = userMapper.selectByExample(example);
		return userList.isEmpty()?null:userList.get(0);
	};

	// 通过用户名判断是否存在，（新增时不能重名）
	public Student existfindByName(String sname) throws Exception {
		List<Student> sList = sm.findName(sname);
		return sList.isEmpty()?null:sList.get(0);
	};

	// 通过角色判断是否存在
	public User existUserWithRoleId(Integer roleId) throws Exception {
		UserExample example = new UserExample();
		Criteria criteria = example.createCriteria();
		criteria.andRoleidEqualTo(roleId);
		List<User> userList = userMapper.selectByExample(example);
		return userList.isEmpty()?null:userList.get(0);
	}

	public PageInfo<Student> findStuPage(Student stu, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		List<Student> stuList = sm.findAll(stu);
		PageInfo<Student> pageInfo = new PageInfo<Student>(stuList);
		return pageInfo;
	}

	public List<Course> findCourse() {
		// TODO Auto-generated method stub
		
		return cm.selectAll();
	}
	
	public List<Student> findAll() {
		// TODO Auto-generated method stub
		List<Student> stuList = sm.findAll(null);
		return stuList;
	};
	
	public List<Course> countStu(){
		return sm.countStu();
	}

}
