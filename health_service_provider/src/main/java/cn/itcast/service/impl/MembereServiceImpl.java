package cn.itcast.service.impl;

import cn.itcast.dao.MemberDao;
import cn.itcast.pojo.Member;
import cn.itcast.service.MemberService;
import cn.itcast.utils.MD5Utils;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service(interfaceClass = MemberService.class)
@Transactional
public class MembereServiceImpl implements MemberService {
    @Autowired
    private MemberDao memberDao;

    /**
     * 1.根据手机号查询会员
     * @param telephone
     * @return
     */
    @Override
    public Member findByTelephone(String telephone) {
        return memberDao.findByTelephone(telephone);
    }

    /**
     * 2.添加会员
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

    /**
     * 3.会员报表查询数量,根据月份
     * @param months
     * @return
     */
    @Override
    public List<Integer> findMemberCountByMonths(List<String> months) {
        List<Integer> memberCount = new ArrayList<>();
        for (String month : months) {
            month=month+".31";
            Integer count = memberDao.findMemberCountByMonths(month);
            memberCount.add(count);
        }
        return memberCount;
    }
}
