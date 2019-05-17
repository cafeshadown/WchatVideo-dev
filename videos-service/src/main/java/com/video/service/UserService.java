package com.video.service;

import com.video.pojo.Users;
import com.video.pojo.UsersReport;

public interface UserService {

	/**
	 * @Description: 判断用户名是否存在
	 */
	boolean queryUsernameIsExist(String username);

	int getCount();

	/**
	 * @Description: 保存用户(用户注册)
	 */

	void saveUser(Users user);

	/**
	 * @Description: 用户登录，根据用户名和密码查询用户
	 */
    Users queryUserForLogin(String username, String password);

	/**
	 * @Description: 用户修改信息
	 */
    void updateUserInfo(Users user);

	/**
	 * @Description: 查询用户信息
	 */
    Users queryUserInfo(String userId);

	/**
	 * @Description: 查询用户是否喜欢点赞视频
	 */
    boolean isUserLikeVideo(String userId, String videoId);

	/**
	 * @Description: 增加用户和粉丝的关系
	 */
    void saveUserFanRelation(String userId, String fanId);

	/**
	 * @Description: 删除用户和粉丝的关系
	 */
    void deleteUserFanRelation(String userId, String fanId);

	/**
	 * @Description: 查询用户是否关注
	 */
    boolean queryIfFollow(String userId, String fanId);

	/**
	 * @Description: 举报用户
	 */
    void reportUser(UsersReport userReport);
}
