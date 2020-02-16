package cn.itcast.dao;

import cn.itcast.pojo.Member;

public interface MemberDao {
    //1.根据手机号查询会员:作用于检查用户是否重复预约 ,登录也需要
    Member findByTelephone(String telephone);
    //2.预约时注册会员//登录也需要
    void add(Member member);
}
