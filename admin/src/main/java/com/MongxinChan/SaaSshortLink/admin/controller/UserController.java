package com.MongxinChan.SaaSshortLink.admin.controller;


import cn.hutool.core.bean.BeanUtil;
import com.MongxinChan.SaaSshortLink.admin.common.convention.result.Result;
import com.MongxinChan.SaaSshortLink.admin.common.convention.result.Results;
import com.MongxinChan.SaaSshortLink.admin.dto.req.UserRegisterReqDTO;
import com.MongxinChan.SaaSshortLink.admin.dto.resp.UserActualRespDTO;
import com.MongxinChan.SaaSshortLink.admin.dto.resp.UserRespDTO;
import com.MongxinChan.SaaSshortLink.admin.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Mongxin
 */
@RestController
@RequiredArgsConstructor //通过构造器方式注入

public class UserController {

//  @Autowired

  private final UserService userService;


  /**
   * 根据用户名查询用户信息
   */
  @GetMapping("/api/saas-short-link/v1/user/{userName}")
  public Result<UserRespDTO> getUserByUsername(@PathVariable("userName")String userName) {
    return  Results.success(userService.getUserByUsername(userName));
  }

  /**
   * 根据用户名查询无脱敏用户信息
   */
  @GetMapping("/api/saas-short-link/v1/actual/{userName}")
  public Result<UserActualRespDTO> getActualUserByUserName(@PathVariable("userName")String userName){
    return Results.success(BeanUtil.toBean(userService.getUserByUsername(userName),UserActualRespDTO.class));
  }

  /**
   * 查询用户名是否存在
   * @param userName 用户名
   * @return 存在返回 true 反之 false
   */
  @GetMapping("/api/saas-short-link/v1/actual/has-userName")
  public Result<Boolean>hasUserName(@PathVariable("userName")String userName){
    return Results.success(userService.hasUsername(userName));
  }

  /**
   * 注册用户
   * @param requestParam 参数体
   * @return 注册是否成功
   */
  @PostMapping("/api/saas-short-link/v1/user")
  public Result<Void> register(@RequestBody UserRegisterReqDTO requestParam){
    userService.register(requestParam);
    return Results.success();
  }
}
