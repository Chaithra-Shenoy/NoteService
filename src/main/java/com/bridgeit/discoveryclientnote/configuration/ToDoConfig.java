/**
 * 
 */
package com.bridgeit.discoveryclientnote.configuration;


import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 
 * 
 * @author Chaithra-Shenoy
 * @since Date 18-07-2018 <br>
 *        <p>
 *        <b>ToDoConfiguration class.</b>
 *        </p>
 */
@Configuration
public class ToDoConfig {

	@Bean
	public ModelMapper model() {
		return new ModelMapper();
	}
}
