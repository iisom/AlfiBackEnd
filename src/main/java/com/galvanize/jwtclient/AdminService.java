package com.galvanize.jwtclient;

import org.springframework.stereotype.Service;
import com.galvanize.jwtclient.exceptions.RoleNotFoundException;
import java.util.Optional;

@Service
public class AdminService {
    public RoleList getRole(){
        return null;
    }
//    AdminService adminService;
//
//    public AdminService(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }
//
//    public void deleteUserRole(String user, String role) {
//        Optional<User> oUser = adminService.findUser(user);
//        Optional<Role> oRole = adminService.findRole(user,role);
//        if(oUser.isPresent() && oRole == role) {
//            oRole.delete(role);
//        }
//    }
//
//    public void deleteRole(String role) {
//       Optional<Role> oRole = adminService.findRole(role);
//       if(oRole.isPresent()) {
//           oRole.delete(role);
//       } else {
//           throw new RoleNotFoundException();
//       }
//    }
//
//    public void deleteUser(String user) {
//        Optional<User> oUser = adminService.findUser(user);
//        if(oUser.isPresent()) {
//            oUser.delete(user);
//        } else {
//            throw new RoleNotFoundException();
//        }
//    }
}



//public void deleteAuto(String vin) {
//    Optional<Automobile> oAuto = autosRepository.findByVin(vin);
//    if(oAuto.isPresent()) {
//        autosRepository.delete(oAuto.get());
//    } else {
//        throw new AutoNotFoundException();
//    }
//}