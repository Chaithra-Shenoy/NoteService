/**
 * 
 */
package com.bridgeit.discoveryclientnote.noteservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.bridgeit.discoveryclientnote.noteservice.model.ResponseDto;


/**
 * 
 * 
 * @author Chaithra-Shenoy
 * @since Date 10-07-2018 <br>
 *        <p>
 *        <b>POJO Class having User related information and method.</b>
 *        </p>
 */
@FeignClient(name="UserService",url="http://localhost:8090")
@Service
public interface IFeign {

	@GetMapping("/user/getalluser")
	public List<?> getUser();
	
	@GetMapping("user/getuser/{email}")
	public  Optional<?> getUserByEmail(@PathVariable String email);
}
