package cn.itcast.service.impl;

import cn.itcast.dao.MemberDao;
import cn.itcast.pojo.Member;
import cn.itcast.service.MemberService;
import cn.itcast.utils.MD5Utils;

public class MembereServiceImpl implements MemberService {
    private MemberDao memberDao;

    /**
     * 根据手机号查询会员
     * @param telephone
     * @return
     */
    @Override
    public Member findByTelephone(String telephone) {
        return memberDao.findByTelephone(telephone);
    }

    /**
     * 添加会员
     * @param member
     */
    @Override
    public void add(Member member) {
        String password = member.getPassword();
        if (password!=null){
            password = MD5Utils.md5(password);
            member.setPassword(password);
        }
        memberDao.add(member);
    }
}
