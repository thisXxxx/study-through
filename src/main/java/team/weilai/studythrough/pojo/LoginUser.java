package team.weilai.studythrough.pojo;

import com.alibaba.fastjson.annotation.JSONField;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author gwj
 * @since 2024/5/16 14:41
 */
public class LoginUser implements UserDetails {
    private User user;
    private List<String> permissions;
    /**
     * 将LoginUser以json保存在redis中时默认是不将该字段序列化的
     * 所以将它序列化忽略，否则会运行出现异常
     */
    @JSONField(serialize = false)
    private List<SimpleGrantedAuthority> authorities;
    public LoginUser(User user, List<String> permissions) {
        this.user = user;
        this.permissions = permissions;
    }

    public LoginUser() {
    }

    public LoginUser(User user, List<String> permissions, List<SimpleGrantedAuthority> authorities) {
        this.user = user;
        this.permissions = permissions;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //将permissions中的String类型权限信息封装成SimpleGrantedAuthority
        if (authorities == null) {
            authorities = permissions.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        }

        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


    /**
     * 获取
     * @return user
     */
    public User getUser() {
        return user;
    }

    /**
     * 设置
     * @param user
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * 获取
     * @return permissions
     */
    public List<String> getPermissions() {
        return permissions;
    }

    /**
     * 设置
     * @param permissions
     */
    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }

    /**
     * 设置
     * @param authorities
     */
    public void setAuthorities(List<SimpleGrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    @Override
    public String toString() {
        return "LoginUser{user = " + user + ", permissions = " + permissions + ", authorities = " + authorities + "}";
    }
}
