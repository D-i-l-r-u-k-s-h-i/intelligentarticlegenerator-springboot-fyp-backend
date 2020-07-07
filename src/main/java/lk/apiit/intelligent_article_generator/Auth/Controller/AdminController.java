package lk.apiit.intelligent_article_generator.Auth.Controller;

import lk.apiit.intelligent_article_generator.Auth.DTO.UserDTO;
import lk.apiit.intelligent_article_generator.Auth.Service.CustomUserDetailService;
import lk.apiit.intelligent_article_generator.Util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin")
@ResponseBody
public class AdminController {

    @Autowired
    private CustomUserDetailService customUserDetailsService;

    @RequestMapping(value = "/register",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> registerNewUser(@RequestHeader(value = "Authorization") String token,@Valid @RequestBody UserDTO userDTO) throws Exception {
        Utils.checkToken(token);
        return ResponseEntity.ok(customUserDetailsService.saveUser(userDTO));
    }
}
