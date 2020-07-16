package lk.apiit.intelligent_article_generator.Auth.Service;

import lk.apiit.intelligent_article_generator.Auth.DTO.AdminDTO;
import lk.apiit.intelligent_article_generator.Auth.DTO.UserDTO;
import lk.apiit.intelligent_article_generator.Auth.Entity.*;
import lk.apiit.intelligent_article_generator.Auth.Repository.AdminRepository;
import lk.apiit.intelligent_article_generator.Auth.Repository.UserRepository;
import lk.apiit.intelligent_article_generator.Auth.Repository.AllUsersRepository;
import lk.apiit.intelligent_article_generator.Auth.UserSession;
import lk.apiit.intelligent_article_generator.Util.Utils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    AllUsersRepository allUsersRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AdminRepository adminRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        AllUsers user = allUsersRepository.findByUsername(username);
        return UserSession.create(user);
    }

    @Transactional
    public UserDetails loadUserById(Long id) {
        Optional<AllUsers> user = allUsersRepository.findById(id.longValue());
        return UserSession.create(user.get());
    }

    @Transactional
    public String saveUser(UserDTO userDTO){
        String ret="";

        AllUsers user= allUsersRepository.findByUsername(userDTO.getUserUserName());
        UserSession userSession = (UserSession) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(user!=null){
            ret = "Sorry this name is taken";
        }
        else{
            //check user role
            if(userSession.getRole().getRoleName().equals(RoleName.ROLE_ADMIN)){

                User cust_by_email= userRepository.findByUserEmail(userDTO.getUserEmail());
                if(cust_by_email!=null){
                    ret="Sorry, user with this Email already exists";
                }
                else{
                    String custPwd= userDTO.getPassword();
                    String pwd = new BCryptPasswordEncoder().encode(custPwd);

                    AllUsers newuser=new AllUsers();
                    newuser.setUsername(userDTO.getUserUserName());
                    newuser.setPassword(pwd);

                    newuser.setRole(new Role(2, RoleName.ROLE_USER));

                    User newCust=new User();
                    newCust.setUserUserName(userDTO.getUserUserName());
                    newCust.setUserEmail(userDTO.getUserEmail());

                    newCust.setUser(newuser);
                    allUsersRepository.save(newuser);
                    userRepository.save(newCust);

                    ret="Successful registration";
                }

            }
            else {
                ret="Only Admin Role can perform this action";
            }

        }

        return ret;
    }

    public String updatePwd(UserDTO userDTO){
        String response="";
        AllUsers u = allUsersRepository.findByUsername(userDTO.getUserUserName());
        if (u != null) {
//            if (bCryptPasswordEncoder.matches(pwdUpdate.getOld_password(), u.getPassword())) {
                if (userDTO.getPassword().equals(userDTO.getConfirmPassword())) {
                    String pwd = new BCryptPasswordEncoder().encode(userDTO.getPassword());
                    u.setPassword(pwd);
                    allUsersRepository.save(u);
                    response.concat("successful password update");
                } else {
                    response.concat("Password missmatch");
                }

        } else {
            response.concat("invalid User");
        }
        return response;
    }

    @Transactional
    public String saveAdmin(AdminDTO adminDTO){
        String ret="";

        AllUsers user= allUsersRepository.findByUsername(adminDTO.getUsername());

        if(user!=null){
            ret = "Sorry this name is taken";
        }
        else{
            UserSession userSession = (UserSession) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            if(bCryptPasswordEncoder.matches(adminDTO.getCurrentAdminPass(),userSession.getPassword()) && userSession.getRole().getRoleId()==1){
                String pwd = bCryptPasswordEncoder.encode(adminDTO.getPassword());

                ModelMapper modelMapper= new ModelMapper();
                Admin admin=modelMapper.map(adminDTO,Admin.class);
                AllUsers newuser=modelMapper.map(adminDTO,AllUsers.class);
                newuser.setPassword(pwd);
                newuser.setRole(new Role(1,RoleName.ROLE_ADMIN));
//                admin.setRole(new Role(1,RoleName.ROLE_ADMIN));

                adminRepository.save(admin);
                allUsersRepository.save(newuser);

                ret="Successful Registration";
            }
            else {
                ret="Sorry, incorrect password";
            }

        }
        return ret;
    }

//    public List<UserDTO> searchUser(String name){
//        List<User> userList = userRepository.searchUser(name);
//
//        List<UserDTO> dtoList= Utils.mapAll(userList, UserDTO.class);
//
//        return dtoList;
//    }
}
