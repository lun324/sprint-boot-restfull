package net.fulugou.demo.shiro;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.mindrot.jbcrypt.BCrypt;

/**
 * 可重写密码匹配规则
 */
public class BCryptCredentialsMatcher implements CredentialsMatcher {

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        JWTAuthenticationToken jwtToken = (JWTAuthenticationToken) token;
        //要验证的明文密码
//        String plaintext = new String(jwtToken.getCredentials());
//        //数据库中的加密后的密文
//        String hashed = info.getCredentials().toString();
//        return BCrypt.checkpw(plaintext, hashed);
        return true;
//            return jwtToken.getCredentials().equals(info.getCredentials());
    }
}
