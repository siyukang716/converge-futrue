package com.cloud.config.shiro;

import org.apache.shiro.authc.*;
import org.apache.shiro.authc.pam.AuthenticationStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.realm.Realm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Iterator;

/***
 * 自定义重写ModularRealmAuthenticator类，用与处理多realm的自定义异常捕获问题
 * @date 2020-3-11
 * @author chenzh
 */
public class CustomModularRealmAuthenticator extends ModularRealmAuthenticator {
 
    private static final Logger log = LoggerFactory.getLogger(ModularRealmAuthenticator.class);
 
    /**
     * 重写多realm校验方法，避免无法捕获到realm中的自定义异常
     * 取消执行代码 realm.getAuthenticationInfo(token); 的tey catch
     * 多个realm时候，如果其中一个realm抛出异常则直接抛出异常，类似single方法，
     * @param realms
     * @param token
     * @return
     */
    @Override
    /*protected AuthenticationInfo doMultiRealmAuthentication(Collection<Realm> realms, AuthenticationToken token) {
        AuthenticationStrategy strategy = getAuthenticationStrategy();
        AuthenticationInfo aggregate = strategy.beforeAllAttempts(realms, token);
        if (log.isTraceEnabled()) {
            log.trace("Iterating through {} realms for PAM authentication", realms.size());
        }
        for (Realm realm : realms) {
            aggregate = strategy.beforeAttempt(realm, token, aggregate);
            if (realm.supports(token)) {
                log.trace("Attempting to authenticate token [{}] using realm [{}]", token, realm);
                AuthenticationInfo info = null;
                Throwable t = null;
                *//*
                * ---
                * 此行代码执行取消try catch，若异常直接抛出，其他同原方法
                * ---
                * *//*
                info = realm.getAuthenticationInfo(token);
                aggregate = strategy.afterAttempt(realm, token, info, aggregate, t);
            } else {
                log.debug("Realm [{}] does not support token {}.  Skipping realm.", realm, token);
            }
        }
        aggregate = strategy.afterAllAttempts(token, aggregate);
        return aggregate;
    }*/


    protected AuthenticationInfo doMultiRealmAuthentication(Collection<Realm> realms, AuthenticationToken token) {
        AuthenticationStrategy strategy = this.getAuthenticationStrategy();
        AuthenticationInfo aggregate = strategy.beforeAllAttempts(realms, token);
        if (log.isTraceEnabled()) {
            log.trace("Iterating through {} realms for PAM authentication", realms.size());
        }

        Iterator var5 = realms.iterator();
        String set = "";
        while(var5.hasNext()) {
            Realm realm = (Realm)var5.next();
            aggregate = strategy.beforeAttempt(realm, token, aggregate);
            if (realm.supports(token)) {
                log.trace("Attempting to authenticate token [{}] using realm [{}]", token, realm);
                AuthenticationInfo info = null;
                Throwable t = null;
                //info = realm.getAuthenticationInfo(token);
                try {
                    info = realm.getAuthenticationInfo(token);
                } catch (Throwable var11) {
                    set += var11.getClass().getName();
                    t = var11;
                    if (log.isDebugEnabled()) {
                        String msg = "Realm [" + realm + "] threw an exception during a multi-realm authentication attempt:";
                        log.debug(msg, var11);
                    }
                }

                aggregate = strategy.afterAttempt(realm, token, info, aggregate, t);
            } else {
                log.debug("Realm [{}] does not support token {}.  Skipping realm.", realm, token);
            }
        }

        aggregate = strategy.afterAllAttempts(token, aggregate);
        if (null == aggregate){
            if (set.contains("LockedAccountException"))
                throw new LockedAccountException();
            if (set.contains("IncorrectCredentialsException"))
                throw new IncorrectCredentialsException();
            if (set.contains( "UnknownAccountException"))
                throw new UnknownAccountException();
        }
        return aggregate;
    }
}