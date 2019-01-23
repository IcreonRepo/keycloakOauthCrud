package com.icreon.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class WebConfig extends WebSecurityConfigurerAdapter{

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/api/v1/user/");
	}

}
