package com.revature.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.revature.models.Property;
import com.revature.models.User;
import com.revature.models.UserDTO;
import com.revature.models.UserStatus;
import com.revature.repositories.UserDAO;
import com.revature.repositories.UserStatusDAO;

@Service
public class UserService {
	
	
	public UserService() {
		super();
	}

	public UserService(UserDAO usdao) {
		super();
		this.usdao = usdao;
	}
	
	public UserService(UserDAO usdao, UserStatusDAO ustatdao) {
		super();
		this.usdao = usdao;
		this.ustatdao = ustatdao;
	}

	@Autowired
	private UserDAO usdao;
	@Autowired
	private UserStatusDAO ustatdao;
	
	
	
	public User findByEmail(String email) {
		return usdao.getUserByEmail(email);
	}

	
	public User upsert(User u) {
		System.out.println("upsert method in UserService");
		System.out.println(u.getId());
		String userStatusString = u.getUserStatus().getStatus();
		UserStatus fullStatus = grabActualUserStatus(u);
		u.setUserStatus(fullStatus);
		usdao.upsertUser(u);
		System.out.println("Upserted this: " + u);
		System.out.println("After, it is this: " + usdao.getUserByEmail(u.getEmail()));
		if(usdao.getUserByEmail(u.getEmail()).equals(u)) {
			System.out.println("inside if conditional, true, upsert method UserService");
			System.out.println("Before reassigning in if in upsert user in UserService: "+ u);
			User user = usdao.getUserByEmail(u.getEmail());
			System.out.println("After reassigning in if in upsert user in UserService: "+ u);
			return user;
		}
		System.out.println("did not hit if conditional, false, upsert method UserService");
		return null;
	}
	
	public boolean verifyUser(String email, String password) {
		User u = usdao.getUserByEmail(email);
		if(u != null) {
			if(u.getPassword().equals(password)) {
				return true;
			}
		}
		 return false;
	}
	

	
	public List<UserDTO> findByState(String state){
		
		List<User> userList = usdao.findByState(state);
		List<UserDTO> dtoList = new ArrayList<>();
		try{
			for(User u: userList) {
				dtoList.add(new UserDTO(u));
			}
				return dtoList;
			} catch (NullPointerException e){
				System.out.println("Exception " + e.getMessage());;
		}
		return null;
	}
	
	
	public UserStatus grabActualUserStatus(User user) {
		String statusString = user.getUserStatus().getStatus();
		UserStatus fullStatusObject = ustatdao.getStatusByString(statusString);
		return fullStatusObject;
		
	}

}
