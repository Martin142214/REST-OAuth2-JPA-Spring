package com.example.RestOAuth2JPA.services.auth;

import java.sql.SQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import com.example.RestOAuth2JPA.DTO.entities.FileDB;
import com.example.RestOAuth2JPA.DTO.entities.auth.Role;
import com.example.RestOAuth2JPA.DTO.entities.auth.User;
import com.example.RestOAuth2JPA.components.classModels.auth.User_login;
import com.example.RestOAuth2JPA.services.fileUpload.FileDbService;
import com.example.RestOAuth2JPA.services.fileUpload.FileUploadUtils;

//import com.example.RestOAuth2JPA.services.userDetails.UserDetailsServiceImplementation;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class AuthService {

   // @Autowired
    //private AuthenticationManager authenticationManager;

    //@Autowired UserDetailsServiceImplementation userDetailsService;

    @Autowired UserService userService;

    @Autowired RoleService roleService;

    @Autowired FileDbService fileDbService;

    @Autowired JwtUtil jwtUtil;

    @Autowired
    public AuthService(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    public void createNewAdminUserAndSaveToDB(final User_login userLoginData, final MultipartFile userImage) {
        /*Privilege readPrivilege = privilegeService.createPrivilegeIfNotFound("READ_PRIVILEGE");
        Privilege createPrivilege = privilegeService.createPrivilegeIfNotFound("CREATE_PRIVILEGE");
        Privilege updatePrivilege = privilegeService.createPrivilegeIfNotFound("UPDATE_PRIVILEGE");
        Privilege deletePrivilege = privilegeService.createPrivilegeIfNotFound("DELETE_PRIVILEGE");

        List<Privilege> adminPrivileges = Arrays.asList(readPrivilege, createPrivilege, updatePrivilege, deletePrivilege);
        roleService.createRoleIfNotFound("ROLE_ADMIN", adminPrivileges);
        roleService.createRoleIfNotFound("ROLE_PATIENT", Arrays.asList(readPrivilege));
        roleService.createRoleIfNotFound("ROLE_DOCTOR", Arrays.asList(readPrivilege));*/

        // the above is done; saved in the DB

        // get all users with all their parent entities data
        /*var users = userService.get_all_users().stream()
                                               .filter(u -> !u.isAdmin())
                                               .collect(Collectors.toList());*/
        
        final Role adminRole = roleService.findByName("ROLE_ADMIN");
        User user = userService.createAdminUserAndReturn(userLoginData, adminRole);

        FileDB userImageFile = FileUploadUtils.mkdir_and_upload_file_and_return_imagePath(userLoginData.getUsername(), adminRole, userImage);
        user.setProfileImage(userImageFile);

        try {
            fileDbService.saveNewFileDB(userImageFile);
            this.userService.saveNewOrUpdateUser(user);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean authenticate(String username, String password) throws Exception {
        try {
            //authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            var auth = SecurityContextHolder.getContext().getAuthentication();
            return true;
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

    public boolean isValidTokenForAuth(String username, String jwtToken, HttpServletRequest request) {
        try {
            username = jwtUtil.getUsernameFromToken(jwtToken);
        } catch (IllegalArgumentException e) {
            System.out.println("Unable to get JWT Token");
        } catch (ExpiredJwtException e) {
            System.out.println("JWT Token has expired");
        }

        var auth = SecurityContextHolder.getContext().getAuthentication();
        //TODO the getAuthentication() method is != null beacause there is some authentication set from somewhere
        // try to find from where is set the authentication
        /*if (username != null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            if (jwtUtil.validateToken(jwtToken, userDetails)) {
                
                var principal = userDetails.getUsername();
                Map<String, String> credentials = new HashMap<>();
                credentials.put("username", username);
                credentials.put("password", "user");
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(principal, credentials, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                var auth1 = SecurityContextHolder.getContext().getAuthentication();
                int i = 0;
            }

        }*/
        return true;
    }

    public RedirectView redirectView(String url){
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(url);
        return redirectView;
    }
}
