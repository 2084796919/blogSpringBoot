package com.luguz.service.Impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luguz.entity.Friend;
import com.luguz.mapper.FriendMapper;
import com.luguz.service.IFriendService;
import org.springframework.stereotype.Service;



@Service
public class FriendServiceImpl extends ServiceImpl<FriendMapper, Friend> implements IFriendService {

}
