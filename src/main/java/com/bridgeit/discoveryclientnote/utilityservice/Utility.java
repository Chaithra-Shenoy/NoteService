/**
 * 
 */
package com.bridgeit.discoveryclientnote.utilityservice;

import java.util.Date;

import org.springframework.stereotype.Component;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * 
 * 
 * @author Chaithra-Shenoy
 * @since Date 10-07-2018 <br>
 *        <p>
 *        <b>Utility Class having Validation method to Validate User
 *        information.</b>
 *        </p>
 */
@Component
public class Utility {

	final static String KEY = "chaithra";

	public Utility() {

	}
	/**
	 * @param id
	 * @return String
	 *         <p>
	 *         Create a valid token based on user id.
	 *         </p>
	 */
	public String createToken(String id) {
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
		Date startTime = new Date();
		Date expireTime = new Date(startTime.getTime() + (1000 * 60 * 60 * 24));

		JwtBuilder builder = Jwts.builder().setId(id).setIssuedAt(startTime).setExpiration(expireTime)
				.signWith(signatureAlgorithm, KEY);
		return builder.compact();
	}

	/**
	 * @param jwt
	 * @return Claims
	 *         <p>
	 *         Pasre the jwt by passing String jwt.Based on jwt passed user
	 *         information can be fetched.
	 *         </p>
	 */
	public Claims parseJwt(String jwt) {
		System.out.println(jwt);
		return Jwts.parser().setSigningKey(KEY).parseClaimsJws(jwt).getBody();
	}

}
