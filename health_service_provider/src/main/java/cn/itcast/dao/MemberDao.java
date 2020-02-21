package cn.itcast.dao;

import cn.itcast.pojo.Member;

public interface MemberDao {
    //1.根据手机号查询会员:作用于检查用户是否重复预约 ,登录也需要
    Member findByTelephone(String telephone);
    //2.预约时注册会员//登录也需要
    void add(Member member);
    //3.根据月份查询会员数
    Integer findMemberCountByMonths(String month);
    //4.查询总会员数
    Integer findMemberTotalCount();
    //5.根据日期查询当天新增会员数
    Integer findMemberCountByDate(String today);
    //6.根据参数的不同,从而查询本周新增会员,本月新增会员
    Integer findMemberCountAfterDate(String thisWeekMonday);
}
