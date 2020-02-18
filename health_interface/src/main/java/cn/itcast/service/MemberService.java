package cn.itcast.service;

import cn.itcast.pojo.Member;

import java.util.List;

public interface MemberService {
    //1.查询会员
    Member findByTelephone(String telephone);
    //2.添加会员
    void add(Member member);
    //3.会员报表查询数量,根据月份
    List<Integer> findMemberCountByMonths(List<String> months);
}
