package com.medication.controller;

import com.medication.dto.LoginRequest;
import com.medication.dto.LoginResponse;
import com.medication.dto.Result;
import com.medication.entity.Patient;
import com.medication.entity.User;
import com.medication.service.PatientService;
import com.medication.service.UserService;
import com.medication.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private PatientService patientService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @PostMapping("/login")
    public Result<LoginResponse> login(@RequestBody LoginRequest request) {
        User user = userService.login(request.getUsername(), request.getPassword());
        
        if (user == null) {
            return Result.error("用户名或密码错误");
        }
        
        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole());
        
        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setUserId(user.getId());
        response.setUsername(user.getUsername());
        response.setRealName(user.getRealName());
        response.setRole(user.getRole());
        response.setPatientId(user.getPatientId());
        
        return Result.success(response);
    }
    
    @PostMapping("/register")
    public Result<Map<String, Object>> register(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");
        String realName = request.get("realName");
        String phone = request.get("phone");
        String gender = request.get("gender");
        Integer age = request.get("age") != null ? Integer.parseInt(request.get("age")) : null;
        
        // 检查用户名是否已存在
        User existUser = userService.findByUsername(username);
        if (existUser != null) {
            return Result.error("用户名已存在");
        }
        
        // 创建患者信息
        Patient patient = new Patient();
        patient.setName(realName);
        patient.setPhone(phone);
        patient.setGender(gender);
        patient.setAge(age);
        patientService.save(patient);
        
        // 创建用户账号
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setRealName(realName);
        user.setRole("PATIENT");
        user.setPatientId(patient.getId());
        user.setPhone(phone);
        user.setStatus(1);
        userService.save(user);
        
        Map<String, Object> result = new HashMap<>();
        result.put("userId", user.getId());
        result.put("patientId", patient.getId());
        result.put("username", username);
        
        return Result.success(result);
    }
    
    @GetMapping("/info")
    public Result<User> getUserInfo(@RequestHeader("Authorization") String token) {
        try {
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            
            Long userId = jwtUtil.getUserIdFromToken(token);
            User user = userService.getById(userId);
            
            if (user != null) {
                user.setPassword(null);
                return Result.success(user);
            }
            
            return Result.error("用户不存在");
        } catch (Exception e) {
            return Result.error("Token无效");
        }
    }
}
