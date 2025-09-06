package com.MongxinChan.SaaSshortLink.admin.controller;


import com.MongxinChan.SaaSshortLink.admin.convention.Result;
import com.MongxinChan.SaaSshortLink.admin.dto.resp.UserRespDTO;
import com.MongxinChan.SaaSshortLink.admin.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor //通过构造器方式注入

public class UserController {

//  @Autowired

  private UserService userService;


  /**
   * 根据用户名查询用户信息
   */
  @GetMapping("/api/SaaSshortLink/v1/user/{userName}")
  public Result<UserRespDTO> getUserByUsername(@PathVariable("userName")String userName) {
    UserRespDTO result = userService.getUserByUsername(userName);
    if (result == null){
      return new Result<UserRespDTO>().setCode("1").setMessage("用户登陆成功");
    } else {
      return new Result<UserRespDTO>().setCode("0").setData(result);
    }
  }
}
