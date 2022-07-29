package com.example.springbootmybatis.controller;

import com.example.springbootmybatis.annotation.Login;
import com.example.springbootmybatis.pojo.JsonRespBo;
import com.example.springbootmybatis.pojo.User;
import com.example.springbootmybatis.pojo.query.UserQuery;
import com.example.springbootmybatis.service.ILoginService;
import com.example.springbootmybatis.service.UserService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private ILoginService loginService;

    @Value("${RSA.Encrypt.PublicKey1}") String publicKey;

    @GetMapping("/pubkey")
    @CrossOrigin
    public String getPubKey(HttpServletRequest request, HttpServletResponse response){
        return publicKey;
    }


    @PostMapping("/login")
    @CrossOrigin
    public JsonRespBo loginAuth(HttpServletRequest request, HttpServletResponse response, @RequestBody User user) throws Exception {
        JsonRespBo result = loginService.login(user, request);
        return result;
    }

    @GetMapping("/logout")
    @CrossOrigin
    public boolean logOut(HttpServletRequest request, HttpServletResponse response) throws Exception {
        boolean b = loginService.logOut(request);
        return b;
    }

    @GetMapping("/home")
    @CrossOrigin
    @Login
    public List<User> listUser(HttpServletResponse response){
        return userService.listUser();
    }

    @ApiOperation("跳转页面")
    @GetMapping("/")
    public PageInfo<User> index(Model model, UserQuery userQuery){
        PageInfo<User> userPageInfo = userService.listUserByName(userQuery);
//        model.addAttribute("page",userPageInfo);
//        return "index";
        return userService.listUserByName(userQuery);
    }

    @ApiOperation("根据用户名搜索")
    @PostMapping("/")
    public String listUserByName(Model model,UserQuery userQuery){
        PageInfo<User> userPageInfo = userService.listUserByName(userQuery);
        model.addAttribute("page",userPageInfo);
        return "index";

    }

    @ApiOperation("根据id搜索")
    @GetMapping("/{id}")
    @CrossOrigin
    @Login
    public User findUserById(@PathVariable("id") Integer id){
        return userService.queryUserById(id);

    }

    @ApiOperation("根据用户id删除用户")
    @DeleteMapping("/delete/{id}")
    @CrossOrigin
    public boolean delete(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes, HttpServletResponse response){
//        System.out.println(id);
//        boolean b = userService.deleteUserById(id);
//        if (b){
//            redirectAttributes.addFlashAttribute("message","删除用户成功");
//            return "redirect:/";
//        }else{
//            redirectAttributes.addFlashAttribute("message","删除用户失败");
//            return "redirect:/";
//        }
//        response.addHeader(  "Access-Control-Allow-Origin","DELETE");//允许所有来源访同
//        response.addHeader(  "Access-Control-Allow-Method","POST,GET");//允许访问的方式
        return userService.deleteUserById(id);
    }

    @ApiOperation("进入用户编辑界面")
    @GetMapping("/edit/{id}")
    public String toEdit(@PathVariable("id") Integer id,Model model){
        model.addAttribute("user",userService.queryUserById(id));
        return "editUser";
    }

    @ApiOperation("新增/编辑用户")
    @PostMapping("/edit")
    public String edit(@RequestBody User user, RedirectAttributes redirectAttributes){
        if (user.getId() == null){
            boolean b = userService.addUser(user);
            if (b){
                redirectAttributes.addFlashAttribute("message","新增用户成功");
                return "redirect:/";
            }else {
                redirectAttributes.addFlashAttribute("message","新增用户失败");
                return "redirect:/";
            }
        } else {
            boolean b = userService.updateUser(user);
            if (b){
                redirectAttributes.addFlashAttribute("message","更新用户成功");
                return "redirect:/";
            }else {
                redirectAttributes.addFlashAttribute("message","更新用户失败");
                return "redirect:/";
            }
        }
    }

//    @ApiOperation("进入用户新增界面")
//    @GetMapping("/update")
//    public String toUpdate(Model model){
//        User user = new User();
//        model.addAttribute("user",user);
//        return "editUser";
//    }

    @ApiOperation("编辑用户")
    @PutMapping("/update")
    @CrossOrigin
    public boolean update(@RequestBody User user){
        return userService.updateUser(user);
    }

    @ApiOperation("新增用户")
    @PostMapping("/add")
    @CrossOrigin
    public boolean add(@RequestBody User user){
        return userService.addUser(user);
    }


}
