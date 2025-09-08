package com.MongxinChan.SaaSshortLink.admin.common.enums;

import com.MongxinChan.SaaSshortLink.admin.common.convention.errorCode.IErrorCode;

public enum UserErrorCodeEnum implements IErrorCode {

  USER_NULL("B000200","用户记录不存在"),
  //枚举类型用,分割

  USER_EXIST("B002001","用户记录已存在");

  private final String code;

  private final String errorMessage;

  UserErrorCodeEnum(String code,String errorMessage){
    this.code=code;
    this.errorMessage=errorMessage;
  }

  @Override
  public String code() {
    return code;
  }

  @Override
  public String errorMessage() {
    return errorMessage;
  }
}
