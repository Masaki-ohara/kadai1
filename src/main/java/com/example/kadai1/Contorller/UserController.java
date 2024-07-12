package com.example.kadai1.controller;

import java.util.List;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.kadai1.model.User;
import com.example.kadai1.service.UserService;

@Controller
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/list")
    public String index(Model model) {
        List<User> users = userService.findAllUsers();
        model.addAttribute("users", users);
        return "list";
    }

    @GetMapping("/add")
    public String addUserForm(Model model) {
        model.addAttribute("user", new User());
        return "add-user";
    }

    @PostMapping("/add")
    public String addUser(User user, Model model) {
        /* 課題2 */
        // 入力に誤りが無いかをチェックする
        if (user.getName() == null || user.getName() == "" ||
                user.getAge() < 0 || user.getAge() > 100 ||
                user.getScore1() < 0 || user.getScore1() > 100 ||
                user.getScore2() < 0 || user.getScore2() > 100 ||
                user.getScore3() < 0 || user.getScore3() > 100) {
            // 入力フォームのオブジェクトをそのままビューに返す
            model.addAttribute("user", user);
            return "add-user";
        }
    
        userService.addUser(user);
        return "redirect:/users/list";
    }

    @GetMapping("/aggregate")
    public String aggregate(Model model) {
        List<User> users = userService.findAllUsers();
        // 各ユーザーの名前
        List<String> names = new ArrayList<>();
        // 各ユーザーの総得点
        List<Integer> totalScores = new ArrayList<>();

        List<Integer> scores1 = new ArrayList<>();
        List<Integer> scores2 = new ArrayList<>();
        List<Integer> scores3 = new ArrayList<>();


        for (User user : users) {
            names.add(user.getName());
            // 各ユーザーのテスト1～3の合計値を要素として追加
            totalScores.add(user.getScore1() + user.getScore2() + user.getScore3());

            scores1.add(user.getScore1());
            scores2.add(user.getScore2());
            scores3.add(user.getScore3());
        }

        model.addAttribute("names", names);
        model.addAttribute("totalScores", totalScores);

        model.addAttribute("scores1", scores1);
        model.addAttribute("scores2", scores2);
        model.addAttribute("scores3", scores3);
        return "aggregate";
    }
}