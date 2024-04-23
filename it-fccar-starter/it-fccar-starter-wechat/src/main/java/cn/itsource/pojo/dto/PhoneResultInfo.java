package cn.itsource.pojo.dto;

import lombok.Data;

@Data
public class PhoneResultInfo {
   private String  errcode;
   private String  errmsg;
   private PhoneInfo phoneInfo;
}
