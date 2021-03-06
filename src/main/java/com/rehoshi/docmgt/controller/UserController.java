package com.rehoshi.docmgt.controller;

import com.rehoshi.docmgt.domain.RespData;
import com.rehoshi.docmgt.domain.entities.User;
import com.rehoshi.docmgt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/***
 * 这是类的描述
 *
 *
 * ************
 *
 * 功能：用户的增删该查
 * 方法：
 *         addUser()增加用户
 *         deleteUser()删除用户
 *         updateUser()更新用户
 *         selectByID()根据id查询用户
 *         selectByName()根据姓名查询用户
 *         selectByAccount()根据账号查询用户
 *
 *
 *
 * 数据的返回:返回json格式
 * @Author:SQY
 * @Date:2020.3.21
 */

@RestController
@RequestMapping("/user")
public class UserController extends HoshiController {


    @Autowired
    private UserService userService;

    /***
     * @description：添加用户
     * @param :
     * @author：SQY
     * @Date:2020.3.21
     *
     */
    @PostMapping("/add")
    public RespData<String> add(User user) {
        return $(respData -> {
            userService.save(user);
            String id = user.getId();
            if (id == null) {
                respData.success(false).msg("插入用户失败");
            } else {
                respData.success(true).data(id).msg("插入用户成功");
            }
        });
    }

    /***
     * 根据ID删除
     * @param id
     * @return
     * @author：SQY
     * @Date:2020.3.21
     */
    @DeleteMapping("/del")
    public RespData<Boolean> del(String id) {
        return $(booleanRespData -> {
            if (id == null) {
                booleanRespData.success(false).msg("未发现该ID");
            } else {
                userService.removeById(id);
                booleanRespData.success(true).msg("已删除");
            }
        });
    }

    /***
     * 根据ID更新用户
     * @param user
     * @return
     * @author：SQY
     * @Date:2020.3.21
     */
    @PutMapping("/update")
    public RespData<Boolean> update(@RequestBody User user) {
        return $(booleanRespData -> {
            if (user.getId() == null) {
                booleanRespData.success(false).msg("未能获取更新ID");
            } else {
                User byId = userService.getById(user.getId());
                byId.setName(user.getName());
                byId.setDescription(user.getDescription());
                userService.updateById(byId);
                booleanRespData.success(true).data(true).msg("修改成功") ;
            }
        });
    }

    /***
     * 根据关键字对姓名账号进行查询
     * @param pageIndex
     * @param pageSize
     * @return
     * @author：SQY
     * @Date:2020.3.21
     */
    @GetMapping("/list/{pageIndex}/{pageSize}")
    public RespData<List<User>> getName(@RequestParam(name = "search",required = false, defaultValue = "") String search,
                                     @PathVariable int pageIndex,
                                     @PathVariable int pageSize) {
        return $(listRespData -> {
            $page().index(pageIndex).size(pageSize);
            List<User> users = userService.listBySearch(search);
            listRespData.success(true).data(users);
        });
    }
}
