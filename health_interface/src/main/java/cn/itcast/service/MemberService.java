package cn.itcast.service;

import cn.itcast.pojo.Member;

public interface MemberService {
    //1.查询会员
    Member findByTelephone(String telephone);
    //2.添加会员
    void add(Member member);
}
