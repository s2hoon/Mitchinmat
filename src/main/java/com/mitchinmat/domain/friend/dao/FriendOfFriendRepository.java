package com.mitchinmat.domain.friend.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mitchinmat.domain.friend.domain.FriendOfFriend;

@Repository
public interface FriendOfFriendRepository extends JpaRepository<FriendOfFriend, Long> {

}
