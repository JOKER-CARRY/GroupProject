package com.utils;

public interface SystemConstants {
    /**
     * 注册成功
     */
    JSONResult REGIST_SUCCESS = new JSONResult(true, "注册成功！");
    /**
     * 注册失败
     */
    JSONResult REGIST_ERROR = new JSONResult(false, "注册失败！");

    /**
     * 用户状态（1：可用  2：不可用）
     */
    Integer USER_STATE_ENABLE = 1;


    /**
     * 加密次数
     */
    Integer HASHITERATIONS = 2;
    /**
     * 检查存在的key
     */
    String EXIST = "exist";

    /**
     * 提示信息key
     */
    String MESSAGE = "message";

    /**
     * 登录用户的key
     */
    String LOGINUSER = "loginUser";

    /**
     * 登录成功
     */
    JSONResult LOGIN_SUCCESS = new JSONResult(true,"登录成功");

    /**
     * 登录失败
     */
    JSONResult LOGIN_ERROR = new JSONResult(false,"登录失败,账号或密码错误！");

    /**
     * 添加成功
     */
    JSONResult ADD_SUCCESS =  new JSONResult(true,"添加成功");

    /**
     * 修改成功
     */
    JSONResult UPDATE_SUCCESS =  new JSONResult(true,"修改成功");

    /**
     * 修改失败
     */
    JSONResult UPDATE_ERROR =  new JSONResult(false,"修改失败");

    /**
     * 添加失败
     */
    JSONResult ADD_ERROR =  new JSONResult(false,"添加失败");

    /**
     * 删除成功
     */
    JSONResult DELETE_SUCCESS = new JSONResult(true,"删除成功");
    /**
     * 删除失败
     */
    JSONResult DELETE_ERROR = new JSONResult(false,"删除失败");

    /**
     * 图片上传地址
     */
    String IMAGE_UPLOAD_PATH = "E:/project/blog/upload/";

    /**
     * 图片回显地址
     */
    String IMAGE_SHOW_PATH = "/file/showImages/**";

    /**
     * lucene索引库的位置
     */
    String LUCENE_REPOSITORY = "E:/lucene/blog/repository";
}
