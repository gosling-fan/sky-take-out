package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.PasswordConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        //1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码比对
        //对前端传过来的明文密码进行md5加密操作
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!password.equals(employee.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (employee.getStatus() == StatusConstant.DISABLE) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        //3、返回实体对象
        return employee;
    }

    /**
     * 员工新增
     * @param employeeDTO
     */
    @Override
    public void add(EmployeeDTO employeeDTO) {
        //1.接受来自前端的员工属性
        Employee employee = new Employee();
        //借助spring工具类，拷贝员工属性
        BeanUtils.copyProperties(employeeDTO,employee);
        //2.添加剩余员工表需设置的信息
        //设置员工账号状态0代表锁定，1代表正常，默认正常
        employee.setStatus(StatusConstant.ENABLE);
        //用MD5加密前端的明文密码
        employee.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes()) );
        //设置创建和修改时间
//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());
//        //设置操作的用户id
//        employee.setUpdateUser(BaseContext.getCurrentId());
//        employee.setCreateUser(BaseContext.getCurrentId());

        employeeMapper.add(employee);
    }

    /**
     * 根据用户名查询用户
     * @param username
     */
    @Override
    public Employee selectEmployeeByusername(String username) {
        Employee employeeMapperByUsername = employeeMapper.getByUsername(username);
        return employeeMapperByUsername;
    }

    /**
     * 员工分页查询
     * @param employeePageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO) {
        //用户名是不必须得，当前页和分页条数是必须的
        //1.借助mybatis的插件pageHelper,其可以动态的给我们写入的sql语句拼入limit 1,10分页条件
        PageHelper.startPage(employeePageQueryDTO.getPage(),employeePageQueryDTO.getPageSize());
        Page<Employee> page =employeeMapper.query(employeePageQueryDTO);
        long total = page.getTotal();
        List records = page.getResult();
        return new PageResult(total,records);
    }
    /**
     * 启用、禁用员工账号
     * @param status
     * @param id
     */
    @Override
    public void enableOrDisable(Integer status, Long id) {
        Employee employee = new Employee();
        employee.setStatus(status);
        employee.setId(id);
//        employee.setUpdateTime(LocalDateTime.now());
        //只有管理员能修改员工账号状态
//        employee.setUpdateUser(1L);
        employeeMapper.update(employee);
    }
    /**
     * 根据id查询员工信息
     * @return
     */
    @Override
    public Employee getEmployeeById(Long id) {
       Employee employee= employeeMapper.getById(id);
       employee.setPassword("******");
       return employee;
    }
    /**
     * 编辑员工信息
     * @param employeeDTO
     */
    @Override
    public void updateEmployee(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        //拷贝employeeDTO中的属性到employee
        BeanUtils.copyProperties(employeeDTO,employee);
        //更新时间设置为当前时间
//        employee.setUpdateTime(LocalDateTime.now());
//        // 更新人设置为当前登录用户，其id存储在ThreadLocal中，在登录时就已经通过拦截器，放入到ThreadLocal中。
//        employee.setUpdateUser(BaseContext.getCurrentId());
        employeeMapper.update(employee);
    }


}
